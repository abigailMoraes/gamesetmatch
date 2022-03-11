package com.zoomers.GameSetMatch.scheduler.matching.util;

import java.util.BitSet;

public abstract class IntSet {

    public abstract boolean contains(int x);

    public static IntSet universe() {
        return UNIVERSE;
    }

    public static IntSet empty() {
        return complement(universe());
    }

    public static IntSet allOf(int x, int... xs) {
        BitSet s = new BitSet();
        s.set(x);
        for (int v : xs) {
            s.set(v);
        }
        return new BinarySet(s);
    }

    public static IntSet noneOf(int x, int... xs) {
        return complement(allOf(x, xs));
    }

    public static IntSet fromBitSet(BitSet s) {
        return new BinarySet((BitSet) s.clone());
    }

    private static IntSet complement(IntSet set) {
        return new Complement(set);
    }

    private static final class BinarySet extends IntSet {

        private final BitSet set;

        private BinarySet(BitSet set) {
            this.set = set;
        }

        @Override
        public boolean contains(int x) {
            return this.set.get(x);
        }
    }

    private static final class Complement extends IntSet {

        private final IntSet delegate;

        private Complement(IntSet delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean contains(int x) {
            return !delegate.contains(x);
        }
    }

    private static final IntSet UNIVERSE = new IntSet() {
        @Override
        public boolean contains(int x) {
            return true;
        }
    };
}
