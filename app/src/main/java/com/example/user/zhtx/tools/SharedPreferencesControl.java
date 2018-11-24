package com.example.user.zhtx.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.example.user.zhtx.pojo.FriendsGPS;
import com.example.user.zhtx.pojo.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 2018/11/11.
 */

public class SharedPreferencesControl {
    private Context context;

    public SharedPreferencesControl(Context context){
        this.context = context;
    }
    private String title="http://172.17.146.102:8080/txzh/pic/";
    private User user;

    public void saveUser( User user){
        SharedPreferences sp = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("id",user.getId());
        editor.putString("name",user.getName());
        editor.putString("password",user.getPassword());
        editor.putString("phonenum",user.getPhonenum());
        editor.putString("address",user.getAddress());
        editor.putInt("gender",user.getGender());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(user.getBirthday()));
        calendar.add(Calendar.DAY_OF_MONTH,1);

        String birthday = format.format(calendar.getTime());

        editor.putString("birthday",birthday);
        editor.putString("login",String.valueOf(user.getLogin()));
        editor.putInt("isBan",user.getIsban());
        editor.putInt("isView",user.getIsview());
        editor.putString("picPath",title+user.getPic()+".jpeg");
        editor.commit();
        this.user = user;
    }

    public User getUser(){
        return user;
    }

    public static boolean isLogin(Context context){
        User user = new SharedPreferencesControl(context).getUser();
        if (TextUtils.isEmpty(user.getName())){
            return false;
        }else {
            return true;
        }
    }

    public void loginOut(){
        SharedPreferences sp = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("id");
        editor.remove("name");
        editor.remove("phonenum");
        editor.remove("password");
        editor.remove("address");
        editor.remove("gender");
        editor.remove("birthday");
        editor.remove("login");
        editor.remove("isBan");
        editor.remove("isView");
        editor.remove("picPath");
        editor.commit();
    }

    public void saveFriendsGPS(String data){
        SharedPreferences sp = context.getSharedPreferences("user",context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sp.edit();
        String reslt = data;
        editor.putString("friendsGPS",data);
        editor.commit();
    }

    public void saveFriends(String data){
        SharedPreferences sp = context.getSharedPreferences("user",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("friends",data);
        String aa = data;
        String data1 = sp.getString("friends","aaa");
        editor.commit();
    }

}
