package com.example.user.zhtx.Map;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.example.user.zhtx.R;
import com.example.user.zhtx.tools.GetLocation;

import java.util.Timer;
import java.util.TimerTask;

public class LocationService extends Service {

    public final IBinder binder = new LocationBinder();
    private MyLocationListener myLocationListener;
    private PointConverge pointConverge;
//    private Timer timer;
    private Drawable drawable1, drawable2, drawable3,
            drawable4, drawable5, drawable6;

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
        startTimer();
        Log.e("查看：", "123456");
        return super.onStartCommand(intent, flags, startId);
    }

    public void initLocation(Context context, BaiduMap baiduMap) {
        myLocationListener = MyLocationListener.newInstance(context, baiduMap);
        myLocationListener.initLocation();

        //点聚合
        pointConverge = PointConverge.newInstance(context, baiduMap);
        pointConverge.pointConverge();
        pointConverge.setListener();

        drawable1 = context.getResources().getDrawable(R.drawable.touxiang);
        drawable2 = context.getResources().getDrawable(R.drawable.wenhao);
        drawable3 = context.getResources().getDrawable(R.drawable.see_area);
        drawable4 = context.getResources().getDrawable(R.drawable.round);
        drawable5 = context.getResources().getDrawable(R.drawable.route);
        drawable6 = context.getResources().getDrawable(R.drawable.pic_head);

        pointConverge.clearMarkers();
        addMarkers();
    }

    /**
     * 定时任务
     */
    public void startTimer() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                myLocationListener.LocationClientStop();
                myLocationListener.initLocation();

                pointConverge.clearMarkers();
                addMarkers();
            }
        };
        timer.schedule(task, 0, 5000);
    }

    public void addMarkers() {
        LatLng llB = new LatLng(39.942821, 116.369199);
        LatLng llC = new LatLng(39.939723, 116.425541);
        LatLng llD = new LatLng(39.906965, 116.401394);
        LatLng llE = new LatLng(39.956965, 116.331394);
        LatLng llF = new LatLng(39.886965, 116.441394);
        LatLng llG = new LatLng(39.996965, 116.411394);

        LatLng[] latLng1 = new LatLng[6];
        latLng1[0] = llB;
        latLng1[1] = llC;
        latLng1[2] = llD;
        latLng1[3] = llE;
        latLng1[4] = llF;
        latLng1[5] = llG;

        Drawable[] drawables = new Drawable[6];
        drawables[0] = drawable1;
        drawables[1] = drawable2;
        drawables[2] = drawable3;
        drawables[3] = drawable4;
        drawables[4] = drawable5;
        drawables[5] = drawable6;

        for (int i=0; i<latLng1.length; i++) {
            pointConverge.addMarkers(latLng1[i], drawables[i]);
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

//        if(timer != null) {
//            timer.cancel();
//            timer = null;
//        }

        super.onDestroy();
    }
}
