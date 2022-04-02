package com.zoomers.GameSetMatch.services;

import com.zoomers.GameSetMatch.controller.Match.ResponseBody.MatchDetailsForCalendar;
import com.zoomers.GameSetMatch.controller.Match.ResponseBody.UsersMatchInfo;
import com.zoomers.GameSetMatch.entity.Match;
import com.zoomers.GameSetMatch.entity.UserInvolvesMatch;
import com.zoomers.GameSetMatch.repository.MatchRepository;
import com.zoomers.GameSetMatch.repository.UserInvolvesMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserInvolvesMatchService {
    @Autowired
    UserInvolvesMatchRepository userInvolvesMatchRepository;
    @Autowired
    MatchRepository matchRepository;

    @Transactional
    public void updateMatchResults(int matchID, int userID, int result) {
        // there is a match for each user, set the result to be the same in both i.e tie, player1 or player2 was the winner
        List<UserInvolvesMatch> matches = userInvolvesMatchRepository.getUserInvolvesMatchByMatchID(matchID);
        for (UserInvolvesMatch match : matches) {
            // tie
            if (result == 0 || result == -1) {
                match.setResults(result);
            } else {
                int opponentsResult = result == 1 ? 2 : 1;
                match.setResults(match.getUserID() == userID ? result : opponentsResult);
            }

        }
        userInvolvesMatchRepository.saveAll(matches);
    }

    public List<MatchDetailsForCalendar> getMatchesByRoundForCalendar(int roundID) {
        List<Match> matches = matchRepository.getMatchesByRound(roundID);

        List<MatchDetailsForCalendar> returnList = new ArrayList<>();
        for (Match m : matches) {
            List<UsersMatchInfo> usersMatch = userInvolvesMatchRepository.getUsersMatchInfoForCalendar(m.getMatchID());
            MatchDetailsForCalendar matchDetailsForCalendar = new MatchDetailsForCalendar(m.getMatchID(),
                    m.getStartTime(), m.getEndTime(), m.getRoundID(), m.getIsConflict(), m.getUserID_1(), m.getUserID_2());
            matchDetailsForCalendar.setParticipants(usersMatch);

            returnList.add(matchDetailsForCalendar);

        }
        return returnList;
    }

}
