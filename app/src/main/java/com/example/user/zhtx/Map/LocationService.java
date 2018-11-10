package com.example.user.zhtx.Map;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.baidu.mapapi.map.BaiduMap;
import java.util.Timer;
import java.util.TimerTask;

public class LocationService extends Service {

//    public static LocationService locationService;
    public final IBinder binder = new LocationBinder();
    public MyLocationListener myLocationListener;
//    public Context context;
//    public BaiduMap baiduMap;

//    public LocationService() {
//        super();
//    }
//
//    public LocationService(Context context, BaiduMap baiduMap) {
//        this.context = context;
//        this.baiduMap = baiduMap;
//    }
//
//    public static LocationService newInstance(Context context, BaiduMap baiduMap) {
//        if (locationService == null) {
//            locationService = new LocationService(context, baiduMap);
//        }
//        return locationService;
//    }

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
        //555555555555
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimer();
        Log.i("查看：", "123456");
        return super.onStartCommand(intent, flags, startId);
    }

    public void initLocation(Context context, BaiduMap baiduMap) {
        myLocationListener = MyLocationListener.newInstance(context, baiduMap);
        myLocationListener.initLocation();

    }

    /**
     * 定时任务
     */
    public void startTimer(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                if (!myLocationListener.getmLocationClient().isStarted()) {
                    myLocationListener.getmLocationClient().start();
                    Log.i("查看：", "1234567");
                }

            }
        };
        timer.schedule(task, 0, 5000);
    }

    public class LocationBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

    @Override
    public void onDestroy() {
        myLocationListener.getmLocationClient().stop();
        super.onDestroy();
    }
}
