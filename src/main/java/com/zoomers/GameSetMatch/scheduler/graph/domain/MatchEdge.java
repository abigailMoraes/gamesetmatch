package com.zoomers.GameSetMatch.scheduler.graph.domain;

import com.zoomers.GameSetMatch.scheduler.matching.util.Tuple;

public class MatchEdge {

    private int label = -1;
    private final int m1, m2, xor;

    public MatchEdge(int m1, int m2) {
        this.m1 = m1;
        this.m2 = m2;
        this.xor = m1 ^ m2;
    }

    public void setLabel(int i) {
        label = i;
    }
    
    public int getM1() {
        return m1;
    }

    public int getM2() {
        return m2;
    }

    public int other (int x) {
        return x ^ xor;
    }

    @Override
    public int hashCode() {
        return xor;
    }



    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        final MatchEdge o = (MatchEdge) other;
        return (m1 == o.getM1() && m2 == o.getM2()) ||
                (m1 == o.getM2() && m2 == o.getM1());
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return new StringBuilder(20).append('{')
                .append(" Match " + m1)
                .append(", ")
                .append(" Match " + m2)
                .append('}')
                .toString();
    }
}
