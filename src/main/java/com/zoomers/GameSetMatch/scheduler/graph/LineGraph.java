package com.zoomers.GameSetMatch.scheduler.graph;

import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;
import com.zoomers.GameSetMatch.scheduler.graph.domain.BipartiteNode;
import com.zoomers.GameSetMatch.scheduler.graph.domain.LineEdge;
import com.zoomers.GameSetMatch.scheduler.graph.domain.LineNode;

import javax.sound.sampled.Line;
import java.util.*;

public class LineGraph {

    private final BipartiteGraph bg;
    private LineNode[] lineNodes;
    private int[] degrees;
    private final int matrixSize;
    private LineEdge[][] edges;

    private int order, size = 0;

    public LineGraph(BipartiteGraph bg) {
        this.bg = bg;
        this.matrixSize = bg.getEdgeCount();
        this.lineNodes = new LineNode[matrixSize];
        this.edges = new LineEdge[matrixSize][matrixSize];
        this.degrees = new int[matrixSize];

        buildGraph();
    }

    private void buildGraph() {

        addNodes();
        addEdges();
    }

    private void addNodes() {

        HashMap<Registrant, List<Timeslot>> adjList = bg.getAdjacencyList();

        for (Registrant r : adjList.keySet()) {

            for (Timeslot t : adjList.get(r)) {

                LineNode ln = new LineNode(r, t);
                if (!Arrays.asList(lineNodes).contains(ln)) {
                    lineNodes[this.order++] = ln;
                }
                else {
                    ln.decreaseID();
                }
            }
        }
    }

    private void addEdges() {

        for (int i = 0; i < matrixSize; i++) {

            for (int j = i + 1; j < matrixSize; j++) {

                if (lineNodes[i].getTimeslot() == lineNodes[j].getTimeslot()) {
                    LineEdge le = new LineEdge(i, j);

                    int u = le.either(), v = le.other(u);
                    ensureEdgeCapacity(u);
                    ensureEdgeCapacity(v);

                    edges[u][degrees[u]++] = le;
                    edges[v][degrees[v]++] = le;
                    this.size++;
                }
            }
        }
    }

    public int getDegree(int i) {
        return degrees[i];
    }

    public List<LineEdge> getEdges(int i) {

        List<LineEdge> lineEdges = Arrays.asList(Arrays.copyOf(edges[i], matrixSize));
        lineEdges.removeIf(Objects::isNull);

        return lineEdges;
    }

    public int[] getNeighbours(int e) {
        List<LineEdge> lineEdges = getEdges(e);
        int[] vertices = new int[lineEdges.size()];
        int degree = lineEdges.size();

        for (int i = 0; i < degree; i++) {
            vertices[i] = lineEdges.get(i).other(e);
        }
        Arrays.sort(vertices);
        return vertices;
    }

    public boolean isAdjacent(int u, int v) {
        int d = degrees[u];
        for (int i = 0; i < d; i++) {
            LineEdge edge = edges[u][v];
            if (edge.other(u) == v) {
                return true;
            }
        }
        return false;
    }

    public LineEdge findEdge(int u, int v) {
        int d = degrees[u];
        for (int i = 0; i < d; i++) {
            if (edges[u][i] == null) {
                continue;
            }

            LineEdge edge = edges[u][i];
            if (edge.other(u) == v) {
                return edge;
            }
        }
        throw new IllegalArgumentException(u + ", " + v + " are not adjacent");
    }

    public LineEdge edgeAt(int u, int i) {
        return edges[u][i];
    }

    public int getOrder() {
        return order;
    }

    public int getSize() {
        return size;
    }

    private void ensureCapacity() {
        if (order >= lineNodes.length) {
            lineNodes      = Arrays.copyOf(lineNodes, order * 2);
            degrees    = Arrays.copyOf(degrees, order * 2);
            edges      = Arrays.copyOf(edges, order * 2);
        }
    }

    private void ensureEdgeCapacity(int i) {
        if (degrees[i] == edges[i].length)
            edges[i] = Arrays.copyOf(edges[i], degrees[i] + 2);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < matrixSize; i++) {
            s.append(i + ": ");
            for (LineEdge j : edges[i]) {
                if (j != null) {
                    s.append(1 + " ");
                }
                else {
                    s.append(0 + " ");
                }
            }
            s.append("\n");
        }
        return s.toString();
    }
}
