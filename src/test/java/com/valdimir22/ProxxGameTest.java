package com.valdimir22;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ProxxGameTest {

    @ParameterizedTest
    @CsvSource({"1,1", "10,0", "1000,1000"})
    void initBoardTest(int maxSize, int numHoles) {
        ProxxGame game = new ProxxGame(maxSize, numHoles);
        game.initBoard();

        Cell[][] board = game.board;
        long existingHoles = Arrays.stream(board)
                .flatMap(Stream::of)
                .filter(c -> c.isBlackHole()) // check the board on existence not opened cells
                .count();
        assertEquals(numHoles, existingHoles);
    }


    @ParameterizedTest
    @CsvSource({"1,2", "0,1", "10,-1", "-1,1", "-4,-2"})
    void initBoardTestFailedWithIllegalArgumentException(int maxSize, int numHoles) {
        ProxxGame game = new ProxxGame(maxSize, numHoles);
        assertThrows(IllegalArgumentException.class, () -> {
            game.initBoard();
        });
    }


    @ParameterizedTest
    @CsvSource({"1000000, 1", "100000, 1000000000"})
    void initBoardTestFailedWithArithmeticException(int maxSize, int numHoles) {
        ProxxGame game = new ProxxGame(maxSize, numHoles);
        assertThrows(ArithmeticException.class, () -> {
            game.initBoard();
        });
    }


    @Test
    void calculateAdjacentBlackHolesTest() {

        ProxxGame game = new ProxxGame(3, 1);
        game.createBoard();

        game.board[1][1].setBlackHole(true);
        game.calculateAdjacentBlackHoles(1, 1);

        game.printBoard(1,1, true);
        assertEquals(1, game.board[0][0].getAdjacentBlackHoles());
        assertEquals(1, game.board[1][0].getAdjacentBlackHoles());
        assertEquals(1, game.board[2][0].getAdjacentBlackHoles());

        assertEquals(1, game.board[0][2].getAdjacentBlackHoles());
        assertEquals(1, game.board[1][2].getAdjacentBlackHoles());
        assertEquals(1, game.board[2][2].getAdjacentBlackHoles());

        assertEquals(1, game.board[0][1].getAdjacentBlackHoles());
        assertEquals(1, game.board[2][1].getAdjacentBlackHoles());


        game = new ProxxGame(3, 1);
        game.createBoard();

        game.board[0][0].setBlackHole(true);
        game.board[2][2].setBlackHole(true);
        game.calculateAdjacentBlackHoles(0, 0);
        game.calculateAdjacentBlackHoles(2, 2);

        game.printBoard(1,1, true);
        assertEquals(1, game.board[0][1].getAdjacentBlackHoles());
        assertEquals(2, game.board[1][1].getAdjacentBlackHoles());
        assertEquals(1, game.board[2][1].getAdjacentBlackHoles());
    }
}