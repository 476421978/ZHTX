package com.example.user.zhtx.tools;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by user on 2018/11/27.
 */

public class GetGroupMemberGps {
    private Context context;
    private static final int SUCCESS=1;
    private String data;
    public GetGroupMemberGps(Context context){
        this.context=context;
    }

    public void get(){
        new Thread(
            new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient();

                    FormBody body = new FormBody.Builder()
                            .add("groupname","test2")
                            .build();

                    final Request request = new Request.Builder()
                            .url(Address.GetGroupMemberGps)
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
                                Message m = new Message();
                                m.what=SUCCESS;
                                m.obj=result;
                                handler.sendMessage(m);
                            }

                        }
                    });
                }
            }
        ).start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if (msg.what==SUCCESS){
                data = msg.obj+"";
            }
        }
    };

    public String getData() {
        return data;
    }
}
