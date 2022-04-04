/**
 * Directed Bipartite Graph from timeslots to registrants.
 * An edge between nodes exists if a registrant is available at that time.
 * Represented as an adjacency list.
 *
 * @since 2022-03-21
 */

package com.zoomers.GameSetMatch.scheduler.graphs;

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
    private final int matchDuration;

    public BipartiteGraph(List<Timeslot> timeslots, List<Registrant> players, int matchDuration) {

        this.timeslots = timeslots;
        this.players = players;
        this.edgeCount = 0;
        this.matchDuration = matchDuration;
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

    /**
     * We create an edge from t to r if:
     * - the time of the match will not exceed 9 pm
     * - Registrant r is availability for the duration of the match
     *
     * @param r, registrant
     * @param t, timeslot
     */
    private void createAdjacencyList(Registrant r, Timeslot t) {

        // TODO: THIS DOESN'T KEEP TRACK OF 9PM
        // UPDATE TO t get time and match duration
        if (t.getTime() + matchDuration / 30.0 > 21.0) {

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

    public int getMatchDuration() {
        return matchDuration;
    }

    public List<Registrant> getPlayers() {
        return players;
    }

    public List<Timeslot> getTimeslots() {
        return timeslots;
    }

    public LinkedHashMap<Timeslot, List<Registrant>> getAdjacencyList() {
        return adjacencyList;
    }

    public void printGraph() {
        for (Timeslot t : adjacencyList.keySet()) {
            System.out.println("\nVertex " + t.toString() + ":");
            for (Registrant r : adjacencyList.get(t)) {
                System.out.print(" -> " + r.getID());
            }
        }
        System.out.println();
    }
}
