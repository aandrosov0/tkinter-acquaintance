package com.github.aandrosov.tkinter.server.route.api;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.aandrosov.tkinter.server.OnRouteListener;
import com.github.aandrosov.tkinter.server.Request;
import com.github.aandrosov.tkinter.server.Response;
import com.github.aandrosov.tkinter.server.entity.EntityService;
import com.github.aandrosov.tkinter.server.entity.MessageEntity;
import com.github.aandrosov.tkinter.server.entity.UserEntity;

import java.util.HashMap;
import java.util.Map;

public class MeMessageSendRoute implements OnRouteListener {

    private final EntityService entityService;

    public MeMessageSendRoute(EntityService entityService) {
        this.entityService = entityService;
    }

    @Override
    public void listen(Request request, Response response, Map<String, String> routeQuery) throws Exception {
        DecodedJWT decodedJWT = request.getJWT();

        if(decodedJWT == null) {
            response.sendHeaders(401, -1);
            return;
        }

        if(!request.isFormUrlEncodedContentType()) {
            response.sendHeaders(415, -1);
            return;
        }

        Map<String, String> formUrlEncoded = request.parseFormUrlEncoded();
        String text = formUrlEncoded.get("text");
        if((text == null) || !formUrlEncoded.containsKey("destinationId")) {
            response.sendHeaders(400, -1);
            return;
        }

        int destinationId;
        try {
            destinationId = Integer.parseInt(formUrlEncoded.get("destinationId"));
        } catch(NumberFormatException ignored) {
            response.sendHeaders(422, -1);
            return;
        }

        long authorId = decodedJWT.getClaim("id").asLong();
        if(destinationId == authorId) {
            response.sendHeaders(409, -1);
            return;
        }

        Map<String, Object> criteria = new HashMap<>();
        criteria.put("id", destinationId);

        if(!entityService.isExists(UserEntity.class, criteria)) {
            response.sendHeaders(404, -1);
            return;
        }

        MessageEntity message = new MessageEntity(authorId, destinationId, text);

        long id = entityService.insert(message);
        response.sendJson("{\"id\":" + id + "}", 200);
    }

    @Override
    public String getRoute() {
        return "/api/me/send/message";
    }
}
