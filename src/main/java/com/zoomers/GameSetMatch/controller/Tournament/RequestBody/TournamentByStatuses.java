package com.zoomers.GameSetMatch.controller.Tournament.RequestBody;

import lombok.Getter;

import java.util.List;

@Getter
public class TournamentByStatuses {
    List<Integer> statuses;
}
