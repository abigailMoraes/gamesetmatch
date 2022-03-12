package com.zoomers.GameSetMatch.scheduler;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;
import com.zoomers.GameSetMatch.scheduler.graph.BipartiteGraph;
import com.zoomers.GameSetMatch.scheduler.matching.algorithms.GreedyMaximumIndependentSet;
import com.zoomers.GameSetMatch.scheduler.matching.algorithms.MaximumMatchScoreMatcher;
import com.zoomers.GameSetMatch.scheduler.matching.util.Tuple;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.*;

public class Scheduler {

    private final List<Registrant> registrants = new ArrayList<>();
    private final List<Timeslot> timeslots = new ArrayList<>();
    private List<Match> matches = new ArrayList<>();
    private Integer[] playerDegrees;
    private Integer[] timeDegrees;
    private final HashMap<Integer, Integer[]> timeRepeats = new HashMap<>();
    private final HashMap<Tuple, Integer> playerRepeats = new HashMap<>();
    private final String playerFileName;

    public Scheduler(String filename) {
        this.playerFileName = filename;
    }

    public static void main(String[] args) {

        Scheduler s = new Scheduler("./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/PlayerSet1.json");
        s.schedule();
    }

    public void schedule() {

        initPlayers();

        try {
            initTimeslots();
        }
        catch (IOException e) {
            System.out.println("IOError: " + e.getMessage());
        }

        // TODO: FIND REGISTRANTS AND INSTANTIATE TIMESLOTS FROM DATABASE

        BipartiteGraph bg = new BipartiteGraph(timeslots, registrants);
        LinkedHashMap<Timeslot, List<Registrant>> bgList = bg.getAdjacencyList();

        createPossibleMatches(bgList);
        setMatchDegrees();

        GreedyMaximumIndependentSet greedyMaximumIndependentSet =
                new GreedyMaximumIndependentSet(
                        new LinkedHashSet<>(this.matches),
                        this.playerDegrees,
                        this.timeDegrees,
                        this.playerRepeats,
                        this.timeRepeats
                );
        Set<Match> returnedMatches = greedyMaximumIndependentSet.findGreedyMaximumIndependentSet();

        List<Registrant> registrantsToBeMatched = findRegistrantsToBeMatched(returnedMatches);
        List<Timeslot> availableTimeslots = findAvailableTimeslots(returnedMatches);

        createSecondaryMatches(registrantsToBeMatched, availableTimeslots);

        MaximumMatchScoreMatcher maximumMatchScoreMatcher = new MaximumMatchScoreMatcher(new LinkedHashSet<>(this.matches));
        returnedMatches.addAll(maximumMatchScoreMatcher.findRemainingMatches());
    }

    private void initPlayers() {

        JSONParser parser = new JSONParser();
        try {

            JSONArray a = (JSONArray) parser.parse(new FileReader(playerFileName));
            for (Object o : a) {
                JSONObject registrant = (JSONObject) o;
                int id = Integer.parseInt((String)registrant.get("id"));
                String availability = (String)registrant.get("availability");
                Registrant r = new Registrant(id, availability);
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

    private void createPossibleMatches(LinkedHashMap<Timeslot, List<Registrant>> bgList) {

        initializePlayerTimeDegrees();

        for (Timeslot t : bgList.keySet()) {
            List<Registrant> registrants = bgList.get(t);
            for (int i = 0; i < registrants.size(); i++) {

                int i_id = registrants.get(i).getID();

                initializeTimeRepeat(i_id);

                for (int j = i+1; j < registrants .size(); j++) {
                    int j_id = registrants.get(j).getID();
                    Match m = new Match(i_id, j_id, t);
                    matches.add(m);

                    initializeTimeRepeat(j_id);
                    initializePlayerRepeat(i_id, j_id);
                    incrementDegrees(t, i_id, j_id);
                }
            }
        }
    }

    private void createSecondaryMatches(
            List<Registrant> registrantsToBeMatched,
            List<Timeslot> availableTimeslots)
    {
        this.matches = new ArrayList<>();

        for (Timeslot t : availableTimeslots) {

            for (int i = 0; i < registrantsToBeMatched.size(); i++) {

                Registrant r1 = registrantsToBeMatched.get(i);

                for (int j = i+1; j < registrantsToBeMatched.size(); j++) {

                    Registrant r2 = registrantsToBeMatched.get(j);
                    Match m = new Match(r1.getID(), r2.getID(), t);
                    m.setMatchScore(calculateMatchScore(r1, r2, t));

                    this.matches.add(m);
                }
            }
        }
    }

    private void initializePlayerTimeDegrees() {

        this.playerDegrees = new Integer[this.registrants.size()];
        Arrays.fill(playerDegrees, -1);

        this.timeDegrees = new Integer[this.timeslots.size()];
        Arrays.fill(timeDegrees, -1);
    }

    private void initializeTimeRepeat(int id) {

        if (!this.timeRepeats.containsKey(id)) {
            Integer[] j_timeslotRepeats = new Integer[this.timeslots.size()];
            Arrays.fill(j_timeslotRepeats, -1);
            this.timeRepeats.put(id, j_timeslotRepeats);
        }
    }

    private void initializePlayerRepeat(int i_id, int j_id) {

        Tuple pair = Tuple.of(i_id, j_id);

        if (!this.playerRepeats.containsKey(pair)) {

            this.playerRepeats.put(pair, -1);
        }
    }

    private void incrementDegrees(Timeslot t, int i_id, int j_id) {

        this.playerDegrees[i_id]++;
        this.playerDegrees[j_id]++;
        this.timeDegrees[t.getID()]++;
        this.timeRepeats.get(i_id)[t.getID()]++;
        this.timeRepeats.get(j_id)[t.getID()]++;
        this.playerRepeats.put(Tuple.of(i_id, j_id), this.playerRepeats.get(Tuple.of(i_id, j_id)) + 1);
    }

    private void setMatchDegrees() {

        for (Match m : matches) {

            m.setDegrees(calculateDegrees(m, this.playerDegrees, this.timeDegrees, this.timeRepeats, this.playerRepeats));
        }
    }

    public static int calculateDegrees(Match m, Integer[] playerDegrees, Integer[] timeDegrees, HashMap<Integer, Integer[]> timeRepeats, HashMap<Tuple, Integer> playerRepeats) {
        int p1Edges = playerDegrees[m.getPlayers().getFirst()];
        int p2Edges = playerDegrees[m.getPlayers().getSecond()];
        int tEdges = timeDegrees[m.getTimeslot().getID()];
        int d1Edges = timeRepeats.get(m.getPlayers().getFirst())[m.getTimeslot().getID()];
        int d2Edges = timeRepeats.get(m.getPlayers().getSecond())[m.getTimeslot().getID()];
        int prEdges = playerRepeats.get(Tuple.of(m.getPlayers().getFirst(), m.getPlayers().getSecond()));
        return p1Edges + p2Edges + tEdges - (d1Edges + d2Edges + prEdges);
    }

    private int calculateMatchScore(Registrant r1, Registrant r2, Timeslot t) {

        int matchScore = 0;
        if (r1.checkAvailability(t.getID()) &&
                r2.checkAvailability(t.getID())) {
            matchScore += 2;
        }
        else if (r1.checkAvailability(t.getID()) ||
                r2.checkAvailability(t.getID())) {
            matchScore++;
        }

        if (r1.hasNotPlayed(r2)) {
            matchScore += 2;
        }

        matchScore -= Math.abs(r1.getSkill() - r2.getSkill());

        return matchScore;
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

    private Set<Match> findRemainingMatches(List<Registrant> registrantsToBeMatched) {

        return new LinkedHashSet<>();
    }
}
