package com.github.aandrosov.tkinter.server;

import com.github.aandrosov.tkinter.server.route.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    private final Router router = new Router();

    private final HttpServer httpServer;

    public Server(String host, int port) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(host, port), 0);

        router.setGetListener(this::getMethodGetListener);
        router.setPostListener(this::getMethodPostListener);
        router.setPutListener(this::getMethodPutListener);
        router.setDeleteListener(this::getMethodDeleteListener);
    }

    public void start() {
        httpServer.createContext("/").setHandler(router);
        httpServer.start();

        InetSocketAddress address = httpServer.getAddress();
        System.out.println("Server running on http://" + address.getHostString() + ":" + address.getPort());
    }

    private OnRouteListener[] getMethodGetListener() {
        return new OnRouteListener[]{
                new UserGetRouteListener()
        };
    }

    private OnRouteListener[] getMethodPostListener() {
        return new OnRouteListener[]{};
    }

    private OnRouteListener[] getMethodPutListener() {
        return new OnRouteListener[]{};
    }

    private OnRouteListener[] getMethodDeleteListener() {
        return new OnRouteListener[]{};
    }
}