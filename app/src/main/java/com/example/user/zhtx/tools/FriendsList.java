package com.example.user.zhtx.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.user.zhtx.pojo.Friend;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by user on 2018/11/25.
 */

public class FriendsList {
    private Context context;
    public FriendsList(Context context){
        this.context = context;
    }

    public ArrayList<Friend> getAll(){
        ArrayList<Friend> friends = null;
        SharedPreferences sp = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        String data = sp.getString("friends","未找到");
        if ("未找到".equals(data)){
            ShowToast.show(context,"从sp获取信息出错");
        }else {
            Gson gson = new Gson();
            friends = gson.fromJson(data,new TypeToken<ArrayList<Friend>>() {}.getType());
            Log.i("getFriendGPS",data+"------------------");
        }
        return friends;
    }

    public ArrayList<Friend> getCare(){
        ArrayList<Friend> list = getAll();
        ArrayList<Friend> careList = new ArrayList<Friend>();
        for (int i=0;i<list.size();i++){
            if (list.get(i).getIsview() == 1){
                careList.add(list.get(i));
            }
        }
        return careList;
    }

    public ArrayList<Friend> update(){
        GetFriends getFriends = new GetFriends(context);
        getFriends.get();
        return getAll();

    }

}
