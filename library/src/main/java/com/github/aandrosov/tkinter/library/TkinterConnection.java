package com.github.aandrosov.tkinter.library;

import com.github.aandrosov.tkinter.library.exception.TkinterAuthException;
import com.github.aandrosov.tkinter.library.exception.TkinterException;
import com.github.aandrosov.tkinter.toolchain.Streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class TkinterConnection {

    private final HttpURLConnection connection;

    public TkinterConnection(String route, int connectTimeout, int readTimeout) throws IOException {
        URL url = new URL(route);
        connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(connectTimeout);
        connection.setReadTimeout(readTimeout);
    }

    public byte[] read() throws IOException, TkinterException {
        int statusCode = connection.getResponseCode();
        if(statusCode == 401) {
            throw new TkinterAuthException("Can't authorize", statusCode);
        } else if(statusCode >= 400) {
            throw new TkinterException("Server returns error code", statusCode);
        }

        try(InputStream inputStream = connection.getInputStream()) {
            return Streams.readStream(inputStream);
        }
    }

    public void write(byte[] data) throws IOException {
        connection.setDoOutput(true);
        try(OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(data);
        }
    }

    public void setRequestMethod(String method) throws ProtocolException {
        connection.setRequestMethod(method);
    }

    public void setRequestHeader(String header, String content) {
        connection.setRequestProperty(header, content);
    }
}
