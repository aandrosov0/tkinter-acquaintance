package com.github.aandrosov.tkinter.library;

import com.github.aandrosov.tkinter.library.exception.TkinterException;
import com.github.aandrosov.tkinter.library.exception.TkinterFileFormatException;
import com.github.aandrosov.tkinter.toolchain.Files;
import com.github.aandrosov.tkinter.toolchain.Streams;
import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TkinterClient {

    public static final String API_URL = "http://127.0.0.1:8080/api";

    public static final String AUTHORIZATION_SCHEME = "Bearer";

    private final Gson gson = new Gson();

    private int readTimeout = 10000;

    private int connectTimeout = 10000;

    public AuthRequest auth() {
        return new AuthRequest(this);
    }

    public UserRequest user() {
        return new UserRequest(this, gson);
    }

    public MePhotoRequest mePhoto(String token) {
        return new MePhotoRequest(this, token);
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    protected byte[] get(String route) throws IOException, TkinterException {
        try(TkinterConnection connection = establishConnection(route)) {
            return connection.read();
        }
    }

    protected byte[] get(String route, String token) throws IOException, TkinterException {
        try(TkinterConnection connection = establishConnection(route, token)) {
            return connection.read();
        }
    }

    protected byte[] post(String route, byte[] data) throws IOException, TkinterException {
        try(TkinterConnection connection = establishConnection(route)) {
            return connection.writeAndRead(data, "application/x-www-form-urlencoded");
        }
    }

    protected byte[] post(String route, byte[] data, String token) throws IOException, TkinterException {
        try(TkinterConnection connection = establishConnection(route, token)) {
            return connection.writeAndRead(data, "application/x-www-form-urlencoded");
        }
    }

    protected byte[] post(String route, String data) throws IOException, TkinterException {
        return post(route, data.getBytes(StandardCharsets.UTF_8));
    }

    protected byte[] post(String route, String data, String token) throws IOException, TkinterException {
        return post(route, data.getBytes(StandardCharsets.UTF_8), token);
    }

    protected byte[] put(String route, File file, String token) throws IOException, TkinterException {
        try(TkinterConnection connection = establishConnection(route, token)) {
            connection.setRequestMethod("PUT");

            try(FileInputStream fileInputStream = new FileInputStream(file)) {
                byte[] data = Streams.readStream(fileInputStream);
                return connection.writeAndRead(data, getFileType(file));
            }
        }
    }

    private String getFileType(File file) throws IOException {
        if(Files.isPng(file)) {
            return "image/png";
        }

        if(Files.isJpeg(file)) {
            return "image/jpeg";
        }

        throw new TkinterFileFormatException("Can't define file format");
    }

    private TkinterConnection establishConnection(String route) throws IOException {
        return new TkinterConnection(API_URL + route, connectTimeout, readTimeout);
    }

    private TkinterConnection establishConnection(String route, String token) throws IOException {
        TkinterConnection connection = establishConnection(route);
        connection.setRequestHeader("Authorization", AUTHORIZATION_SCHEME + " " + token);
        return connection;
    }
}
