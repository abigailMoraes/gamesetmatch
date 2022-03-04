package com.zoomers.GameSetMatch.scheduler.graph;

import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;
import com.zoomers.GameSetMatch.scheduler.graph.domain.BipartiteNode;
import com.zoomers.GameSetMatch.scheduler.graph.domain.LineNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LineGraph {

    private BipartiteGraph bg;
    private List<LineNode> lineNodes;
    private int matrixSize;
    private boolean[][] adjMatrix;

    public LineGraph(BipartiteGraph bg) {
        this.bg = bg;
        this.lineNodes = new ArrayList<>();
        this.matrixSize = bg.getEdgeCount();
        this.adjMatrix = new boolean[matrixSize][matrixSize];

        for (boolean[] b : adjMatrix) {
            Arrays.fill(b, false);
        }

        buildGraph();
    }

    private void buildGraph() {

        HashMap<Registrant, List<Timeslot>> adjList = bg.getAdjacencyList();

        for (Registrant r : adjList.keySet()) {

            for (Timeslot t : adjList.get(r)) {

                LineNode ln = new LineNode(r, t);
                if (!lineNodes.contains(ln)) {
                    lineNodes.add(ln);
                }
                else {
                    ln.decreaseID();
                }
            }
        }

        for (int i = 0; i < lineNodes.size(); i++) {

            for (int j = i + 1; j < lineNodes.size(); j++) {

                if (lineNodes.get(i).getTimeslot() == lineNodes.get(j).getTimeslot()) {

                    adjMatrix[lineNodes.get(i).getId()][lineNodes.get(j).getId()] = true;
                    adjMatrix[lineNodes.get(j).getId()][lineNodes.get(i).getId()] = true;
                }
            }
        }
    }
}
