package com.github.aandrosov.tkinter.server.entity;

import com.github.aandrosov.tkinter.server.database.Column;
import com.github.aandrosov.tkinter.server.database.Table;
import com.github.aandrosov.tkinter.toolchain.Hash;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

@Table("user")
public class UserEntity extends Entity {

    @Column("name")
    private String name;

    @Column("about")
    private String about;

    @Column("phone")
    private String phone;

    @Column("password")
    private String password;

    @Column("city_id")
    private CityEntity cityEntity;

    public UserEntity() {

    }

    public UserEntity(int id, String name, String about, String phone, String password, CityEntity cityEntity) {
        setId(id);
        this.name = name;
        this.about = about;
        this.phone = phone;
        this.password = password;
        this.cityEntity = cityEntity;
    }

    public UserEntity(String name, String about, String phone, String password, CityEntity cityEntity) {
        this(0, name, about, phone, password, cityEntity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String info) {
        this.about = info;
    }

    public String getPhone() {
        return phone;
    }

    public CityEntity getCity() {
        return cityEntity;
    }

    public void setCity(CityEntity cityEntity) {
        this.cityEntity = cityEntity;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + getId() + "," +
                "\"name\":\"" + name + "\"," +
                "\"about\":\"" + about + "\"," +
                "\"phone\":\"" + phone + "\"," +
                "\"city_id\":" + cityEntity.getId() + "}";
    }

    @Override
    protected void setColumnFieldValue(Field field, Object value) throws Exception {
        if(field.getType().equals(CityEntity.class)) {
            cityEntity = EntityFactory.getBy("id", value, CityEntity.class);
        } else {
            super.setColumnFieldValue(field, value);
        }
    }

    @Override
    protected Object getColumnFieldValue(Field field) throws Exception {
        if(field.getType().equals(CityEntity.class)) {
            return cityEntity.getId();
        } else if(field.getAnnotation(Column.class).value().equals("password")) {
            return Hash.byteArrayToHex(Hash.generateMD5(((String)field.get(this)).getBytes(StandardCharsets.UTF_8)));
        }

        return super.getColumnFieldValue(field);
    }
}
