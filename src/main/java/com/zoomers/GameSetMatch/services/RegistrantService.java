package com.zoomers.GameSetMatch.services;

import com.zoomers.GameSetMatch.entity.Availability;
import com.zoomers.GameSetMatch.entity.Match;
import com.zoomers.GameSetMatch.entity.UserMatchTournamentInfo;
import com.zoomers.GameSetMatch.repository.AvailabilityRepository;
import com.zoomers.GameSetMatch.repository.MatchRepository;
import com.zoomers.GameSetMatch.repository.UserMatchTournamentRepository;
import com.zoomers.GameSetMatch.scheduler.SpringConfig;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
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

    public String initAvailability(int r_id) {

        AvailabilityService availabilityService = SpringConfig.getBean(AvailabilityService.class);
        List<String> availabilityList = availabilityService.getPlayerAvailabilities(r_id);
        StringBuilder sb = new StringBuilder();
        String availability = "";

        assert(availabilityList.size() == 7);

        for (String a : availabilityList) {

            availability = availability.concat(a);
        }

        return availability;
    }

    public Set<Integer> initPlayersToPlay(int id, Set<Integer> playersToPlay) {

        List<UserMatchTournamentInfo> matchesPlayed = userMatchTournamentRepository.findPastMatchesByUserID(id);

        for (UserMatchTournamentInfo userMatchTournamentInfo : matchesPlayed) {

            List<Match> m = matchRepository.getMatchesByID(userMatchTournamentInfo.getMatchID());
            assert(m.size() == 1);

            Match match = m.get(0);

            if (playersToPlay.contains(match.getUserID_1())) {
                playersToPlay.remove(match.getUserID_1());
            }
            else if (playersToPlay.contains(match.getUserID_2())) {
                playersToPlay.remove(match.getUserID_2());
            }
        }

        return playersToPlay;
    }

    public int initLosses(int id) {

        List<UserMatchTournamentInfo> matchesPlayed = userMatchTournamentRepository.findPastMatchesByUserID(id);

        int losses = 0;

        for (UserMatchTournamentInfo userMatchTournamentInfo : matchesPlayed) {

            List<Match> m = matchRepository.getMatchesByID(userMatchTournamentInfo.getMatchID());
            assert(m.size() == 1);

            Match match = m.get(0);

            if (match.getUserID_1() == id && userMatchTournamentInfo.getResults() == 2) {
                losses++;
            }
            else if (match.getUserID_2() == id && userMatchTournamentInfo.getResults() == 1) {
                losses++;
            }
        }

        return losses;
    }
}
