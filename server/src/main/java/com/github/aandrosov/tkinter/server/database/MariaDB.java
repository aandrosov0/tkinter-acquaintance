package com.github.aandrosov.tkinter.server.database;

import com.github.aandrosov.tkinter.server.Main;
import com.github.aandrosov.tkinter.toolchain.Files;

import java.io.File;
import java.nio.file.Paths;
import java.sql.*;

public class MariaDB {

    public static final String HOST_PROPERTY_NAME = "db.host";
    public static final String PORT_PROPERTY_NAME = "db.port";
    public static final String NAME_PROPERTY_NAME = "db.name";
    public static final String USER_PROPERTY_NAME = "db.user";
    public static final String PASS_PROPERTY_NAME = "db.pass";

    private MariaDB() {

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
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void fillPreparedStatement(PreparedStatement statement, Object[] values) throws SQLException {
        for(int i = 0; i < values.length; i++) {
            statement.setObject(i + 1, values[i]);
        }
    }
}
