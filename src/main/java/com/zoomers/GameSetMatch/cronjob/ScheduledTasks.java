package com.zoomers.GameSetMatch.cronjob;

import com.zoomers.GameSetMatch.repository.RoundRepository;
import com.zoomers.GameSetMatch.repository.TournamentRepository;
import com.zoomers.GameSetMatch.scheduler.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
//    @Scheduled(cron = "@midnight", zone="America/Los_Angeles")
    public void RunScheduler(){

//        Set<Integer> ongoing_tournamentIDs;
//        Set<Integer> new_tournamentIDs;
//        System.out.println("Midnight scheduling");
//        Date today = new Date();
//        String end_date = dateFormat.format(today);
//        System.out.println(end_date);
//
//        ongoing_tournamentIDs = roundRepository.findNextRoundTournamentId(end_date, TournamentStatus.ONGOING.getStatus());
//        System.out.println("TournamentIds to schedule next round: " + ongoing_tournamentIDs);
//
//        for(Integer tournamentID : ongoing_tournamentIDs){
//            try {
//
//                scheduler.createSchedule(tournamentID);
//            }
//            catch (ScheduleException e) {
//
//                System.out.println(e.getMessage());
//            }
//        }
//
//        new_tournamentIDs = tournamentRepository.getTournamentsPastCloseRegistrationDate(TournamentStatus.READY_TO_SCHEDULE.getStatus(), TournamentStatus.OPEN_FOR_REGISTRATION.getStatus());
//        System.out.println("TournamentIds to schedule first round " + new_tournamentIDs);
//        for(Integer tournamentID : new_tournamentIDs){
//            tournamentRepository.setTournamentStatus(TournamentStatus.READY_TO_SCHEDULE.getStatus(), tournamentID);
//            try {
//
//                scheduler.createSchedule(tournamentID);
//            }
//            catch (ScheduleException e) {
//
//                System.out.println(e.getMessage());
//            }
//        }


    }

}