package com.zoomers.GameSetMatch.entity.EnumsForColumns;

// User_involves_match result
public enum MatchResult {
    PENDING(-1),
    TIE(0),
    WIN(1),
    LOSS(2);

    private final int result;

    private MatchResult(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}

