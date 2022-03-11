package com.zoomers.GameSetMatch.scheduler.graph.domain;

public class LineEdge {

    private final int u, v, xor;

    public LineEdge(int u, int v) {

        this.u = u;
        this.v = v;
        this.xor = u ^ v;
    }

    public int either() {
        return u;
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
        final LineEdge o = (LineEdge) other;
        return (u == o.u && v == o.v) ||
                (u == o.v && v == o.u);
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return new StringBuilder(20).append('{')
                .append(u)
                .append(", ")
                .append(v)
                .append('}')
                .toString();
    }
}
