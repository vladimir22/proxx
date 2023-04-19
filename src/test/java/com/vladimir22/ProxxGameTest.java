package com.vladimir22;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.stream.Stream;

import static com.vladimir22.ProxxGame.LOSE;
import static com.vladimir22.ProxxGame.WIN;
import static org.junit.jupiter.api.Assertions.*;

class ProxxGameTest {

    @ParameterizedTest
    @CsvSource({"1,2", "0,1", "10,-1", "-1,1", "-100,-10"})
    void newProxxGameTest_IllegalArgumentExceptionThrowsIfNotAllowedArgs(int maxSize, int numHoles) {
        assertThrows(IllegalArgumentException.class, () -> {
            new ProxxGame(maxSize, numHoles);
        });
    }

    @ParameterizedTest
    @CsvSource({"1000000, 1", "100000, 1000000000"})
    void newProxxGameTest_ArithmeticExceptionThrowsIfArgsOutOfTheRange(int maxSize, int numHoles) {
        assertThrows(ArithmeticException.class, () -> {
            new ProxxGame(maxSize, numHoles);
        });
    }

    @ParameterizedTest
    @CsvSource({"1,1", "10,0", "1000,1000", "10,100"})
    void assignBlackHolesTest_AllowedArgs(int maxSize, int numHoles) {
        ProxxGame game = new ProxxGame(maxSize, numHoles);
        game.createBoard();
        game.assignRandomBlackHoles();

        Cell[][] board = game.board;
        long existingHoles = Arrays.stream(board)
                .flatMap(Stream::of)
                .filter(c -> c.isBlackHole())
                .count();
        assertEquals(numHoles, existingHoles);
    }

    @Test
    void calculateAdjacentBlackHolesTest_AdjacentBlackHolesFor1BlackHole() {

        ProxxGame game = new ProxxGame(3, 0);
        game.createBoard();

        game.board[1][1].setBlackHole(true);
        game.iterateAdjacentCells(1,1, (xx, yy) -> game.addAdjacentBlackHole(xx, yy));

        game.printBoard(1, 1, true);

        game.iterateAdjacentCells(1,1, (xx, yy) ->  assertEquals(1, game.board[xx][yy].getAdjacentBlackHoles()));
    }

    @Test
    void calculateAdjacentBlackHolesTest_AdjacentBlackHolesFor2BlackHoles() {

        ProxxGame game = new ProxxGame(3, 0);
        game.createBoard();

        game.board[0][0].setBlackHole(true);
        game.iterateAdjacentCells(0,0, (xx, yy) -> game.addAdjacentBlackHole(xx, yy));

        game.board[2][2].setBlackHole(true);
        game.iterateAdjacentCells(2,2, (xx, yy) -> game.addAdjacentBlackHole(xx, yy));


        game.printBoard(1,1, true);

        game.iterateAdjacentCells(0,0, (xx, yy) -> {
            if (xx == 1 && yy == 1) {
                assertEquals(2, game.board[xx][yy].getAdjacentBlackHoles(), "board[1][1] cell should have 2 adjacentBlackHoles");
            } else {
                assertEquals(1, game.board[xx][yy].getAdjacentBlackHoles());
            }
        });
    }

    @Test
    void playTest_WinOnBoardWithoutBlackHoles() {
        ProxxGame game = new ProxxGame(3, 0);
        game.createBoard();
        game.assignRandomBlackHoles();

        String inputXY = "0 0\n";
        assertEquals(WIN,game.play(new ByteArrayInputStream(inputXY.getBytes())), "Board without black holes. You have to win");
    }

    @Test
    void playTest_LoseOnBoardThatContainsOnlyBlackHoles() {
        ProxxGame game = new ProxxGame(3, 9);
        game.createBoard();
        game.assignRandomBlackHoles();

        String inputXY = "0 0\n";
        assertEquals(LOSE,game.play(new ByteArrayInputStream(inputXY.getBytes())), "Board contains only black holes. You have to lose");
    }

    @Test
    void playTest_WinOnBoardWithSingleBlackHole() {
        ProxxGame game = new ProxxGame(3, 0);
        game.createBoard();

        game.board[1][1].setBlackHole(true);
        game.iterateAdjacentCells(1,1, (xx, yy) -> game.addAdjacentBlackHole(xx, yy));

        String inputXY = "0 0\n 0 1\n 0 2\n 1 0\n 1 2\n 2 0\n 2 1\n 2 2\n";
        assertEquals(WIN,game.play(new ByteArrayInputStream(inputXY.getBytes())), "Single black hole only in the board[1][1] cell. You have to win");
    }

    @Test
    void playTest_LoseOnBoardWithSingleBlackHole() {
        ProxxGame game = new ProxxGame(3, 0);
        game.createBoard();

        game.board[1][1].setBlackHole(true);
        game.iterateAdjacentCells(1,1, (xx, yy) -> game.addAdjacentBlackHole(xx, yy));

        String inputXY = "0 0\n 0 1\n 0 2\n 1 0\n 1 2\n 2 0\n 2 1\n 1 1\n";
        assertEquals(LOSE,game.play(new ByteArrayInputStream(inputXY.getBytes())), "Single black hole only in the board[1][1] cell. You have to lose");
    }

    @Test
    void playTest_LoseOnBoardWithMultipleBlackHole() {
        ProxxGame game = new ProxxGame(10, 10);
        game.createBoard();
        game.assignRandomBlackHoles();

        game.board[1][1].setBlackHole(true);
        game.iterateAdjacentCells(1,1, (xx, yy) -> game.addAdjacentBlackHole(xx, yy));

        String inputXY = "1 1\n";
        assertEquals(LOSE,game.play(new ByteArrayInputStream(inputXY.getBytes())), "Black hole only in the board[1][1] cell. You have to lose");
    }


    @Test
    void playTest_WinOnBoardWithMultipleBlackHole() {
        ProxxGame game = new ProxxGame(10, 2);
        game.createBoard();

        game.board[1][1].setBlackHole(true);
        game.iterateAdjacentCells(1,1, (xx, yy) -> game.addAdjacentBlackHole(xx, yy));

        game.board[9][9].setBlackHole(true);
        game.iterateAdjacentCells(9,9, (xx, yy) -> game.addAdjacentBlackHole(xx, yy));

        String inputXY = "0 0\n 0 1\n 0 2\n 1 0\n 1 2\n 2 0\n 2 1\n 2 2\n 4 4\n";
        assertEquals(WIN,game.play(new ByteArrayInputStream(inputXY.getBytes())), "Black hole only in the board[1][1] cell. You have to lose");
    }




    // ---- Test only methods ----
    private ByteArrayInputStream sendCoordinates(int x, int y){
       String text = String.format("%d %d\n",x,y);
       return new ByteArrayInputStream(text.getBytes());
    }


}