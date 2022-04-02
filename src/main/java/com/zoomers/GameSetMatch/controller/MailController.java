package com.zoomers.GameSetMatch.controller;


import com.zoomers.GameSetMatch.controller.Match.RequestBody.IncomingMatch;
import com.zoomers.GameSetMatch.entity.User;
import com.zoomers.GameSetMatch.entity.UserInvolvesMatch;
import com.zoomers.GameSetMatch.entity.UserRegistersTournament;
import com.zoomers.GameSetMatch.repository.*;
import com.zoomers.GameSetMatch.services.UserRegistersTournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3700)
@RequestMapping("/api")
public class MailController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserInvolvesMatchRepository userInvolvesMatchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoundRepository repository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    UserRegistersTournamentService userRegistersTournamentService;

    @Autowired
    MatchRepository matchRepository;

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping(value = "/publish")
    public void sendMails(@RequestBody List<IncomingMatch> schedule) throws MessagingException {

        // currently, only support google account
        // make sure the server account has IMAP turned on, see link blow
        // https://support.google.com/mail/answer/7126229?hl=en#zippy=

        for (IncomingMatch match : schedule) {

            List<UserInvolvesMatch> participants = userInvolvesMatchRepository.getUserInvolvesMatchByMatchID(match.getMatchID());
            Integer roundNumber = repository.getRoundNumberByRoundID(match.getRoundID());
            Integer tournamentID = repository.getTournamentIDByRoundID(match.getRoundID());
            String tournamentName = tournamentRepository.getNameByTournamentID(tournamentID);

            matchRepository.updateMatchPublishStatus(match.getMatchID());

            for (UserInvolvesMatch participant : participants) {

                User user = userRepository.getUserById(participant.getUserID());

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message);

                helper.setSubject("[GameSetMatch]Incoming Match Notification");

                String to = user.getEmail();
                helper.setTo(to);

                String firstName = user.getName().split("\\s+")[0];
                String date = match.getStartTime().format(formatter).split("\\s+")[0];
                String startTime = match.getStartTime().format(formatter).split("\\s+")[1];
                String endTime = match.getEndTime().format(formatter).split("\\s+")[1];


                boolean html = true;
                helper.setText("<p>Dear " + firstName + ",</p><br>"
                        + "<p>Round <i>" + roundNumber + "</i> for the tournament <b><i>" + tournamentName + "</i></b> has been scheduled!</p><br>"
                        + "<p>You have a match scheduled on <b>" + date + "</b> from " +
                        "<b>" + startTime + "</b>" + " to " + "<b>" + endTime + "</b>"
                        + ". Be prepared and hope you can have fun.</p><br>" +
                        "<p>Best,</p><p>GameSetMatch</p>", html);

                mailSender.send(message);
            }
        }
    }

    public void sendCancelMail(Integer tournamentID) throws MessagingException {
        List<UserRegistersTournament> registrants = userRegistersTournamentService.getRegistrantsByTournamentID(tournamentID);
        String tournamentName = tournamentRepository.getNameByTournamentID(tournamentID);

        for (UserRegistersTournament registrant : registrants) {
            User user = userRepository.getUserById(registrant.getUserID());

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setSubject("[GameSetMatch]Tournament Cancellation");

            String to = user.getEmail();
            helper.setTo(to);

            String firstName = user.getName().split("\\s+")[0];

            boolean html = true;
            helper.setText("<p>Dear " + firstName + ",</p><br>"
                    + "<p>We are sorry to inform you that the tournament <b><i>" + tournamentName + "</i></b> has been canceled!</p><br>"
                    + "<p> But don't worry, you can go to GameSetMatch and explore other interesting tournaments!</p><br>" +
                    "<p>Best,</p><p>GameSetMatch</p>", html);

            mailSender.send(message);
        }
    }
}
