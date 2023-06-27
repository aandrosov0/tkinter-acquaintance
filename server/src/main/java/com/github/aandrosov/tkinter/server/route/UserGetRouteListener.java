package com.github.aandrosov.tkinter.server.route;

import com.github.aandrosov.tkinter.server.OnRouteListener;
import com.github.aandrosov.tkinter.server.Request;
import com.github.aandrosov.tkinter.server.Response;

import java.io.IOException;
import java.util.Map;

public class UserGetRouteListener implements OnRouteListener {

    @Override
    public void listen(Request request, Response response, Map<String, String> routeQuery) throws IOException {
    }

    @Override
    public String getRoute() {
        return "/api/user/get/:id";
    }
}
