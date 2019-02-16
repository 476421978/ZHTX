package com.example.user.zhtx.tools;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.example.user.zhtx.activity.RegisterActivity;

/**
 * Created by user on 2018/11/4.
 */

public class SingleErrDiaog {
    public static void show(Context context,String title,String content){
        new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(content)
            .setPositiveButton("确定",null)
            .show();
    }
}
