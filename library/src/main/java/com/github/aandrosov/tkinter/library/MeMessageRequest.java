package com.github.aandrosov.tkinter.library;

import com.github.aandrosov.tkinter.library.enitity.MessageEntity;
import com.github.aandrosov.tkinter.library.exception.TkinterException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MeMessageRequest {

    private final TkinterClient client;
    private final String token;

    private final Gson gson;

    public MeMessageRequest(TkinterClient client, Gson gson, String token) {
        this.client = client;
        this.token = token;
        this.gson = gson;
    }

    public long send(MessageEntity message) throws IOException, TkinterException {
        String data = "text=" + message.getText() + "&destinationId=" + message.getDestinationId();
        String response = new String(client.post("/me/message/send", data), StandardCharsets.UTF_8);
        return JsonParser.parseString(response).getAsJsonObject().get("id").getAsLong();
    }

    public List<MessageEntity> getAmount() throws IOException, TkinterException {
        String response = new String(client.get("/me/messages/get", token), StandardCharsets.UTF_8);
        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
        return gson.fromJson(json.getAsJsonArray("messages"), new TypeToken<List<MessageEntity>>(){}.getType());
    }
}
