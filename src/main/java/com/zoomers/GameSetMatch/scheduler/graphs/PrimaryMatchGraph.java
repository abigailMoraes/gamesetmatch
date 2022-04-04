/**
 * Undirected (Weighted) Graph represented as:
 * - Vertices: Possible Matches (r1, r2, t)
 * - Edges: If two matches share a player or timeslot
 *
 * NOTE ABOUT DEGREE COUNTING:
 *
 * In order to reduce space overhead, we only store a single instance of each match
 * and calculate vertex degrees as a function of:
 * - number of matches r1 has +
 * - number of matches r2 has +
 * - number of matches per timeslot -
 * - number of overlapping edges for players -
 * - number of overlapping edges for time
 *
 * @since 2022-03-21
 */

package com.zoomers.GameSetMatch.scheduler.graphs;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;
import com.zoomers.GameSetMatch.scheduler.matching.util.Tuple;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class PrimaryMatchGraph extends MatchGraph {

    private final HashMap<Integer, Integer> playerDegrees = new HashMap<>();
    private Integer[] timeDegrees;
    private final HashMap<Integer, Integer[]> timeRepeats = new HashMap<>();
    private final HashMap<Tuple, Integer> playerRepeats = new HashMap<>();

    public PrimaryMatchGraph(BipartiteGraph bipartiteGraph) {

        super(new HashSet<>(bipartiteGraph.getPlayers()),
                new LinkedHashSet<>(bipartiteGraph.getTimeslots()),
                new HashSet<>()
        );

        initializeTimeDegrees();
    }

    @Override
    public void addMatch(Match m) {

        if (!matches.contains(m)) {
            matches.add(m);

            int p1_id = m.getPlayers().getFirst();
            int p2_id = m.getPlayers().getSecond();
            Timeslot t = m.getTimeslot();

            initializePlayerDegrees(p1_id);
            initializePlayerDegrees(p2_id);
            initializeTimeRepeat(p2_id);
            initializePlayerRepeat(p1_id, p2_id);
            incrementDegrees(t, p1_id, p2_id);
        }
        else {
            System.out.println("Already contains " + m);
        }
    }

    private void initializePlayerDegrees(int id) {

        if (!this.playerDegrees.containsKey(id)) {
            this.playerDegrees.put(id, 0);
        }
    }

    private void initializeTimeDegrees() {

        this.timeDegrees = new Integer[this.timeslots.size()];
        Arrays.fill(timeDegrees, -1);
    }

    /**
     *
     * @param id
     */
    public void initializeTimeRepeat(int id) {

        if (!this.timeRepeats.containsKey(id)) {
            Integer[] j_timeslotRepeats = new Integer[this.timeslots.size()];
            Arrays.fill(j_timeslotRepeats, -1);
            this.timeRepeats.put(id, j_timeslotRepeats);
        }
    }

    /**
     * playerRepeats accounts for whether an extra edge has been added between
     * two matches that share both players.
     *
     * Given:
     * - M1 P1 v P2 at 9
     * - M2 P1 v P2 at 10
     *
     * Both P1 and P2 would increment their degrees (+2), even though only
     * one edge should exist between this match.
     *
     * @param p1_id
     * @param p2_id
     */
    private void initializePlayerRepeat(int p1_id, int p2_id) {

        Tuple pair = Tuple.of(p1_id, p2_id);

        if (!this.playerRepeats.containsKey(pair)) {

            this.playerRepeats.put(pair, -1);
        }
    }

    private void incrementDegrees(Timeslot t, int p1_id, int p2_id) {

        this.playerDegrees.put(p1_id, this.playerDegrees.get(p1_id) + 1);
        this.playerDegrees.put(p2_id, this.playerDegrees.get(p2_id) + 1);
        this.timeDegrees[t.getID()]++;
        this.timeRepeats.get(p1_id)[t.getID()]++;
        this.timeRepeats.get(p2_id)[t.getID()]++;
        this.playerRepeats.put(Tuple.of(p1_id, p2_id), this.playerRepeats.get(Tuple.of(p1_id, p2_id)) + 1);
    }

    public void decrementDegree(Match match) {

        int m1First = match.getPlayers().getFirst();
        int m1Second = match.getPlayers().getSecond();
        int m1Time = match.getTimeslot().getID();
        this.playerDegrees.put(m1First, this.playerDegrees.get(m1First) - 1);
        this.playerDegrees.put(m1Second, this.playerDegrees.get(m1Second) - 1);
        this.timeDegrees[m1Time]--;
        this.timeRepeats.get(m1First)[m1Time]--;
        this.timeRepeats.get(m1Second)[m1Time]--;
        this.playerRepeats.put(Tuple.of(m1First, m1Second), this.playerRepeats.get(Tuple.of(m1First, m1Second)) - 1);
    }

    public void setMatchDegrees() {

        for (Match m : matches) {

            int p1Edges = playerDegrees.get(m.getPlayers().getFirst());
            int p2Edges = playerDegrees.get(m.getPlayers().getSecond());
            int tEdges = timeDegrees[m.getTimeslot().getID()];
            int d1Edges = timeRepeats.get(m.getPlayers().getFirst())[m.getTimeslot().getID()];
            int d2Edges = timeRepeats.get(m.getPlayers().getSecond())[m.getTimeslot().getID()];
            int prEdges = playerRepeats.get(Tuple.of(m.getPlayers().getFirst(), m.getPlayers().getSecond()));
            int degrees = p1Edges + p2Edges + tEdges - (d1Edges + d2Edges + prEdges);

            m.setDegrees(degrees);
        }
    }
}
