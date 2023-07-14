package com.github.aandrosov.tkinter.library;

import com.github.aandrosov.tkinter.library.exception.TkinterException;

import java.io.*;

public class MePhotoRequest {

    private final TkinterClient client;

    private final String token;

    public MePhotoRequest(TkinterClient client, String token) {
        this.client = client;
        this.token = token;
    }

    public void upload(File photo) throws IOException, TkinterException {
        client.put("/me/upload/photo", photo, token);
    }

    public byte[] get() throws IOException, TkinterException {
        return client.get("/me/get/photo", token);
    }
}
