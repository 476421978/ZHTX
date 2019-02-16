package com.example.user.zhtx.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by user on 2018/11/25.
 */

public class GetFriends {
    //private ArrayList<> list;
    private Context context;
    public GetFriends(Context context){
        this.context = context;
    }

    public void get(){
        new Thread(
            new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient();

                    final SharedPreferences sp = context.getSharedPreferences("user",context.MODE_PRIVATE);

                    FormBody body = new FormBody.Builder()
                            .add("userid",sp.getInt("id",0)+"")
                            .add("uuid",sp.getString("uuid",""))
                            .build();

                    Request request = new Request.Builder()
                            .url(Address.GetFriends)
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
                                SharedPreferencesControl control = new SharedPreferencesControl(context);
                                control.saveFriends(result);
                            }else{
                                ShowToast.show(context,"请求好友信息出错");
                            }

                        }
                    });


                }
            }
        ).start();
    }

}
