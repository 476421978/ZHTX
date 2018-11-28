package com.example.user.zhtx.fragment;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.route.PlanNode;
import com.example.user.zhtx.LoginActivity;
import com.example.user.zhtx.Map.AlarmSettings;
import com.example.user.zhtx.Map.CircleImageView;
import com.example.user.zhtx.Map.GeoCoderManager;
import com.example.user.zhtx.Map.LocationService;
import com.example.user.zhtx.Map.MyLocationListener;
import com.example.user.zhtx.Map.Navigation;
import com.example.user.zhtx.Map.PointConverge;
import com.example.user.zhtx.Map.RoutePlan;
import com.example.user.zhtx.Map.SensorManage;
import com.example.user.zhtx.R;
import com.example.user.zhtx.tools.Address;
import com.example.user.zhtx.tools.GetGeoCoderResult;
import com.example.user.zhtx.tools.GetLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapPage extends Fragment implements View.OnClickListener, BaiduMap.OnMapClickListener, BaiduMap.OnMapLongClickListener,BaiduMap.OnMarkerClickListener{
    private boolean pressed = false;
    private MapView mMapView = null;
    private BaiduMap baiduMap;
    private List<OverlayOptions> options = new ArrayList<OverlayOptions>();
    private BitmapDescriptor marking;
    public Double latitude,longitude;
    private PlanNode stNode,enNode;
    private Button Route_btn, Bike_Guide_btn, walking_btn, driving_btn, biking_btn;
    private TextView address_txt, addressSematic_txt, distance_txt, duration_txt,name_txt,phone_text;
    private Marker markered;
    private ImageButton ShowGps_btn;
    private LatLng endPt ;
    private OverlayOptions option;
    //可用
    private SensorManage sensorManage;
    private GeoCoderManager geoCoderManager;
    public MyLocationListener myLocationListener;
    private Navigation navigation;
    private RoutePlan routePlan;
    private PointConverge pointConverge;
    public OverlayManager overlay;
    private boolean showRoute = false;
    private boolean isFirstLoc = false;
    public LocationService locationService;
    private LocationConn locationConn;
    private LinearLayout linearLayout,self_info_ll,ll_info;
    private RelativeLayout relativeLayout;

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
        baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(baiduMap.getMapStatus()));



        linearLayout = (LinearLayout) v.findViewById(R.id.ll_info);
        relativeLayout = (RelativeLayout) v.findViewById(R.id.ll_info_1);

        self_info_ll = (LinearLayout)v.findViewById(R.id.self_info_ll);

        Route_btn = (Button)v.findViewById(R.id.route_btn);
        Route_btn.setOnClickListener(this);
        ShowGps_btn =(ImageButton)v.findViewById(R.id.showgps_btn);
        ShowGps_btn.setOnClickListener(this);
        Bike_Guide_btn = (Button)v.findViewById(R.id.bike_guide_btn);
        Bike_Guide_btn.setOnClickListener(this);

        address_txt = (TextView) v.findViewById(R.id.address_txt);
        addressSematic_txt = (TextView) v.findViewById(R.id.addressSematic_txt);
        name_txt = (TextView)v.findViewById(R.id.name_txt);
        phone_text =(TextView) v.findViewById(R.id.phonenum_txt);

        walking_btn = (Button)v.findViewById(R.id.walking_btn);
        walking_btn.setOnClickListener(this);
        driving_btn = (Button)v.findViewById(R.id.driving_btn);
        driving_btn.setOnClickListener(this);
        biking_btn = (Button)v.findViewById(R.id.biking_btn);
        biking_btn.setOnClickListener(this);

        sensorManage = SensorManage.newInstance();

        /*---------------定位设置---------------------------*/
        //定位
        myLocationListener = MyLocationListener.newInstance(getContext(), baiduMap);


        //点聚合
        pointConverge = PointConverge.newInstance(getContext(), baiduMap,name_txt,phone_text);
        pointConverge.pointConverge();
        pointConverge.setListener();

        /**可用*/

        //地理编码检索
        geoCoderManager = GeoCoderManager.newInstance(getContext());

        //导航
        navigation = Navigation.newInstance(getContext());

        //路线规划
        routePlan = RoutePlan.newInstance(getContext(), baiduMap);
        routePlan.showRoutePlan();

    //    addMarkers();

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        geoCoderManager.GeoCoderDestroy();

        routePlan.SearchDestory();

        baiduMap.setMyLocationEnabled(false);

        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();

        myLocationListener.LocationClientStop();

        explicitStop();

        baiduMap = null;
        mMapView = null;

        Log.e("onDestroy:", "执行onDestroy");
    }

    @Override
    public void onResume() {
        super.onResume();

        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();

        explicitStart();

        sensorManage.starts(getContext());

        Log.e("onResume:", "执行onResume");

        AlarmSettings.newInstance().startLocationService(getContext(), 300,
                LocationService.class, "ALARM_ACTION");
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
        stNode = PlanNode.withLocation(GetLocation.newInstance().getMyLL());

        switch (v.getId()){

            case R.id.route_btn:
                if(showRoute == true){
                    routePlan.RemoveRoute(showRoute);
                }

                routePlan.searchRoute(stNode, enNode, RoutePlan.WALKING);

                linearLayout.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
                showRoute = true;
                break;
            case R.id.showgps_btn:
                myLocationListener.isFirstLoc = true;
                myLocationListener.initLocation();
                break;
            case R.id.bike_guide_btn:
                navigation.Bike_initNavi(GetLocation.newInstance().getMyLL(), endPt);

                break;
            case R.id.walking_btn:

                routePlan.searchRoute(stNode, enNode, RoutePlan.WALKING);

                linearLayout.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
                if(showRoute == true){
                    routePlan.RemoveRoute(showRoute);

                }
                MarkerRemove();
                break;
            case R.id.biking_btn:

                routePlan.searchRoute(stNode, enNode, RoutePlan.BIKING);

                linearLayout.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
                if(showRoute == true){
                    routePlan.RemoveRoute(showRoute);

                }
                MarkerRemove();
                break;
            case R.id.driving_btn:

                routePlan.searchRoute(stNode, enNode, RoutePlan.DRIVING);

                linearLayout.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
                if(showRoute == true){
                    routePlan.RemoveRoute(showRoute);
                }
                MarkerRemove();
                break;
        }

    }

    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(getActivity(),"点击了地图",Toast.LENGTH_SHORT).show();
        MarkerRemove();
        linearLayout.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.GONE);

        if(showRoute == true){
            routePlan.RemoveRoute(showRoute);
        }
    }

    private void MarkerRemove() {
        if (markered!=null) {
            if (isFirstLoc == true) {
                markered.remove();
            }
            isFirstLoc = false;
        }
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        /**         * 存储定位点信息         */
        MarkerRemove();
        if (latLng != null) {
            linearLayout.setVisibility(View.VISIBLE);
            self_info_ll.setVisibility(View.GONE);
            latitude = latLng.latitude;
            longitude = latLng.longitude;

            marking = BitmapDescriptorFactory.fromResource(R.drawable.point);
            option = new MarkerOptions().position(latLng).icon(marking);
            markered = (Marker)baiduMap.addOverlay(option);
            Log.e("adsfasdf",latLng.latitude+";"+latLng.longitude);

            enNode = PlanNode.withLocation(latLng);
            endPt = latLng;

            isFirstLoc = true;

            showAddress(latLng);
            Toast.makeText(getActivity(), "获取经度："+longitude+"获取纬度："+latitude, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(), "对不起，并未获取到经纬度数据", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean onMarkerClick(Marker marker) {

        linearLayout.setVisibility(View.VISIBLE);
        Route_btn.setVisibility(View.VISIBLE);
        LatLng clickMarker = marker.getPosition();
        enNode = PlanNode.withLocation(clickMarker);
        endPt = clickMarker;
        showAddress(clickMarker);
        return false;
    }

    public void showAddress(LatLng latLng) {
        geoCoderManager.PositionChange(latLng);
        geoCoderManager.setAddressListner(new GeoCoderManager.AddressListner() {

            @Override
            public void getGetGeoCoderResult(GetGeoCoderResult getGeoCoderResult) {
                address_txt.setText(getGeoCoderResult.getAddress());
                addressSematic_txt.setText(getGeoCoderResult.getAddressSematic());


            }
        });
    }

    private class LocationConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            locationService = ((LocationService.LocationBinder)service).getService();
            locationService.initPointConverge(getContext(), baiduMap, name_txt, phone_text);
            locationService.initLocation(getContext(), baiduMap);
            sendSelfGPS();
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
    private void sendSelfGPS(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                SharedPreferences sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                FormBody body = new FormBody.Builder()
                        .add("userid",sp.getInt("id",0)+"")
                        .add("atitude", new GetLocation().getLatitude()+"")
                        .add("longatitude",new GetLocation().getLongitude()+"")
                        .build();

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
