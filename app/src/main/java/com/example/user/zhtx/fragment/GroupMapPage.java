package com.example.user.zhtx.fragment;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
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
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.PlanNode;
import com.example.user.zhtx.Map.GeoCoderManager;
import com.example.user.zhtx.Map.GroupLocationListener;
import com.example.user.zhtx.Map.GroupLocationService;
import com.example.user.zhtx.Map.Navigation;
import com.example.user.zhtx.Map.PointConverge;
import com.example.user.zhtx.Map.RoutePlan;
import com.example.user.zhtx.Map.SensorManage;
import com.example.user.zhtx.R;
import com.example.user.zhtx.tools.GetGeoCoderResult;
import com.example.user.zhtx.tools.GetLocation;

@SuppressLint("ValidFragment")
public class GroupMapPage extends Fragment implements View.OnClickListener, BaiduMap.OnMapClickListener,
        BaiduMap.OnMapLongClickListener,BaiduMap.OnMarkerClickListener, BaiduMap.OnMapLoadedCallback{
    private MapView mMapView = null;
    private BaiduMap baiduMap;
    private BitmapDescriptor marking;
    private PlanNode stNode,enNode;
    private Button Route_btn, Bike_Guide_btn, walking_btn, driving_btn, biking_btn;
    private TextView address_txt, addressSematic_txt, name_txt, phone_text;
    private Marker markered;
    private ImageButton ShowGps_btn;
    private LatLng endPt ;
    private OverlayOptions option;
    //可用
    private SensorManage sensorManage;
    private GeoCoderManager geoCoderManager;
    public GroupLocationListener groupLocationListener;
    private Navigation navigation;
    private RoutePlan routePlan;
    private PointConverge pointConverge;
    private boolean showRoute = false;
    private boolean isFirstLoc = false;
    public GroupLocationService groupLocationService;
    private GroupLocationConn groupLocationConn;
    private LinearLayout linearLayout,self_info_ll;
    private RelativeLayout relativeLayout;
    private MapStatus ms;

    @SuppressLint("ValidFragment")
    public GroupMapPage(LinearLayout activity_main_page_ll_map) {
        activity_main_page_ll_map.setVisibility(View.GONE);
    }

    /*-----------导航的声明---------------*/
    @Override
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
        groupLocationListener = GroupLocationListener.newInstance(getContext(), baiduMap);
        groupLocationListener.initLocation();
        groupLocationListener.setMyLocationListener(new GroupLocationListener.MyLocation() {

            @Override
            public void getMyLocation(LatLng latLng) {
                ms = new MapStatus.Builder()
                        .target(latLng).zoom(18).build();
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
            }
        });

        //点聚合
        pointConverge = PointConverge.newInstance(getContext(), baiduMap);
        pointConverge.pointConverge(name_txt,phone_text);
        pointConverge.setListener();

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
        Destroy();
    }

    public void Destroy() {
        if (mMapView != null) {
            geoCoderManager.GeoCoderDestroy();

            routePlan.SearchDestory();

            baiduMap.setMyLocationEnabled(false);

            //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
            mMapView.onDestroy();

            explicitStop();

            baiduMap = null;
            mMapView = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();

        explicitStart();

        sensorManage.starts(getContext());

//        AlarmSettings.newInstance().startLocationService(getContext(), 5,
//                GroupLocationService.class, "ALARM_ACTION");
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
                groupLocationListener.isFirstLoc = true;
                groupLocationListener.initLocation();
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

            marking = BitmapDescriptorFactory.fromResource(R.drawable.point);
            option = new MarkerOptions().position(latLng).icon(marking);
            markered = (Marker)baiduMap.addOverlay(option);

            enNode = PlanNode.withLocation(latLng);
            endPt = latLng;

            isFirstLoc = true;

            showAddress(latLng);
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

    @Override
    public void onMapLoaded() {
        ms = new MapStatus.Builder().zoom(9).build();
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }

    private class GroupLocationConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            groupLocationService = ((GroupLocationService.GroupLocationBinder)service).getService();
            groupLocationService.initPointConverge(getContext(), baiduMap, name_txt, phone_text);
            groupLocationService.initLocation(getContext());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    public void explicitStart() {
        if (groupLocationConn == null) {
            groupLocationConn = new GroupLocationConn();
        }
        Intent bindIntent = new Intent(getContext(), GroupLocationService.class);
//        getContext().getApplicationContext().startService(bindIntent);
        getContext().getApplicationContext().bindService(bindIntent,
                groupLocationConn, Service.BIND_AUTO_CREATE);
    }

    public void explicitStop() {
//        getContext().getApplicationContext().stopService(new Intent(getContext(), LocationService.class));
        getContext().getApplicationContext().unbindService(groupLocationConn);
    }
}