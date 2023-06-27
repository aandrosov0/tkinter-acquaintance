package com.github.aandrosov.tkinter.server.database;

@Table("user")
public class UserEntity extends Entity {

    @Column("name")
    private String name;

    @Column("info")
    private String info;

    @Column("phone")
    private String phone;

    @Column("password")
    private String password;

    public UserEntity() {

    }

    public UserEntity(int id, String name, String info, String phone, String password) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.phone = phone;
        this.password = password;
    }

    public UserEntity(String name, String info, String phone, String password) {
        this(0, name, info, phone, password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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
        this.password = password;
    }
}
