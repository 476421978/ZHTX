package com.example.user.zhtx.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.user.zhtx.pojo.User;

/**
 * Created by user on 2018/11/11.
 */

public class SharedPreferencesControl {
    private static SharedPreferencesControl instance = new SharedPreferencesControl();
    public static SharedPreferencesControl getInstance(){return instance;}

    public static void saveUserInfo(Context context,User user){
        SharedPreferences sp = context.getSharedPreferences("user",Context.MODE_MULTI_PROCESS);
    }
}
