package com.zoomers.GameSetMatch.controller;


import com.zoomers.GameSetMatch.controller.Match.RequestBody.IncomingMatch;
import com.zoomers.GameSetMatch.services.SendEMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3700)
@RequestMapping("/api")
public class MailController {

    @Autowired
    SendEMailService sendEMailService;

    @PostMapping(value = "/publish")
    public void sendEMails(@RequestBody List<IncomingMatch> schedule) throws MessagingException {
        sendEMailService.sendSchedulePublishedEmails(schedule);
    }
}
