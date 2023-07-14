package com.github.aandrosov.tkinter.library;

import com.github.aandrosov.tkinter.library.enitity.UserEntity;
import com.github.aandrosov.tkinter.library.exception.TkinterException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AuthRequest {

    private final TkinterClient client;

    public AuthRequest(TkinterClient client) {
        this.client = client;
    }

    public String login(String phone, String password) throws IOException, TkinterException {
        String data = "phone=" + phone + "&password=" + password;
        String response = new String(client.post("/auth/login", data), StandardCharsets.UTF_8);
        return ((JsonObject) JsonParser.parseString(response)).get("token").getAsString();
    }

    public long register(UserEntity user) throws IOException, TkinterException {
        String data = "name=" + user.getName() +
                "&about=" + user.getAbout() +
                "&phone=" + user.getPhone() +
                "&password=" + user.getPassword() +
                "&cityId=" + user.getCityId();
        String response = new String(client.post("/auth/register", data), StandardCharsets.UTF_8);
        return ((JsonObject) JsonParser.parseString(response)).get("id").getAsLong();
    }
}
