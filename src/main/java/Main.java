

import com.valdimir22.ProxxGame;

import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Stream;


public class Main {

    private static Integer MAX_SIZE = 10; // Width and length of board
    private static Integer NUM_HOLES = 5; // Number of holes in the board

    // The Board
    static private Cell[][] board;

    // Cell of the board
    private static class Cell {
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

        ProxxGame game = new ProxxGame(10,10);
        game.initBoard();
        game.play();
    }


    private static void createBoard() {

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


    private static void play() {

        Scanner sc = new Scanner(System.in);

        while (isHiddenCellsExist()) {

            System.out.println(String.format("\n\nEnter X and Y", MAX_SIZE));
            int x = sc.nextInt();
            int y = sc.nextInt();

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
    }


    private static boolean isHiddenCellsExist() {
        return Arrays.stream(board)
                .flatMap(Stream::of)
                .filter(c -> !c.isOpened() && !c.isBlackHole()) // check the board on existence not opened cells
                .findAny()
                .isPresent();
    }


    private static void openAdjacentCells(int xToOpen, IntFunction<Integer> xToOpenFunction, int yToOpen, IntFunction<Integer> yToOpenFunction) {
        int x = xToOpenFunction.apply(xToOpen);
        int y = yToOpenFunction.apply(yToOpen);

        if (x >= MAX_SIZE || y >= MAX_SIZE || x < 0 || y < 0) {
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
    }

    private static void addAdjacentBlackHole(int x, int y) {
        if (x >= MAX_SIZE || x < 0 || y >= MAX_SIZE || y < 0) {
            return;
        }
        Cell cell = board[x][y];
        int adjacentHoles = cell.getAdjacentBlackHoles();
        cell.setAdjacentBlackHoles(++adjacentHoles);
    }

    private static void printBoard(int xSelected, int ySelected, boolean openBoard) {

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