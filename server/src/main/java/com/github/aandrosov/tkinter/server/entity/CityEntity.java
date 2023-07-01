package com.github.aandrosov.tkinter.server.entity;

import com.github.aandrosov.tkinter.server.database.Column;
import com.github.aandrosov.tkinter.server.database.Table;

@Table("city")
public class CityEntity extends Entity {

    @Column("name")
    private String name;

    public CityEntity() {

    }

    public CityEntity(long id, String name) {
        setId(id);
        this.name = name;
    }

    public CityEntity(String name) {
        this(0, name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + getId() + "," +
                "\"name\":\"" + name + "\"}";
    }
}