package com.UI.EaseUI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.user.zhtx.R;
import com.hyphenate.easeui.ui.EaseBaseActivity;

public class sendMyLocation extends EaseBaseActivity{
    private Button btn_send;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    //定位相关
    private LocationClient mLocationClient;
    private boolean isFirstIn = true;
    //经纬度
    private double mLatitude;
    private double mLongtitude;
    //定位模式（三种其中一种）
    private MyLocationConfiguration.LocationMode mLocationMode;
    // 自定义定位图标
    private BitmapDescriptor mIconLocation;
    //定位监听
    private MyLocationListener mLocationListener;

    // 覆盖物相关
    private BitmapDescriptor mMarker;
    private RelativeLayout mMarkerLy;

    private Handler handler;

    private GeoCoder mSearch;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_my_location);
        //初始化
        init();
        //初始化定位
        initLocation();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        //成功获取地址信息 才能点击发送按钮
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public void init(){
        btn_send = (Button) findViewById(R.id.btn_send);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        //初始化地图
        mBaiduMap = mMapView.getMap();
        //普通定位模式
        mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
        //开始定位
        mLocationClient = new LocationClient(sendMyLocation.this);
        if (!mLocationClient.isStarted()){
            mLocationClient.start();
        }


    }

    public void sendLocation(View view) {
        System.out.println("发送位置");
        Intent intent = this.getIntent();
        intent.putExtra("latitude",mLatitude);
        intent.putExtra("longitude",mLongtitude);
        intent.putExtra("address","广东轻工职业技术学院");
        this.setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(com.hyphenate.easeui.R.anim.slide_in_from_left, com.hyphenate.easeui.R.anim.slide_out_to_right);
    }

    //结束当前页面
    public void back(View v) {
        finish();
    }


    //初始化定位信息
    public void initLocation(){
        //计算两点之间的距离
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(500);//设置比例

        mBaiduMap.setMapStatus(msu);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //定位监听
        mLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);
        //设置定位
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);

        /*Toast.makeText(MainActivity.this,"0",Toast.LENGTH_SHORT).show();*/
    }

    //百度地图 位置监听 获取经纬度
    private class MyLocationListener implements BDLocationListener {
        public void onReceiveLocation(BDLocation bdLocation) {//获取经纬度 重复获取
            // 更新经纬度
            mLatitude = bdLocation.getLatitude();
            mLongtitude = bdLocation.getLongitude();
            // 设置定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(10)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(0)
                    .latitude(mLatitude)
                    .longitude(mLongtitude)
                    .build();
            mBaiduMap.setMyLocationData(locData);

            // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
            mIconLocation = BitmapDescriptorFactory
                    .fromResource(R.drawable.navi_map_gps_locked);
            MyLocationConfiguration config = new MyLocationConfiguration(mLocationMode, true,null);
            mBaiduMap.setMyLocationConfiguration(config);

            //第一次定位
            if (isFirstIn)
            {
                LatLng latLng = new LatLng(mLatitude,
                        mLongtitude);//经纬度
                //设置地图中心位置
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(msu);
                isFirstIn = false;
     /*           Toast.makeText(MainActivity.this,"第一次定位",Toast.LENGTH_SHORT).show();*/
            }
            //反向编码 获取地址
            LatLng latlng = new LatLng(mLatitude, mLongtitude);
            mSearch = GeoCoder.newInstance();
            mSearch.setOnGetGeoCodeResultListener(listener);
            mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(latlng));
            System.out.println(latlng);
         /*
            GeoCoderManager geoCoderManager = GeoCoderManager.newInstance(sendMyLocation.this);
            geoCoderManager.PositionChange(latlng);
            geoCoderManager.setAddressListner(new GeoCoderManager.AddressListner() {
                public void getGetGeoCoderResult(GetGeoCoderResult getGeoCoderResult) {
                    getGeoCoderResult.getAddressSematic();
                    System.out.println(getGeoCoderResult.getAddressSematic());
                }
            });*/

        }
    }

    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果
                Toast.makeText(getApplicationContext(),"找不到位置1",Toast.LENGTH_SHORT).show();
            }
            //获取地理编码结果
        }
        @Override

        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
                Toast.makeText(getApplicationContext(),"找不到位置2",Toast.LENGTH_SHORT).show();
            }
            //获取反向地理编码结果
            System.out.println(result+"-----");
        }
    };






    @Override
    protected void onDestroy() {
        mBaiduMap.setMyLocationEnabled(false);

        super.onDestroy();
    }
}
