package com.example.user.zhtx.pojo;

/**
 * Created by user on 2018/11/4.
 */

import java.util.Date;
import java.sql.Timestamp;

public class User {
    private int id;
    private String name;
    private String password;
    private String phonenum;
    private String address;
    private byte gender;
    private Date birthday;
    private byte login;
    private byte isBan;
    private byte isView;            // 可见范围 0是所有可见 1是部分看见
    private String pic;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public byte getLogin() {
        return login;
    }

    public void setLogin(byte login) {
        this.login = login;
    }

    public byte getIsBan() {
        return isBan;
    }

    public void setIsBan(byte isBan) {
        this.isBan = isBan;
    }

    public byte getIsView() {
        return isView;
    }

    public void setIsView(byte isView) {
        this.isView = isView;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
