package com.zoomers.GameSetMatch.scheduler.matching.domain;

import com.zoomers.GameSetMatch.scheduler.graph.LineGraph;
import com.zoomers.GameSetMatch.scheduler.matching.util.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Matching {

    private static final int UNMATCHED = -1;

    private final int[] match;

    private Matching(int n) {
        this.match = new int[n];
        Arrays.fill(match, UNMATCHED);
    }

    public boolean isMatched(int v) {
        return !unmatched(v);
    }

    public boolean unmatched(int v) {
        int w = match[v];
        return w < 0 || match[w] != v;
    }
    public int other(int v) throws IllegalArgumentException {
        if (unmatched(v)) {
            throw new IllegalArgumentException(v + " is not matched");
        }
        return match[v];
    }

    public void match(int u, int v) {
        match[u] = v;
        match[v] = u;
    }

    Iterable<Tuple> matches() {
        List<Tuple> tuples = new ArrayList<Tuple>(match.length / 2);

        for (int v = 0; v < match.length; v++) {
            int w = match[v];
            if (w > v && match[w] == v) {
                tuples.add(Tuple.of(v, w));
            }
        }

        return tuples;
    }

    public static Matching empty(LineGraph lg) {
        return new Matching(lg.getOrder());
    }

    public int[] getMatch() {
        return match;
    }
}
