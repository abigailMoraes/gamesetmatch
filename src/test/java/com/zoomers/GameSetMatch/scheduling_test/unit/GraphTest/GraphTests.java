package com.zoomers.GameSetMatch.scheduling_test.unit.GraphTest;

import com.zoomers.GameSetMatch.scheduler.Scheduler;
import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;

public class GraphTests {

    @Test
    void GraphOne() {
        Scheduler s = new Scheduler("./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/PlayerSet1.json");
        s.schedule();
    }

    @Test
    void GraphTwo() {
        JSONArray array = new JSONArray();

        for (int i = 0; i < 50; i++) {
            JSONObject player =  new JSONObject();
            player.put("id", Integer.toString(i));
            player.put("availability", "111111111111111111111111");

            array.put(player);
        }

        String filename = "./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/PlayerSet2.json";

        try {
            FileWriter file = new FileWriter(filename);
            file.write(array.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scheduler s = new Scheduler(filename);
        s.schedule();
    }

    @Test
    void GraphThree() {
        JSONArray array = new JSONArray();

        for (int i = 0; i < 2; i++) {
            JSONObject player =  new JSONObject();
            player.put("id", Integer.toString(i));
            player.put("availability", "100000000000000000000000");

            array.put(player);
        }

        String filename = "./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/PlayerSet3.json";

        try {
            FileWriter file = new FileWriter(filename);
            file.write(array.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scheduler s = new Scheduler(filename);
        s.schedule();
    }

    @Test
    void StressTest() {
        JSONArray array = new JSONArray();

        for (int i = 0; i < 300; i++) {
            JSONObject player =  new JSONObject();
            player.put("id", Integer.toString(i));
            player.put("availability", "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");

            array.put(player);
        }

        String filename = "./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/stress1.json";

        try {
            FileWriter file = new FileWriter(filename);
            file.write(array.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scheduler s = new Scheduler(filename);
        s.schedule();
    }
}
