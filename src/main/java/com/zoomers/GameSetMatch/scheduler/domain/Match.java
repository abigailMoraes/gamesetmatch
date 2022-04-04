package com.zoomers.GameSetMatch.scheduler.domain;

import com.zoomers.GameSetMatch.scheduler.enumerations.MatchStatus;
import com.zoomers.GameSetMatch.scheduler.matching.util.Tuple;

import java.util.Calendar;
import java.util.Objects;

public class Match {

    private MatchStatus matchStatus;
    private int degrees = 0;
    private final Tuple players;
    private Timeslot timeslot;
    private int skillWeight;
    private int matchScore = 0;
    private final int matchDuration;
    private float matchIndex = 0;

    public Match(int p1, int p2, Timeslot timeslot, int matchDuration, int skillWeight) {
        this.players = Tuple.of(p1, p2);
        this.timeslot = timeslot;
        this.skillWeight = skillWeight;
        this.matchDuration = matchDuration;

        setMatchIndex(matchDuration);
    }

    private void setMatchIndex(int matchDuration) {

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

        if (this.timeslot.getTime() == m2.getTimeslot().getTime()) {
            return true;
        }
        else if (this.timeslot.getTime() < m2.getTimeslot().getTime() &&
                this.timeslot.getTime() + matchIndex > m2.getTimeslot().getTime()) {
            return true;
        }
        else {
            return m2.getTimeslot().getTime() < this.timeslot.getTime() &&
                    m2.getTimeslot().getTime() + matchIndex > this.timeslot.getTime();
        }
    }

    public boolean shareDate(Match m2) {

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(this.timeslot.getDate());
        cal2.setTime(m2.getTimeslot().getDate());
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

    public void moveToNextWeek() {
        this.timeslot = new Timeslot(this.timeslot);
        this.timeslot.addWeek();
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
        return Objects.equals(players, match.players) && Objects.equals(timeslot, match.timeslot);
    }

    @Override
    public String toString() {
        return "Match{ Player " +
                players.getFirst() + " vs Player " + players.getSecond() + " at " + timeslot +
                ". Match Status: " + matchStatus + " " +
                '}';
    }
}
