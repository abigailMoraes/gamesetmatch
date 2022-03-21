package com.zoomers.GameSetMatch.scheduler.domain;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Timeslot {

    private final float time;
    private final int id;
    private Date date;

    public Timeslot(float time, Date date) {

        this.time = time;
        this.id = convertToID(time, date);
        this.date = date;
    }

    private int convertToID(float time, Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return (int)((time - 9)* 2) + 24 * (calendar.get(Calendar.DAY_OF_WEEK) - 1);
    }

    private String convertTimeToString(float timeToConvert) {

        String timeFormat = "";

        if (timeToConvert < 10) {
            timeFormat += "0" + Math.floor(this.time);
        }
        else {
            timeFormat += Math.floor(this.time);
        }

        if (timeToConvert % 0.5 == 0) {
            timeFormat += ":30";
        }
        else {
            timeFormat += ":00";
        }

        return timeFormat + ":00";
    }

    public void addWeek() {

        LocalDate nextWeek = this.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusWeeks(1);

        System.out.println("    Before: " + this.date);
        this.date = Date.from(nextWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        System.out.println("    After: " + this.date);
    }

    public float getTime() {
        return time;
    }

    public int getID() { return id; }

    public Date getDate() {
        return date;
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
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        return sdfDate.format(this.date) + " " + convertTimeToString(time);
    }
}
