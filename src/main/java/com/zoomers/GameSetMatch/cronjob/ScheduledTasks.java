package com.zoomers.GameSetMatch.cronjob;

import com.zoomers.GameSetMatch.repository.RoundHasRepository;
import com.zoomers.GameSetMatch.repository.TournamentRepository;
import com.zoomers.GameSetMatch.scheduler.Scheduler;
import com.zoomers.GameSetMatch.scheduler.enumerations.TournamentStatus;
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

@Component
@Configuration
@EnableScheduling
public class ScheduledTasks {

    private final RoundHasRepository roundHasRepository;
    private final TournamentRepository tournamentRepository;
    private  List<Integer> ongoing_tournamentIDs;
    private  List<Integer> new_tournamentIDs;
    private TournamentStatus tournamentStatus;
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");


    @Autowired
    private Scheduler scheduler;

    public ScheduledTasks(RoundHasRepository roundHasRepository, TournamentRepository tournamentRepository, Scheduler scheduler) {
        this.roundHasRepository = roundHasRepository;
        this.tournamentRepository = tournamentRepository;
        this.scheduler = scheduler;
    }

//    @Scheduled (initialDelay = 1000, fixedDelay=Long.MAX_VALUE)
    @Scheduled(cron = "@midnight", zone="America/Los_Angeles")
    public void RunScheduler(){
        System.out.println("Midnight scheduling");
        Date today = new Date();
        String end_date = dateFormat.format(today);
//        System.out.println(end_date);

        ongoing_tournamentIDs = this.roundHasRepository.findNextRoundTournamentId(end_date);
//        System.out.println(ongoing_tournamentIDs);

        for(Integer tournamentID : ongoing_tournamentIDs){
            this.scheduler.createSchedule(tournamentID);
        }

        new_tournamentIDs = this.tournamentRepository.CloseRegistrationDate();
//        System.out.println(new_tournamentIDs);
        for(Integer tournamentID : new_tournamentIDs){
            this.tournamentRepository.setTournamentStatus(tournamentStatus.REGISTRATION_CLOSED.getStatus(), tournamentID);
            this.scheduler.createSchedule(tournamentID);
        }


    }

}