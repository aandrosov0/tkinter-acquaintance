package com.github.aandrosov.tkinter.server.database;

public abstract class Entity {
    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
