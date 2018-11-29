package com.example.user.zhtx.pojo;

/**
 * Created by user on 2018/12/3.
 */

public class UserMessage {

    /**
     * success : true
     * message : 获取成功
     * data : {"id":28,"name":"测试0001222222","password":"123456","phonenum":"15815667851","address":"567810","gender":0,"birthday":1543420800000,"login":0,"isban":0,"isview":0,"pic":"15815667851","uuid":null}
     */

    private String success;
    private String message;
    private User data;

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

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

}
