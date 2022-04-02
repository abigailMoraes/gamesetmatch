package com.zoomers.GameSetMatch.services.Errors;

public class InvalidActionForTournamentStatusException extends Exception {
    public InvalidActionForTournamentStatusException(String message) {
        super(message);
    }
}