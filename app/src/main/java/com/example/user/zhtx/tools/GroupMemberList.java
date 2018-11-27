package com.example.user.zhtx.tools;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.user.zhtx.pojo.GroupMember;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by user on 2018/11/27.
 */

public class GroupMemberList {
    private Context context;
    private String data;
    private GetGroupMemberGps g;
    private Handler handler;
    public GroupMemberList(Context context){
        this.context = context;
    }

    public ArrayList<GroupMember> get(){
        ArrayList<GroupMember> list = null;
        Gson gson = new Gson();
        handler = new Handler(){
            @Override
            public void handleMessage(Message message){
                if (message.arg1 == 1){
                    Bundle bundle = message.getData();
                    data = bundle.getString("data");
                    ShowToast.show(context,data);
                }
            }
        };

        g = new GetGroupMemberGps(context, handler);
        g.get();
        list = gson.fromJson(data,new TypeToken<ArrayList<GroupMember>>() {}.getType());
        return list;
    }


}
