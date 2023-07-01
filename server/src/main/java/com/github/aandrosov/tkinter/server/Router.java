package com.github.aandrosov.tkinter.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Router implements HttpHandler {

    private OnRequestMethodListener methodGetListener = () -> new OnRouteListener[]{};

    private OnRequestMethodListener methodPostListener = () -> new OnRouteListener[]{};

    private OnRequestMethodListener methodPutListener = () -> new OnRouteListener[]{};

    private OnRequestMethodListener methodDeleteListener = () -> new OnRouteListener[]{};

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();

        switch(requestMethod) {
            case "GET":
                matchRoutes(exchange, methodGetListener.getRoutes());
                break;
            case "POST":
                matchRoutes(exchange, methodPostListener.getRoutes());
                break;
            case "PUT":
                matchRoutes(exchange, methodPutListener.getRoutes());
                break;
            case "DELETE":
                matchRoutes(exchange, methodDeleteListener.getRoutes());
                break;
            default:
                exchange.getResponseHeaders().add("Allow", "GET, POST, PUT, DELETE");
                exchange.sendResponseHeaders(405, -1);
        }

        String formattedNowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-M-yyyy hh:mm:ss"));
        System.out.println(formattedNowTime + " " + exchange.getRequestURI().getPath() + " " + requestMethod + " [" + exchange.getResponseCode() + "]");

        exchange.close();
    }

    private void matchRoutes(HttpExchange exchange, OnRouteListener[] routes) throws IOException {
        for(OnRouteListener route : routes) {
            String routePath = route.getRoute();

            Map<String, String> routeMatches = matchRoute(routePath, exchange.getRequestURI().getPath());

            if(routeMatches != null) {
                executeRouteAndCatchException(route, exchange, routeMatches);
                return;
            }
        }

        exchange.sendResponseHeaders(404, -1);
    }

    private void executeRouteAndCatchException(OnRouteListener listener, HttpExchange exchange, Map<String, String> routeQuery) throws IOException {
        try {
            listener.listen(new Request(exchange), new Response(exchange), routeQuery);
        } catch(Exception exception) {
            System.err.println("Exception have caught:\n    " + exception.getMessage() + "\n    " + exception.getStackTrace()[0]);
            exchange.sendResponseHeaders(500, -1);
        }
    }

    private Map<String, String> matchRoute(String match, String path) {
        String[] splitMatch = match.split("/");
        String[] splitPath = path.split("/");

        if(splitMatch.length != splitPath.length) {
            return null;
        }

        for(int i = 0; i < splitMatch.length; i++) {
            if(!splitMatch[i].equals(splitPath[i]) && !(splitMatch[i].length() > 1 && splitMatch[i].charAt(0) == ':')) {
                return null;
            }
        }

        Map<String, String> matches = new HashMap<>();
        for(int i = 0; i < splitMatch.length; i++) {
            String word = splitMatch[i];

            if(word.length() > 1 && word.charAt(0) == ':') {
                matches.put(word.substring(1), splitPath[i]);
            }
        }

        return matches;
    }

    public void setGetListener(OnRequestMethodListener listener) {
        this.methodGetListener = listener;
    }

    public void setPostListener(OnRequestMethodListener listener) {
        this.methodPostListener = listener;
    }

    public void setPutListener(OnRequestMethodListener listener) {
        this.methodPutListener = listener;
    }

    public void setDeleteListener(OnRequestMethodListener listener) {
        this.methodDeleteListener = listener;
    }
}
