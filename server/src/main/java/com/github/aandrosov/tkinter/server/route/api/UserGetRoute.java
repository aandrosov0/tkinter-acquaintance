package com.github.aandrosov.tkinter.server.route.api;

import com.github.aandrosov.tkinter.server.OnRouteListener;
import com.github.aandrosov.tkinter.server.Request;
import com.github.aandrosov.tkinter.server.Response;
import com.github.aandrosov.tkinter.server.entity.EntityFactory;
import com.github.aandrosov.tkinter.server.entity.UserEntity;

import java.util.Map;

public class UserGetRoute implements OnRouteListener {

    @Override
    public void listen(Request request, Response response, Map<String, String> routeQuery) throws Exception {
        int id;
        try {
            id = Integer.parseInt(routeQuery.get("id"));
        } catch(NumberFormatException ignored) {
            response.sendHeaders(422, -1);
            return;
        }

        UserEntity user = EntityFactory.getBy("id", id, UserEntity.class);

        if(user == null) {
            response.sendHeaders(404, -1);
            return;
        }

        response.sendJson(user.toString(), 200);
    }

    @Override
    public String getRoute() {
        return "/api/user/get/:id";
    }
}
