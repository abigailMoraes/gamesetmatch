package com.zoomers.GameSetMatch.scheduler;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;
import com.zoomers.GameSetMatch.scheduler.graph.BipartiteGraph;
import com.zoomers.GameSetMatch.scheduler.graph.MatchGraph;
import com.zoomers.GameSetMatch.scheduler.matching.algorithms.GreedyMaximumIndependentSet;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.*;

public class Scheduler {

    private final List<Registrant> registrants = new ArrayList<>();
    private final List<Timeslot> timeslots = new ArrayList<>();
    private final List<Match> matches = new ArrayList<>();
    private Integer[] degrees;
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

        // TODO: FIND REGISTRANTS AND INSTANTIATE TIMESLOTS

        BipartiteGraph bg = new BipartiteGraph(timeslots, registrants);
        LinkedHashMap<Timeslot, List<Registrant>> bgList = bg.getAdjacencyList();

        createPossibleMatches(bgList);
        setMatchDegrees();

        GreedyMaximumIndependentSet greedyMaximumIndependentSet = new GreedyMaximumIndependentSet(new LinkedHashSet<>(this.matches), this.degrees);
        Set<Match> returnedMatches = greedyMaximumIndependentSet.findGreedyMaximumIndependentSet();

        for (Match m : returnedMatches) {
            System.out.println(m);
        }

        List<Registrant> registrantsToBeMatched = findRegistrantsToBeMatched(returnedMatches);

        for (Registrant r : registrantsToBeMatched) {
            System.out.println(r);
        }

        returnedMatches.addAll(findRemainingMatches(registrantsToBeMatched));
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

        this.degrees = new Integer[this.registrants.size()];
        Arrays.fill(degrees, -1);

        for (Timeslot t : bgList.keySet()) {
            List<Registrant> players = bgList.get(t);
            for (int i = 0; i < players.size(); i++) {
                for (int j = i+1; j < players.size(); j++) {
                    Match m = new Match(players.get(i).getID(), players.get(j).getID(), t.getTime());
                    matches.add(m);
                    this.degrees[players.get(i).getID()]++;
                    this.degrees[players.get(j).getID()]++;
                }
            }
        }
    }

    private void setMatchDegrees() {

        for (Match m : matches) {

            int p1Edges = this.degrees[m.getPlayers().getFirst()];
            int p2Edges = this.degrees[m.getPlayers().getSecond()];
            m.setDegrees(p1Edges + p2Edges);
        }
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

    private Set<Match> findRemainingMatches(List<Registrant> registrantsToBeMatched) {
        
        return new LinkedHashSet<>();
    }
}
