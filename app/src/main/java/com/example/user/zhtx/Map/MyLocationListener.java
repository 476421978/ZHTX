package com.example.user.zhtx.Map;

import android.content.Context;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.user.zhtx.tools.GetLocation;

public class MyLocationListener implements BDLocationListener {

    private static MyLocationListener myLocationListener;
    private Context context;
    private BaiduMap baiduMap;
    public boolean isFirstLoc = true;
    private  SensorManage sensorManage;
    private float mCurrentX;
    private LocationClient mLocationClient = null;
    private LatLng myLL;

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
        sensorManage = SensorManage.newInstance();
        sensorManage.setOrientationSensorListener(new SensorManage.OnOrientationListener() {

            @Override
            public void onOrientationListener(float x) {
                mCurrentX = x;
            }
        });

        //定位
        mLocationClient = new LocationClient(context.getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(this);    //注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        option.setNeedDeviceDirect(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setPriority(LocationClientOption.NetWorkFirst);// 网络定位优先

        mLocationClient.setLocOption(option);

        mLocationClient.start();
        mLocationClient.requestLocation();
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        setLocationConfig(bdLocation);

        myLL = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        GetLocation.newInstance().setMyLLs(myLL);

        if (isFirstLoc) {
            myLocation.getMyLocation(myLL);
            isFirstLoc = false;
        }
    }

    public void setLocationConfig(BDLocation bdLocation) {
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);

        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(mCurrentX)
                .latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude())
                .satellitesNum(100).build();
        // 设置定位数据
        baiduMap.setMyLocationData(locData);

        MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, null);
        baiduMap.setMyLocationConfiguration(config);
    }

    private MyLocation myLocation;

    public void setMyLocationListener(MyLocation myLocation) {
        this.myLocation = myLocation;
    }

    public interface MyLocation {
        void getMyLocation(LatLng latLng);
    }

    public void LocationClientStop() {
        mLocationClient.stop();
    }
}
