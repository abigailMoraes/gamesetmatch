package com.zoomers.GameSetMatch.scheduler.matching.algorithms;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentFormat;
import com.zoomers.GameSetMatch.scheduler.graphs.BipartiteGraph;
import com.zoomers.GameSetMatch.scheduler.graphs.MatchGraph;
import com.zoomers.GameSetMatch.scheduler.graphs.PrimaryMatchGraph;

import java.util.ArrayList;
import java.util.List;

public class PriorityMatchingAlgorithm {

    private final PrimaryMatchGraph primaryMatchGraph;
    private final List<Registrant> registrantsToMatch;
    private final TournamentFormat format;
    private final int currentRound;

    public PriorityMatchingAlgorithm(
            PrimaryMatchGraph primaryMatchGraph,
            List<Registrant> registrantsToMatch,
            TournamentFormat format,
            int currentRound
    ) {

        this.primaryMatchGraph = primaryMatchGraph;
        this.registrantsToMatch = new ArrayList<>(registrantsToMatch);
        this.format = format;
        this.currentRound = currentRound;

        pruneRegistrants();
    }

    private void pruneRegistrants() {

        if (this.format == TournamentFormat.ROUND_ROBIN) {

            pruneRegistrantsRoundRobin();
        }
        else {

            pruneRegistrantsKnockout();
        }

    }

    private void pruneRegistrantsRoundRobin() {

        int expectedGamesPlayed = this.registrantsToMatch.size() - currentRound;
        this.registrantsToMatch.removeIf(r -> r.getPlayersToPlay().size() > expectedGamesPlayed);
    }

    private void pruneRegistrantsKnockout() {

    }

    public Match findPriorityMatches() {

        return null;
    }

    public PrimaryMatchGraph getUpdatedMatchGraph() {
        return this.primaryMatchGraph;
    }
}
