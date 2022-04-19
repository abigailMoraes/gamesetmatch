package com.zoomers.GameSetMatch.scheduler.domain;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.zoomers.GameSetMatch.services.DateAndLocalDateService.timezoneOfData;

public class Timeslot {

    private final float time;
    private final int id;
    private Date date;

    public Timeslot(float time, Date date) {

        this.time = time;
        this.id = convertToID(time, date);
        this.date = date;
    }

    public Timeslot(Timeslot timeslot) {
        this.time = timeslot.time;
        this.date = timeslot.date;
        this.id = convertToID(this.time, this.date);
    }

    private int convertToID(float time, Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return (int)((time - 9)* 2) + 24 * (calendar.get(Calendar.DAY_OF_WEEK) - 1);
    }

    private String convertTimeToString(float timeToConvert) {

        String timeFormat = "";

        if (timeToConvert < 10) {
            timeFormat += "0" + (int)Math.floor(this.time);
        }
        else {
            timeFormat += (int)Math.floor(this.time);
        }

        if (timeToConvert % 1 == 0) {
            timeFormat += ":00";
        }
        else {
            timeFormat += ":30";
        }

        return timeFormat + ":00";
    }

    public void addWeek() {

        LocalDate nextWeek = this.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusWeeks(1);
        this.date = Date.from(nextWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public float getTime() {
        return time;
    }

    public int getID() { return id; }

    public Date getDate() {
        return this.date;
    }

    public String getDateString() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        return sdfDate.format(this.date);
    }

    public LocalDateTime getLocalStartDateTime() {

        LocalDate localDate = this.date.toInstant().atZone(timezoneOfData).toLocalDate();

        int minutes = 30;
        if (this.time % 1 == 0) {
            minutes = 0;
        }

        LocalTime localTime = LocalTime.of(
                (int)Math.floor(this.time),
                minutes,
                0,
                0
        );
        return LocalDateTime.of(localDate, localTime);
    }

    public LocalDateTime getLocalEndDateTime(int matchDuration) {

        LocalDateTime startTime = this.getLocalStartDateTime();

        long timeToAdd = Math.round(Math.ceil(matchDuration/30.0)) * 30;

        return startTime.plusMinutes(timeToAdd);
    }

    public String getEndTime(int matchDuration) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        return sdfDate.format(this.date) + " " + convertTimeToString(time + matchDuration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Timeslot timeslot = (Timeslot) o;
        return id == timeslot.id && Objects.equals(date, timeslot.date);
    }

    @Override
    public String toString() {
        return this.date + " " + convertTimeToString(time);
    }
}