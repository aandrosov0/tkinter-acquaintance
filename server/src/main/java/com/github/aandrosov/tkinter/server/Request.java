package com.github.aandrosov.tkinter.server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.aandrosov.tkinter.toolchain.Streams;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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

    public DecodedJWT getJWT() {
        String header = exchange.getRequestHeaders().getFirst("Authorization");

        if(header == null) {
            return null;
        }

        String[] credentials = header.split(" ", 2);

        if((credentials.length < 2)
                || !credentials[0].equals(Server.JWT_AUTHORIZATION_SCHEME)) {
            return null;
        }

        JWTVerifier verifier = JWT.require(Server.SECURITY_ALGORITHM)
                .withClaim("id", (claim, decodedJWT) -> {
                    Claim idClaim = decodedJWT.getClaim("id");
                    return idClaim != null && idClaim.asLong() != null;
                })
                .build();

        try {
            return verifier.verify(credentials[1]);
        } catch(JWTVerificationException ignored) {
            return null;
        }
    }

    public Map<String, String> parseFormUrlEncoded() throws IOException {
        String data = new String(Streams.readStream(exchange.getRequestBody()), StandardCharsets.UTF_8);
        Map<String, String> map = new HashMap<>();

        for(String query : data.split("&")) {
            String[] variable = query.split("=", 2);

            if(variable.length != 2) {
                return map;
            }

            map.put(variable[0], variable[1]);
        }

        return map;
    }

    public boolean isFormUrlEncodedContentType() {
        return "application/x-www-form-urlencoded".equals(exchange.getRequestHeaders().getFirst("Content-Type"));
    }

    public boolean isImageContentType() {
        String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
        return "image/jpeg".equals(contentType) || "image/png".equals(contentType);
    }
}
