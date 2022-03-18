package com.zoomers.GameSetMatch.scheduler;

import com.zoomers.GameSetMatch.entity.Tournament;
import com.zoomers.GameSetMatch.scheduler.abstraction.TypeMatcher;
import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.domain.MockTournament;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;
import com.zoomers.GameSetMatch.scheduler.enumerations.Skill;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentType;
import com.zoomers.GameSetMatch.scheduler.graph.BipartiteGraph;
import com.zoomers.GameSetMatch.scheduler.graph.PrimaryMatchGraph;
import com.zoomers.GameSetMatch.scheduler.graph.SecondaryMatchGraph;
import com.zoomers.GameSetMatch.scheduler.matching.algorithms.GreedyMaximumIndependentSet;
import com.zoomers.GameSetMatch.scheduler.matching.algorithms.GreedyMinimumWeightIndependentSet;
import com.zoomers.GameSetMatch.scheduler.matching.algorithms.MatchingAlgorithm;
import com.zoomers.GameSetMatch.scheduler.matching.algorithms.MaximumMatchScoreMatcher;
import com.zoomers.GameSetMatch.scheduler.matching.formatMatchers.DoubleKnockoutMatcher;
import com.zoomers.GameSetMatch.scheduler.matching.formatMatchers.RoundRobinMatcher;
import com.zoomers.GameSetMatch.scheduler.matching.formatMatchers.SingleKnockoutMatcher;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.springframework.data.jpa.repository.Query;

import java.io.*;
import java.util.*;

public class Scheduler {

    private final List<Registrant> registrants = new ArrayList<>();
    private final List<Timeslot> timeslots = new ArrayList<>();
    private String playerFileName;
    private MockTournament tournament;
    private TypeMatcher typeMatcher;

    public Scheduler(MockTournament tournament, String filename) {

        this.tournament = tournament;
        this.playerFileName = filename;

        setTypeMatcher(tournament.getTournamentType());
    }

    public Scheduler(int tournamentID) {
    }

    private void setTypeMatcher(TournamentType type) {

        switch (type) {
            case SINGLE_KNOCKOUT:
                typeMatcher = new SingleKnockoutMatcher();
                break;
            case DOUBLE_KNOCKOUT:
                typeMatcher = new DoubleKnockoutMatcher();
                break;
            case ROUND_ROBIN:
                typeMatcher = new RoundRobinMatcher();
                break;
        }
    }

    public void schedule() {

        Set<Match> returnedMatches = new LinkedHashSet<>(schedulePrimaryMatches());

        returnedMatches.addAll(scheduleSecondaryMatches(returnedMatches));
    }

    private Set<Match> schedulePrimaryMatches() {

        initPlayers();

        try {
            initTimeslots();
        }
        catch (IOException e) {
            System.out.println("IOError: " + e.getMessage());
        }

        // TODO: FIND REGISTRANTS USING TOURNAMENT_ID AND INSTANTIATE TIMESLOTS FROM DATABASE

        BipartiteGraph bg = new BipartiteGraph(timeslots, registrants, tournament.getMatchDuration());

        PrimaryMatchGraph matchGraph = typeMatcher.createPossiblePrimaryMatches(bg);

        for (Match m : matchGraph.getMatches()) {
            System.out.println("Possible Match: " + m);
        }

        matchGraph.setMatchDegrees();
        MatchingAlgorithm greedyMaximumIndependentSet = getMatchingAlgorithm(tournament.isMatchBySkill(), matchGraph);

        Set<Match> matches = greedyMaximumIndependentSet.findMatches();

        for (Match m : matches) {
            System.out.println("Primary Match: " + m);
        }

        return matches;
    }

    private MatchingAlgorithm getMatchingAlgorithm(boolean isMatchBySkill, PrimaryMatchGraph matchGraph) {

        if (isMatchBySkill)
        {
            return new GreedyMinimumWeightIndependentSet(matchGraph);
        }
        else
        {
            return new GreedyMaximumIndependentSet(matchGraph);
        }
    }

    private Set<Match> scheduleSecondaryMatches(Set<Match> matches) {

        List<Registrant> registrantsToBeMatched = findRegistrantsToBeMatched(matches);
        List<Timeslot> availableTimeslots = findAvailableTimeslots(matches);

        SecondaryMatchGraph secondaryMatchGraph = typeMatcher.createPossibleSecondaryMatches(
                registrantsToBeMatched,
                availableTimeslots,
                tournament.getMatchDuration()
        );

        MatchingAlgorithm maximumMatchScoreMatcher = new MaximumMatchScoreMatcher(secondaryMatchGraph);

        Set<Match> newMatches = maximumMatchScoreMatcher.findMatches();

        for (Match m : newMatches) {
            System.out.println("Secondary Match: " + m);
        }

        return newMatches;
    }

    private List<Registrant> findRegistrantsToBeMatched(Set<Match> returnedMatches) {

        List<Registrant> toBeMatched = new ArrayList<>(this.registrants);
        toBeMatched.removeIf(registrant -> {
            for (Match m : returnedMatches) {
                if (registrant.getID() == m.getPlayers().getFirst() ||
                        registrant.getID() == m.getPlayers().getSecond()) {
                    return true;
                }
            }
            return false;
        });

        return toBeMatched;
    }

    private List<Timeslot> findAvailableTimeslots(Set<Match> returnedMatches) {

        List<Timeslot> availableTimeslots = new ArrayList<>(this.timeslots);
        availableTimeslots.removeIf(timeslot -> {
            for (Match m : returnedMatches) {
                if (timeslot.getID() == m.getTimeslot().getID()) {
                    return true;
                }
            }
            return false;
        });

        return availableTimeslots;
    }

    private void initPlayers() {

        JSONParser parser = new JSONParser();
        try {

            JSONArray a = (JSONArray) parser.parse(new FileReader(playerFileName));
            for (Object o : a) {
                JSONObject registrant = (JSONObject) o;
                int id = Integer.parseInt((String)registrant.get("id"));
                String availability = (String)registrant.get("availability");

                Skill skill;

                if (tournament.isMatchBySkill()) {
                    skill = Skill.values()[Integer.parseInt((String)registrant.get("skill")) - 1];
                }
                else {
                    skill = Skill.BEGINNER;
                }

                Registrant r = new Registrant(id, availability, skill);
                registrants.add(r);
            }
        }
        catch (Exception e) {

            System.out.println("Exception: " + e);
        }
    }

    private void initTimeslots() throws IOException {
        File slots = new File("./data/Timeslots");
        Scanner scanner = new Scanner(slots);

        while (scanner.hasNext()) {
            Timeslot t = new Timeslot(Float.parseFloat(scanner.nextLine()));
            this.timeslots.add(t);
        }
    }

}
