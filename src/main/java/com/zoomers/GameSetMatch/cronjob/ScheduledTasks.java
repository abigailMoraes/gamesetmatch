package com.zoomers.GameSetMatch.cronjob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Configuration
@EnableScheduling
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron ="0 0 7-18 ? * *", zone="America/Los_Angeles")
    public void CloseRegistration() {
        System.out.println("Midnight scheduling");
        log.info("The time is now {}", dateFormat.format(new Date()));
    }

}
