package com.vladimir22;

public class Play {

    static Integer maxSize;
    static Integer numBlackHoles;
    static String incomingArguments;
    public static void main(String... args) {

        Thread.setDefaultUncaughtExceptionHandler(new ProxxGameExceptionHandler());

        incomingArguments = String.join(" ", args);
        maxSize = Integer.valueOf(args[0]);
        numBlackHoles = Integer.valueOf(args[1]);

        ProxxGame game = new ProxxGame(maxSize,numBlackHoles);
        game.createBoard();
        game.assignRandomBlackHoles();
        game.play(System.in);
    }


    static class ProxxGameExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable throwable) {

            String details = String.format("\tmaxSize=%s, numBlackHoles=%s, incomingArguments=%s, error:%s",maxSize, numBlackHoles, incomingArguments, throwable.getMessage());
            if (throwable instanceof IllegalArgumentException exception) {
                System.out.println("Incorrect incoming arguments:");
            } else if (throwable instanceof NumberFormatException exception) {
                System.out.println("Wrong format of incoming arguments:");
            } else if (throwable instanceof ArithmeticException exception) {
                System.out.println("Incoming arguments out of the range:");
            } else {
                System.out.println("Unknown exception:");
                throwable.printStackTrace();
            }
            System.out.println(details);
        }
    }


}