package com.zoomers.GameSetMatch.services;

import com.zoomers.GameSetMatch.controller.Match.RequestBody.IncomingMatch;
import com.zoomers.GameSetMatch.entity.User;
import com.zoomers.GameSetMatch.entity.UserInvolvesMatch;
import com.zoomers.GameSetMatch.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SendEMailService {
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

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping(value = "/publish")
    public void sendSchedulePublishedEmails(@RequestBody List<IncomingMatch> schedule) throws MessagingException {

        // currently, only support google account
        // make sure the server account has IMAP turned on, see link blow
        // https://support.google.com/mail/answer/7126229?hl=en#zippy=

        for (IncomingMatch match : schedule) {

            List<UserInvolvesMatch> participants = userInvolvesMatchRepository.getUserInvolvesMatchByMatchID(match.getMatchID());
            Integer roundNumber = repository.getRoundNumberByRoundID(match.getRoundID());
            Integer tournamentID = repository.getTournamentIDByRoundID(match.getRoundID());
            String tournamentName = tournamentRepository.getNameByTournamentID(tournamentID);

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
                        + " PST. Good luck and have fun!.</p><br>" +
                        "<p>Best,</p><p>GameSetMatch</p>", html);

                mailSender.send(message);
            }
        }
    }

    public void sendCancelMail(List<UserRegistersTournamentRepository.IRegistrant> registrants, String tournamentName) throws MessagingException {
        for (UserRegistersTournamentRepository.IRegistrant registrant : registrants) {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setSubject("[GameSetMatch]Tournament Cancellation");

            String to = registrant.getEmail();
            helper.setTo(to);

            String firstName = registrant.getName().split("\\s+")[0];

            boolean html = true;
            helper.setText("<p>Dear " + firstName + ",</p><br>"
                    + "<p>We are sorry to inform you that the tournament <b><i>" + tournamentName + "</i></b> has been canceled!</p><br>"
                    + "<p> But don't worry, you can go to GameSetMatch and explore other interesting tournaments!</p><br>" +
                    "<p>Best,</p><p>GameSetMatch</p>", html);

            mailSender.send(message);
        }
    }
}
