package com.github.aandrosov.tkinter.server.route.api;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.aandrosov.tkinter.server.OnRouteListener;
import com.github.aandrosov.tkinter.server.Request;
import com.github.aandrosov.tkinter.server.Response;
import com.github.aandrosov.tkinter.server.entity.Entity;
import com.github.aandrosov.tkinter.server.entity.EntityService;
import com.github.aandrosov.tkinter.server.entity.MessageEntity;
import com.github.aandrosov.tkinter.toolchain.Strings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeMessagesGetRoute implements OnRouteListener {

    private final EntityService entityService;

    public MeMessagesGetRoute(EntityService entityService) {
        this.entityService = entityService;
    }

    @Override
    public void listen(Request request, Response response, Map<String, String> routeQuery) throws Exception {
        DecodedJWT jwt = request.getJWT();

        if(jwt == null) {
            response.sendHeaders(401, -1);
            return;
        }

        long userId = jwt.getClaim("id").asLong();

        Map<String, Object> criteria = new HashMap<>();
        criteria.put("author_id", userId);
        criteria.put("destination_id", userId);

        List<Entity> messages = entityService.find(MessageEntity.class, criteria);
        String jsonMessages = Strings.arrayToStringSequence(messages.toArray(), ",", "");
        response.sendJson("{\"messages\":[" + jsonMessages + "]}", 200);
    }

    @Override
    public String getRoute() {
        return "/api/me/messages/get";
    }
}
