package com.zoomers.GameSetMatch.controller.Tournament.RequestBody;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class IncomingAvailability {
    private Date date;
    private List<Boolean> slots;

    public int getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return Calendar.DAY_OF_WEEK;
    }

    public List<Boolean> getSlots() {
        return slots;
    }
}
