package com.github.aandrosov.tkinter.server.database;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class EntityFactory {

    public static void setEntityColumnFieldsByResultSet(Entity entity, ResultSet resultSet) {
//        Arrays.stream(entity.getClass().getDeclaredFields()).forEach(field -> System.out.println(field.getName()));
    }

    public static <T extends Entity> T getFromDatabaseByResultSet(ResultSet result, Class<T> tClass) throws SQLException {
        T entity = createEntity(tClass);

        if(!result.first()) {
            return null;
        }

        setEntityColumnFieldsByResultSet(entity, result);
        return entity;
    }

    public static <T extends Entity> T getFromDatabaseById(int id, Class<T> tClass) {
        return null;
    }

    private static <T extends Entity> T createEntity(Class<T> tClass) {
        try {
            return tClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
