package com.zoomers.GameSetMatch.scheduler.matching.util;

import java.util.Objects;

public class Tuple {

    private final int first, second;

    private Tuple(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple tuple = (Tuple) o;

        if (this.first != tuple.getFirst()) return false;
        if (this.second != tuple.getSecond()) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "{ " + this.first + ", " + this.second + " }";
    }

    public static Tuple of(int first, int second) {
        return new Tuple(first, second);
    }
}
