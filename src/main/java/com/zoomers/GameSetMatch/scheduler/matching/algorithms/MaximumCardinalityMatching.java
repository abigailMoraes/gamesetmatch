package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.graph.LineGraph;
import com.zoomers.GameSetMatch.scheduler.graph.domain.LineEdge;
import com.zoomers.GameSetMatch.scheduler.matching.domain.Matching;
import com.zoomers.GameSetMatch.scheduler.matching.util.IntSet;
import com.zoomers.GameSetMatch.scheduler.matching.util.Tuple;
import com.zoomers.GameSetMatch.scheduler.matching.util.UnionFind;

import java.util.*;

public class MaximumCardinalityMatching {

    // The LineGraph we're matching
    private final LineGraph graph;

    // The current matching
    private final Matching matching;

    // Subset of vertices to be matched
    private final IntSet subset;

    // Data Structures

    // Storage of the forest, even and odd levels
    private final int[] even, odd;

    // Null vertex
    private static final int nil = -1;

    // Queue of even (free) vertices to start paths from
    private final FixedSizeQueue queue;

    // Union-Find to store blossoms
    private final UnionFind uf;

    // Map stores the bridges of the blossom - indexed by support vertices
    private final Map<Integer, Tuple> bridges = new HashMap<>();

    // Temporary array to fill with path information
    private final int[] path;

    // Temporary bitsets when walking to check for paths/blossoms
    private final BitSet vAncestors, wAncestors;

    private final int numMatched;

    public MaximumCardinalityMatching(LineGraph graph, Matching matching, int numMatched, IntSet subset) {
        this.graph = graph;
        this.matching = matching;
        this.subset = subset;

        this.even = new int[graph.getOrder()];
        this.odd = new int[graph.getOrder()];

        this.queue = new FixedSizeQueue(graph.getOrder());
        this.uf = new UnionFind(graph.getOrder());

        // temp storage
        this.path = new int[graph.getOrder()];
        vAncestors = new BitSet(graph.getOrder());
        wAncestors = new BitSet(graph.getOrder());

        while (augment()) {
            numMatched += 2;
        }

        this.numMatched = numMatched;
    }

    private boolean augment() {

        // reset data structures
        Arrays.fill(even, nil);
        Arrays.fill(odd, nil);

        uf.clear();
        bridges.clear();
        queue.clear();

        for (int v = 0; v < graph.getOrder(); v++) {
            if (subset.contains(v) && matching.unmatched(v)) {
                even[v] = v;
                queue.enqueue(v);
            }
        }

        while (!queue.isEmpty()) {
            int v = queue.poll();

            int d = graph.getDegree(v);

            for (int j = 0; j < d; j++) {
                LineEdge le = graph.edgeAt(v, j);

                if (Objects.isNull(le)) {
                    continue;
                }

                int w = le.other(v);

                if (!subset.contains(w)) {
                    continue;
                }

                // If both the endpoints of the edge are on even level
                // it is either an augmenting path or a blossom
                if (even[uf.find(w)] != nil) {
                    if (check(v, w)) {
                        return true;
                    }
                }
                else if (odd[w] == nil) {
                    odd[w] = v;
                    try {
                        int u = matching.other(w);

                        if (even[uf.find(u)] == nil) {
                            even[u] = w;
                            queue.enqueue(u);
                        }
                    }
                    catch (IllegalArgumentException e) {
                        System.out.println("Exception: " + e.getMessage());
                    }
                }
            }
        }

        return false;
    }

    private boolean check(int v, int w) {

        if (uf.isConnected(v, w)) {
            return false;
        }

        vAncestors.clear();
        wAncestors.clear();
        int vCurr = v;
        int wCurr = w;

        while (true) {
            vCurr = parent(vAncestors, vCurr);
            wCurr = parent(wAncestors, wCurr);

            if (vCurr == wCurr) {
                blossom(v, w, vCurr);
                return false;
            }

            if (uf.find(even[vCurr]) == vCurr && uf.find(even[wCurr]) == wCurr) {
                augment(v);
                augment(w);
                matching.match(v, w);
                return true;
            }

            if (wAncestors.get(vCurr)) {
                blossom(v, w, vCurr);
                return false;
            }

            if (vAncestors.get(wCurr)) {
                blossom(v, w, wCurr);
                return false;
            }
        }
    }

    private int parent(BitSet ancestors, int curr) {

        curr = uf.find(curr);
        ancestors.set(curr);
        int parent = uf.find(even[curr]);

        if (parent == curr) { return curr; }

        ancestors.set(parent);

        return uf.find(odd[parent]);
    }

    private void blossom(int v, int w, int base) {
        base = uf.find(base);
        int[] supports1 = blossomSupports(v, w, base);
        int[] supports2 = blossomSupports(w, v, base);

        for (int i : supports1) {
            uf.union(i, supports1[0]);
        }

        for (int j : supports2) {
            uf.union(j, supports2[0]);
        }

        even[uf.find(base)] = even[base];
    }

    private int[] blossomSupports(int v, int w, int base) {
        int n = 0;
        path[n++] = uf.find(v);
        Tuple b = Tuple.of(v, w);
        while (path[n-1] != base) {
            int u = even[path[n-1]];
            path[n++] = u;
            this.bridges.put(u, b);
            queue.enqueue(u);
            path[n++] = uf.find(odd[u]);
        }

        return Arrays.copyOf(path, n);
    }

    private void augment(int v) {

        int n = buildPath(path, 0, v, nil);
        for (int i = 2; i < n; i += 2) {
            matching.match(path[i], path[i-1]);
        }
    }

    private int buildPath(int[] path, int i, int start, int goal) {

        while (true) {

            while (odd[start] != nil) {

                Tuple bridge = bridges.get(start);
                int j = buildPath(path, i, bridge.getFirst(), start);
                reverse(path, i, j - 1);
                i = j;

                start = bridge.getSecond();
            }

            path[i++] = start;

            if (matching.unmatched(start)) {
                return i;
            }

            path[i++] = matching.other(start);

            if (path[i - 1] == goal) {
                return i;
            }

            start = odd[path[i - 1]];
        }
    }

    public static int maximise(LineGraph g, Matching m, int n, IntSet s) {

        MaximumCardinalityMatching mcm = new MaximumCardinalityMatching(g, m, n, s);
        return mcm.numMatched;
    }

    public static int maximise(LineGraph g, Matching m, int n) {
        return maximise(g, m, n, IntSet.universe());
    }

    public static Matching maxCardinalMatch(LineGraph g) {
        Matching m = Matching.empty(g);
        maximise(g, m, 0);
        return m;
    }

    private static final class FixedSizeQueue {

        private final int[] vertices;
        private int i = 0;
        private int n = 0;

        private FixedSizeQueue(int n) {
            vertices = new int[n];
        }

        public void enqueue(int e) {
            vertices[n++] = e;
        }

        // return first element
        public int poll() {
            return vertices[i++];
        }

        public boolean isEmpty() {
            return i == n;
        }

        public void clear() {
            i = n = 0;
        }
    }

    public static void reverse(int[] path, int i, int j) {
        while (i < j) {
            int tmp = path[i];
            path[i] = path[j];
            path[j] = tmp;
            i++;
            j--;
        }
    }
}
