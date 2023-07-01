package com.github.aandrosov.tkinter.server;

import com.github.aandrosov.tkinter.toolchain.Files;

import java.io.File;
import java.nio.file.Paths;

public class Main {

    public static final String CONFIG = "config.properties";

    public static final String HOST_PROPERTY_NAME = "server.host";
    public static final String PORT_PROPERTY_NAME = "server.port";

    public static void main(String[] args) throws Exception {
        File config = Files.getFileOrMake(Paths.get(CONFIG));

        int port = Integer.parseInt(Files.getFilePropertyOrTerminate(config, PORT_PROPERTY_NAME));
        String host = Files.getFilePropertyOrTerminate(config, HOST_PROPERTY_NAME);

        Server server = new Server(host, port);
        server.start();
    }
}
