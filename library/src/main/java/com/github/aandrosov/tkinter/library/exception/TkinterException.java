package com.github.aandrosov.tkinter.library.exception;

public class TkinterException extends Exception {

    private final int statusCode;

    public TkinterException(String message, int statusCode) {
        super(message + "\nStatus Code: " + statusCode);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
