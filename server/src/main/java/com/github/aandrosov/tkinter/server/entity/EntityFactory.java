package com.github.aandrosov.tkinter.server.entity;

import com.github.aandrosov.tkinter.server.database.MariaDB;
import com.github.aandrosov.tkinter.server.database.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityFactory {

    public static <T extends Entity> T getBy(String column, Object value, Class<T> classType) throws Exception {
        T entity = classType.newInstance();

        try(MariaDB db = MariaDB.connect(); PreparedStatement statement = db.findByColumn(entity.getTableName(), column, value)) {
            ResultSet resultSet = statement.getResultSet();

            if(!resultSet.first()) {
                return null;
            }

            entity.setResultSetData(resultSet);
        }
        return entity;
    }

    public static <T extends Entity> List<T> getAmount(int limit, int offset, Class<T> classType) throws Exception {
        List<T> entities = new ArrayList<>();

        String table = classType.getAnnotation(Table.class).value();
        try(MariaDB db = MariaDB.connect(); Statement statement = db.getAmount(table, limit, offset)) {
            ResultSet resultSet = statement.getResultSet();

            while(resultSet.next()) {
                T entity = classType.newInstance();
                entity.setResultSetData(resultSet);
                entities.add(entity);
            }
        }

        return entities;
    }

    public static long insert(Entity entity) throws Exception {
        Map<String, Object> values = entity.getColumnsAndValuesWithoutPrimaryKeys();

        try(MariaDB db = MariaDB.connect(); PreparedStatement statement = db.insert(entity.getTableName(), values)) {
            ResultSet resultSet = statement.getResultSet();

            if(!resultSet.first()) {
                return -1;
            }

            return resultSet.getLong("id");
        }
    }

    public static boolean isExists(String column, Object value, Class<? extends Entity> classType) throws Exception {
        String table = classType.getAnnotation(Table.class).value();
        try(MariaDB db = MariaDB.connect(); PreparedStatement statement = db.findByColumn(table, column, value)) {
            return statement.getResultSet().first();
        }
    }
}
