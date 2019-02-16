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
 * Created by user on 2018/11/27.
 */

public class UpdateSelfGps {
    private Context context;

    public UpdateSelfGps(Context context){
        this.context=context;
    }

    public void sendSelfGPS(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
                FormBody body = new FormBody.Builder()
                        .add("userid",sp.getInt("id",0)+"")
                        .add("atitude", new GetLocation().getLatitude()+"")
                        .add("longatitude",new GetLocation().getLongitude()+"")
                        .build();

                Log.i("location",new GetLocation().getLatitude()+"    "+new GetLocation().getLongitude()+"   "+sp.getInt("id",0) );

                Request request = new Request.Builder()
                        .url(Address.SendSelfGPS)
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
                    }
                });

            }
        }).start();
    }
}
