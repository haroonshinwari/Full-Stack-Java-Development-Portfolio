package guessthenumber.controllers;

import java.time.LocalDateTime;

public class Error {

    private LocalDateTime timestamp = LocalDateTime.now();
    private String message;

    public Error(String message) {
        this.message = message;
    }
}
