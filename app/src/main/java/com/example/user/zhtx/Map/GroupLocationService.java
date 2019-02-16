package com.example.user.zhtx.Map;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.example.user.zhtx.pojo.FriendsGPS;
import com.example.user.zhtx.pojo.GroupMemberMessage;
import com.example.user.zhtx.tools.Address;
import com.example.user.zhtx.tools.FriendsGPSList;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GroupLocationService extends Service {

    private Bitmap FriendPic;
    private Double Flatitude , Flongittude;
    private LatLng friends;
    public final IBinder binder = new GroupLocationBinder();
    private PointConverge pointConverge;
    private Timer timer;
    private List<GroupMemberMessage.DataBean> list;
    private String Fphonenum;
    private String address ="http://172.17.146.102:8080/txzh/pic/";
    private String FPicAddress;
    private String jpeg=".jpeg";
    private LatLng[] latLng1;
    private Handler mhandler;

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }

    public void initLocation(Context context) {
        if(!checkNetworkState()){
            Toast.makeText(getApplication(),"网络没有打开啊，请打开网络后再试",Toast.LENGTH_LONG).show();
        }

        timer = new Timer();
        startTimer(context);
    }

    public void initPointConverge(Context context, BaiduMap baiduMap, TextView name_txt, TextView phone_txt) {
        //点聚合
        pointConverge = PointConverge.newInstance(context, baiduMap);
        pointConverge.pointConverge(name_txt,phone_txt);
        pointConverge.setListener();
        pointConverge.clearMarkers();
    }

    public void startTimer(final Context context) {
        mhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 100) {

                    TimerTask task = new TimerTask() {

                        @Override
                        public void run() {
                            pointConverge.clearMarkers();
                            addMarkers(context);
                        }
                    };
                    timer.schedule(task, 0, 5000);
                }
            }
        };
    }
    private boolean checkNetworkState(){
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(getApplication().CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if((ni ==null||(!ni.isConnected()))){
            return false;
        }
        return  true;
    }

    public void addMarkers(Context context) {
        String name, phonenum;
        latLng1 = new LatLng[getDatas(context).size()];

        Bitmap[] bitmaps = new Bitmap[getDatas(context).size()];
        for (int i = 0; i< getDatas(context).size(); i++) {
            Flatitude = getDatas(context).get(i).getAtitude();
            Flongittude = getDatas(context).get(i).getLongatitude();
            friends = new LatLng(Flatitude, Flongittude);
            latLng1[i] = friends;

            //存放图片信息
            Fphonenum = getDatas(context).get(i).getPhonenum();
            FPicAddress = address+Fphonenum+jpeg;
            bitmaps[i] = downloadingImageAndShow();

            //存放额外信息
            name = getDatas(context).get(i).getName();
            phonenum = getDatas(context).get(i).getPhonenum();

            pointConverge.addMarkers(latLng1[i], bitmaps[i],name,phonenum);
        }
    }

//    public void addMarkers(List<GroupMemberMessage.DataBean> list) {
//        String name, phonenum;
//        latLng1 = new LatLng[list.size()];
//
//        Bitmap[] bitmaps = new Bitmap[list.size()];
//        for (int i = 0; i< list.size(); i++) {
//            Flatitude = list.get(i).getAtitude();
//            Flongittude = list.get(i).getLongatitude();
//            friends = new LatLng(Flatitude, Flongittude);
//            latLng1[i] = friends;
//
//            //存放图片信息
//            Fphonenum = list.get(i).getPhonenum();
//            FPicAddress = address+Fphonenum+jpeg;
//            bitmaps[i] = downloadingImageAndShow();
//
//            //存放额外信息
//            name = list.get(i).getName();
//            phonenum = list.get(i).getPhonenum();
//
//            pointConverge.addMarkers(latLng1[i], bitmaps[i],name,phonenum);
//        }
//    }

    public class GroupLocationBinder extends Binder {
        public GroupLocationService getService() {
            return GroupLocationService.this;
        }
    }

    public List<GroupMemberMessage.DataBean> getDatas(final Context context) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();


                        SharedPreferences sp = context.getSharedPreferences("user", context.MODE_PRIVATE);
                        FormBody body = new FormBody.Builder()
                                .add("groupname", "test2")
                                .add("uuid",sp.getString("uuid",""))
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
                                Gson gson = new Gson();
                                GroupMemberMessage m = gson.fromJson(result, GroupMemberMessage.class);
                                if ("true".equals(m.getSuccess())) {
                                    Message message = new Message();
                                    message.what = 100;
                                    message.obj = m.getData();
                                    list = m.getData();
                                    mhandler.sendMessage(message);
                                }
                            }
                        });
                    }
                }
        ).start();
        return list;
    }
    private Bitmap downloadingImageAndShow() {
        if (FPicAddress==null){
            return null;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder().build();
                Request request = new Request.Builder()
                        .url(FPicAddress)
                        .post(formBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        if(!response.isSuccessful()){
                            return;
                        }
                        byte [] b = null;
                        try{
                            b = response.body().bytes();
                        }catch(IOException e){
                            e.printStackTrace();
                            return;

                        }
                        FriendPic = BitmapFactory.decodeByteArray(b, 0, b.length);
                    }
                });
            }
        }).start();
        return FriendPic;
    }
}
