package com.zoomers.GameSetMatch.controller.Match.RequestBody;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomingCheckNewMatchTime {
    String newMatchAsAvailabilityString;
    int dayOfWeek;
}
