package com.UI.domain;

/**
 * Created by huang on 2018/11/19.
 */

public class User {

    /**
     * id : 30
     * name : lck
     * password : 123
     * phonenum : 13692824048
     * address : 123
     * gender : 1
     * birthday : 1541865600000
     * login : 0
     * isban : 0
     * isview : 0
     * pic : 13692824048
     */

    private int id;
    private String name;
    private String password;
    private String phonenum;
    private String address;
    private int gender;
    private long birthday;
    private int login;
    private int isban;
    private int isview;
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public int getIsban() {
        return isban;
    }

    public void setIsban(int isban) {
        this.isban = isban;
    }

    public int getIsview() {
        return isview;
    }

    public void setIsview(int isview) {
        this.isview = isview;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", phonenum='" + phonenum + '\'' +
                ", address='" + address + '\'' +
                ", gender=" + gender +
                ", birthday=" + birthday +
                ", login=" + login +
                ", isban=" + isban +
                ", isview=" + isview +
                ", pic='" + pic + '\'' +
                '}';
    }
}
