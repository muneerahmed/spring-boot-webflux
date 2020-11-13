package com.java;

import java.util.Arrays;
import java.util.List;

public class MethodReference {

    public static void main(String[] args) {

        List<String> alphabets = Arrays.asList("a", "b", "c");

        alphabets.stream().map(i -> i.toUpperCase()).forEach(i -> System.out.println(i));

        alphabets.stream().map(String::toUpperCase).forEach(System.out::println);

    }
}
