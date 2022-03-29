package com.zoomers.GameSetMatch.cronjob;

import com.zoomers.GameSetMatch.entity.RoundHas;
import com.zoomers.GameSetMatch.repository.RoundHasRepository;
import com.zoomers.GameSetMatch.repository.TournamentRepository;
import com.zoomers.GameSetMatch.scheduler.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
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
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private Scheduler scheduler;

    public ScheduledTasks(RoundHasRepository roundHasRepository, TournamentRepository tournamentRepository) {
        this.roundHasRepository = roundHasRepository;
        this.tournamentRepository = tournamentRepository;
    }

    //    @Scheduled(cron = "@midnight", zone="America/Los_Angeles")
    @Scheduled (initialDelay = 10000, fixedDelay=Long.MAX_VALUE)
    public void RunScheduler() {

        System.out.println("Midnight scheduling");
        Date today = new Date();
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        log.info("The time is now {}", dateFormat1.format(today));
//        df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
//        String PST = df.format(today);
//        System.out.println(PST);

        // Todo: Call scheduler to schedule subsequent round for ongoing tournament
        ongoing_tournamentIDs = this.roundHasRepository.findRoundsPastEndDate();
        System.out.println(ongoing_tournamentIDs);

    //        List<RoundHas> tournaments = this.roundHasRepository.findRoundsPastEndDate1();
    //        System.out.println(tournaments);

        // Todo: Call scheduler to schedule round for new tournament
        new_tournamentIDs = this.tournamentRepository.findTournamentPastCloseDate();
        System.out.println(new_tournamentIDs);
        for(Integer tournamentID : new_tournamentIDs){
            this.tournamentRepository.setTournamentStatus(1, tournamentID);
            scheduler.createSchedule(tournamentID);
        }


    }

}