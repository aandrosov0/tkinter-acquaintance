package com.github.aandrosov.tkinter.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


public class Response {

    private final HttpExchange exchange;

    public Response(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public void sendHeaders(int code, long length) throws IOException {
        exchange.sendResponseHeaders(code, length);
    }

    public void sendData(byte[] data, int code, String contentType) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", contentType);
        exchange.sendResponseHeaders(code, data.length);
        exchange.getResponseBody().write(data);
    }

    public void sendJson(String json, int code) throws IOException {
        sendData(json.getBytes(StandardCharsets.UTF_8), code, "application/json; charset=utf-8");
    }

    public void sendText(String text, int code) throws IOException {
        sendData(text.getBytes(StandardCharsets.UTF_8), code, "text/html; charset=utf-8");
    }

    public void sendData(byte[] data, int code) throws IOException {
        sendData(data, code, "application/octet-stream");
    }

    public Headers getHeaders() {
        return exchange.getResponseHeaders();
    }

    public int getCode() {
        return exchange.getResponseCode();
    }

    public OutputStream getBody() {
        return exchange.getResponseBody();
    }
}
