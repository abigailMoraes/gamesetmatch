package com.zoomers.GameSetMatch.scheduler.matching.formatMatchers;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentFormat;
import com.zoomers.GameSetMatch.scheduler.graphs.*;
import com.zoomers.GameSetMatch.scheduler.matching.util.Tuple;
import com.zoomers.GameSetMatch.scheduler.scorers.Scorer;

import java.util.*;

public abstract class FormatMatcher {

    private Scorer scorer;
    private TournamentFormat format;

    public void initScorer(Scorer scorer) {
        this.scorer = scorer;
    }

    public void initFormat(TournamentFormat format) { this.format = format; }

    public RoundRobinGraph createRoundRobinMatches(
            List<Registrant> registrantsToMatch,
            Set<Tuple> registrantMatches,
            List<Timeslot> timeslots,
            int matchDuration
    ) {
        RoundRobinGraph roundRobinGraph = new RoundRobinGraph(registrantsToMatch, timeslots);

        for (Tuple pair : registrantMatches) {

            Registrant r1 = registrantsToMatch.stream().filter(r -> r.getID() == pair.getFirst()).findFirst().get();
            Registrant r2 = registrantsToMatch.stream().filter(r -> r.getID() == pair.getSecond()).findFirst().get();
            for (Timeslot t : timeslots) {

                if (invalidTimeslot(t, matchDuration)) {
                    continue;
                }

                if (!isMatchValid(r1, r2, t)) {
                    continue;
                }

                Match m = new Match(
                        r1.getID(),
                        r2.getID(),
                        t,
                        matchDuration,
                        Math.abs(r1.getSkill() - r1.getSkill())
                );
                m.setMatchScore(scorer.calculateMatchByScore(r1, r2, t, matchDuration));

                roundRobinGraph.addMatch(m);
            }
        }

        return roundRobinGraph;
    }

    public PrimaryMatchGraph createPossiblePrimaryMatches(BipartiteGraph bipartiteGraph) {

        PrimaryMatchGraph primaryMatchGraph = new PrimaryMatchGraph(bipartiteGraph);

        Map<Timeslot, List<Registrant>> bgList = bipartiteGraph.getAdjacencyList();

        for (Timeslot t : bgList.keySet()) {
            List<Registrant> registrants = bgList.get(t);
            for (int i = 0; i < registrants.size(); i++) {

                Registrant r1 = registrants.get(i);
                primaryMatchGraph.initializeTimeRepeat(r1.getID());

                for (int j = i+1; j < registrants.size(); j++) {

                    Registrant r2 = registrants.get(j);

                    if (!(format == TournamentFormat.DOUBLE_KNOCKOUT && registrants.size() == 2)) {
                        if (!isMatchValid(r1, r2, t)) {
                            continue;
                        }
                    }

                    Match m = new Match(
                            r1.getID(),
                            r2.getID(),
                            t,
                            bipartiteGraph.getMatchDuration(),
                            Math.abs(registrants.get(i).getSkill() - registrants.get(j).getSkill())
                    );

                    primaryMatchGraph.addMatch(m);
                }
            }
        }

        return primaryMatchGraph;
    }

    public SecondaryMatchGraph createPossibleSecondaryMatches(
            List<Registrant> registrantsToBeMatched,
            List<Timeslot> availableTimeslots,
            int matchDuration
    ){

        SecondaryMatchGraph secondaryMatchGraph = new SecondaryMatchGraph(
                registrantsToBeMatched,
                availableTimeslots
        );

        for (Timeslot t : availableTimeslots) {

            if (invalidTimeslot(t, matchDuration)) {
                continue;
            }

            for (int i = 0; i < registrantsToBeMatched.size(); i++) {

                Registrant r1 = registrantsToBeMatched.get(i);

                for (int j = i+1; j < registrantsToBeMatched.size(); j++) {

                    Registrant r2 = registrantsToBeMatched.get(j);

                    if (!(format == TournamentFormat.DOUBLE_KNOCKOUT && registrantsToBeMatched.size() == 2)) {
                        if (!isMatchValid(r1, r2, t)) {
                            continue;
                        }
                    }

                    Match m = new Match(
                            r1.getID(),
                            r2.getID(),
                            t,
                            matchDuration,
                            Math.abs(r1.getSkill() - r1.getSkill())
                    );
                    m.setMatchScore(
                            scorer.calculateMatchByScore(r1, r2, t, matchDuration) +
                            calculateMatchFormatScore(r1, r2)
                    );

                    secondaryMatchGraph.addMatch(m);
                }
            }
        }

        return secondaryMatchGraph;
    }

    public BestOfMatchGraph createPossibleBestOfMatches(
            Set<Registrant> registrants,
            Set<Timeslot> timeslots,
            Set<Match> existingMatches,
            int bestOfSeries,
            int matchDuration
    ) {

        BestOfMatchGraph bestOfMatchGraph = new BestOfMatchGraph(
                registrants,
                timeslots,
                bestOfSeries,
                matchDuration
        );

        List<Registrant> registrantsList = new ArrayList<>(registrants);

        for (Match m : existingMatches) {

            for (Timeslot t : timeslots) {

                if (Objects.equals(t.getDate(), m.getTimeslot().getDate())) {
                    continue;
                }

                if (invalidTimeslot(t, matchDuration)) {
                    continue;
                }

                Registrant r1 = registrantsList.stream().filter(r -> r.getID() == m.getPlayers().getFirst()).findFirst().get();
                Registrant r2 = registrantsList.stream().filter(r -> r.getID() == m.getPlayers().getSecond()).findFirst().get();

                if (r1.getGamesToSchedule() == 0 || r2.getGamesToSchedule() == 0) {
                    continue;
                }

                if (!(format == TournamentFormat.DOUBLE_KNOCKOUT && registrants.size() == 2)) {
                    if (!isMatchValid(r1, r2, t)) {
                        continue;
                    }
                }

                Match seriesMatch = new Match(
                        m.getPlayers().getFirst(),
                        m.getPlayers().getSecond(),
                        t,
                        matchDuration,
                        1
                );

                seriesMatch.setMatchScore(scorer.calculateMatchByScore(r1, r2, t, matchDuration));
                bestOfMatchGraph.addMatch(seriesMatch);
            }
        }

        return bestOfMatchGraph;
    }

    public BracketMatchGraph createPossibleBracketMatches(
            List<Registrant> registrants,
            List<Timeslot> timeslots,
            List<Tuple> matchedPlayers,
            int matchDuration
    ) {

        BracketMatchGraph bracketMatchGraph = new BracketMatchGraph(registrants, timeslots);

        for (Tuple pair : matchedPlayers) {

            for (Timeslot t : timeslots) {

                if (invalidTimeslot(t, matchDuration)) {
                    continue;
                }

                Registrant r1 = registrants.stream().filter(r -> r.getID() == pair.getFirst()).findFirst().get();
                Registrant r2 = registrants.stream().filter(r -> r.getID() == pair.getSecond()).findFirst().get();

                if (!isMatchValid(r1, r2, t)) {
                    continue;
                }

                Match bracketMatch = new Match(
                        pair.getFirst(),
                        pair.getSecond(),
                        t,
                        matchDuration,
                        Math.abs(r1.getSkill() - r1.getSkill())
                );

                bracketMatch.setMatchScore(scorer.calculateMatchByScore(r1, r2, t, matchDuration));
                bracketMatchGraph.addMatch(bracketMatch);
            }
        }

        return bracketMatchGraph;
    }

    private boolean isMatchValid(Registrant r1, Registrant r2, Timeslot t) {

        return areMatchConditionsSatisfied(r1, r2, t);
    }

    private boolean invalidTimeslot(Timeslot t, int matchDuration) {

        return t.getTime() + matchDuration / 30.0 > 21.0;
    }

    protected abstract boolean areMatchConditionsSatisfied(Registrant r1, Registrant r2, Timeslot t);

    protected abstract int calculateMatchFormatScore(Registrant r1, Registrant r2);
}
