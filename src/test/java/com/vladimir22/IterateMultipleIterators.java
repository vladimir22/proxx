package com.vladimir22;

import lombok.AllArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/* Task description:

Given:
N sorted iterators of integers.

The task:
Implement iterator (Iterator<Integer>) that produces globally sorted sequence
from the given N iterators. Task must be solved in the most efficient
way possible (i.e it's not allowed to copy entire content of iterators into
some structure and then sort it).

For example:
Iterator A1: 6, 8, 19, 21, 32, 66, 67, 77, 89
Iterator A2: 1, 3, 5,  24, 33, 45, 57, 59, 89
Iterator A3: 2, 4, 9,  18, 22, 44, 46, 89, 89

Final globally sorted Iterator must produce:

1 (from A2)
2 (from A3)
3 (from A2)
4 (from A3)
5 (from A2)
6 (from A1)
8 (from A1)
9 (from A3)
18 (from A3)
19 (from A1)
21 (from A1)
22 (from A3)
...
*/

public class IterateMultipleIterators {

    @AllArgsConstructor
    public static class SingleArray<T> implements Iterable<T> {
        private final T[] values;

        class SingleArrayIterator implements Iterator<T> {
            int index = 0;
            public boolean hasNext() {
                return index < SingleArray.this.values.length ? true: false;
            }
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return values[index++];
            }
        }

        public Iterator<T> iterator() {
            return new SingleArrayIterator();
        }
    }


    public static class MultiArray<T> implements Iterable<T> {
        private final Iterator<T>[] iterators;
        private final Comparator<T> comparator;

        private StringBuilder logs; // logs are needed to show iteration flow details

        public MultiArray( Comparator<T> comparator, Iterator<T>... iterators) {
            this.iterators = iterators;
            this.comparator = comparator;
            this.logs = new StringBuilder("");
        }

        class MultiArrayIterator implements Iterator<T> {

            private final T[] currentIteratorValues; // it's like a 'buffer' for current iterator values

            MultiArrayIterator(int length) {
                this.currentIteratorValues = (T[]) new Object[length];
            }

            public boolean hasNext() {
                for (int i = 0; i< currentIteratorValues.length; i++) {
                    if (MultiArray.this.iterators[i].hasNext()){
                        return true;
                    }
                    if (currentIteratorValues[i] != null){
                        return true; // if iterator already done, let's return current buffered values
                    }
                }
                return false;
            }

            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                int minIndex = -1;
                T minValue = null;
                for (int i = 0; i< currentIteratorValues.length; i++) {
                    // read iterator values
                    if (currentIteratorValues[i] == null && MultiArray.this.iterators[i].hasNext()){
                        currentIteratorValues[i] = iterators[i].next();
                    }
                    // resolve minValue
                    if (minValue == null || comparator.compare(currentIteratorValues[i],minValue) < 0 ){
                        minValue = currentIteratorValues[i];
                        minIndex = i;
                    }
                }

                // cleanup iteratorValues item and return minValue
                currentIteratorValues[minIndex] = null;
                logs.append("Return '").append(minValue).append("' value from [").append(minIndex).append("] iterator\n");
                return minValue;
            }
        }

        public Iterator<T> iterator() {
            return new MultiArrayIterator(iterators.length);
        }

        public String getLogs(){
            return logs.toString();
        }
    }

    public static void main(String[] args) {
        // Create arrays with implemented iterator
        SingleArray<Integer> a0 = new SingleArray<>(new Integer[]{6, 8, 19, 21, 32, 66, 67, 77, 89});
        SingleArray<Integer> a1 = new SingleArray<>(new Integer[]{1, 3, 5,  24, 33, 45, 57, 59, 89});
        SingleArray<Integer> a2 = new SingleArray<>(new Integer[]{2, 4, 9,  18, 22, 44, 46, 89, 89});
       // Create corresponding comparator
        Comparator<Integer> comparator = Comparator.comparingInt(Integer::intValue);

        // Create MultiArray that will iterate multiple iterators
        MultiArray<Integer> multiArray = new MultiArray<>(comparator, a0.iterator(), a1.iterator(), a2.iterator());

        System.out.println("--- Iterate multiArray ---");
        for (Integer s : multiArray) {
            System.out.println(s);
        }

        System.out.println("--- Print multiArray logs ---");
        System.out.println(multiArray.getLogs());
    }

    @Test
    void multiArrayIteratorIteratesIntegers() {

        // Create array with implemented iterator
        SingleArray<Integer> a0 = new SingleArray<>(new Integer[]{1, 4, 7,10});
        SingleArray<Integer> a1 = new SingleArray<>(new Integer[]{2, 5, 8,10});
        SingleArray<Integer> a2 = new SingleArray<>(new Integer[]{3, 6, 9,10});

        Comparator<Integer> comparator = Comparator.comparingInt(Integer::intValue);

        // Create MultiArray that will iterate multiple array iterators
        MultiArray<Integer> multiArray = new MultiArray<>(comparator, a0.iterator(), a1.iterator(), a2.iterator());

        List<Integer> list = new ArrayList<>();
        multiArray.iterator().forEachRemaining(list::add);

        assertThat(list.toArray(), Matchers.arrayContaining(1,2,3,4,5,6,7,8,9,10,10,10));

        assertEquals(multiArray.getLogs(), """
                Return '1' value from [0] iterator
                Return '2' value from [1] iterator
                Return '3' value from [2] iterator
                Return '4' value from [0] iterator
                Return '5' value from [1] iterator
                Return '6' value from [2] iterator
                Return '7' value from [0] iterator
                Return '8' value from [1] iterator
                Return '9' value from [2] iterator
                Return '10' value from [0] iterator
                Return '10' value from [1] iterator
                Return '10' value from [2] iterator
                """);
    }

    @Test
    void multiArrayIteratorCanIterateOtherObjectsByProvidedComparator() {

        // Create array with implemented iterator
        SingleArray<String> a0 = new SingleArray<>(new String[]{"1", "4", "7", "test0"});
        SingleArray<String> a1 = new SingleArray<>(new String[]{"2", "5", "8", "test1"});
        SingleArray<String> a2 = new SingleArray<>(new String[]{"3", "6", "9", "test2"});

        Comparator<String> comparator = Comparator.comparing(String::toString);

        MultiArray<String> multiArray = new MultiArray<>(comparator, a0.iterator(), a1.iterator(), a2.iterator());

        List<String> list = new ArrayList<>();
        multiArray.iterator().forEachRemaining(list::add);

        assertThat(list.toArray(), Matchers.arrayContaining("1","2","3","4","5","6","7","8","9","test0","test1","test2"));

        assertEquals(multiArray.getLogs(), """
                Return '1' value from [0] iterator
                Return '2' value from [1] iterator
                Return '3' value from [2] iterator
                Return '4' value from [0] iterator
                Return '5' value from [1] iterator
                Return '6' value from [2] iterator
                Return '7' value from [0] iterator
                Return '8' value from [1] iterator
                Return '9' value from [2] iterator
                Return 'test0' value from [0] iterator
                Return 'test1' value from [1] iterator
                Return 'test2' value from [2] iterator
                """);
    }

}

