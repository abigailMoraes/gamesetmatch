package com.zoomers.GameSetMatch.controller.Error;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class ApiException {

    private HttpStatus httpStatus;
    private String message;
    private List<String> errors;

    public ApiException(HttpStatus httpStatus, String message, List<String> errors) {
        super();
        this.httpStatus = httpStatus;
        this.message = message;
        this.errors = errors;
    }

    public ApiException(HttpStatus httpStatus, String message, String error) {
        super();
        this.httpStatus = httpStatus;
        this.message = message;
        errors = Arrays.asList(error);
    }

    public ApiException(HttpStatus httpStatus, String message) {
        super();
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
