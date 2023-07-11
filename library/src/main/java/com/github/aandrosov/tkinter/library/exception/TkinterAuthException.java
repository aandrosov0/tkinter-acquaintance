package com.github.aandrosov.tkinter.library.exception;

public class TkinterAuthException extends TkinterException {

    public TkinterAuthException(String message, int statusCode) {
        super(message, statusCode);
    }
}
