package com.example.user.zhtx.pojo;

/**
 * Created by user on 2018/11/23.
 */

public class FriendsGPS {


    /**
     * name : 测试0001
     * phonenum : 13692824048
     * address : 12312wqewq
     * gender : 1
     * birthday : 1543420800000
     * pic : 13692824048
     * atitude : 39.939723
     * longatitude : 116.425541
     * attention : 1
     * isView : 0
     */

    private String name;
    private String phonenum;
    private String address;
    private int gender;
    private long birthday;
    private String pic;
    private double atitude;
    private double longatitude;
    private int attention;
    private int isView;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public double getAtitude() {
        return atitude;
    }

    public void setAtitude(double atitude) {
        this.atitude = atitude;
    }

    public double getLongatitude() {
        return longatitude;
    }

    public void setLongatitude(double longatitude) {
        this.longatitude = longatitude;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public int getIsView() {
        return isView;
    }

    public void setIsView(int isView) {
        this.isView = isView;
    }
}
