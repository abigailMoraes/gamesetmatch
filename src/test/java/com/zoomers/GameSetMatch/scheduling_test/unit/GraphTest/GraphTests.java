package com.zoomers.GameSetMatch.scheduling_test.unit.GraphTest;

import com.zoomers.GameSetMatch.repository.TournamentRepository;
import com.zoomers.GameSetMatch.scheduler.Scheduler;
import com.zoomers.GameSetMatch.scheduler.domain.MockTournament;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

// import org.junit.jupiter.api.Test;

@RunWith( SpringRunner.class )
// @Import({TournamentRepository.class})
@SpringBootTest
// @EnableAutoConfiguration
// @DataJpaTest
public class GraphTests {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private Scheduler scheduler;

    private final MockTournament tournament1 = new MockTournament(
            0,
            1,
            1,
            1,
            30,
            Calendar.getInstance().getTime(),
            0
    );

    private final MockTournament weightedTournament1 = new MockTournament(
            0,
            1,
            1,
            1,
            30,
            Calendar.getInstance().getTime(),
            0
    );

    private final MockTournament StressTournament = new MockTournament(
            0,
            1,
            4,
            1,
            180,
            Calendar.getInstance().getTime(),
            0
    );

    @Rule
    public Timeout globalTime = Timeout.seconds(300);
/*
    @Test
    public void BaseMatchingTest() {
        Scheduler s = new Scheduler(tournament1, "./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/BaseMatchingTest.json");
        s.schedule();
    }

    @Test
    public void weightedBaseMatchingTest() {
        Scheduler s = new Scheduler(weightedTournament1, "./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/BaseMatchingTest.json");
        s.schedule();
    }

    @Test
    public void SingleMatchingTest() {
        Scheduler s = new Scheduler(tournament1, "./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/SingleMatch.json");
        s.schedule();
    }

    @Test
    public void BaseSecondaryMatching() {

        String filename = "./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/BaseSecondaryMatching.json";

        Scheduler s = new Scheduler(tournament1, filename);
        s.schedule();
    }

    @Test
    public void GeneralUpperBoundStressTestFullAvailability() {
        JSONArray array = new JSONArray();
        try {

            for (int i = 0; i < 100; i++) {
                JSONObject player =  new JSONObject();
                player.put("id", Integer.toString(i));
                player.put("availability", "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");

                array.put(player);
            }
        }
            catch (JSONException e) {

            System.out.println(e);
        }

        String filename = "./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/GeneralFullAvailability.json";

        try {
            FileWriter file = new FileWriter(filename);
            file.write(array.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scheduler s = new Scheduler(StressTournament, filename);
        s.schedule();
    }

    @Test
    public void GeneralUpperBoundStressTestNoAvailability() {
        JSONArray array = new JSONArray();
        try {

            for (int i = 0; i < 100; i++) {
                JSONObject player =  new JSONObject();
                player.put("id", Integer.toString(i));
                player.put("availability", "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");

                array.put(player);
            }
        }
        catch (JSONException e) {

            System.out.println(e);
        }

        String filename = "./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/GeneralNoAvailability.json";

        try {
            FileWriter file = new FileWriter(filename);
            file.write(array.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scheduler s = new Scheduler(StressTournament, filename);
        s.schedule();
    }

    @Test
    public void MultiWeekPrimaryScheduling() {

        String filename = "./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/MultiWeekPrimaryScheduling.json";

        Scheduler s = new Scheduler(tournament1, filename);
        s.schedule();
    }

    @Test
    public void GraphFive() {

        String filename = "./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/PlayerSet4.json";

        Scheduler s = new Scheduler(tournament1, filename);
        s.schedule();
    }

    @Test
    public void GraphSix() {

        String filename = "./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/PlayerSet5.json";

        Scheduler s = new Scheduler(tournament1, filename);
        s.schedule();
    }

    @Test
    public void BestOfMatching() {

        String filename = "./src/test/java/com/zoomers/GameSetMatch/scheduling_test/json_files/BestOfMatching.json";

        Scheduler s = new Scheduler(tournament1, filename);
        s.schedule();
    }

    @Test
    public void StressTestNoAvailability() {
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

        Scheduler s = new Scheduler(StressTournament, filename);
        s.schedule();
    }

    @Test
    public void StressTestFullAvailability() {
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

        Scheduler s = new Scheduler(StressTournament, filename);
        s.schedule();
    }
*/
    @Test
    public void BasicDBIntegration() {

        scheduler.createSchedule(1);
    }
}
