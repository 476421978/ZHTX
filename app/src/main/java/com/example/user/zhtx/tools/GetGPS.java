package com.example.user.zhtx.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.user.zhtx.pojo.FriendsGPS;
import java.io.IOException;
import java.util.ArrayList;

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
    private String result;
    private ArrayList<FriendsGPS> list;
    public static final int GPS_SUCCESS = 1;
    public static final int GPS_FAIL = 1;

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
                        .add("userid",28+"")
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
                        if (result.length()>0){
                            Message message = new Message();
                            message.what = GPS_SUCCESS;
                            message.obj = result;
                            handler.sendMessage(message);
                        }else {
                            ShowToast.show(context,"请求好友GPS出错");
                            return;
                        }
                    }
                });



            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            if (msg.what==GPS_SUCCESS){
                result = msg.obj+"";
                SharedPreferencesControl control = new SharedPreferencesControl(context);
                control.saveFriendsGPS(result);
            }
        }
    };


}



