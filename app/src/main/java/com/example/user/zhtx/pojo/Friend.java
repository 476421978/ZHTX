package com.example.user.zhtx.pojo;

/**
 * Created by user on 2018/11/25.
 */

public class Friend {

    /**
     * id : 30
     * name : 测试0001
     * password : null
     * phonenum : 13692824048
     * address : 12312wqewq
     * gender : 1
     * birthday : 1543420800000
     * login : 0
     * isban : 0
     * isview : 0
     * pic : 13692824048
     */

    private int id;
    private String name;
    private Object password;
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

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
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
}
