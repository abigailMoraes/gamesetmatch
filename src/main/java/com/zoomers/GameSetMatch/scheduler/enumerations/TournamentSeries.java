package com.zoomers.GameSetMatch.scheduler.enumerations;

public enum TournamentSeries {
    BEST_OF_1(1),
    BEST_OF_3(3),
    BEST_OF_5(5),
    BEST_OF_7(7);

    private int numberOfGames;
    private TournamentSeries(int numberOfGames) {

        this.numberOfGames = numberOfGames;
    }

    public int getNumberOfGames() { return numberOfGames; }
}
