package com.example.user.zhtx.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by user on 2018/11/22.
 *
 * 这个类用来向后台请求好友GPS信息
 */

public class GetGPS {
    private Context context;

    public GetGPS(Context context){
        this.context=context;
    }

    public void getFriendsGps(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                SharedPreferences sp = context.getSharedPreferences("user",Context.MODE_PRIVATE);

                FormBody body = new FormBody.Builder()
                        .add("userid",sp.getInt("userid",28)+"")
                        .add("uuid",sp.getString("uuid",""))
                        .build();

                final Request request = new Request.Builder()
                        .url(Address.GetFirendsGPS)
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        return;
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        Log.i("service",result);

                        if (result.length()>0){
                            SharedPreferencesControl control = new SharedPreferencesControl(context);
                            control.saveFriendsGPS(result);
                        }else {
                            ShowToast.show(context,"请求好友GPS出错");
                            return;
                        }
                    }
                });
            }
        }).start();
    }
}



