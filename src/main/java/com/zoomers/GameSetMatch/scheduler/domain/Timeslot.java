package com.zoomers.GameSetMatch.scheduler.domain;

import java.util.Objects;

public class Timeslot {

    private float time;
    private int id;

    public Timeslot(float time) {

        this.time = time;
        this.id = convertToID(time);
    }

    private int convertToID(float time) {

        return (int)((time - 7)* 2);
    }

    public float getTime() {
        return time;
    }

    public int getID() {
        return id;
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
