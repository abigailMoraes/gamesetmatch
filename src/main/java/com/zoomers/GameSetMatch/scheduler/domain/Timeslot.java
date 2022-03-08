package com.zoomers.GameSetMatch.scheduler.domain;

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
}
