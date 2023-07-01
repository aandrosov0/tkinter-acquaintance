package com.github.aandrosov.tkinter.server;

import com.github.aandrosov.tkinter.server.route.api.*;
import com.auth0.jwt.algorithms.Algorithm;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    public final static Algorithm SECURITY_ALGORITHM = Algorithm.HMAC256("Bu2'!Hy&^RQrE7'");

    public final static String JWT_AUTHORIZATION_SCHEME = "Bearer";

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
                new UserGetRoute(),
                new UserGetAmountRoute()
        };
    }

    private OnRouteListener[] getMethodPostListener() {
        return new OnRouteListener[]{
                new LoginRoute(),
                new RegisterRoute()
        };
    }

    private OnRouteListener[] getMethodPutListener() {
        return new OnRouteListener[]{};
    }

    private OnRouteListener[] getMethodDeleteListener() {
        return new OnRouteListener[]{};
    }
}