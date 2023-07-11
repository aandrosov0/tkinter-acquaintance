package com.github.aandrosov.tkinter.library;

import com.github.aandrosov.tkinter.library.exception.TkinterException;

import java.io.*;

public class MePhotoRequest {

    private final TkinterClient client;

    public MePhotoRequest(TkinterClient client) {
        this.client = client;
    }

    public void uploadPhoto(File photo) throws IOException, TkinterException {
        client.PUT("/me/upload/photo", photo);
    }

    public byte[] getPhoto() throws IOException, TkinterException {
        return client.GET("/me/get/photo");
    }
}
