package com.zoomers.GameSetMatch.scheduler.matching.util;

import java.util.Arrays;

public class UnionFind {

    final int[] forest;

    public UnionFind(int numPlayers) {
        this.forest = new int[numPlayers];
        Arrays.fill(this.forest, -1);
    }

    public int find(int u) {
        return forest[u] < 0 ? u : (forest[u] = find(forest[u]));
    }

    public void union(int u, int v) {
        int uBase = find(u);
        int vBase = find(v);

        if (uBase == vBase) {
            return;
        }

        if (forest[uBase] < forest[vBase]) {
            join(vBase, uBase);
        }
        else {
            join(uBase, vBase);
        }
    }

    private void join(int sBase, int dBase) {
        forest[sBase] = forest[sBase] + forest[dBase];
        forest[dBase] = sBase;
    }

    public boolean isConnected(int u, int v) {

        return find(u) == find(v);
    }

    public void clear() {
        Arrays.fill(forest, -1);
    }
}
