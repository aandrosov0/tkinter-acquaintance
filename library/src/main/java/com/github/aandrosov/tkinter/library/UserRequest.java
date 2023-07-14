package com.github.aandrosov.tkinter.library;

import com.github.aandrosov.tkinter.library.enitity.UserEntity;
import com.github.aandrosov.tkinter.library.exception.TkinterException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UserRequest {

    private final TkinterClient client;

    private final Gson gson;

    public UserRequest(TkinterClient client, Gson gson) {
        this.client = client;
        this.gson = gson;
    }

    public UserEntity get(long id) throws IOException, TkinterException {
        String response = new String(client.get("/user/get/" + id), StandardCharsets.UTF_8);
        return gson.fromJson(response, UserEntity.class);
    }

    public List<UserEntity> getAmount(int limit, int offset) throws IOException, TkinterException {
        String route = "/user/get/amount/limit/" + limit + "/offset/" + offset;
        String response = new String(client.get(route), StandardCharsets.UTF_8);
        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
        return gson.fromJson(json.getAsJsonArray("users"), new TypeToken<List<UserEntity>>(){}.getType());
    }
}
