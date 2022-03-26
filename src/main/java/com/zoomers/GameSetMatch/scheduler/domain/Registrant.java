package com.zoomers.GameSetMatch.scheduler.domain;

import com.zoomers.GameSetMatch.repository.AvailabilityRepository;
import com.zoomers.GameSetMatch.repository.MatchRepository;
import com.zoomers.GameSetMatch.repository.UserMatchTournamentRepository;
import com.zoomers.GameSetMatch.scheduler.SpringConfig;
import com.zoomers.GameSetMatch.scheduler.enumerations.PlayerStatus;
import com.zoomers.GameSetMatch.scheduler.enumerations.Skill;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentFormat;
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
    private int losses = 0;
    private int gamesToSchedule;
    private PlayerStatus status = PlayerStatus.SAFE;

    public Registrant(int id, int skillLevel, int tournamentId) {
        this.id = id;
        this.skillLevel = Skill.values()[skillLevel];
        this.tournamentId = tournamentId;
        // assert(this.availability.length() == 24);// * 7);
    }

    public boolean checkAvailability(int timeID) {
        return this.availability.charAt(timeID) == '1';
    }

    public void initAvailability() {

        this.registrantService = SpringConfig.getBean(RegistrantService.class);
        this.availability = registrantService.initAvailability(this.id, this.tournamentId);
    }

    public void initCurrentStatus(TournamentFormat format) {

        this.playersToPlay = registrantService.initPlayersToPlay(this.id, this.playersToPlay);
        this.losses = registrantService.initLosses(this.id);

        switch(format) {
            case ROUND_ROBIN:
            {
                if (this.playersToPlay.size() == 0) {
                    this.status = PlayerStatus.ELIMINATED;
                }
            }
            break;
            case SINGLE_KNOCKOUT:
            {
                if (this.losses >= 1) {
                    this.status = PlayerStatus.ELIMINATED;
                }
            }
            break;
            case DOUBLE_KNOCKOUT:
            {
                if (this.losses >= 2) {
                    this.status = PlayerStatus.ELIMINATED;
                }
                else if (this.losses == 1) {
                    this.status = PlayerStatus.ONE_LOSS;
                }
            }
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

    public int getLosses() {
        return losses;
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
