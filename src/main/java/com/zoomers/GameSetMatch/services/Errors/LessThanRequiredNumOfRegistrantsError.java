package com.zoomers.GameSetMatch.services.Errors;

public class LessThanRequiredNumOfRegistrantsError extends Exception {
    public LessThanRequiredNumOfRegistrantsError(String message) {
        super(message);
    }
}
