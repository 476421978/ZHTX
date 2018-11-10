package com.example.user.zhtx.pojo;

/**
 * Created by user on 2018/11/14.
 */

public class MessageInfo {
    private String success;
    private String message;

    public MessageInfo() {
    }
    public MessageInfo(String success, String message) {
        this.success = success;
        this.message = message;
    }
    public String getSuccess() {
        return success;
    }
    public String getMessage() {
        return message;
    }
}
