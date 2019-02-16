package com.example.user.zhtx.internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.user.zhtx.tools.ShowToast;

/**
 * Created by user on 2018/10/21.
 *
 * 这个类是检查网络是否连接，如果未联网返回false，已联网则返回true
 */

public class CheckNetwork {
    public static  boolean NetworkState(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if ((ni == null) || (!ni.isConnected())){
            ShowToast.show(context,"无法连接到网络");
            return false;
        }else{
            return true;
        }
    }
}
