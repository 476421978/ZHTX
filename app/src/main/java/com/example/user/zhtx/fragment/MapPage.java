package com.example.user.zhtx.fragment;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.route.PlanNode;
import com.example.user.zhtx.Map.GeoCoderManager;
import com.example.user.zhtx.Map.LocationService;
import com.example.user.zhtx.Map.MyLocationListener;
import com.example.user.zhtx.Map.Navigation;
import com.example.user.zhtx.Map.PointConverge;
import com.example.user.zhtx.Map.RoutePlan;
import com.example.user.zhtx.Map.SensorManage;
import com.example.user.zhtx.R;

import java.util.ArrayList;
import java.util.List;

public class MapPage extends Fragment implements View.OnClickListener, BaiduMap.OnMapClickListener, BaiduMap.OnMapLongClickListener, BaiduMap.OnMarkerClickListener {
    private boolean pressed = false;
    private MapView mMapView = null;
    private BaiduMap baiduMap;
    private List<OverlayOptions> options = new ArrayList<OverlayOptions>();
    private BitmapDescriptor marking;
    public Double latitude,longitude;
    private PlanNode stNode,enNode;
    private Button Route_btn, Bike_Guide_btn, Walk_Guide_btn;
    private ImageButton ShowGps_btn;
    //   private LocationClient mLocationClient = null;
    //   public BDLocationListener myListener = null;
    private LatLng endPt ;

    //可用
    private SensorManage sensorManage;
    private GeoCoderManager geoCoderManager;
    public MyLocationListener myLocationListener;
    private Navigation navigation;
    private RoutePlan routePlan;
    private PointConverge pointConverge;
    public OverlayManager overlay;

    public LocationService locationService;
    private LocationConn locationConn;
    private LinearLayout linearLayout;


    /*-----------导航的声明---------------*/
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle) {

        //Location.initLocation(getBaseContext());  //放在main java文件中，初始化sdk
        SDKInitializer.initialize(getActivity().getApplicationContext());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        View v = inflater.inflate(R.layout.fragment_map, group, false);

        mMapView = (MapView)v.findViewById(R.id.bmapView);
        baiduMap= mMapView.getMap();

        baiduMap.setOnMapClickListener(this);
        baiduMap.setOnMapLongClickListener(this);
        baiduMap.setOnMarkerClickListener(this);

        //点聚合
        pointConverge = PointConverge.newInstance(getContext(), baiduMap);
        pointConverge.pointConverge();
        pointConverge.setListener();

        linearLayout = (LinearLayout) v.findViewById(R.id.ll_info);
        linearLayout.setVisibility(View.GONE);

        Route_btn = (Button)v.findViewById(R.id.route_btn);
        Route_btn.setOnClickListener(this);
    //    Route_btn.setVisibility(View.GONE);
        ShowGps_btn =(ImageButton)v.findViewById(R.id.showgps_btn);
        ShowGps_btn.setOnClickListener(this);
        Bike_Guide_btn = (Button)v.findViewById(R.id.bike_guide_btn);
        Bike_Guide_btn.setOnClickListener(this);
        Walk_Guide_btn = (Button)v.findViewById(R.id.walk_guide_btn);
        Walk_Guide_btn.setOnClickListener(this);

        sensorManage = SensorManage.newInstance();

        /*---------------定位设置---------------------------*/
        //定位
        myLocationListener = MyLocationListener.newInstance(getContext(), baiduMap);
//        mLocationClient = new LocationClient(getContext().getApplicationContext());     //声明LocationClient类
//        myListener = myLocationListener;
//        mLocationClient.registerLocationListener(myListener);    //注册监听函数
//        mLocationClient.setLocOption(MyLocationListener.getLCOption());


        /**可用*/

        //地理编码检索
        geoCoderManager = GeoCoderManager.newInstance(getContext());

        //导航
        navigation = Navigation.newInstance(getContext());

        //路线规划
        routePlan = RoutePlan.newInstance(getContext(), baiduMap);
        routePlan.showRoutePlan();

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        explicitStop();
    }

    @Override
    public void onResume() {
        super.onResume();

        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();

        sensorManage.starts(getContext());

        explicitStart();

        //    mLocationClient.start();
        //    mLocationClient.requestLocation();


    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();

        sensorManage.stops();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.route_btn:
                if(pressed == true){
                    overlay.removeFromMap();
                }

                stNode = PlanNode.withLocation(myLocationListener.getMyLL());
                routePlan.searchRoute(stNode, enNode);
                break;
            case R.id.showgps_btn:
                myLocationListener.MyLocation();
                break;
            case R.id.bike_guide_btn:

                navigation.Bike_initNavi(myLocationListener.getMyLL(), endPt);
                break;
        }

    }

    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(getActivity(),"点击了地图",Toast.LENGTH_SHORT).show();

//        View view = View.inflate(getContext(), R.layout.activity_main_page, null);
//        LinearLayout linearLayout = view.findViewById(R.id.activity_main_page_ll_map);
//
        linearLayout.setVisibility(View.GONE);

        if(pressed == true){
            overlay.removeFromMap();
        }
        Route_btn.setVisibility(View.GONE);
        //baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15).build()));
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        /**         * 存储定位点信息         */
        if (latLng != null) {
            linearLayout.setVisibility(View.VISIBLE);

        //    Route_btn.setVisibility(View.VISIBLE);
            latitude = latLng.latitude;
            longitude = latLng.longitude;

            View view = View.inflate(getContext(), R.layout.head_mark, null);

            marking = BitmapDescriptorFactory.fromView(view);
            OverlayOptions option = new MarkerOptions().position(latLng).icon(marking);
            Log.e("adsfasdf",latLng.latitude+";"+latLng.longitude);
            enNode = PlanNode.withLocation(latLng);
            endPt = latLng;
            options.add(option);
            baiduMap.addOverlays(options);

            pointConverge.addMarkers(latLng);
        }else {
            Toast.makeText(getActivity(), "对不起，并未获取到经纬度数据", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        linearLayout.setVisibility(View.VISIBLE);

        Route_btn.setVisibility(View.VISIBLE);
        LatLng clickMarker = marker.getPosition();
        enNode = PlanNode.withLocation(clickMarker);
        endPt = clickMarker;

        geoCoderManager.PositionChange(clickMarker);
        return false;
    }

    private class LocationConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            locationService = ((LocationService.LocationBinder)service).getService();
            locationService.initLocation(getContext(), baiduMap);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    public void explicitStart() {
        if (locationConn == null) {
            locationConn = new LocationConn();
        }
        Intent bindIntent = new Intent(getContext(), LocationService.class);
//        getContext().getApplicationContext().startService(bindIntent);
        getContext().getApplicationContext().bindService(bindIntent,
                locationConn, Service.BIND_AUTO_CREATE);
    }

    public void explicitStop() {
//        getContext().getApplicationContext().stopService(new Intent(getContext(), LocationService.class));
        getContext().getApplicationContext().unbindService(locationConn);
    }
}
