package com.github.aandrosov.tkinter.library.enitity;

import com.github.aandrosov.tkinter.toolchain.Hash;

import java.nio.charset.StandardCharsets;

public class UserEntity extends Entity {

    private String name;

    private String about;

    private String phone;

    private String password;

    private long cityId;

    public UserEntity() {
        super(0);
    }

    public UserEntity(int id, String name, String about, String phone, String password, long cityId) {
        super(id);
        this.name = name;
        this.about = about;
        this.phone = phone;
        this.cityId = cityId;
        setPassword(password);
    }

    public UserEntity(String name, String about, String phone, String password, long cityId) {
        this(0, name, about, phone, password, cityId);
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

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        byte[] hashedPassword = Hash.generateMD5(password.getBytes(StandardCharsets.UTF_8));
        this.password = Hash.byteArrayToHex(hashedPassword);
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }
}
