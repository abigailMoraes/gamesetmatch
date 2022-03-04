package com.zoomers.GameSetMatch.scheduler.graph.domain;

import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;
import org.springframework.data.util.Pair;
import java.util.List;
import java.util.Objects;

public class LineNode {

    private Pair<Registrant, Timeslot> player_timeslot_pair;
    private static int id = 0;
    private final int node_id = id++;

    public LineNode(Registrant p, Timeslot t) {
        player_timeslot_pair = Pair.of(p, t);
    }

    public void decreaseID() {
        this.id--;
    }

    public Registrant getPlayer() {
        return player_timeslot_pair.getFirst();
    }

    public Timeslot getTimeslot() {
        return player_timeslot_pair.getSecond();
    }

    public int getId() {
        return node_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineNode lineNode = (LineNode) o;
        return node_id == lineNode.node_id &&
                ((player_timeslot_pair.getFirst().equals(((LineNode) o).player_timeslot_pair.getFirst()) &&
                 player_timeslot_pair.getSecond().equals(((LineNode) o).player_timeslot_pair.getSecond())) ||
                 (player_timeslot_pair.getFirst().equals(((LineNode) o).player_timeslot_pair.getSecond()) &&
                  player_timeslot_pair.getSecond().equals(((LineNode) o).player_timeslot_pair.getFirst())));
    }

    @Override
    public int hashCode() {
        return Objects.hash(player_timeslot_pair, node_id);
    }
}
