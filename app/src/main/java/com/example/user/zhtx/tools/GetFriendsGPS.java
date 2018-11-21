package com.example.user.zhtx.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.user.zhtx.pojo.FriendsGPS;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by user on 2018/11/23.
 */

public class GetFriendsGPS {
    private static ArrayList<FriendsGPS> list;
    private Context context;

    public GetFriendsGPS(Context context){
        this.context = context;
    }

    public ArrayList<FriendsGPS> get(){
        SharedPreferences sp = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        String data = sp.getString("friendsGPS","未找到");
        Gson gson = new Gson();
        Log.i("getFriendGPS",data+"------------------");
        ArrayList<FriendsGPS> list = gson.fromJson(data, new TypeToken<ArrayList<FriendsGPS>>() {}.getType());
        return  list;
    }

    public ArrayList<FriendsGPS> update(){
        GetGPS getGPS = new GetGPS(context);
        getGPS.getFriendsGps();
        ArrayList<FriendsGPS> list = get();
        return list;
    }

}




