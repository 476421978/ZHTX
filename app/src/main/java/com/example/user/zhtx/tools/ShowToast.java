package com.example.user.zhtx.tools;

import android.content.Context;
import android.widget.Toast;


/**
 * Created by user on 2018/10/23.
 */

public class ShowToast {
    public static void show(Context context,String string){
        Toast.makeText(context,string,Toast.LENGTH_SHORT).show();
    }
}
