package com.example.user.zhtx.Map;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.example.user.zhtx.R;
import com.example.user.zhtx.pojo.FriendsGPS;
import com.example.user.zhtx.tools.FriendsGPSList;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LocationService extends Service {

    private Double Flatitude , Flongittude;
    private LatLng friends;
    public final IBinder binder = new LocationBinder();
    private MyLocationListener myLocationListener;
    private PointConverge pointConverge;
    //    private Timer timer;
    private Drawable drawable1, drawable2, drawable3,
            drawable4, drawable5, drawable6;
    private Timer timer;
    private ArrayList<FriendsGPS> list;
    private URL urls;
    private String Fphonenum;
    private String address ="http://172.17.146.102:8080/txzh/pic/";
    private String FPicAddress=null;
    private String jpeg=".jpeg";
    private Bitmap FriendPic;

    @Override
    public void onCreate() {
        Log.i("MyService", "创建服务");
        super.onCreate();
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("MyService", "绑定服务");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("MyService", "解绑服务");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("查看：", "123456");
        return super.onStartCommand(intent, flags, startId);
    }

    public void initLocation(Context context, BaiduMap baiduMap) {
        myLocationListener = MyLocationListener.newInstance(context, baiduMap);
        myLocationListener.initLocation();


        FriendsGPSList getFriendsGPS = new FriendsGPSList(getApplication());
        list =  getFriendsGPS.getShow();
        if(!checkNetworkState()){
            Toast.makeText(getApplication(),"网络没有打开啊，请打开网络后再试",Toast.LENGTH_LONG).show();
        }
        addMarkers();
        startTimer();
    }

    public void initPointConverge(Context context, BaiduMap baiduMap, TextView name_txt, TextView phone_txt) {
        //点聚合
        pointConverge = PointConverge.newInstance(context, baiduMap,name_txt,phone_txt);
        pointConverge.pointConverge();
        pointConverge.setListener();

        pointConverge.clearMarkers();
    }

    /**
     * 定时任务
     */
    public void startTimer() {
        timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                myLocationListener.LocationClientStop();
                myLocationListener.initLocation();

                downloadingImageAndShow();
                getmarkerInfo();
                pointConverge.clearMarkers();
                addMarkers();
            }
        };
        timer.schedule(task, 0, 30000);

        Log.e("查看：", "123456.5");
    }
    private boolean checkNetworkState(){
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(getApplication().CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if((ni ==null||(!ni.isConnected()))){
            return false;
        }
        return  true;
    }
    private void getmarkerInfo() {

    }

    public void addMarkers() {
        String name,id,phonenum;
        LatLng[] latLng1 = new LatLng[list.size()];
        Bitmap[] bitmaps = new Bitmap[list.size()];
        if (list.size()==0){
            return;
        }
        for (int i=0; i<list.size(); i++) {

            //坐标信息
            Flatitude = list.get(i).getAtitude();
            Flongittude = list.get(i).getLongatitude();
            friends = new LatLng(Flatitude,Flongittude);
            latLng1[i] = friends;
            //存放图片信息
            Fphonenum = list.get(i).getPhonenum();
            FPicAddress = address+Fphonenum+jpeg;
            downloadingImageAndShow();
            bitmaps[i] = FriendPic;
            //存放额外信息
            name = list.get(i).getName();
            phonenum = list.get(i).getPhonenum();

            pointConverge.addMarkers(latLng1[i], bitmaps[i],name,phonenum);
            System.out.println(i+"111111111111111111");
        }
    }

    public class LocationBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

    @Override
    public void onDestroy() {
        Log.i("MyService", "销毁服务");

        if(timer != null) {
            timer.cancel();
            timer = null;
        }

        super.onDestroy();
    }
    private void downloadingImageAndShow() {
        if (FPicAddress==null){
            return;
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
                        System.out.println("输出了很多东西+++++++++++++++++");


                    }
                });
            }
        }).start();

    }
}
