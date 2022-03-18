package com.zoomers.GameSetMatch.scheduling_test.unit.GraphTest;

import com.zoomers.GameSetMatch.scheduler.Scheduler;
import com.zoomers.GameSetMatch.scheduler.domain.MockTournament;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentSeries;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentType;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class GraphTests {

    private MockTournament tournament = new MockTournament(
            0,
            TournamentType.SINGLE_KNOCKOUT,
            TournamentSeries.BEST_OF_1,
            false,
            70,
            new Date()
    );

    @Test
    void GraphOne() {
        Scheduler s = new Scheduler(tournament, "./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/PlayerSet1.json");
        s.schedule();
    }

    @Test
    void GraphTwo() {
        JSONArray array = new JSONArray();

        try {
            for (int i = 0; i < 50; i++) {
                JSONObject player = new JSONObject();
                player.put("id", Integer.toString(i));
                player.put("availability", "111111111111111111111111");

                array.put(player);
            }
        }
        catch (JSONException e) {

            System.out.println(e);
        }

        String filename = "./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/PlayerSet2.json";

        try {
            FileWriter file = new FileWriter(filename);
            file.write(array.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scheduler s = new Scheduler(tournament, filename);
        s.schedule();
    }

    @Test
    void GraphThree() {
        JSONArray array = new JSONArray();
        try {

            for (int i = 0; i < 2; i++) {
                JSONObject player =  new JSONObject();
                player.put("id", Integer.toString(i));
                player.put("availability", "101000000000000000000000");

                array.put(player);
            }
        }
            catch (JSONException e) {

            System.out.println(e);
        }

        String filename = "./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/PlayerSet2.json";

        try {
            FileWriter file = new FileWriter(filename);
            file.write(array.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scheduler s = new Scheduler(tournament, filename);
        s.schedule();
    }

    @Test
    void GraphFour() {

        String filename = "./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/PlayerSet3.json";

        Scheduler s = new Scheduler(tournament, filename);
        s.schedule();
    }

    @Test
    void GraphFive() {

        String filename = "./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/PlayerSet4.json";

        Scheduler s = new Scheduler(tournament, filename);
        s.schedule();
    }

    @Test
    void GraphSix() {

        String filename = "./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/PlayerSet5.json";

        Scheduler s = new Scheduler(tournament, filename);
        s.schedule();
    }

    @Test
    void StressTestNoAvailability() {
        JSONArray array = new JSONArray();

        try {
            for (int i = 0; i < 300; i++) {
                JSONObject player =  new JSONObject();
                player.put("id", Integer.toString(i));
                player.put("availability", "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
                player.put("skill", "1");

                array.put(player);
            }
        }
            catch (JSONException e) {

            System.out.println(e);
        }

        String filename = "./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/stress1.json";

        try {
            FileWriter file = new FileWriter(filename);
            file.write(array.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scheduler s = new Scheduler(tournament, filename);
        s.schedule();
    }

    @Test
    void StressTestFullAvailability() {
        JSONArray array = new JSONArray();

        try {
            for (int i = 0; i < 300; i++) {
                JSONObject player =  new JSONObject();
                player.put("id", Integer.toString(i));
                player.put("availability", "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
                player.put("skill", "1");

                array.put(player);
            }
        }
        catch (JSONException e) {

            System.out.println(e);
        }

        String filename = "./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/stress2.json";

        try {
            FileWriter file = new FileWriter(filename);
            file.write(array.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scheduler s = new Scheduler(tournament, filename);
        s.schedule();
    }
}
