package com.zoomers.GameSetMatch.controller.Tournament.RequestBody;

import java.util.Date;
import java.util.List;

public class IncomingAvailability {
    private Date date;
    private List<Boolean> slots;

    public Date getDate() {
        return date;
    }

    public List<Boolean> getSlots() {
        return slots;
    }
}
