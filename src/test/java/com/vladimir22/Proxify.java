package com.vladimir22;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class Proxify {

    static class Solution {

        /**
         * This is a demo task.
         *
         * Write a function:
         *
         * class Solution { public int solution(int[] A); }
         *
         * that, given an array A of N integers, returns the smallest positive integer (greater than 0) that does not occur in A.
         *
         * For example, given A = [1, 3, 6, 4, 1, 2], the function should return 5.
         *
         * Given A = [1, 2, 3], the function should return 4.
         *
         * Given A = [−1, −3], the function should return 1.
         *
         * Write an efficient algorithm for the following assumptions:
         *
         * N is an integer within the range [1..100,000];
         * each element of array A is an integer within the range [−1,000,000..1,000,000].
         */
        static public int findUniquePositiveNumber(int[] A) {
            // Sort array: O(n) = log(n)
            int[] sorted = Arrays.stream(A)
                    .filter(v -> v > 0)
                    .sorted()
                    .distinct()
                    .toArray();

            if (sorted.length ==0 || sorted[0] != 1) {
                return 1;
            }
            int previous = sorted[0];

            // Find absent number: O(n) = n
            for (int i = 1; i < sorted.length; i++) {
                if (sorted[i] != previous + 1) {
                    return previous + 1;
                }
                previous = sorted[i];
            }
            // Otherwise, return last element + 1. Total time complexity: O(n) = n*log(n)
            return Math.addExact(sorted[sorted.length-1], 1);
        }
    }

    @ParameterizedTest
    @MethodSource("arraysWithout1")
    void processArrayWithout1(int[] array) {
        assertEquals(1, Solution.findUniquePositiveNumber(array));
    }
    @ParameterizedTest
    @MethodSource("arraysWithout2")
    void processArrayWithout2(int[] array) {
        assertEquals(2, Solution.findUniquePositiveNumber(array));
    }
    @ParameterizedTest
    @MethodSource("arraysWithout1000")
    void processArrayWithout1000(int[] array) {
        assertEquals(1000, Solution.findUniquePositiveNumber(array));
    }
    static Stream<Arguments> arraysWithout1() {
        return Stream.of(
                arguments(new int[]{0,-1,Integer.MAX_VALUE}),
                arguments(new int[]{Integer.MAX_VALUE,0,0,-1,Integer.MIN_VALUE}),
                arguments(new int[]{-10,Integer.MAX_VALUE,0,2,-1,Integer.MIN_VALUE}),
                arguments(new int[]{-1,-1,-2,Integer.MIN_VALUE})
        );
    }
    static Stream<Arguments> arraysWithout2() {
        return Stream.of(
                arguments(new int[]{0,-1,1,3,Integer.MAX_VALUE}),
                arguments(new int[]{Integer.MAX_VALUE,0,Integer.MIN_VALUE,0,1,1}),
                arguments(new int[]{-1, 0,1,1, 1000,1000})
        );
    }
    static Stream<Arguments> arraysWithout1000() {
        return Stream.of(
                arguments(IntStream.range(-1000,1000).toArray()),
                arguments( Stream.of(IntStream.range(-1000,1000).toArray(), IntStream.range(1001,10000)).toArray())
        );
    }
}
