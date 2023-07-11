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

    private final Gson gson = new Gson();

    private String token;

    private int readTimeout = 10000;

    private int connectTimeout = 10000;

    public TkinterClient(String phone, String password) throws IOException, TkinterException {
        token = auth().login(phone, password);
    }

    public AuthRequest auth() {
        return new AuthRequest(this);
    }

    public UserRequest user() {
        return new UserRequest(this, gson);
    }

    public MePhotoRequest mePhoto() {
        return new MePhotoRequest(this);
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
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

    protected byte[] GET(String route) throws IOException, TkinterException {
        TkinterConnection connection = new TkinterConnection(API_URL + route, connectTimeout, readTimeout);
        if(token != null) {
            connection.setRequestHeader("Authorization", "Bearer " + token);
        }

        return connection.read();
    }

    protected byte[] POST(String route, byte[] data) throws IOException, TkinterException {
        TkinterConnection connection = new TkinterConnection(API_URL + route, connectTimeout, readTimeout);
        connection.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        if(token != null) {
            connection.setRequestHeader("Authorization", "Bearer " + token);
        }

        connection.write(data);
        return connection.read();
    }

    protected byte[] POST(String route, String data) throws IOException, TkinterException {
        return POST(route, data.getBytes(StandardCharsets.UTF_8));
    }

    protected byte[] PUT(String route, File file) throws IOException, TkinterException {
        TkinterConnection connection = new TkinterConnection(API_URL + route, connectTimeout, readTimeout);
        if(token != null) {
            connection.setRequestHeader("Authorization", "Bearer " + token);
        }

        String contentType = getFileType(file);
        connection.setRequestHeader("Content-Type", contentType);

        try(FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] data = Streams.readStream(fileInputStream);
            connection.write(data);
        }

        return connection.read();
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
}
