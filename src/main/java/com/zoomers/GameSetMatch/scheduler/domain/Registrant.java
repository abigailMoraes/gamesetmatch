package com.zoomers.GameSetMatch.scheduler.domain;

import com.zoomers.GameSetMatch.entity.Availability;
import com.zoomers.GameSetMatch.entity.User;
import com.zoomers.GameSetMatch.entity.UserMatchTournamentInfo;
import com.zoomers.GameSetMatch.entity.Match;
import com.zoomers.GameSetMatch.repository.AvailabilityRepository;
import com.zoomers.GameSetMatch.repository.MatchRepository;
import com.zoomers.GameSetMatch.repository.UserMatchTournamentRepository;
import com.zoomers.GameSetMatch.scheduler.enumerations.Skill;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Registrant {

    @Autowired
    private AvailabilityRepository availabilityRepository;
    @Autowired
    private UserMatchTournamentRepository userMatchTournamentRepository;
    @Autowired
    private MatchRepository matchRepository;

    private final int id;
    private String availability; // 24 * 7 character string
    private Skill skillLevel;
    private Set<Integer> playersToPlay;
    private int losses = 0;
    private int gamesToSchedule;

    public Registrant(int id, int skillLevel) {
        this.id = id;
        this.skillLevel = Skill.values()[skillLevel - 1];

        // assert(this.availability.length() == 24);// * 7);
    }

    public boolean checkAvailability(int timeID) {
        return this.availability.charAt(timeID) == '1';
    }

    public void initAvailability() {

        List<Availability> availabilityList = availabilityRepository.findRegistrantAvailability(this.id);
        StringBuilder sb = new StringBuilder();

        assert(availabilityList.size() == 7);

        for (Availability a : availabilityList) {

            this.availability = sb.append(this.availability).append(a.toString()).toString();
        }
    }

    public void initCurrentStatus() {

        List<UserMatchTournamentInfo> matchesPlayed = userMatchTournamentRepository.findPastMatchesByUserID(id);

        for (UserMatchTournamentInfo userMatchTournamentInfo : matchesPlayed) {

            List<Match> m = matchRepository.getMatchesByID(userMatchTournamentInfo.getMatchID());
            assert(m.size() == 1);

            Match match = m.get(0);

            if (this.playersToPlay.contains(match.getUserID_1())) {
                this.playersToPlay.remove(match.getUserID_1());
            }
            else if (this.playersToPlay.contains(match.getUserID_2())) {
                this.playersToPlay.remove(match.getUserID_2());
            }

            if (match.getUserID_1() == this.id && userMatchTournamentInfo.getResults() == 2) {
                this.losses++;
            }
            else if (match.getUserID_2() == this.id && userMatchTournamentInfo.getResults() == 1) {
                this.losses++;
            }
        }
    }

    public void setPlayersToPlay(Set<Integer> playersToPlay) {
        this.playersToPlay = playersToPlay;
    }

    public void decreaseGamesToSchedule() { this.gamesToSchedule--; }

    public boolean hasNotPlayed(Registrant r2) {
        return this.playersToPlay.contains(r2.getID());
    }

    public int getSkill() {
        return this.skillLevel.ordinal();
    }

    public int getID() {
        return this.id;
    }

    public void setGamesToSchedule(int gamesToSchedule) {
        this.gamesToSchedule = gamesToSchedule;
    }

    public void setAvailability(String availability) { this.availability = availability; }

    public String getAvailability() {
        return availability;
    }

    public int getLosses() {
        return losses;
    }

    public int getGamesToSchedule() { return gamesToSchedule; }

    public Set<Integer> getPlayersToPlay() {
        return playersToPlay;
    }

    @Override
    public String toString() {
        return "Registrant{" +
                "id=" + id +
                '}';
    }
}
