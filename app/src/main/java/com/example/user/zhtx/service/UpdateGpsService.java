package com.example.user.zhtx.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.user.zhtx.tools.Address;
import com.example.user.zhtx.tools.GetGPS;
import com.example.user.zhtx.tools.SharedPreferencesControl;
import com.example.user.zhtx.tools.ShowToast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by user on 2018/11/26.
 */

public class UpdateGpsService extends Service {
    public static final String TAG= "UpdateGpsService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flag,int startId){
        new Thread(new Runnable() {
            @Override
            public void run() {
            /*    OkHttpClient client = new OkHttpClient();

                SharedPreferences sp = getBaseContext().getSharedPreferences("user",Context.MODE_PRIVATE);

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
                        Log.i("service",result);
                        if (result.length()>0){
                            SharedPreferencesControl control = new SharedPreferencesControl(getBaseContext());
                            control.saveFriendsGPS(result);
                        }else {
                            ShowToast.show(getBaseContext(),"请求好友GPS出错");
                            return;
                        }
                    }
                });*/

                GetGPS getGPS = new GetGPS(getBaseContext());
                getGPS.getFriendsGps();
            }
        }).start();



        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int time = 1000*5;
        long startTime = SystemClock.elapsedRealtime()+time;

        Intent intent1 = new Intent(this,UpdateGpsService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this,0,intent1,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,startTime,pendingIntent);
        return super.onStartCommand(intent,flag,startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
