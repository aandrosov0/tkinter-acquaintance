package com.github.aandrosov.tkinter.server.database;

import com.github.aandrosov.tkinter.server.Main;
import com.github.aandrosov.tkinter.toolchain.Files;

import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Map;

public class MariaDB implements AutoCloseable {

    public static final String HOST_PROPERTY_NAME = "db.host";
    public static final String PORT_PROPERTY_NAME = "db.port";
    public static final String NAME_PROPERTY_NAME = "db.name";
    public static final String USER_PROPERTY_NAME = "db.user";
    public static final String PASS_PROPERTY_NAME = "db.pass";

    private Connection connection;

    private MariaDB() {

    }

    public static MariaDB connect() {
        File file = Paths.get(Main.CONFIG).toFile();
        String host = Files.getFilePropertyOrTerminate(file, HOST_PROPERTY_NAME);
        String port = Files.getFilePropertyOrTerminate(file, PORT_PROPERTY_NAME);
        String name = Files.getFilePropertyOrTerminate(file, NAME_PROPERTY_NAME);
        String user = Files.getFilePropertyOrTerminate(file, USER_PROPERTY_NAME);
        String pass = Files.getFilePropertyOrTerminate(file, PASS_PROPERTY_NAME);

        MariaDB mariaDB = new MariaDB();
        String url = "jdbc:mariadb://" + host + ":" + port + "/" + name;
        try {
            mariaDB.connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return mariaDB;
    }

    public PreparedStatement findByColumn(String table, String columnName, Object value) throws SQLException {
        String sql = "select * from " + table + " where " + columnName + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, value);
        preparedStatement.execute();
        return preparedStatement;
    }

    public PreparedStatement insert(String table, Map<String, Object> namedValues) throws SQLException {
        StringBuilder sets = new StringBuilder();
        Object[] values = namedValues.values().toArray();

        for(String key : namedValues.keySet()) {
            if(sets.length() > 0) {
                sets.append(',').append(key).append("=?");
            } else {
                sets.append(key).append("=?");
            }
        }

        String sql = "insert into " + table + " set " + sets + " returning id";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        for(int i = 1; i <= values.length; i++) {
            preparedStatement.setObject(i, values[i-1]);
        }
        preparedStatement.execute();
        return preparedStatement;
    }

    public Statement getAmount(String table, int limit, int offset) throws SQLException {
        String sql = "select * from " + table + " limit " + offset + "," + limit;
        Statement statement = connection.createStatement();
        statement.execute(sql);
        return statement;
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
