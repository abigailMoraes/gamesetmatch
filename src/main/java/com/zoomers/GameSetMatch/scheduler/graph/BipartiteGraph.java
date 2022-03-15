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
    private int matchIndex;

    public BipartiteGraph(List<Timeslot> timeslots, List<Registrant> players, int matchDuration) {

        this.timeslots = timeslots;
        this.players = players;
        this.edgeCount = 0;

        this.adjacencyList = new LinkedHashMap<>();

        setMatchIndex(matchDuration);

        this.buildGraph();
    }

    private void setMatchIndex(int matchDuration) {

        this.matchIndex = (int) Math.ceil(matchDuration / 30.0);
    }

    private void buildGraph() {

        for (Timeslot t : timeslots) {
            for (Registrant r : players) {
                createAdjacencyList(r, t);
            }
        }
    }

    private void createAdjacencyList(Registrant r, Timeslot t) {

        if (t.getID() + matchIndex > timeslots.size()) {
            return;
        }

        for (int i = t.getID(); i < t.getID() + matchIndex; i++) {

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
