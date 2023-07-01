package com.github.aandrosov.tkinter.server.entity;

import com.github.aandrosov.tkinter.server.database.Column;
import com.github.aandrosov.tkinter.server.database.Table;
import com.github.aandrosov.tkinter.toolchain.Classes;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Entity {

    @Column(value = "id", isPrimaryKey = true)
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    protected void setColumnFieldValue(Field field, Object value) throws Exception {
        field.set(this, value);
    }

    protected Object getColumnFieldValue(Field field) throws Exception {
        return field.get(this);
    }

    public void setResultSetData(ResultSet resultSet) throws Exception {
        List<Field> fields = Classes.getAnnotatedFields(getClass(), Column.class);
        for(Field field : fields) {
            field.setAccessible(true);
            String columnName = field.getAnnotation(Column.class).value();
            Object columnValue = resultSet.getObject(columnName);
            setColumnFieldValue(field, columnValue);
        }
    }

    public String getTableName() {
        if(!getClass().isAnnotationPresent(Table.class)) {
            return null;
        }

        return getClass().getAnnotation(Table.class).value();
    }

    public Map<String, Object> getColumnsAndValuesWithoutPrimaryKeys() throws Exception {
        List<Field> fields = Classes.getAnnotatedFields(getClass(), Column.class);
        Map<String, Object> values = new HashMap<>();

        for(Field field : fields) {
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            if(!column.isPrimaryKey()) {
                Object value = getColumnFieldValue(field);
                values.put(column.value(), value);
            }
        }

        return values;
    }
}
