package com.zoomers.GameSetMatch.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateAndLocalDateService {
    public static int DaysBetweenRounds = 2;

    public static Date localDateToDate(LocalDate date) {
        return Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
