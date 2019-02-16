package com.example.user.zhtx.pojo;

/**
 * Created by user on 2018/11/27.
 */

public class GroupMember {

    /**
     * userid : 43
     * name : gg
     * groupname : test2
     * phonenum : 15014635129
     * gender : 1
     * pic : 15014635129
     * atitude : 0
     * longatitude : 0
     */

    private int userid;
    private String name;
    private String groupname;
    private String phonenum;
    private int gender;
    private String pic;
    private double atitude;
    private double longatitude;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
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
}
