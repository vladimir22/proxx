package com.vladimir22.agmatics;

import java.util.List;
import java.util.stream.Collectors;

public class DevicesDataDAO {


    public static boolean isDeviceIdExist(String deviceId) {
        return false;
    }


    public static void  main (String args   [] ) {

        // Get sum of all even numbers double numbers
        List<String> nums = List.of("4.8", "-1", "-4", "3.5", "2", "4");
        Double sum = nums.stream()
                .map(Double::parseDouble)
                .peek(n -> System.out.println("number: " + n + " is even: " + (n % 2 == 0)))
                .filter(n -> n % 2 == 0)
                .collect(Collectors.summingDouble(Double::doubleValue));

        System.out.println("sum: " + sum);



        // write fibonacci sequence using stream









    }
}
