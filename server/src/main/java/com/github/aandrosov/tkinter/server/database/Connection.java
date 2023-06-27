package com.github.aandrosov.tkinter.server.database;

import com.github.aandrosov.tkinter.server.Main;
import com.github.aandrosov.tkinter.toolchain.Files;
import org.mariadb.jdbc.Configuration;
import org.mariadb.jdbc.client.Client;

import java.nio.file.Paths;
import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Connection extends org.mariadb.jdbc.Connection {

    public static final String HOST_PROPERTY_NAME = "db.host";
    public static final String PORT_PROPERTY_NAME = "db.port";
    public static final String NAME_PROPERTY_NAME = "db.name";
    public static final String USER_PROPERTY_NAME = "db.user";
    public static final String PASS_PROPERTY_NAME = "db.pass";

    public Connection(Configuration conf, ReentrantLock lock, Client client) {
        super(conf, lock, client);
    }

    public static Connection connect() {
        File file = Paths.get(Main.CONFIG).toFile();
        String host = Files.getFilePropertyOrTerminate(file, HOST_PROPERTY_NAME);
        String port = Files.getFilePropertyOrTerminate(file, PORT_PROPERTY_NAME);
        String name = Files.getFilePropertyOrTerminate(file, NAME_PROPERTY_NAME);
        String user = Files.getFilePropertyOrTerminate(file, USER_PROPERTY_NAME);
        String pass = Files.getFilePropertyOrTerminate(file, PASS_PROPERTY_NAME);

        String url = "jdbc:mariadb://" + host + ":" + port + "/" + name;
        try {
            return (Connection) DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            super.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
