package com.example.user.zhtx.pojo;

/**
 好友申请   ID 头像
 */

public class add{

    /**
     * id : 7
     * inviterid : 43
     * inviteeid : 44
     * inviterName : gg
     * inviterPhone : 15014635129
     * pic : 15014635129
     */

    private int id;
    private int inviterid;
    private int inviteeid;
    private String inviterName;
    private String inviterPhone;
    private String pic;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInviterid() {
        return inviterid;
    }

    public void setInviterid(int inviterid) {
        this.inviterid = inviterid;
    }

    public int getInviteeid() {
        return inviteeid;
    }

    public void setInviteeid(int inviteeid) {
        this.inviteeid = inviteeid;
    }

    public String getInviterName() {
        return inviterName;
    }

    public void setInviterName(String inviterName) {
        this.inviterName = inviterName;
    }

    public String getInviterPhone() {
        return inviterPhone;
    }

    public void setInviterPhone(String inviterPhone) {
        this.inviterPhone = inviterPhone;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return "add{" +
                "id=" + id +
                ", inviterid=" + inviterid +
                ", inviteeid=" + inviteeid +
                ", inviterName='" + inviterName + '\'' +
                ", inviterPhone='" + inviterPhone + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }
}
