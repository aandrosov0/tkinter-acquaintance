package com.github.aandrosov.tkinter.server.entity;

import com.github.aandrosov.tkinter.server.database.Column;
import com.github.aandrosov.tkinter.server.database.Table;
import com.github.aandrosov.tkinter.toolchain.Classes;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class Entity {

    @Column("id")
    private long id;

    public static String getTable(Class<?> entityClass) {
        if(!entityClass.isAnnotationPresent(Table.class)) {
            return null;
        }

        return entityClass.getAnnotation(Table.class).value();
    }

    public void init(ResultSet resultSet) throws SQLException, IllegalAccessException {
        List<Field> columns = Classes.getAnnotatedFields(getClass(), Column.class);

        for(Field field : columns) {
            Column column = field.getDeclaredAnnotation(Column.class);
            field.setAccessible(true);
            Object fieldValue = resultSet.getObject(column.value());
            field.set(this, fieldValue);
        }
    }

    public Map<String, Object> getColumns() throws IllegalAccessException {
        List<Field> fields = Classes.getAnnotatedFields(getClass(), Column.class);
        Map<String, Object> columns = new HashMap<>();

        for(Field field : fields) {
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            columns.put(column.value(), field.get(this));
        }

        return columns;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
