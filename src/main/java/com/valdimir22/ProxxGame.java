package com.valdimir22;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.function.IntFunction;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ProxxGame {

    private final Integer maxSize; // Width and length of board
    private final Integer numBlackHoles; // Number of holes in the board

    // The Board
    protected Cell[][] board;


    public void initBoard() {

        int maxCells = Math.multiplyExact(maxSize,maxSize);

        // Validate incoming parameters
        if (numBlackHoles < 0 || numBlackHoles > maxCells){
            throw new IllegalArgumentException(String.format("Incorrect number of black holes: %s", numBlackHoles));
        }
        if (maxSize < 0 ){
            throw new IllegalArgumentException(String.format("Incorrect max size of the board: %s", maxSize));
        }


        createBoard();

        // Set random blackHoles
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

            calculateAdjacentBlackHoles(x,y);
            i++;
        }
    }

    protected void createBoard() {
        board = new Cell[maxSize][maxSize];
        for (int y = 0; y < maxSize; y++)
            for (int x = 0; x < maxSize; x++)
                board[x][y] = new Cell();
    }

    protected void calculateAdjacentBlackHoles(int x, int y) {
        // Calculate adjacent BalckHoles in the nearby cells
        for (int xx=x-1; xx<=x+1; xx++) {
            for (int yy = y - 1; yy <= y + 1; yy++) {
                if (xx == x && yy == y) {
                    continue; // skip original cell coordinate
                }
                addAdjacentBlackHole(xx, yy);
            }
        }
    }

    public void play() {

        Scanner sc = new Scanner(System.in);

        while (isHiddenCellsExist()) {

            // Enter coordinates
            System.out.println(String.format("\n\nEnter X and Y coordinates from 0 to %s", maxSize-1));
            int x = sc.nextInt();
            int y = sc.nextInt();

            // Validate coordinates
            if (x < 0 || x >= maxSize && y < 0 || y >= maxSize) {
                System.out.println("Wrong coordinates");
                continue;
            }

            Cell cell = board[x][y];

            if (cell.isBlackHole()) {
                printBoard(x,y, true);
                System.out.println("Game Over! Black Hole has been opened ...");
                return;
            }
            if (cell.getAdjacentBlackHoles() == 0) {
                openAdjacentCells(x, xf -> xf, y, yf -> yf); // open nearby cells
            } else {
                cell.setOpened(true);
            }
            printBoard(x,y, false);
        }

        printBoard(0,0, true);
        System.out.println("Congratulations!!! You WIN !!!");
        return;
    }


    private boolean isHiddenCellsExist() {
        return Arrays.stream(board)
                .flatMap(Stream::of)
                .filter(c -> !c.isOpened() && !c.isBlackHole()) // check the board on existence not opened cells
                .findAny()
                .isPresent();
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


    private void addAdjacentBlackHole(int x, int y) {
        if (x >= maxSize || x < 0 || y >= maxSize || y < 0) {
            return;
        }
        Cell cell = board[x][y];
        int adjacentHoles = cell.getAdjacentBlackHoles();
        cell.setAdjacentBlackHoles(++adjacentHoles);
    }


    protected void printBoard(int xSelected, int ySelected, boolean openBoard) {

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
