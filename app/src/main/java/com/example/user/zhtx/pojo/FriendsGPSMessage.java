package com.example.user.zhtx.pojo;

import java.util.List;

/**
 * Created by user on 2018/12/3.
 */

public class FriendsGPSMessage {

    /**
     * success : true
     * message : 获取成功
     * data : [{"name":"测试0001","phonenum":"13692824048","address":"12312wqewq","gender":1,"birthday":1543420800000,"pic":"13692824048","atitude":39.939723,"longatitude":116.425541,"attention":1,"isView":0},{"name":"测试003","phonenum":"12345678910","address":"123","gender":1,"birthday":1541865600000,"pic":"12345678910","atitude":39.939723,"longatitude":116.425541,"attention":1,"isView":0},{"name":"测试004","phonenum":"00000000001","address":"123","gender":1,"birthday":1542693397000,"pic":"00000000001","atitude":39.906965,"longatitude":116.401365,"attention":0,"isView":0},{"name":"测试005","phonenum":"00000000002","address":"123","gender":1,"birthday":1542693458000,"pic":"00000000002","atitude":39.956967,"longatitude":116.331395,"attention":0,"isView":0},{"name":"测试006","phonenum":"00000000003","address":"123","gender":1,"birthday":1542693496000,"pic":"00000000003","atitude":39.956968,"longatitude":116.331352,"attention":1,"isView":0},{"name":"测试007","phonenum":"00000000004","address":"123","gender":1,"birthday":1542693552000,"pic":"00000000004","atitude":39.95697,"longatitude":116.331351,"attention":1,"isView":0},{"name":"测试0001","phonenum":"13692824048","address":"12312wqewq","gender":1,"birthday":1543420800000,"pic":"13692824048","atitude":39.939723,"longatitude":116.425541,"attention":1,"isView":0},{"name":"gg","phonenum":"15014635129","address":"DDF","gender":1,"birthday":1543420800000,"pic":"15014635129","atitude":0,"longatitude":0,"attention":1,"isView":0}]
     */

    private String success;
    private String message;
    private List<FriendsGPS> data;

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

    public List<FriendsGPS> getData() {
        return data;
    }

    public void setData(List<FriendsGPS> data) {
        this.data = data;
    }
    
}
