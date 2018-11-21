package com.example.user.zhtx.Map;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.PlanNode;
import com.example.user.zhtx.R;
import com.example.user.zhtx.tools.GetLocation;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;

public class MyLocationListener implements BDLocationListener {

    private static MyLocationListener myLocationListener;
    private Context context;
    private BaiduMap baiduMap;
    private boolean isFirstLoc = true;
    private LatLng myLL;

  //  private MyLocationListener myLocationListener;
    private LocationClient mLocationClient = null;
 //   private BDLocationListener myListener = null;

    public MyLocationListener(Context context, BaiduMap baiduMap) {
        this.context = context;
        this.baiduMap = baiduMap;
    }

    public static MyLocationListener newInstance(Context context, BaiduMap baiduMap) {
        if (myLocationListener == null) {
            myLocationListener = new MyLocationListener(context, baiduMap);
        }
        return myLocationListener;
    }

    public void initLocation() {
        //定位
    //    myLocationListener = new MyLocationListener(context, baiduMap);;
        mLocationClient = new LocationClient(context.getApplicationContext());     //声明LocationClient类
    //    myListener = this;
        mLocationClient.registerLocationListener(this);    //注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        option.setNeedDeviceDirect(true);
        option.setIsNeedLocationPoiList(true);
        option.setPriority(LocationClientOption.NetWorkFirst);// 网络定位优先

        mLocationClient.setLocOption(option);

        mLocationClient.start();
        mLocationClient.requestLocation();

//        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//        int alarmType = AlarmManager.RTC;
//
//        long time = 1000;
//
//        String ALARM_ACTION = "ALARM_ACTION";
////        Intent intent = new Intent(ALARM_ACTION);
//        Intent intent = new Intent(context, LocationService.class);
//        intent.setAction(ALARM_ACTION);
//
//        PendingIntent allarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//        Log.i("查看：", "123");
//        alarmManager.set(alarmType, time, allarmIntent);


//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//
//            @Override
//            public void run() {
//                if (!mLocationClient.isStarted()) {
//                    mLocationClient.start();
//                }
//                mLocationClient.requestLocation();
//                Log.i("查看：", "123");
//            }
//        };
//        timer.schedule(task, 0, 2000);
    }

    @Override
    public void onReceiveLocation(final BDLocation bdLocation) {
    //    float lux = SensorManage.newInstance().getLux1();
    //    Log.i("方向：", String.valueOf(lux));

        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);

        baiduMap.setOnMyLocationClickListener(new BaiduMap.OnMyLocationClickListener() {
            @Override
            public boolean onMyLocationClick() {
                if(bdLocation != null) {
                    System.out.println("城市：" + bdLocation.getCity());
                    Toast.makeText(context,
                            bdLocation.getCountry(), Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(SensorManage.newInstance().getLux1())
                .latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude())
                .satellitesNum(100).build();
        // 设置定位数据
        baiduMap.setMyLocationData(locData);

        //    System.out.println("方向：" + lux1);

        MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
//            mCurrentMarker = BitmapDescriptorFactory
//                    .fromResource(R.drawable.icon_geo);
////设置坐标点

    //    LatLng myLatLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
    //    stNode = PlanNode.withLocation(myLatLng);
    //    startPt = myLatLng;
    //    if(worked==false) {
    //    addMarkers(bdLocation.getLatitude(), bdLocation.getLongitude());
    //    PointConverge.newInstance(context, baiduMap).addMarkers(bdLocation.getLatitude(), bdLocation.getLongitude());
    //    }

        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, null);
        baiduMap.setMyLocationConfiguration(config);

        myLL = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());

        GetLocation.getInstance().setLatitude(myLL);
        GetLocation.getInstance().setLongitude(myLL);

        Log.e("", String.valueOf(myLL.latitude));
        Log.e("", String.valueOf(myLL.longitude));

        if (isFirstLoc) {
            isFirstLoc = false;
            MyLocation();
        }

//        View view = View.inflate(context, R.layout.head_mark, null);
//        //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
//        InfoWindow mInfoWindow = new InfoWindow(view, myLL, -50);
//        //显示InfoWindow
//        baiduMap.showInfoWindow(mInfoWindow);
    }

    public void MyLocation() {
        MapStatus.Builder builder = new MapStatus.Builder()
                .target(myLL)//地图缩放中心点
                .zoom(18f);//缩放倍数 百度地图支持缩放21级 部分特殊图层为20级
        //改变地图状态
        baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    public LatLng getMyLL() {
        return myLL;
    }

    public LocationClient getmLocationClient() {
        return mLocationClient;
    }

    private void sendSelfGPS(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

            //    FormBody body = new FormBody.Builder()
             //           .add("")
            }
        }).start();
    }
}
