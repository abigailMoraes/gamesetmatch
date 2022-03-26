package com.zoomers.GameSetMatch.controller;


import com.zoomers.GameSetMatch.controller.Match.RequestBody.IncomingMatch;
import com.zoomers.GameSetMatch.entity.User;
import com.zoomers.GameSetMatch.entity.UserInvolvesMatch;
import com.zoomers.GameSetMatch.repository.UserInvolvesMatchRepository;
import com.zoomers.GameSetMatch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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

    @PostMapping(value = "/publish")
    public void sendMails(@RequestBody List<IncomingMatch> schedule) throws MessagingException {

        // currently, only support google account
        // make sure the server account has IMAP turned on, see link blow
        // https://support.google.com/mail/answer/7126229?hl=en#zippy=
        String from = "zoomers319@gmail.com";

        for (IncomingMatch match : schedule) {

            List<UserInvolvesMatch> participants = userInvolvesMatchRepository.getUserInvolvesMatchByMatchID(match.getMatchID());

            for (UserInvolvesMatch participant : participants) {

                User user = userRepository.getUserById(participant.getUserID());

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message);

                helper.setSubject("[GameSetMatch]Incoming Match Notification");
                helper.setFrom(from);

                String to = user.getEmail();
                helper.setTo(to);

                String firstName = user.getName().split("\\s+")[0];
                String date = match.getStartTime().split("\\s+")[0];
                String startTime = match.getStartTime().split("\\s+")[1];
                String endTime = match.getEndTime().split("\\s+")[1];


                boolean html = true;
                helper.setText("<p>Dear " + firstName + ",</p><br>"
                        + "<p>You have a match scheduled on <b>" + date + "</b> from " +
                        "<b>" + startTime + "</b>" + " to " + "<b>" + endTime + "</b>"
                        + ". Be prepared and hope you can have fun.</p><br>" +
                        "<p>Best,</p><p>GameSetMatch</p>", html);

                mailSender.send(message);
            }
        }
    }
}
