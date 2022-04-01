package com.zoomers.GameSetMatch.cronjob;

import com.zoomers.GameSetMatch.repository.RoundRepository;
import com.zoomers.GameSetMatch.repository.TournamentRepository;
import com.zoomers.GameSetMatch.scheduler.Scheduler;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentStatus;
import com.zoomers.GameSetMatch.scheduler.exceptions.ScheduleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
@Configuration
@EnableScheduling
public class ScheduledTasks {


    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Autowired
    RoundRepository roundRepository;

    @Autowired
    TournamentRepository tournamentRepository;

    @Autowired
    Scheduler scheduler;

    public ScheduledTasks() {
    }

//    @Scheduled (initialDelay = 1000, fixedDelay=Long.MAX_VALUE)
    @Scheduled(cron = "@midnight", zone="America/Los_Angeles")
    public void RunScheduler(){

        Set<Integer> ongoing_tournamentIDs;
        Set<Integer> new_tournamentIDs;
        System.out.println("Midnight scheduling");
        Date today = new Date();
        String end_date = dateFormat.format(today);
//        System.out.println(end_date);

        ongoing_tournamentIDs = roundRepository.findNextRoundTournamentId(end_date);
        System.out.println("TournamentIds to schedule next round: " + ongoing_tournamentIDs);

        for(Integer tournamentID : ongoing_tournamentIDs){
            try {

                scheduler.createSchedule(tournamentID);
            }
            catch (ScheduleException e) {

                System.out.println(e.getMessage());
            }
        }

        new_tournamentIDs = tournamentRepository.getTournamentsPastCloseRegistrationDate();
        System.out.println("TournamentIds to schedule first round " + new_tournamentIDs);
        for(Integer tournamentID : new_tournamentIDs){
            tournamentRepository.setTournamentStatus(TournamentStatus.REGISTRATION_CLOSED.getStatus(), tournamentID);
            try {

                scheduler.createSchedule(tournamentID);
            }
            catch (ScheduleException e) {

                System.out.println(e.getMessage());
            }
        }


    }

}