

import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Stream;


public class Main {

    public static Integer MAX_SIZE = 10; // Width and length of board
    public static Integer NUM_HOLES = 5; // Number of holes in the board

    // The Board
    static private Cell[][] board;

    // Cell of the board
    public static class Cell {
        private boolean isOpened;
        private boolean isBlackHole;
        private int adjacentBlackHoles; // number of neighbour black holes

        public boolean isOpened() {
            return isOpened;
        }

        public boolean isBlackHole() {
            return isBlackHole;
        }

        public int getAdjacentBlackHoles() {
            return adjacentBlackHoles;
        }

        public void setOpened(boolean opened) {
            isOpened = opened;
        }

        public void setBlackHole(boolean blackHole) {
            isBlackHole = blackHole;
        }

        public void setAdjacentBlackHoles(int adjacentBlackHoles) {
            this.adjacentBlackHoles = adjacentBlackHoles;
        }
    }


    public static void main(String... args) {
        createBoard();
        play();
    }


    public static void createBoard() {

        // Init board
        board = new Cell[MAX_SIZE][MAX_SIZE];
        for (int y = 0;y < MAX_SIZE; y++)
            for (int x = 0; x < MAX_SIZE; x++)
                board[x][y] = new Cell();

        // Set random blackHoles
        List<int[]> blackHoles = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < NUM_HOLES; i++) {
            int x = random.ints(0, MAX_SIZE)
                    .findFirst()
                    .getAsInt();
            int y = random.ints(0, MAX_SIZE)
                    .findFirst()
                    .getAsInt();
            Cell blackHole = board[x][y];
            blackHole.setBlackHole(true);
            blackHoles.add(new int[]{x, y});
        }

        // Calculate number of adjacent blackHoles
        for (int[] blackHole : blackHoles) {
            int x = blackHole[0];
            int y = blackHole[1];

            addAdjacentBlackHole(x, y + 1);
            addAdjacentBlackHole(x - 1, y + 1);
            addAdjacentBlackHole(x - 1, y);
            addAdjacentBlackHole(x - 1, y - 1);
            addAdjacentBlackHole(x, y - 1);
            addAdjacentBlackHole(x + 1, y - 1);
            addAdjacentBlackHole(x + 1, y);
            addAdjacentBlackHole(x + 1, y + 1);
        }
    }


    public static void play() {

        Scanner sc = new Scanner(System.in);

        while (hiddenCellsExists()) {

            System.out.println(String.format("Enter X and Y", MAX_SIZE));
            int x = sc.nextInt();
            int y = sc.nextInt();

            Cell cell = board[x][y];

            if (cell.isBlackHole()) {
                System.out.println("Game Over: Black Hole has FIRED!");
                printBoard(x,y, true);
                return;
            }
            if (cell.getAdjacentBlackHoles() == 0) {
                openAdjacentCells(x, xf -> xf, y, yf -> yf); // open nearby cells
            } else {
                cell.setOpened(true);
            }
            printBoard(x,y, false);
        }

        System.out.println("Congratulations!!! You WIN !!!");
        printBoard(0,0, true);

    }


    public static boolean hiddenCellsExists() {
        return Arrays.stream(board)
                .flatMap(Stream::of)
                .filter(c -> !c.isOpened() && !c.isBlackHole()) // check the board on existence not opened cells
                .findAny()
                .isPresent();
    }


    public static int openAdjacentCells(int xToOpen, IntFunction<Integer> xToOpenFunction, int yToOpen, IntFunction<Integer> yToOpenFunction) {
        int x = xToOpenFunction.apply(xToOpen);
        int y = yToOpenFunction.apply(yToOpen);

        if (x >= MAX_SIZE || y >= MAX_SIZE || x < 0 || y < 0) {
            return 0;
        }

        Cell cell = board[x][y];

        if (cell.isOpened()) {
            return cell.getAdjacentBlackHoles();
        }

        cell.setOpened(true);
        if (cell.getAdjacentBlackHoles() > 0) {
            return cell.getAdjacentBlackHoles();
        }


        openAdjacentCells(x, xf -> ++xf, y, yf -> yf);
        openAdjacentCells(x, xf -> ++xf, y, yf -> ++yf);
        openAdjacentCells(x, xf -> xf, y, yf -> ++yf);
        openAdjacentCells(x, xf -> --xf, y, yf -> ++yf);
        openAdjacentCells(x, xf -> --xf, y, yf -> yf);
        openAdjacentCells(x, xf -> --xf, y, yf -> --yf);

        return 0;
    }

    public static void addAdjacentBlackHole(int x, int y) {
        if (x >= MAX_SIZE || x < 0 || y >= MAX_SIZE || y < 0) {
            return;
        }
        Cell cell = board[x][y];
        int adjacentHoles = cell.getAdjacentBlackHoles();
        cell.setAdjacentBlackHoles(++adjacentHoles);
    }

    public static void printBoard(int xSelected, int ySelected, boolean openBoard) {
        StringBuilder sb = new StringBuilder();


        sb.append("\nXY\t");
        for (int x = 0; x < MAX_SIZE; x++) {
            sb.append("x"+x+"\t");
        }

        for (int y = 0; y < MAX_SIZE; y++) {
            sb.append("\ny"+y+"");
            for (int x = 0; x < MAX_SIZE; x++) {
                sb.append("\t");
                Cell cell = board[x][y];

                if (x == xSelected && y == ySelected) {
                    sb.append("[");
                }

                if (cell.isOpened() || openBoard) {
                    if (cell.isBlackHole) {
                        sb.append("*");
                    } else {
                        sb.append(cell.adjacentBlackHoles);
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