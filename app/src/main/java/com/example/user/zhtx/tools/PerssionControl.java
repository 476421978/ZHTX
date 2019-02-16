package com.example.user.zhtx.tools;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by user on 2018/10/21.
 */

public class PerssionControl {
    private  static PerssionControl instance = new PerssionControl();
    private PerssionControl(){}
    public static PerssionControl getInstance(){
        return instance;
    }
    public void getPermission(Context context, String[] permissions){
        if (permissions.length == 0){
            return;
        }else {
            for (int i=0;i<permissions.length;i++) {
                int hasPerssion = ContextCompat.checkSelfPermission(context, permissions[i]);
                if (hasPerssion == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("已有权限： " + permissions[i] + "-------------------------------");
                }else{
                    System.out.println("成功申请到新权限：  "+permissions[i]+"--------------");
                }
            }
            ActivityCompat.requestPermissions((Activity)context,permissions,300);
        }
    }
}
