package com.github.aandrosov.tkinter.server.route.api;

import com.github.aandrosov.tkinter.server.OnRouteListener;
import com.github.aandrosov.tkinter.server.Request;
import com.github.aandrosov.tkinter.server.Response;
import com.github.aandrosov.tkinter.server.entity.EntityFactory;
import com.github.aandrosov.tkinter.server.entity.UserEntity;

import java.util.List;
import java.util.Map;

public class UserGetAmountRoute implements OnRouteListener {

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

        List<UserEntity> users = EntityFactory.getAmount(limit, offset, UserEntity.class);
        StringBuilder jsonUsers = new StringBuilder();

        for(UserEntity user : users) {
            if(jsonUsers.length() > 0) {
                jsonUsers.append(",");
            }
            jsonUsers.append(user.toString());
        }

        response.sendJson("{\"users\":[" + jsonUsers + "]}", 200);
    }

    @Override
    public String getRoute() {
        return "/api/user/get/amount/limit/:limit/offset/:offset";
    }
}
