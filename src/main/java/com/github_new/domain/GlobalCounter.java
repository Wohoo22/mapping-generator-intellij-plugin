package com.github_new.domain;

public class GlobalCounter {
    private static Integer counter;

    public static void reset() {
        counter = 0;
    }

    public static void inc() {
        counter++;
    }

    public static Integer get() {
        return counter;
    }
}
