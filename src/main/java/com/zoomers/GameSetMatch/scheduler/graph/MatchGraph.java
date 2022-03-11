package com.zoomers.GameSetMatch.scheduler.graph;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.graph.domain.MatchEdge;

import java.util.*;

public class MatchGraph {

    private final List<Match> matches;
    private final int matrixSize;
    private final LinkedHashMap<Match, List<Match>> matchEdges;
    private int size = 0;


    public MatchGraph(List<Match> matches) {
        this.matches = matches;
        this.matrixSize = matches.size();
        this.matchEdges = new LinkedHashMap<>();
        this.size = 0;
        buildGraph();
    }

    private void buildGraph() {

        addNodes();
    }

    private void addNodes() {

        for (Match m : this.matches) {
            matchEdges.put(m, new ArrayList<>());
        }
    }

    private boolean sharePlayers(Match m1, Match m2) {
        return m1.getPlayers().getFirst() == m2.getPlayers().getFirst() ||
                m1.getPlayers().getFirst() == m2.getPlayers().getSecond() ||
                m1.getPlayers().getSecond() == m2.getPlayers().getFirst() ||
                m1.getPlayers().getSecond() == m2.getPlayers().getSecond();
    }

    private boolean shareTimeslot(Match m1, Match m2) {
        return m1.getTimeslot() == m2.getTimeslot();
    }

    public int getDegree(Match match) {

        /*
        if (!this.matchEdges.containsKey(match)) {
            return -1;
        }*/

        return this.matchEdges.get(match).size();
    }

    public Set<Match> getMatches() {
        return this.matchEdges.keySet();
    }

    public int getSize() { return size; }

    public int getOrder() {
        return this.matchEdges.keySet().size();
    }

    public void visitMatch(Match m) {

        for (Match m2 : this.matchEdges.get(m)) {
            this.matchEdges.remove(m2);
        }

        m.setNeedsAdminAttention(false);
        this.matchEdges.remove(m);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Match m1 : this.matchEdges.keySet()) {
            s.append(m1.toString() + ": ");
            for (Match m2 : this.matchEdges.get(m1)) {
                s.append("--> " + m2.toString());
            }
            s.append("\n");
        }
        return s.toString();
    }
}
