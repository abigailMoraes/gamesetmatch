package com.zoomers.GameSetMatch.services;

import com.zoomers.GameSetMatch.entity.EnumsForColumns.MatchResult;
import com.zoomers.GameSetMatch.entity.Match;
import com.zoomers.GameSetMatch.repository.MatchRepository;
import com.zoomers.GameSetMatch.repository.UserMatchTournamentRepository;
import com.zoomers.GameSetMatch.repository.UserRegistersTournamentRepository;
import com.zoomers.GameSetMatch.scheduler.SpringConfig;
import com.zoomers.GameSetMatch.scheduler.exceptions.ScheduleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RegistrantService {

    @Autowired
    private UserMatchTournamentRepository userMatchTournamentRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private UserRegistersTournamentRepository userRegistersTournamentRepository;

    public String initAvailability(int r_id, int t_id) throws ScheduleException {

        AvailabilityService availabilityService = SpringConfig.getBean(AvailabilityService.class);
        List<String> availabilityList = availabilityService.getPlayerAvailabilities(r_id, t_id);
        String availability = "";

        if (availabilityList.size() != 7) { throw new ScheduleException("Availability is Incorrect Length"); }

        for (String a : availabilityList) {

            availability = availability.concat(a);
        }

        return availability;
    }

    public Set<Integer> initPlayersToPlay(int id, Set<Integer> playersToPlay, int t_id) {

        List<Integer> matchesPlayed = userMatchTournamentRepository.findTournamentMatchIDsByUserIDWithResultsGreaterThan(id, t_id, MatchResult.TIE.getResult());

        for (Integer m_id : matchesPlayed) {

            List<Match> m = matchRepository.getMatchesByID(m_id);
            Match match = m.get(0);

            if (playersToPlay.contains(match.getUserID_1())) {
                playersToPlay.remove(match.getUserID_1());
            }
            else playersToPlay.remove(match.getUserID_2());
        }

        return playersToPlay;
    }

    public int initStatus(int id, int t_id) throws ScheduleException {

        List<Integer> status = userRegistersTournamentRepository.getPlayerStatusByTournamentID(id, t_id);
        if (status.get(0) == null) { throw new ScheduleException("Player Status is null"); }
        return status.get(0);
    }
}
