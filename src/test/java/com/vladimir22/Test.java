package com.vladimir22;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Test {

    public static void main(String[] args) {
        System.out.println("Hello, World!");

        // Set from array
        int[] endings = {1, 2, 3, 4, 5};;


        // convert int[] to Integer[]
        Integer[] endingsInteger = new Integer[endings.length];
for (int i = 0; i < endings.length; i++) {
            endingsInteger[i] = endings[i];
        }

        Set<Integer> endSet = new HashSet(Collections.singleton(endings));


    }


}
