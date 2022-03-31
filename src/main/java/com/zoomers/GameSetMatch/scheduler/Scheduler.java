/**
 * Service for the Scheduling Algorithm; given a Tournament ID,
 * the algorithm will produce a schedule for all players registered
 * in the tournament.
 *
 * @since 2022-03-21
 */

package com.zoomers.GameSetMatch.scheduler;

import com.zoomers.GameSetMatch.entity.Round;
import com.zoomers.GameSetMatch.entity.User;
import com.zoomers.GameSetMatch.entity.UserInvolvesMatch;
import com.zoomers.GameSetMatch.repository.*;
import com.zoomers.GameSetMatch.scheduler.exceptions.*;
import com.zoomers.GameSetMatch.scheduler.graphs.*;
import com.zoomers.GameSetMatch.scheduler.domain.*;
import com.zoomers.GameSetMatch.scheduler.enumerations.*;
import com.zoomers.GameSetMatch.scheduler.matching.algorithms.*;
import com.zoomers.GameSetMatch.scheduler.matching.typeMatchers.*;
import com.zoomers.GameSetMatch.scheduler.matching.util.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class Scheduler {

    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private UserRegistersTournamentRepository userRegistersTournamentRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private RoundRepository roundRepository;
    @Autowired
    private UserInvolvesMatchRepository userInvolvesMatchRepository;

    private List<Registrant> REGISTRANTS;
    private List<Timeslot> TIMESLOTS;
    private MockTournament MOCK_TOURNAMENT;
    private TypeMatcher typeMatcher;
    private Calendar CALENDAR;
    private TournamentStatus originalStatus;

    public void createSchedule(int tournamentID) throws ScheduleException {

        this.REGISTRANTS = new ArrayList<>();
        this.TIMESLOTS = new ArrayList<>();
        this.CALENDAR = Calendar.getInstance();

        this.MOCK_TOURNAMENT = tournamentRepository.getMockTournamentByID(tournamentID);
        assert(this.MOCK_TOURNAMENT != null);
        this.originalStatus = MOCK_TOURNAMENT.getTournamentStatus();

        initTypeMatcher(this.MOCK_TOURNAMENT.getTournamentFormat());
        initTournamentPlayers(tournamentID);
        this.CALENDAR.setTime(this.MOCK_TOURNAMENT.getStartDate());
        initTimeslots();

        checkIfLastRound();

        schedule();
    }

    private void initTypeMatcher(TournamentFormat type) {

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
    @Transactional
    private Set<Match> schedule() throws ScheduleException {

        Set<Match> returnedMatches = new LinkedHashSet<>();

        switch (MOCK_TOURNAMENT.getTournamentFormat()) {
            case ROUND_ROBIN:
                returnedMatches.addAll(scheduleRoundRobin());
                break;
            case SINGLE_KNOCKOUT:
                returnedMatches.addAll(scheduleSingleKnockout());
                break;
            case DOUBLE_KNOCKOUT:
                returnedMatches.addAll(scheduleDoubleKnockout());
                break;
            case SINGLE_BRACKET:
                returnedMatches.addAll(scheduleBracket());
                break;
            default:
                throw new InvalidFormatException("The tournament format isn't valid.");
        }

        if (MOCK_TOURNAMENT.getTournamentSeries() != TournamentSeries.BEST_OF_1) {

            int expectedMatches = (this.REGISTRANTS.size() / 2) * MOCK_TOURNAMENT.getTournamentSeries().getNumberOfGames();
            returnedMatches.addAll(scheduleBestOfMatches(returnedMatches, expectedMatches));
        }

        Date roundEndDate = ((Match)returnedMatches.toArray()[returnedMatches.size() - 1]).getTimeslot().getDate();
        MOCK_TOURNAMENT.setRoundEndDate(roundEndDate);

        Round newRound = new Round();

        newRound.setTournamentID(this.MOCK_TOURNAMENT.getTournamentID());
        newRound.setRoundNumber(this.MOCK_TOURNAMENT.getCurrentRound());
        newRound.setEndDate(this.MOCK_TOURNAMENT.getRoundEndDate());
        newRound.setStartDate( this.MOCK_TOURNAMENT.getStartDate());

        Round persistedRound = roundRepository.save(newRound);

        List<com.zoomers.GameSetMatch.entity.Match> matchEntities = new ArrayList<>();
        for (Match m : returnedMatches) {
            com.zoomers.GameSetMatch.entity.Match matchEntity = new com.zoomers.GameSetMatch.entity.Match();
            matchEntity.setStartTime(m.getTimeslot().getLocalStartDateTime());
            matchEntity.setEndTime(m.getTimeslot().getLocalEndDateTime(this.MOCK_TOURNAMENT.getMatchDuration()));
            matchEntity.setIsConflict(m.getMatchStatus().ordinal());
            matchEntity.setRoundID(persistedRound.getRoundID());
            matchEntity.setUserID_1(m.getPlayers().getFirst());
            matchEntity.setUserID_2(m.getPlayers().getSecond());
            matchEntities.add(matchEntity);
            // System.out.println(m);
        }

        TournamentStatus newStatus = this.originalStatus == TournamentStatus.OPEN_FOR_REGISTRATION ||
                this.originalStatus == TournamentStatus.REGISTRATION_CLOSED ?
                TournamentStatus.READY_TO_PUBLISH_SCHEDULE : TournamentStatus.READY_TO_PUBLISH_NEXT_ROUND;

        tournamentRepository.updateTournament(
                this.MOCK_TOURNAMENT.getTournamentID(),
                newStatus.getStatus(),
                this.MOCK_TOURNAMENT.getCurrentRound()
            );

        matchRepository.saveAll(matchEntities);
        return returnedMatches;
    }

    private Set<Match> scheduleRoundRobin() {

        Set<Match> matches = new LinkedHashSet<>();

        List<Registrant> registrantsToMatch = new ArrayList<>(REGISTRANTS);
        List<Registrant> roundRobinList = new ArrayList<>(REGISTRANTS);
        if (registrantsToMatch.size() % 2 != 0) {
            roundRobinList.add(new Registrant(-1, 0, MOCK_TOURNAMENT.getTournamentID()));
        }

        Set<Tuple> registrantMatches = new HashSet<>();

        Registrant firstRegistrant = roundRobinList.remove(0);
        int index = MOCK_TOURNAMENT.getCurrentRound() % roundRobinList.size();
        registrantMatches.add(Tuple.of(firstRegistrant.getID(), roundRobinList.get(index).getID()));

        for (int i = 1; i < roundRobinList.size() / 2; i++) {

            Registrant r1 = roundRobinList.get((MOCK_TOURNAMENT.getCurrentRound() + i) % roundRobinList.size());
            Registrant r2 = roundRobinList.get((MOCK_TOURNAMENT.getCurrentRound() + roundRobinList.size() - i) % roundRobinList.size());

            if (r1.getID() == -1 || r2.getID() == -1) {
                continue;
            }

            registrantMatches.add(Tuple.of(r1.getID(), r2.getID()));
        }

        int expectedMatches = registrantMatches.size();

        while (matches.size() < expectedMatches) {

            RoundRobinGraph roundRobinGraph = typeMatcher.createRoundRobinMatches(
                    registrantsToMatch,
                    registrantMatches,
                    TIMESLOTS,
                    MOCK_TOURNAMENT.getMatchDuration()
            );

            MatchingAlgorithm maximumMatchScoreMatcher = new MaximumMatchScoreMatcher(roundRobinGraph);

            matches.addAll(maximumMatchScoreMatcher.findMatches());

            for (Match m : matches) {

                registrantsToMatch.removeIf(registrant ->
                        m.getPlayers().getFirst() == registrant.getID() ||
                                m.getPlayers().getSecond() == registrant.getID()
                );

                registrantMatches.removeIf(tuple -> tuple.getFirst() == m.getPlayers().getFirst() ||
                        tuple.getSecond() == m.getPlayers().getFirst() ||
                        tuple.getFirst() == m.getPlayers().getSecond() ||
                        tuple.getSecond() == m.getPlayers().getSecond());
            }

            if (registrantsToMatch.isEmpty()) {
                CALENDAR.setTime(MOCK_TOURNAMENT.getStartDate());
                break;
            }

            addWeek();
        }

        return matches;
    }

    private Set<Match> scheduleSingleKnockout() throws ScheduleException {

        Set<Match> returnedMatches = new HashSet<>();

        if (MOCK_TOURNAMENT.getCurrentRound() == 1 &&
        !((REGISTRANTS.size() & (REGISTRANTS.size() - 1)) == 0)) {

            returnedMatches.addAll(scheduleFirstKnockoutRound());
        }
        else {
            returnedMatches.addAll(schedulePrimaryMatches());
            returnedMatches.addAll(scheduleSecondaryMatches(returnedMatches));
        }
        return returnedMatches;
    }

    private Set<Match> scheduleFirstKnockoutRound() {

        int floorPowerOfTwo = (int) Math.pow(2, 32 - Integer.numberOfLeadingZeros(REGISTRANTS.size()) - 1);
        int expectedMatches = REGISTRANTS.size() - floorPowerOfTwo;

        Set<Match> matches = new HashSet<>();
        List<Registrant> registrantsToMatch = new ArrayList<>(REGISTRANTS);

        while (matches.size() < expectedMatches) {

            System.out.println("Initial Single Knockout : " + this.CALENDAR.getTime());

            SecondaryMatchGraph secondaryMatchGraph = typeMatcher.createPossibleSecondaryMatches(
                    registrantsToMatch,
                    TIMESLOTS,
                    MOCK_TOURNAMENT.getMatchDuration()
            );

            MatchingAlgorithm initialKnockoutAlgorithm = new InitialKnockoutMatchingAlgorithm(secondaryMatchGraph, expectedMatches);

            matches.addAll(initialKnockoutAlgorithm.findMatches());

            for (Match m : matches) {
                registrantsToMatch.removeIf(registrant ->
                        m.getPlayers().getFirst() == registrant.getID() ||
                                m.getPlayers().getSecond() == registrant.getID()
                );
            }

            addWeek();
        }

        CALENDAR.setTime(MOCK_TOURNAMENT.getStartDate());

        return matches;
    }

    private Set<Match> schedulePrimaryMatches() throws ScheduleException {

        Set<Match> matches = new LinkedHashSet<>();
        List<Registrant> registrantsToMatch = new ArrayList<>(REGISTRANTS);

        while (true) {

            System.out.println("Primary: " + this.CALENDAR.getTime());

            BipartiteGraph bg = new BipartiteGraph(TIMESLOTS, registrantsToMatch, MOCK_TOURNAMENT.getMatchDuration());
            PrimaryMatchGraph matchGraph = typeMatcher.createPossiblePrimaryMatches(bg);

            if (matchGraph.getMatches().size() == 0) {
                CALENDAR.setTime(MOCK_TOURNAMENT.getStartDate());
                break;
            }

            matchGraph.setMatchDegrees();

            MatchingAlgorithm greedyMaximumIndependentSet = getMatchingAlgorithm(MOCK_TOURNAMENT.getMatchBy(), matchGraph);

            matches.addAll(greedyMaximumIndependentSet.findMatches());

            for (Match m : matches) {
                registrantsToMatch.removeIf(registrant ->
                        m.getPlayers().getFirst() == registrant.getID() ||
                                m.getPlayers().getSecond() == registrant.getID()
                );
            }

            addWeek();
        }

        for (Match match : matches) {

            System.out.println("Primary Match: " + match);
        }

        return matches;
    }

    private MatchingAlgorithm getMatchingAlgorithm(MatchBy matchBy, PrimaryMatchGraph matchGraph) throws InvalidMatchByException {

        switch (matchBy) {
            case MATCH_BY_SKILL:
                return new GreedyMinimumWeightIndependentSet(matchGraph);
            case MATCH_BY_RANDOM:
                return new GreedyMaximumIndependentSet(matchGraph);
            case MATCH_BY_SEED:
                return new GreedySeededIndependentSet(matchGraph);
            default:
                throw new InvalidMatchByException("Invalid tournament match-by value");
        }
    }

    private Set<Match> scheduleSecondaryMatches(Set<Match> matches) {

        List<Registrant> registrantsToMatch = findRegistrantsToBeMatched(matches);
        List<Timeslot> availableTimeslots = findAvailableTimeslots(matches);
        Set<Match> newMatches = new LinkedHashSet<>();

        while (registrantsToMatch.size() > 1) {

            System.out.println("Secondary: " + this.CALENDAR.getTime());

            SecondaryMatchGraph secondaryMatchGraph = typeMatcher.createPossibleSecondaryMatches(
                    registrantsToMatch,
                    availableTimeslots,
                    MOCK_TOURNAMENT.getMatchDuration()
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
            availableTimeslots = TIMESLOTS;
        }

        decreaseWeek();

        for (Match match : newMatches) {

            System.out.println("Secondary Match: " + match);
        }

        return newMatches;
    }

    private Set<Match> scheduleDoubleKnockout() throws ScheduleException {

        Set<Match> returnedMatches = new HashSet<>();

        if (MOCK_TOURNAMENT.getCurrentRound() == 1 &&
                !((REGISTRANTS.size() & (REGISTRANTS.size() - 1)) == 0)) {

            returnedMatches.addAll(scheduleFirstKnockoutRound());
        }
        else {
            returnedMatches.addAll(scheduleNoLossMatches());
            returnedMatches.addAll(scheduleOneLossMatches());
        }
        return returnedMatches;
    }

    private Set<Match> scheduleNoLossMatches() throws ScheduleException {

        List<Registrant> savedRegistrants = new ArrayList<>(REGISTRANTS);
        REGISTRANTS.removeIf(registrant -> registrant.getStatus() == PlayerStatus.ONE_LOSS);

        Set<Match> returnedMatches = new HashSet<>();
        returnedMatches.addAll(schedulePrimaryMatches());
        returnedMatches.addAll(scheduleSecondaryMatches(returnedMatches));

        REGISTRANTS = savedRegistrants;
        return returnedMatches;
    }

    private Set<Match> scheduleOneLossMatches() throws ScheduleException {

        List<Registrant> savedRegistrants = new ArrayList<>(REGISTRANTS);
        REGISTRANTS.removeIf(registrant -> registrant.getStatus() == PlayerStatus.SAFE);

        Set<Match> returnedMatches = new HashSet<>();
        returnedMatches.addAll(schedulePrimaryMatches());
        returnedMatches.addAll(scheduleSecondaryMatches(returnedMatches));

        REGISTRANTS = savedRegistrants;
        return returnedMatches;
    }

    private Set<Match> scheduleBracket() throws ScheduleException {

        Set<Match> returnedMatches = new HashSet<>();

        if (MOCK_TOURNAMENT.getCurrentRound() == 1 &&
                !((REGISTRANTS.size() & (REGISTRANTS.size() - 1)) == 0)) {

            returnedMatches.addAll(scheduleFirstKnockoutRound());
        }
        else {
            returnedMatches.addAll(scheduleBracketMatches());
        }
        return returnedMatches;
    }

    private Set<Match> scheduleBracketMatches() throws ScheduleException {

        int previousRoundID = roundRepository.getLastTournamentRound(MOCK_TOURNAMENT.getTournamentID());
        List<com.zoomers.GameSetMatch.entity.Match> previousRoundMatches = matchRepository.getMatchesByRound(previousRoundID);
        Set<Match> matches;

        if (previousRoundMatches.size() < REGISTRANTS.size()) {

            matches = schedulePrimaryMatches();
            matches.addAll(scheduleSecondaryMatches(matches));
        }
        else {

            matches = new LinkedHashSet<>();
            int expectedMatches = previousRoundMatches.size() / 2;
            List<Tuple> matchedPlayers = new ArrayList<>();

            int i = 0;

            while (i + 1 < previousRoundMatches.size()) {

                List<UserInvolvesMatch> uimListOne = userInvolvesMatchRepository.getUserInvolvesMatchByMatchID(previousRoundMatches.get(i).getMatchID());
                List<UserInvolvesMatch> uimListTwo = userInvolvesMatchRepository.getUserInvolvesMatchByMatchID(previousRoundMatches.get(i+1).getMatchID());

                int winnerOne;
                int winnerTwo;

                if (uimListOne.get(0).getResults() == 1) {
                    winnerOne = uimListOne.get(0).getUserID();
                }
                else {
                    winnerOne = uimListOne.get(1).getUserID();
                }

                if (uimListTwo.get(0).getResults() == 1) {
                    winnerTwo = uimListTwo.get(0).getUserID();
                }
                else {
                    winnerTwo = uimListTwo.get(1).getUserID();
                }

                matchedPlayers.add(Tuple.of(winnerOne, winnerTwo));

                i += 2;
            }

            while (matches.size() < expectedMatches) {

                System.out.println("Bracket Match: " + this.CALENDAR.getTime());

                BracketMatchGraph bracketMatchGraph = typeMatcher.createPossibleBracketMatches(
                        REGISTRANTS,
                        TIMESLOTS,
                        matchedPlayers,
                        MOCK_TOURNAMENT.getMatchDuration()
                );

                MatchingAlgorithm matchingAlgorithm = new MaximumMatchScoreMatcher(bracketMatchGraph);
                matches.addAll(matchingAlgorithm.findMatches());

                for (Match m : matches) {
                    matchedPlayers.removeIf(pair ->
                            m.getPlayers().getFirst() == pair.getFirst() ||
                            m.getPlayers().getFirst() == pair.getSecond() ||
                                    m.getPlayers().getSecond() == pair.getFirst()||
                                    m.getPlayers().getSecond() == pair.getSecond()
                    );
                }

                addWeek();
            }

            decreaseWeek();
        }

        return matches;
    }

    private Set<Match> scheduleBestOfMatches(Set<Match> matches, int expectedMatches) {

        if (this.MOCK_TOURNAMENT.getTournamentSeries().getNumberOfGames() == 1) {
            return Set.of();
        }

        Set<Match> matchesToSchedule = new LinkedHashSet<>(matches);

        while (matches.size() < expectedMatches) {

            System.out.println("Best-Of: " + this.CALENDAR.getTime());

            BestOfMatchGraph bestOfMatchGraph = typeMatcher.createPossibleBestOfMatches(
                    new LinkedHashSet<>(REGISTRANTS),
                    new LinkedHashSet<>(TIMESLOTS),
                    matchesToSchedule,
                    this.MOCK_TOURNAMENT.getTournamentSeries().getNumberOfGames(),
                    this.MOCK_TOURNAMENT.getMatchDuration()
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

        for (Match match : matches) {

            System.out.println("Best-Of Match: " + match);
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
        initTimeslots();
    }

    private void decreaseWeek() {

        CALENDAR.add(Calendar.WEEK_OF_YEAR, -1);
        initTimeslots();
    }

    /**
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

    /**
     * @param returnedMatches, the list of matches already scheduled by Primary Scheduling
     * @return list of timeslots that are still available for matching
     */
    private List<Timeslot> findAvailableTimeslots(Set<Match> returnedMatches) {

        List<Timeslot> availableTimeslots = new ArrayList<>(this.TIMESLOTS);
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

    private void initTournamentPlayers(int tournamentID) {

        REGISTRANTS = userRegistersTournamentRepository.getSchedulerRegistrantsByTournamentID(tournamentID);
        Set<Integer> registrantIDs = REGISTRANTS.stream().map(Registrant::getID).collect(Collectors.toSet());

        for (Registrant r : REGISTRANTS) {

            r.setGamesToSchedule(this.MOCK_TOURNAMENT.getTournamentSeries().getNumberOfGames());
            r.setPlayersToPlay(new LinkedHashSet<>(registrantIDs));
            r.initAvailability();
            r.initCurrentStatus(
                    this.MOCK_TOURNAMENT.getTournamentFormat(),
                    this.MOCK_TOURNAMENT.getMatchBy(),
                    this.MOCK_TOURNAMENT.getTournamentID()
            );
        }

        REGISTRANTS.removeIf(registrant -> registrant.getStatus() == PlayerStatus.ELIMINATED);
    }

    private void initTimeslots() {

        Date date = CALENDAR.getTime();
        this.TIMESLOTS = new ArrayList<>();

        String str = "9,9.5,10,10.5,11,11.5,12,12.5,13,13.5,14,14.5,15,15.5,16,16.5,17,17.5,18,18.5,19,19.5,20,20.5";
        String[] arrOfStr = str.split(",");
         for (int i = 0; i < 7; i++) {
            for(String s : arrOfStr) {
                float time = Float.parseFloat(s);
                Timeslot t = new Timeslot(time, CALENDAR.getTime());
                this.TIMESLOTS.add(t);
            }
            CALENDAR.add(Calendar.DATE, 1);

        }

        CALENDAR.setTime(date);
    }

    private void checkIfLastRound() {

        if (this.MOCK_TOURNAMENT.getTournamentFormat() == TournamentFormat.ROUND_ROBIN) {
            checkIfAllPlayersHavePlayed();
        } else {
            if (this.REGISTRANTS.size() == 2) {
                this.MOCK_TOURNAMENT.setTournamentStatus(TournamentStatus.FINAL_ROUND);
            }
            else {
                this.MOCK_TOURNAMENT.setTournamentStatus(TournamentStatus.ONGOING);
            }
        }
    }

    private void checkIfAllPlayersHavePlayed() {

        for (Registrant r : REGISTRANTS) {
            if (r.getPlayersToPlay().size() > 1) {
                this.MOCK_TOURNAMENT.setTournamentStatus(TournamentStatus.ONGOING);
                return;
            }
        }
        this.MOCK_TOURNAMENT.setTournamentStatus(TournamentStatus.FINAL_ROUND);
    }
}
