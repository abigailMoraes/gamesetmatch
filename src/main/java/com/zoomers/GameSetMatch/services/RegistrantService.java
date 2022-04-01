package com.zoomers.GameSetMatch.services;

import com.zoomers.GameSetMatch.entity.Match;
import com.zoomers.GameSetMatch.entity.UserMatchTournamentInfo;
import com.zoomers.GameSetMatch.repository.MatchRepository;
import com.zoomers.GameSetMatch.repository.UserMatchTournamentRepository;
import com.zoomers.GameSetMatch.scheduler.SpringConfig;
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

    public String initAvailability(int r_id, int t_id) {

        AvailabilityService availabilityService = SpringConfig.getBean(AvailabilityService.class);
        List<String> availabilityList = availabilityService.getPlayerAvailabilities(r_id, t_id);
        StringBuilder sb = new StringBuilder();
        String availability = "";

        assert(availabilityList.size() == 7);

        for (String a : availabilityList) {

            availability = availability.concat(a);
        }

        return availability;
    }

    public Set<Integer> initPlayersToPlay(int id, Set<Integer> playersToPlay, int t_id) {

        // TODO CHANGE TO findPastMatchesByUserID(id);
        List<Integer> matchesPlayed = userMatchTournamentRepository.findPastTournamentMatchIDsByUserID(id, t_id);

        for (Integer m_id : matchesPlayed) {

            List<Match> m = matchRepository.getMatchesByID(m_id);
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

            if (match.getUserID_1() == id && userMatchTournamentInfo.getResults().equals("Loss")) {//== 2) {
                losses++;
            }
            else if (match.getUserID_2() == id && userMatchTournamentInfo.getResults().equals("Loss")) {// == 1) {
                losses++;
            }
        }

        return losses;
    }
}
