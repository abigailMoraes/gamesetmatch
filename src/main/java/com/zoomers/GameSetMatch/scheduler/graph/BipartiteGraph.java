package com.zoomers.GameSetMatch.scheduler.graph;

import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;
import com.zoomers.GameSetMatch.scheduler.graph.domain.BipartiteEdge;
import com.zoomers.GameSetMatch.scheduler.graph.domain.BipartiteNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BipartiteGraph {

    private final List<Timeslot> timeslots;
    private final List<Registrant> players;
    private int edgeCount;
    private final HashMap<Registrant, List<Timeslot>> adjacencyList;

    public BipartiteGraph(List<Timeslot> timeslots, List<Registrant> players) {

        this.timeslots = timeslots;
        this.players = players;
        this.edgeCount = 0;

        this.adjacencyList = new HashMap<>();

        this.buildGraph();
    }

    private void buildGraph() {

        for (Registrant p : players) {

            for (Timeslot t : timeslots) {

                createAdjacencyList(p, t);
            }
        }
    }

    private void createAdjacencyList(Registrant p, Timeslot t) {

        if (p.getAvailability().charAt(t.getID()) == '1') {

            adjacencyList.put(p, new ArrayList<>());

            if (!adjacencyList.get(p).contains(t)) {

                adjacencyList.get(p).add(t);
                edgeCount++;
            }
        }
    }

    public int getEdgeCount() {
        return edgeCount;
    }

    public HashMap<Registrant, List<Timeslot>> getAdjacencyList() {
        return adjacencyList;
    }
}
