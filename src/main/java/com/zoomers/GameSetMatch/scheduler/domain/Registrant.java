package com.zoomers.GameSetMatch.scheduler.domain;

import com.zoomers.GameSetMatch.repository.AvailabilityRepository;
import com.zoomers.GameSetMatch.repository.MatchRepository;
import com.zoomers.GameSetMatch.repository.UserMatchTournamentRepository;
import com.zoomers.GameSetMatch.scheduler.SpringConfig;
import com.zoomers.GameSetMatch.scheduler.enumerations.MatchBy;
import com.zoomers.GameSetMatch.scheduler.enumerations.PlayerStatus;
import com.zoomers.GameSetMatch.scheduler.enumerations.Skill;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentFormat;
import com.zoomers.GameSetMatch.scheduler.exceptions.ScheduleException;
import com.zoomers.GameSetMatch.services.RegistrantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.Set;

@Configurable
public class Registrant {

    @Autowired
    private AvailabilityRepository availabilityRepository;
    @Autowired
    private UserMatchTournamentRepository userMatchTournamentRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private RegistrantService registrantService;

    private final int id;
    private final int tournamentId;
    private String availability; // 24 * 7 character string
    private Skill skillLevel;
    private Set<Integer> playersToPlay;
    private int gamesToSchedule;
    private PlayerStatus status = PlayerStatus.SAFE;

    public Registrant(int id, int skillLevel, int tournamentId) {
        this.id = id;
        this.skillLevel = Skill.values()[skillLevel];
        this.tournamentId = tournamentId;
        this.registrantService = SpringConfig.getBean(RegistrantService.class);
    }

    public boolean checkAvailability(int timeID, int matchDuration) {

        int matchIndex = (int) Math.ceil(matchDuration / 30.0);
        for (int i = timeID; i < timeID + matchIndex; i++) {
            if (this.availability.charAt(i) == '0') {
                return false;
            }
        }

        return true;
    }

    public void initAvailability() throws ScheduleException {

        this.availability = registrantService.initAvailability(this.id, this.tournamentId);
    }

    public void initCurrentStatus(TournamentFormat format, MatchBy matchBy, int tournamentId) throws ScheduleException {

        if (matchBy == MatchBy.MATCH_BY_RANDOM) {
            this.skillLevel = Skill.INTERMEDIATE;
        }

        this.playersToPlay = registrantService.initPlayersToPlay(this.id, this.playersToPlay, tournamentId);

        switch(format) {
            case SINGLE_KNOCKOUT:
            case DOUBLE_KNOCKOUT:
            case SINGLE_BRACKET: {
                this.status = PlayerStatus.values()[registrantService.initStatus(this.id, tournamentId)];
            }
            break;
        }
    }

    public void setPlayersToPlay(Set<Integer> playersToPlay) {
        this.playersToPlay = playersToPlay;
        this.playersToPlay.remove(this.id);
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

    public int getGamesToSchedule() { return gamesToSchedule; }

    public Set<Integer> getPlayersToPlay() {
        return playersToPlay;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Registrant{" +
                "id=" + id +
                '}';
    }
}
