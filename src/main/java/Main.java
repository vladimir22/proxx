

import com.valdimir22.ProxxGame;

import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Stream;


public class Main {

    public static void main(String... args) {
        ProxxGame game = new ProxxGame(10,10);
        game.initBoard();
        game.play();
    }

}