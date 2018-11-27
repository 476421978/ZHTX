package com.example.user.zhtx.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.user.zhtx.pojo.Friend;
import com.example.user.zhtx.pojo.GroupMember;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by user on 2018/11/27.
 */

public class GroupMemberList {
    private Context context;
    public GroupMemberList(Context context){
        this.context = context;
    }



    public ArrayList<GroupMember> get(){
        ArrayList<GroupMember> list = null;
        GetGroupMemberGps g = new GetGroupMemberGps(context);
        g.get();
        String data = g.getData();
        Gson gson = new Gson();
        list = gson.fromJson(data,new TypeToken<ArrayList<GroupMember>>() {}.getType());
        return list;
    }
}
