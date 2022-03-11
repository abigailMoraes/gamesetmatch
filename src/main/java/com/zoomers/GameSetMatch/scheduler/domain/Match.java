package com.zoomers.GameSetMatch.scheduler.domain;

import com.zoomers.GameSetMatch.scheduler.matching.util.Tuple;

public class Match {

    private static int id = 0;
    private final int match_id = id++;
    private boolean needsAdminAttention = false;
    private int degrees = 0;
    private final Tuple players;
    private final Timeslot timeslot;
    private int skillWeight = 1;

    public Match(int p1, int p2, Timeslot timeslot, int skillWeight) {
        this.players = Tuple.of(p1, p2);
        this.skillWeight = skillWeight;
        this.timeslot = timeslot;
    }

    public Match(int p1, int p2, Timeslot timeslot) {
        this.players = Tuple.of(p1, p2);
        this.timeslot = timeslot;
    }



    public boolean sharePlayers(Match m2) {
        return this.players.getFirst() == m2.getPlayers().getFirst() ||
                this.players.getFirst() == m2.getPlayers().getSecond() ||
                this.players.getSecond() == m2.getPlayers().getFirst() ||
                this.players.getSecond() == m2.getPlayers().getSecond();
    }

    public boolean shareTimeslot(Match m2) {
        return this.timeslot == m2.getTimeslot();
    }

    public int getMatch_id() {
        return match_id;
    }

    public Tuple getPlayers() {
        return players;
    }

    public Timeslot getTimeslot() { return timeslot; }

    public int getDegrees() { return degrees; }

    public void setNeedsAdminAttention(boolean needsAdminAttention) {
        this.needsAdminAttention = needsAdminAttention;
    }

    public void addDegrees() { this.degrees++; }

    public void decreaseDegrees() { this.degrees--; }

    public void setDegrees(int degrees) {
        this.degrees = degrees;
    }

    @Override
    public String toString() {
        return "Match{ Player " +
                players.getFirst() + " vs Player " + players.getSecond() + " at " + timeslot +
                ". Needs admin attention: " + needsAdminAttention + " " +
                '}';
    }
}
