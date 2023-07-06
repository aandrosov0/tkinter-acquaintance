package com.github.aandrosov.tkinter.server.entity;

import com.github.aandrosov.tkinter.server.database.MariaDB;
import com.github.aandrosov.tkinter.toolchain.Strings;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityService {

    public long insert(Entity entity) throws SQLException, IllegalAccessException {
        Map<String, Object> columns = entity.getColumns();
        columns.remove("id");

        String names = Strings.arrayToStringSequence(columns.keySet().toArray(), ",", "=?");
        String sql = "insert into " + Entity.getTable(entity.getClass()) + " set " + names + " returning id";

        try(Connection connection = MariaDB.connect(); PreparedStatement statement = connection.prepareStatement(sql)) {
            MariaDB.fillPreparedStatement(statement, columns.values().toArray());
            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.first()) {
                return -1;
            }

            long id = resultSet.getLong(1);
            entity.setId(id);
            return id;
        }
    }

    public void update(Entity entity) throws SQLException, IllegalAccessException {
        Map<String, Object> columns = entity.getColumns();
        columns.remove("id");

        String names = Strings.arrayToStringSequence(columns.keySet().toArray(), ",", "=?");
        String sql = "update " + Entity.getTable(entity.getClass()) + " set " + names + " where id=" + entity.getId();

        try(Connection connection = MariaDB.connect(); PreparedStatement statement = connection.prepareStatement(sql)) {
            Object[] values = columns.values().toArray();
            MariaDB.fillPreparedStatement(statement, values);
            statement.execute();
        }
    }

    public boolean isExists(Class<?> entityClass, Map<String, Object> criteria) throws SQLException {
        String table = Entity.getTable(entityClass);
        try(PreparedStatement statement = findEntity(table, criteria)) {
            return statement.getResultSet().first();
        }
    }

    public List<Entity> find(Class<?> entityClass, Map<String, Object> criteria) throws ReflectiveOperationException, SQLException {
        List<Entity> entities = new ArrayList<>();

        String table = Entity.getTable(entityClass);
        try(PreparedStatement statement = findEntity(table, criteria)) {
            ResultSet resultSet = statement.getResultSet();

            while(resultSet.next()) {
                Entity entity = (Entity) entityClass.newInstance();
                entity.init(resultSet);
                entities.add(entity);
            }

            return entities;
        }
    }

    public Entity findOne(Class<?> entityClass, Map<String, Object> criteria) throws ReflectiveOperationException, SQLException {
        String table = Entity.getTable(entityClass);
        try(PreparedStatement statement = findEntity(table, criteria)) {
            ResultSet resultSet = statement.getResultSet();
            if(!resultSet.first()) {
                return null;
            }

            Entity entity = (Entity) entityClass.newInstance();
            entity.init(resultSet);
            return entity;
        }
    }

    public Entity findById(Class<?> entityClass, Object id) throws SQLException, ReflectiveOperationException {
        String sql = "select * from " + Entity.getTable(entityClass) + " where id=" + id;

        try(Connection connection = MariaDB.connect(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            if(!resultSet.first()) {
                return null;
            }

            Entity entity = (Entity) entityClass.newInstance();
            entity.init(resultSet);

            return entity;
        }
    }

    public List<Entity> fetch(Class<?> entityClass, int offset, int limit) throws SQLException, ReflectiveOperationException {
        List<Entity> entities = new ArrayList<>();
        String sql = "select * from " + Entity.getTable(entityClass) + " limit " + offset + "," + limit;

        try(Connection connection = MariaDB.connect(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                Entity entity = (Entity) entityClass.newInstance();
                entity.init(resultSet);
                entities.add(entity);
            }

            return entities;
        }
    }

    private PreparedStatement findEntity(String table, Map<String, Object> criteria) throws SQLException {
        String sql = "select * from " + table;

        if(criteria != null && !criteria.isEmpty()) {
            String sqlCriteria = Strings.arrayToStringSequence(criteria.keySet().toArray(), " or ", "=?");
            sql += " where " + sqlCriteria;
        }

        try(Connection connection = MariaDB.connect()) {
            PreparedStatement statement = connection.prepareStatement(sql);

            if(criteria != null && !criteria.isEmpty()) {
                MariaDB.fillPreparedStatement(statement, criteria.values().toArray());
            }

            statement.execute();
            return statement;
        }
    }
}
