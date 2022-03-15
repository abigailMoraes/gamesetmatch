package com.zoomers.GameSetMatch.scheduler.domain;

import com.zoomers.GameSetMatch.scheduler.enumerations.MatchStatus;
import com.zoomers.GameSetMatch.scheduler.matching.util.Tuple;

public class Match {

    private static int id = 0;
    private final int match_id = id++;
    private MatchStatus matchStatus;
    private int degrees = 0;
    private final Tuple players;
    private final Timeslot timeslot;
    private int skillWeight = 0;
    private int matchScore = 0;
    private float matchDuration = 0;

    public Match(int p1, int p2, Timeslot timeslot, int matchDuration, int skillWeight) {
        this.players = Tuple.of(p1, p2);
        this.timeslot = timeslot;
        this.skillWeight = skillWeight;

        setMatchDuration(matchDuration);
    }

    private void setMatchDuration(int matchDuration) {

        float matchInterval = matchDuration / 30f;
        this.matchDuration = (float) Math.ceil(matchInterval * 2) / 2;
    }

    public boolean sharePlayers(Match m2) {
        return this.players.getFirst() == m2.getPlayers().getFirst() ||
                this.players.getFirst() == m2.getPlayers().getSecond() ||
                this.players.getSecond() == m2.getPlayers().getFirst() ||
                this.players.getSecond() == m2.getPlayers().getSecond();
    }

    public boolean shareTimeslot(Match m2) {

        if (this.timeslot == m2.getTimeslot()) {
            return true;
        }
        else if (this.timeslot.getTime() <= m2.getTimeslot().getTime() &&
                this.timeslot.getTime() + matchDuration > m2.getTimeslot().getTime()) {
            return true;
        }
        else return m2.getTimeslot().getTime() <= this.timeslot.getTime() &&
                    m2.getTimeslot().getTime() + matchDuration > this.timeslot.getTime();
    }

    public int getMatch_id() {
        return match_id;
    }

    public Tuple getPlayers() {
        return players;
    }

    public Timeslot getTimeslot() { return timeslot; }

    public int getSkillWeight() { return skillWeight; }

    public int getMatchScore() { return matchScore; }

    public MatchStatus getMatchStatus() { return matchStatus; }

    public int getDegrees() { return degrees; }

    public void setDegrees(int degrees) {
        this.degrees = degrees;
    }

    public void addDegrees() { this.degrees++; }

    public void decreaseDegrees() { this.degrees--; }

    public void setMatchStatus(MatchStatus matchStatus) {
        this.matchStatus = matchStatus;
    }

    public void setMatchScore(int matchScore) { this.matchScore = matchScore; }

    public void setSkillWeight(int skillWeight) { this.skillWeight = skillWeight; }

    @Override
    public String toString() {
        return "Match{ Player " +
                players.getFirst() + " vs Player " + players.getSecond() + " at " + timeslot +
                ". Match Status: " + matchStatus + " " +
                '}';
    }
}
