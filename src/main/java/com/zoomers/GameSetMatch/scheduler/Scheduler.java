package com.zoomers.GameSetMatch.scheduler;

import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;
import com.zoomers.GameSetMatch.scheduler.graph.BipartiteGraph;
import com.zoomers.GameSetMatch.scheduler.graph.LineGraph;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Scheduler {

    private final List<Registrant> registrants = new ArrayList<>();
    private final List<Timeslot> timeslots = new ArrayList<>();
    private String playerFileName;
    
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
        LineGraph lg = new LineGraph(bg);
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
}
