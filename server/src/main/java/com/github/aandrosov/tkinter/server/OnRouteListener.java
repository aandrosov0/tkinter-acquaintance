package com.github.aandrosov.tkinter.server;

import java.io.IOException;
import java.util.Map;

public interface OnRouteListener {

    void listen(Request request, Response response, Map<String, String> routeQuery) throws IOException;

    String getRoute();
}