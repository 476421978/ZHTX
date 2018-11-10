package com.example.user.zhtx.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.clusterutil.clustering.Cluster;
import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.example.user.zhtx.Map.BitmapTextView;
import com.example.user.zhtx.R;

import java.util.ArrayList;
import java.util.List;

public class MapPage extends Fragment implements View.OnClickListener, SensorEventListener {
    private static Bitmap BbitmapTest;
    List<MyItem> items1 = new ArrayList<MyItem>();
    private boolean pressed = false;
    View mView;
    MapView mMapView = null;
    private static final int REQUEST_CODE = 0; // 请求码
    BaiduMap baiduMap;
    List<OverlayOptions> options = new ArrayList<OverlayOptions>();
    LatLng point1 = new LatLng(39.92235, 116.380338);
    LatLng point2 = new LatLng(39.947246, 116.414977);
    BitmapTextView btv = new BitmapTextView();
    Bitmap bitmapTest ;
    BitmapDescriptor bitmap,bitmap1,marking;
    ImageView image;
    Double latitude,longitude;
    RoutePlanSearch mSearch;
    private ClusterManager<MyItem> mClusterManager ;
    PlanNode stNode,enNode;
    MapStatus ms;
    Button button,button2;
    WalkNaviLaunchParam param ;
    WalkingRoutePlanOption walkingRoutePlanOption;
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = null;
    private LatLng myLL;
    private boolean isFirstLoc = true;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker;
    WalkingRouteOverlay overlay;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private float lux1;
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle) {

        //Location.initLocation(getBaseContext());  //放在main java文件中，初始化sdk
        SDKInitializer.initialize(getActivity().getApplicationContext());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        View v = inflater.inflate(R.layout.fragment_map, group, false);

        mMapView = (MapView)v.findViewById(R.id.bmapView);
//        mPermissionsChecker = new PermissionsChecker(this);
//        mPermissionsChecker.lacksPermissions(PERMISSIONS);

        baiduMap= mMapView.getMap();
        //    baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(10f).build()));
    //    baiduMap.setOnMapLoadedCallback(this);

        MaplongClick();
        ShowRoutePlan();
        mapClick();
        MarkClick();

        PointConverge();


        /*View view =View.inflate(this,R.layout.layout_,null);
        image = (ImageView) findViewById(R.id.abc);
        bitmap1 = BitmapDescriptorFactory.fromView(view);
        bitmap = BitmapDescriptorFactory.fromView(view);//准备 marker 的图片
        OverlayOptions option1 =  new MarkerOptions()
                .position(point1)
                .icon(bitmap);
        OverlayOptions option2 =  new MarkerOptions()
                .position(point2)
                .icon(bitmap1);
        //获取添加的 marker 这样便于后续的操作
        options.add(option1);
        options.add(option2);
        baiduMap.addOverlays(options);*/



        //LatLng point = new LatLng(39.963175,116.400244);//baiduMap.getMapStatus.target
        //bitmapTest = btv.drawBitMapText("231313213213213213213",bitmap);
        //int width = bitmapTest.getWidth();
        //int height = bitmapTest.getHeight();
        //Log.e("TAG","bitmap:"+width+";"+height);
        //准备 marker option 添加 marker 使用

        button = (Button)v.findViewById(R.id.btn);
        button.setOnClickListener((View.OnClickListener)this);
        button.setVisibility(View.GONE);
        button2 =(Button)v.findViewById(R.id.btn2);
        button2.setOnClickListener(this);
        //bitmapTest = btv.drawBitMapText("个人信息",bitmap);
        //BbitmapTest = bitmapTest;
        //convertViewToBitmap(mView);

//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量


        //final InfoWindow infoWindow = new InfoWindow( bitmapTest, point1, -80,listener);
        /* List<MyItem> items = new ArrayList<MyItem>();
        items.add(new MyItem(point1));
        items.add(new MyItem(point2));
        mClusterManager.addItems(items);*/
        baiduMap.setOnMapStatusChangeListener(mClusterManager);
        baiduMap.setOnMarkerClickListener(mClusterManager);


        /*---------------定位设置---------------------------*/
        //定位
        mLocationClient = new LocationClient(getContext().getApplicationContext());     //声明LocationClient类
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener);    //注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(2000);
        option.setIsNeedAddress(true);
        option.setNeedDeviceDirect(true);
        option.setIsNeedLocationPoiList(true);
        option.setPriority(LocationClientOption.NetWorkFirst);// 网络定位优先
        mLocationClient.setLocOption(option);

        mLocationClient.start();
        mLocationClient.requestLocation();


        //传感器
        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        if (mSensor != null){
            // Snull;uccess! There's a magnetometer.
            List<Sensor> oriSensors = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
            for(int i=0; i<oriSensors.size(); i++) {
                if ((oriSensors.get(i).getVendor().contains("Google LLC")) &&
                        (oriSensors.get(i).getVersion() == 3)){
                    // Use the version 3 gravity sensor.
                    mSensor = oriSensors.get(i);
                }
            }
        } else {
            // Failure! No magnetometer.
        }

        return v;
    }

    private void PositionChange(LatLng latLng) {
            // 创建地理编码检索实例
            GeoCoder geoCoder = GeoCoder.newInstance();
            //
            OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
                // 反地理编码查询结果回调函数
                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                    if (result == null
                            || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        // 没有检测到结果
                    }
                    String addressText = result.getAddress();//这里的addressText就是我们要的地址
                    Toast.makeText(getContext(),"地址："+addressText, Toast.LENGTH_SHORT).show();
                }

                // 地理编码查询结果回调函
                @Override
                public void onGetGeoCodeResult(GeoCodeResult result) {
                    if (result == null
                            || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        // 没有检测到结果
                    }
                }
            };
            // 设置地理编码检索监听者
            geoCoder.setOnGetGeoCodeResultListener(listener);
            //
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
            // 释放地理编码检索实例
            // geoCoder.destroy();

        }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();

        mLocationClient.start();
        mLocationClient.requestLocation();

        mSensorManager.registerListener((SensorEventListener) this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();

        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        lux1 = event.values[0];
        //    System.out.println(lux1);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void addMarkers(double latitude, double longitude) {

        LatLng llA = new LatLng(latitude, longitude + 0.001);
        LatLng llB = new LatLng(latitude+0.001, longitude+0.002);
        LatLng llC = new LatLng(latitude+0.002, longitude+0.002);
        LatLng llD = new LatLng(latitude-0.01, longitude+0.01);
        LatLng llE = new LatLng(latitude-0.02, longitude+0.02);
        LatLng llF = new LatLng(latitude-0.01, longitude+0.01);
        LatLng llG = new LatLng(latitude-0.02, longitude+0.02);

        items1.add(new MyItem(llA));
        items1.add(new MyItem(llB));
        items1.add(new MyItem(llC));
        items1.add(new MyItem(llD));
        items1.add(new MyItem(llE));
        items1.add(new MyItem(llF));
        items1.add(new MyItem(llG));
        mClusterManager.addItems(items1);
    }

    public class MyItem implements ClusterItem {
        private final LatLng mPosition;
        public MyItem(LatLng latLng) {
            mPosition = latLng;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public BitmapDescriptor getBitmapDescriptor() {
            return BitmapDescriptorFactory
                    .fromResource(R.drawable.marker);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                if(pressed == true){
                    overlay.removeFromMap();
                }
                walkingRoutePlanOption.from(stNode);
                walkingRoutePlanOption.to(enNode);
                mSearch.walkingSearch(walkingRoutePlanOption);
                break;
            case R.id.btn2:
                MyLocation();
                break;
        }

    }

    private void MaplongClick() {
        baiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                /**         * 存储定位点信息         */
                if (latLng != null) {
                    button.setVisibility(View.VISIBLE);
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                //    showSearchResult(latitude, longitude);
                //    getLocationData(latLng);
                    marking = BitmapDescriptorFactory.fromResource(R.drawable.marker);
                    OverlayOptions option = new MarkerOptions().position(latLng).icon(marking);
                    Log.e("adsfasdf",latLng.latitude+";"+latLng.longitude);
                    enNode = PlanNode.withLocation(latLng);
                    options.add(option);
                    baiduMap.addOverlays(options);
                }else {
                    Toast.makeText(getActivity(), "对不起，并未获取到经纬度数据", Toast.LENGTH_SHORT).show();
                }
            }
            /**             * 发起反向搜索             */
        });
    }

    private void ShowRoutePlan() {
        mSearch = RoutePlanSearch.newInstance();
        walkingRoutePlanOption = new WalkingRoutePlanOption();

        OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {

            public void onGetWalkingRouteResult(WalkingRouteResult result) {
                // 获取步行线路规划结果
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(getActivity(), "抱歉，未找到结果",
                            Toast.LENGTH_SHORT).show();
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    // result.getSuggestAddrInfo()
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {

                    // route = result.getRouteLines().get(0);
                    overlay = new MyWalkingRouteOverlay(baiduMap);
                    //mBaiduMap.setOnMarkerClickListener(overlay);
                    WalkingRouteOverlay routeOverlay = overlay;
                    overlay.setData(result.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                    pressed= true;
                }

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }

        };

        mSearch.setOnGetRoutePlanResultListener(listener);
    }

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {
        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }
        @Override
        public int getLineColor() {
            //红色的路径
            return Color.RED;
        }
        @Override
        public BitmapDescriptor getStartMarker() {
            //自定义的起点图标
            return BitmapDescriptorFactory.fromResource(R.drawable.myicon_start);
        }
        @Override
        public BitmapDescriptor getTerminalMarker() {
            //自定义的终点图标
            return BitmapDescriptorFactory.fromResource(R.drawable.myicon_end);
        }
    }

    private void mapClick() {

        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(getActivity(),"点击了地图",Toast.LENGTH_SHORT).show();
                if(pressed == true){
                    overlay.removeFromMap();
                }
                button.setVisibility(View.GONE);
                //baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15).build()));

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    private void MarkClick() {
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //bitmapTest = btv.drawBitMapText("个人信息",bitmap);
                button.setVisibility(View.VISIBLE);
                LatLng ClickMarker = marker.getPosition();
                enNode = PlanNode.withLocation(ClickMarker);
                PositionChange(ClickMarker);
                return false;
            }
        });
    }

    private void PointConverge() {
        mClusterManager = new ClusterManager<MyItem>(getContext(), baiduMap);
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>(){

            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster){
                Toast.makeText(getContext(),"有" + cluster.getSize() + "个点", Toast.LENGTH_SHORT).show();
                return false;
            }});
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>(){
            @Override
            public boolean onClusterItemClick(MyItem item) {
                //Toast.makeText(getContext(), "点击单个Item", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(final BDLocation bdLocation) {

            // 开启定位图层
            baiduMap.setMyLocationEnabled(true);

            baiduMap.setOnMyLocationClickListener(new BaiduMap.OnMyLocationClickListener() {
                @Override
                public boolean onMyLocationClick() {
                    if(bdLocation != null) {
                        System.out.println("城市：" + bdLocation.getCity());
                        Toast.makeText(getActivity(),
                                bdLocation.getCountry(), Toast.LENGTH_LONG).show();
                    }
                    return false;
                }
            });

            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(lux1)
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .satellitesNum(100).build();
            // 设置定位数据
            baiduMap.setMyLocationData(locData);

            //    System.out.println("方向：" + lux1);

            mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
            mCurrentMarker = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_geo);
//设置坐标点

            LatLng myLatLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            stNode = PlanNode.withLocation(myLatLng);
            addMarkers(bdLocation.getLatitude(), bdLocation.getLongitude());

            // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
            MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
            baiduMap.setMyLocationConfiguration(config);

            myLL = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            if (isFirstLoc) {
                isFirstLoc = false;
                MyLocation();
            }
        }
    }

    public void MyLocation() {
        MapStatus.Builder builder = new MapStatus.Builder()
                .target(myLL)//地图缩放中心点
                .zoom(18f);//缩放倍数 百度地图支持缩放21级 部分特殊图层为20级
        //改变地图状态
        baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }
}
