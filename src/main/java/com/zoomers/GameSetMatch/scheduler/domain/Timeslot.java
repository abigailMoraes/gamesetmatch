package com.zoomers.GameSetMatch.scheduler.domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Timeslot {

    private final float time;
    private final int id;
    private final Date date;

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

    public float getTime() {
        return time;
    }

    public int getID() { return id; }

    public Date getDate() {
        return date;
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
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(this.date) + " at " + this.time;
    }
}
