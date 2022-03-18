package com.zoomers.GameSetMatch.scheduler.domain;

import java.util.Date;
import java.util.Objects;

public class Timeslot {

    private final float time;
    private final int id;
    private final Date date;

    public Timeslot(float time, Date date) {

        this.time = time;
        this.id = convertToID(time);
        this.date = date;
    }

    private int convertToID(float time) {

        return (int)((time - 9)* 2);
    }

    public float getTime() {
        return time;
    }

    public int getID() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Timeslot timeslot = (Timeslot) o;
        return id == timeslot.id;
    }

    @Override
    public String toString() {
        return " " + time;
    }
}
