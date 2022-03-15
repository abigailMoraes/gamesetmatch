package com.zoomers.GameSetMatch.scheduler.graph;

import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class BipartiteGraph {

    private final List<Timeslot> timeslots;
    private final List<Registrant> players;
    private int edgeCount;
    private final LinkedHashMap<Timeslot, List<Registrant>> adjacencyList;
    private float matchDuration;

    public BipartiteGraph(List<Timeslot> timeslots, List<Registrant> players, int matchDuration) {

        this.timeslots = timeslots;
        this.players = players;
        this.edgeCount = 0;

        this.adjacencyList = new LinkedHashMap<>();

        setMatchDuration(matchDuration);

        this.buildGraph();
    }

    private void setMatchDuration(int matchDuration) {

        float matchInterval = matchDuration / 30f;
        this.matchDuration = (float) Math.ceil(matchInterval * 2) / 2;
    }

    private void buildGraph() {

        for (Timeslot t : timeslots) {
            for (Registrant r : players) {
                createAdjacencyList(r, t);
            }
        }
    }

    private void createAdjacencyList(Registrant r, Timeslot t) {

        for (int i = t.getID(); i < t.getID() + matchDuration; i++) {

            if (r.getAvailability().charAt(i) != '1') {

                return;
            }
        }

        if (!adjacencyList.containsKey(t)) {
            adjacencyList.put(t, new ArrayList<>());
        }

        if (!adjacencyList.get(t).contains(r)) {
            adjacencyList.get(t).add(r);
            edgeCount++;
        }
    }

    public int getEdgeCount() {
        return edgeCount;
    }

    public LinkedHashMap<Timeslot, List<Registrant>> getAdjacencyList() {
        return adjacencyList;
    }

    public void printGraph() {
        for (Timeslot t : adjacencyList.keySet()) {
            System.out.println("\nVertex " + t.getTime() + ":");
            for (Registrant r : adjacencyList.get(t)) {
                System.out.print(" -> " + r.getID());
            }
        }
        System.out.println();
    }
}
