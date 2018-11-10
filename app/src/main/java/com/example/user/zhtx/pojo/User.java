package com.example.user.zhtx.pojo;

/**
 * Created by user on 2018/11/4.
 */

import java.sql.Timestamp;

public class User {
    private Integer id;
    private String name;
    private String password;
    private String phonenum;
    private String address;
    private byte gender;
    private Timestamp birthday;
    private byte login;
    private byte isBan;
    private byte isView;            // 可见范围
    private String pic;



    public User() {
        super();
        // TODO Auto-generated constructor stub
    }

    public User(Integer id, String name, String password, String phonenum, String address, byte gender,
                Timestamp birthday, byte login, byte isBan, byte isView, String pic) {
        super();
        this.id = id;
        this.name = name;
        this.password = password;
        this.phonenum = phonenum;
        this.address = address;
        this.gender = gender;
        this.birthday = birthday;
        this.login = login;
        this.isBan = isBan;
        this.isView = isView;
        this.pic = pic;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        name = name;
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
    public Timestamp getBirthday() {
        return birthday;
    }
    public void setBirthday(Timestamp birthday) {
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
    public String getPic() {
        return pic;
    }
    public void setPic(String pic) {
        this.pic = pic;
    }

    public byte getIsView() {
        return isView;
    }
    public void setIsView(byte isView) {
        this.isView = isView;
    }
}
