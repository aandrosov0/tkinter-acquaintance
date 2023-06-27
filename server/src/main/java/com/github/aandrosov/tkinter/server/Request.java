package com.github.aandrosov.tkinter.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.InputStream;
import java.net.URI;

public class Request {

    private final HttpExchange exchange;

    public Request(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public InputStream getBody() {
        return exchange.getRequestBody();
    }

    public Headers getHeaders() {
        return exchange.getRequestHeaders();
    }

    public String getRequestMethod() {
        return exchange.getRequestMethod();
    }

    public URI getURI() {
        return exchange.getRequestURI();
    }
}
