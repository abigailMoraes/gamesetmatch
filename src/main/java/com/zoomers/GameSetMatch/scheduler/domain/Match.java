package com.zoomers.GameSetMatch.scheduler.domain;

import com.zoomers.GameSetMatch.scheduler.enumerations.MatchStatus;
import com.zoomers.GameSetMatch.scheduler.matching.util.Tuple;

import java.text.SimpleDateFormat;
import java.util.Objects;

public class Match {

    private static int id = 0;
    private final int match_id = id++;
    private MatchStatus matchStatus;
    private int degrees = 0;
    private final Tuple players;
    private Timeslot timeslot;
    private int skillWeight = 0;
    private int matchScore = 0;
    private int matchDuration = 0;
    private float matchIndex = 0;

    public Match(int p1, int p2, Timeslot timeslot, int matchDuration, int skillWeight) {
        this.players = Tuple.of(p1, p2);
        this.timeslot = timeslot;
        this.skillWeight = skillWeight;

        setMatchIndex(matchDuration);
    }

    private void setMatchIndex(int matchDuration) {

        this.matchDuration = matchDuration;
        float matchInterval = matchDuration / 30f;
        this.matchIndex = (float) Math.ceil(matchInterval) / 2;
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
                this.timeslot.getTime() + matchIndex > m2.getTimeslot().getTime()) {
            return true;
        }
        else {
            return m2.getTimeslot().getTime() <= this.timeslot.getTime() &&
                    m2.getTimeslot().getTime() + matchIndex > this.timeslot.getTime();
        }
    }

    public boolean shareDate(Match m2) {

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(this.timeslot.getDate()).equals(sdf.format(m2.getTimeslot().getDate()));
    }

    public void moveToNextWeek() {
        this.timeslot.addWeek();
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

    public int getMatchDuration() { return matchDuration; }

    public float getMatchIndex() { return matchIndex; }

    public MatchStatus getMatchStatus() { return matchStatus; }

    public int getDegrees() { return degrees; }

    public void setTimeslot(Timeslot timeslot) { this.timeslot = timeslot; }

    public void setDegrees(int degrees) {
        this.degrees = degrees;
    }

    public void setMatchStatus(MatchStatus matchStatus) {
        this.matchStatus = matchStatus;
    }

    public void setMatchScore(int matchScore) { this.matchScore = matchScore; }

    public void setSkillWeight(int skillWeight) { this.skillWeight = skillWeight; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return match_id == match.match_id && Objects.equals(players, match.players) && Objects.equals(timeslot, match.timeslot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(match_id, players, timeslot);
    }

    @Override
    public String toString() {
        return "Match{ Player " +
                players.getFirst() + " vs Player " + players.getSecond() + " at " + timeslot +
                ". Match Status: " + matchStatus + " " +
                '}';
    }
}
