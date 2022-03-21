/**
 * Controller for the Scheduling Algorithm; given a Tournament ID,
 * the algorithm will produce a schedule for all players registered
 * in the tournament.
 *
 * @version 2.16
 */

package com.zoomers.GameSetMatch.scheduler;

import com.zoomers.GameSetMatch.entity.Tournament;
import com.zoomers.GameSetMatch.repository.TournamentRepository;
import com.zoomers.GameSetMatch.scheduler.abstraction.TypeMatcher;
import com.zoomers.GameSetMatch.scheduler.domain.*;
import com.zoomers.GameSetMatch.scheduler.enumerations.*;
import com.zoomers.GameSetMatch.scheduler.abstraction.graph.*;
import com.zoomers.GameSetMatch.scheduler.matching.algorithms.*;
import com.zoomers.GameSetMatch.scheduler.matching.formatMatchers.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Scheduler {

    private final TournamentRepository tournamentRepository;

    private final List<Registrant> REGISTRANTS = new ArrayList<>();
    private List<Timeslot> timeslots = new ArrayList<>();
    private String playerFileName;
    private MockTournament tournament;
    private TypeMatcher typeMatcher;
    private final Calendar CALENDAR = Calendar.getInstance();

    public Scheduler(MockTournament tournament, String filename) {

        this.tournament = tournament;
        this.playerFileName = filename;
        this.CALENDAR.setTime(this.tournament.getStartDate());

        setTypeMatcher(tournament.getTournamentType());
        initPlayers();

        try {
            initTimeslots();
        }
        catch (IOException e) {
            System.out.println("IOError: " + e.getMessage());
        }
    }

    public Scheduler(int tournamentID) {

        // this.tournamentRepository = new TournamentRepository();
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

    /**
     * Creates matches built on 3 scheduling variations:
     * - Primary Scheduling: Selects matches for which both players are available and match conditions are satisfied
     * - Secondary Scheduling: Schedules all players who were not matched in Primary Scheduling to a match
     * - Best-Of Scheduling: Scheduling matches that occur in series, e.g. Best-of-5
     *
     * @return set of scheduled matches
     */
    public Set<Match> schedule() {

        Set<Match> returnedMatches = new LinkedHashSet<>();

        returnedMatches.addAll(schedulePrimaryMatches());
        returnedMatches.addAll(scheduleSecondaryMatches(returnedMatches));

        if (tournament.getTournamentSeries() != TournamentSeries.BEST_OF_1) {

            int expectedMatches = (this.REGISTRANTS.size() / 2) * tournament.getTournamentSeries().getNumberOfGames();
            returnedMatches.addAll(scheduleBestOfMatches(returnedMatches, expectedMatches));
        }

        Date roundEndDate = ((Match)returnedMatches.toArray()[returnedMatches.size() - 1]).getTimeslot().getDate();
        tournament.setRoundEndDate(roundEndDate);

        for (Match m : returnedMatches) {

            System.out.println(m);
        }

        return returnedMatches;
    }

    private Set<Match> schedulePrimaryMatches() {

        // TODO: FIND REGISTRANTS USING TOURNAMENT_ID AND INSTANTIATE TIMESLOTS FROM DATABASE

        Set<Match> matches = new LinkedHashSet<>();
        List<Registrant> registrantsToMatch = new ArrayList<>(REGISTRANTS);

        while (true) {

            System.out.println(CALENDAR.getTime());

            BipartiteGraph bg = new BipartiteGraph(timeslots, registrantsToMatch, tournament.getMatchDuration());
            PrimaryMatchGraph matchGraph = typeMatcher.createPossiblePrimaryMatches(bg);

            if (matchGraph.getMatches().size() == 0) {
                break;
            }

            matchGraph.setMatchDegrees();
            MatchingAlgorithm greedyMaximumIndependentSet = getMatchingAlgorithm(tournament.isMatchBySkill(), matchGraph);

            matches.addAll(greedyMaximumIndependentSet.findMatches());

            for (Match m : matches) {
                registrantsToMatch.removeIf(registrant ->
                        m.getPlayers().getFirst() == registrant.getID() ||
                                m.getPlayers().getSecond() == registrant.getID()
                );
            }

            addWeek();
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

        List<Registrant> registrantsToMatch = findRegistrantsToBeMatched(matches);
        List<Timeslot> availableTimeslots = findAvailableTimeslots(matches);
        Set<Match> newMatches = new LinkedHashSet<>();

        while (registrantsToMatch.size() != 0) {

            SecondaryMatchGraph secondaryMatchGraph = typeMatcher.createPossibleSecondaryMatches(
                    registrantsToMatch,
                    availableTimeslots,
                    tournament.getMatchDuration()
            );

            MatchingAlgorithm maximumMatchScoreMatcher = new MaximumMatchScoreMatcher(secondaryMatchGraph);

            newMatches.addAll(maximumMatchScoreMatcher.findMatches());

            for (Match m : newMatches) {
                registrantsToMatch.removeIf(registrant ->
                        m.getPlayers().getFirst() == registrant.getID() ||
                                m.getPlayers().getSecond() == registrant.getID()
                );
            }

            addWeek();
        }

        return newMatches;
    }

    private Set<Match> scheduleBestOfMatches(Set<Match> matches, int expectedMatches) {

        if (this.tournament.getTournamentSeries().getNumberOfGames() == 1) {
            return Set.of();
        }

        Set<Match> matchesToSchedule = new LinkedHashSet<>(matches);

        while (matches.size() < expectedMatches) {

            BestOfMatchGraph bestOfMatchGraph = typeMatcher.createPossibleBestOfMatches(
                    new LinkedHashSet<>(REGISTRANTS),
                    new LinkedHashSet<>(timeslots),//findAvailableTimeslots(matches)),
                    matchesToSchedule,
                    this.tournament.getTournamentSeries().getNumberOfGames(),
                    this.tournament.getMatchDuration()
            );

            MatchingAlgorithm bestOfMatching = new BestOfMatchingAlgorithm(bestOfMatchGraph);
            Set<Match> bestOfMatches = new LinkedHashSet<>(bestOfMatching.findMatches());

            if (bestOfMatches.size() < matchesToSchedule.size()) {

                Set<Match> matchesAlreadyScheduled = matchesToSchedule.stream().limit(bestOfMatches.size()).collect(Collectors.toCollection(LinkedHashSet::new));

                matchesToSchedule = matchesToSchedule.stream()
                        .skip(bestOfMatches.size())
                        .limit(matches.size() - bestOfMatches.size())
                        .collect(Collectors.toCollection(LinkedHashSet::new));

                matchesToSchedule.addAll(matchesAlreadyScheduled);
            }

            addWeek();
            matches.addAll(bestOfMatches);
        }

        return matches;
    }

    /**
     *  Helper function to facilitate scheduling games across more than one week.
     *  Since availability is recurring weekly, the availability is reused for
     *  scheduling next week's matches.
     */
    private void addWeek() {

        CALENDAR.add(Calendar.WEEK_OF_YEAR, 1);

        try {
            initTimeslots();
        }
        catch (IOException e) {
            System.out.println("IOError: " + e.getMessage());
        }
    }

    /**
     *
     *
     * @param returnedMatches, the list of matches already scheduled by Primary Scheduling
     * @return registrants that were not matched in primary scheduling
     */
    private List<Registrant> findRegistrantsToBeMatched(Set<Match> returnedMatches) {

        List<Registrant> toBeMatched = new ArrayList<>(this.REGISTRANTS);
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

                Registrant r = new Registrant(id, availability, skill, tournament.getTournamentSeries().getNumberOfGames());
                REGISTRANTS.add(r);
            }
        }
        catch (Exception e) {

            System.out.println("Exception: " + e);
        }
    }

    private void initTimeslots() throws IOException {

        Date date = CALENDAR.getTime();
        this.timeslots = new ArrayList<>();

        File slots = new File("./data/Timeslots");
        Scanner scanner = new Scanner(slots);

        for (int i = 0; i < 7; i++) {
            while (scanner.hasNext()) {
                float time = Float.parseFloat(scanner.nextLine());
                Timeslot t = new Timeslot(time, CALENDAR.getTime());
                this.timeslots.add(t);
            }
            CALENDAR.add(Calendar.DATE, 1);
            scanner = new Scanner(slots);

        }

        CALENDAR.setTime(date);
    }
}
