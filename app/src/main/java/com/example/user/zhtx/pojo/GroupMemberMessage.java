package com.example.user.zhtx.pojo;

import java.util.List;

/**
 * Created by user on 2018/12/3.
 */

public class GroupMemberMessage {


    /**
     * success : true
     * message : 获取成功
     * data : [{"userid":43,"name":"gg","groupname":"test2","phonenum":"15014635129","gender":1,"pic":"15014635129","atitude":39.95697,"longatitude":116.331338},{"userid":30,"name":"测试0001","groupname":"test2","phonenum":"13692824048","gender":1,"pic":"13692824048","atitude":39.939723,"longatitude":116.425541},{"userid":31,"name":"测试003","groupname":"test2","phonenum":"12345678910","gender":1,"pic":"12345678910","atitude":39.939723,"longatitude":116.425541}]
     */

    private String success;
    private String message;
    private List<DataBean> data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * userid : 43
         * name : gg
         * groupname : test2
         * phonenum : 15014635129
         * gender : 1
         * pic : 15014635129
         * atitude : 39.95697
         * longatitude : 116.331338
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
}
