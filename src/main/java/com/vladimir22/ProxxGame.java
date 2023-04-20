package com.vladimir22;

import java.io.InputStream;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;


public class ProxxGame {

    public static final Integer WIN = 0;
    public static final Integer LOSE = 1;

    private final Integer maxSize; // Width and length of board
    private final Integer numBlackHoles; // Number of holes in the board

    Cell[][] board;

    public ProxxGame(Integer maxSize, Integer numBlackHoles) {
        // Validate incoming args
        int maxCells = Math.multiplyExact(maxSize,maxSize);
        if (numBlackHoles < 0 || numBlackHoles > maxCells){
            throw new IllegalArgumentException(String.format("Incorrect number of black holes: %s", numBlackHoles));
        }
        if (maxSize < 0 ){
            throw new IllegalArgumentException(String.format("Incorrect max size of the board: %s", maxSize));
        }

        this.maxSize = maxSize;
        this.numBlackHoles = numBlackHoles;
    }

    void createBoard() {
        board = new Cell[maxSize][maxSize];
        for (int y = 0; y < maxSize; y++)
            for (int x = 0; x < maxSize; x++)
                board[x][y] = new Cell();
    }

    void assignRandomBlackHoles() {
        Random random = new Random();
        int i = 0;
        while (i < numBlackHoles) {
            int x = random.ints(0, maxSize)
                    .findFirst()
                    .getAsInt();
            int y = random.ints(0, maxSize)
                    .findFirst()
                    .getAsInt();
            Cell cell = board[x][y];

            if (cell.isBlackHole()){
                continue; // skip generated duplicate coordinates
            }

            cell.setBlackHole(true);

            iterateAdjacentCells(x, y, (xx, yy) -> addAdjacentBlackHole(xx, yy));
            i++;
        }
    }

    void addAdjacentBlackHole(int x, int y) {
        if (x >= maxSize || x < 0 || y >= maxSize || y < 0) {
            return;
        }
        Cell cell = board[x][y];
        int adjacentHoles = cell.getAdjacentBlackHoles();
        cell.setAdjacentBlackHoles(++adjacentHoles);
    }

    void iterateAdjacentCells(int x, int y, BiConsumer<Integer,Integer> biConsumer) {
        for (int xx=x-1; xx<=x+1; xx++) {
            if (xx < 0 || xx > maxSize-1) {
                continue; // skip non-existing cell coordinate
            }
            for (int yy = y - 1; yy <= y + 1; yy++) {
                if (yy < 0 || yy > maxSize-1) {
                    continue; // skip non-existing cell coordinate
                }
                if (xx == x && yy == y) {
                    continue; // skip original cell coordinate
                }
                biConsumer.accept(xx, yy);
            }
        }
    }


    public int play(InputStream is) {
        Scanner sc = new Scanner(is);
        int x = 0;
        int y = 0;

        while (true) {

            try {
                System.out.println(String.format("\n\nEnter X and Y coordinates from 0 to %s", maxSize-1));
                x = sc.nextInt();
                y = sc.nextInt();
            } catch (InputMismatchException e ){
               System.out.println("Incorrect input, please try again");
               sc.nextLine();
               continue;
            }

            // Validate coordinates
            if (x < 0 || x >= maxSize && y < 0 || y >= maxSize) {
                System.out.println("Wrong coordinates");
                continue;
            }

            Cell cell = board[x][y];

            if (cell.isBlackHole()) {
                printBoard(x,y, true);
                System.out.println("Game Over! Black Hole has been opened ...");
                return LOSE;
            }
            if (cell.getAdjacentBlackHoles() == 0) {
                openAdjacentCells(x, xf -> xf, y, yf -> yf); // open nearby cells
            } else {
                cell.setOpened(true);
            }

            if (isBoardAlreadyOpened()) {
                printBoard(x,y, true);
                System.out.println("Congratulations!!! You WIN !!!");
                return WIN;
            }
            printBoard(x,y, false);
        }
    }

    private boolean isBoardAlreadyOpened() {
        return Arrays.stream(board)
                .flatMap(Stream::of)
                .filter(c -> !c.isOpened() && !c.isBlackHole()) // check the board on existence of not opened cells
                .findAny()
                .isEmpty();
    }

    private void openAdjacentCells(int xToOpen, IntFunction<Integer> xToOpenFunction, int yToOpen, IntFunction<Integer> yToOpenFunction) {
        int x = xToOpenFunction.apply(xToOpen);
        int y = yToOpenFunction.apply(yToOpen);

        if (x >= maxSize || y >= maxSize || x < 0 || y < 0) {
            return;
        }

        Cell cell = board[x][y];

        if (cell.isOpened()) {
            return;
        }

        cell.setOpened(true);

        if (cell.getAdjacentBlackHoles() > 0) {
            return;
        }

        openAdjacentCells(x, xf -> ++xf, y, yf -> yf);
        openAdjacentCells(x, xf -> ++xf, y, yf -> ++yf);
        openAdjacentCells(x, xf -> xf, y, yf -> ++yf);
        openAdjacentCells(x, xf -> --xf, y, yf -> ++yf);
        openAdjacentCells(x, xf -> --xf, y, yf -> yf);
        openAdjacentCells(x, xf -> --xf, y, yf -> --yf);
        openAdjacentCells(x, xf -> xf, y, yf -> --yf);
        openAdjacentCells(x, xf -> ++xf, y, yf -> --yf);
    }

    void printBoard(int xSelected, int ySelected, boolean openBoard) {
        StringBuilder sb = new StringBuilder();
        sb.append("\nXY\t");
        for (int x = 0; x < maxSize; x++) {
            sb.append("x"+x+"\t");
        }
        for (int y = 0; y < maxSize; y++) {
            sb.append("\ny"+y+"");
            for (int x = 0; x < maxSize; x++) {
                sb.append("\t");

                Cell cell = board[x][y];

                if (x == xSelected && y == ySelected) {
                    sb.append("[");
                }
                if (cell.isOpened() || openBoard) {
                    if (cell.isBlackHole()) {
                        sb.append("*");
                    } else {
                        sb.append(cell.getAdjacentBlackHoles());
                    }
                } else {
                    sb.append(".");
                }
                if (x == xSelected && y == ySelected) {
                    sb.append("]");
                }
            }
        }
        System.out.println(sb);
    }
}
