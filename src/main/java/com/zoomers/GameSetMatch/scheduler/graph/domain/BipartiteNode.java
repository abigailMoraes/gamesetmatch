package com.zoomers.GameSetMatch.scheduler.graph.domain;

import java.util.Objects;

public class BipartiteNode {

    private int id; // userID for player, availability index for timeslot
    private boolean isPlayer;

    public BipartiteNode(int id, boolean isPlayer) {
        this.id = id;
        this.isPlayer = isPlayer;
    }

    public int getID() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BipartiteNode that = (BipartiteNode) o;
        return id == that.id && isPlayer == that.isPlayer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isPlayer);
    }
}
