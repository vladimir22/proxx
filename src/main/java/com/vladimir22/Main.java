package com.vladimir22;

public class Main {

    public static void main(String... args) {
        int maxSize = Integer.valueOf(args[0]);
        int numBlackHoles = Integer.valueOf(args[1]);
        ProxxGame game = new ProxxGame(maxSize,numBlackHoles);
        game.initBoard();
        game.play();
    }

}