package com.tu.common.bean;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.features.jpa.annotation.Column;
import org.apache.ibatis.features.jpa.annotation.Entity;
import org.apache.ibatis.features.jpa.annotation.Id;

@Entity(name = "sys_user")
public class User {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "usercode")
    private String usercode;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "salt")
    private String salt;
    @Column(name = "locked")
    private char locked1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public char getLocked1() {
        return locked1;
    }

    public void setLocked1(char locked1) {
        this.locked1 = locked1;
    }
}
