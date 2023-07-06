package com.github.aandrosov.tkinter.server.route.api;

import com.github.aandrosov.tkinter.server.OnRouteListener;
import com.github.aandrosov.tkinter.server.Request;
import com.github.aandrosov.tkinter.server.Response;
import com.github.aandrosov.tkinter.server.entity.Entity;
import com.github.aandrosov.tkinter.server.entity.EntityService;
import com.github.aandrosov.tkinter.server.entity.UserEntity;
import com.github.aandrosov.tkinter.toolchain.Strings;

import java.util.List;
import java.util.Map;

public class UserGetAmountRoute implements OnRouteListener {

    private final EntityService entityService;

    public UserGetAmountRoute(EntityService entityService) {
        this.entityService = entityService;
    }

    @Override
    public void listen(Request request, Response response, Map<String, String> routeQuery) throws Exception {
        int limit, offset;
        try {
            limit = Integer.parseInt(routeQuery.get("limit"));
            offset = Integer.parseInt(routeQuery.get("offset"));
        } catch(NumberFormatException ignored) {
            response.sendHeaders(422, -1);
            return;
        }

        List<Entity> users = entityService.fetch(UserEntity.class, offset, limit);
        String jsonUsers = Strings.arrayToStringSequence(users.toArray(), ",", "");
        response.sendJson("{\"users\":[" + jsonUsers + "]}", 200);
    }

    @Override
    public String getRoute() {
        return "/api/user/get/amount/limit/:limit/offset/:offset";
    }
}
