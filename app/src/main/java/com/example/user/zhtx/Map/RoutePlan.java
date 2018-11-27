package com.example.user.zhtx.Map;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.overlayutil.BikingRouteOverlay;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.example.user.zhtx.R;
import com.example.user.zhtx.tools.MyBikingRouteResult;
import com.example.user.zhtx.tools.MyDrivingRouteResult;
import com.example.user.zhtx.tools.MyWalkingRouteResult;

import java.util.List;

public class RoutePlan {

    public static String WALKING = "walking";
    public static String DRIVING = "driving";
    public static String BIKING = "biking";

    private static RoutePlan routePlan;
    private RoutePlanSearch mSearch;
    private WalkingRoutePlanOption walkingRoutePlanOption;
    private DrivingRoutePlanOption drivingRoutePlanOption;
    private BikingRoutePlanOption bikingRoutePlanOption;
    private WalkingRouteOverlay walkingRouteOverlay;
    private DrivingRouteOverlay drivingRouteOverlay;
    private BikingRouteOverlay bikingRouteOverlay;
    private TransitRouteOverlay transitRouteOverlay;

    private Context context;
    private BaiduMap baiduMap;

    public RoutePlan(Context context, BaiduMap baiduMap) {
        this.context = context;
        this.baiduMap = baiduMap;
    }

    public static RoutePlan newInstance(Context context, BaiduMap baiduMap) {
        if (routePlan == null) {
            routePlan = new RoutePlan(context, baiduMap);
        }
        return routePlan;
    }

    public void showRoutePlan() {
        mSearch = RoutePlanSearch.newInstance();


        mSearch.setOnGetRoutePlanResultListener(new RoutePlanResult());
    }

    public void searchRoute(PlanNode stNode, PlanNode enNode, String select) {
        switch (select) {
            case "walking":
                walkingRoutePlanOption = new WalkingRoutePlanOption();
                walkingRoutePlanOption.from(stNode);
                walkingRoutePlanOption.to(enNode);
                mSearch.walkingSearch(walkingRoutePlanOption);
                break;
            case "driving":
                drivingRoutePlanOption = new DrivingRoutePlanOption();
                drivingRoutePlanOption.from(stNode);
                drivingRoutePlanOption.to(enNode);
                mSearch.drivingSearch(drivingRoutePlanOption);
                break;
            case "biking":
                bikingRoutePlanOption = new BikingRoutePlanOption();
                bikingRoutePlanOption.from(stNode);
                bikingRoutePlanOption.to(enNode);
                bikingRoutePlanOption.ridingType(0);
                mSearch.bikingSearch(bikingRoutePlanOption);
                break;

        }
    }

    public void RemoveRoute(boolean showRoute){
        if(showRoute){


                if(walkingRouteOverlay!=null) {
                    walkingRouteOverlay.removeFromMap();
                }


                if(drivingRouteOverlay!=null) {
                    drivingRouteOverlay.removeFromMap();
                }


                if(bikingRouteOverlay!=null) {
                    bikingRouteOverlay.removeFromMap();
                }


        }

    }

    private class RoutePlanResult implements OnGetRoutePlanResultListener {

        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
            // 获取步行线路规划结果
            if (walkingRouteResult == null || walkingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(context, "抱歉，未找到结果",
                        Toast.LENGTH_SHORT).show();
            }
            if (walkingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                return;
            }
            if (walkingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {

                // route = result.getRouteLines().get(0);
                walkingRouteOverlay = new MyWalkingRouteOverlay(baiduMap);
                //mBaiduMap.setOnMarkerClickListener(overlay);
            //    WalkingRouteOverlay routeOverlay = overlay;
                walkingRouteOverlay.setData(walkingRouteResult.getRouteLines().get(0));
                walkingRouteOverlay.addToMap();
                walkingRouteOverlay.zoomToSpan();

                MyWalkingRouteResult myWalkingRouteResult = new MyWalkingRouteResult();

                int distance = 0;
                int duration = 0;

                for(int i=0; i<walkingRouteResult.getRouteLines().size(); i++) {
                    System.out.println("方案"+ (i+1) +"：" + walkingRouteResult.getRouteLines().get(i).getAllStep().get(0).getExit().getTitle());

                    for (int j=0; j<walkingRouteResult.getRouteLines().get(i).getAllStep().size(); j++){
                        System.out.println("步行路段"+ j +"：" + walkingRouteResult.getRouteLines()
                                .get(i).getAllStep().get(j).getInstructions() + ", "
                                + "\n路段道路名称: " + walkingRouteResult.getRouteLines().get(i).getAllStep().get(j).getName()
                                + "\n路线长度: " + walkingRouteResult.getRouteLines().get(i).getAllStep().get(j).getDistance()
                                + "\n路段耗时: " + walkingRouteResult.getRouteLines().get(i).getAllStep().get(j).getDuration()
                                + "\n地理坐标集合: " + walkingRouteResult.getRouteLines().get(i).getAllStep().get(j).getWayPoints());

                        distance += walkingRouteResult.getRouteLines().get(i).getAllStep().get(j).getDistance();
                        duration += walkingRouteResult.getRouteLines().get(i).getAllStep().get(j).getDuration();


                        myWalkingRouteResult.setDistance(distance);
                        myWalkingRouteResult.setDuration(duration);
                    }
                }
                Log.e("步行总长度", String.valueOf(distance));
                Log.e("步行总耗时", String.valueOf(duration));
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
            // 获取步行线路规划结果
            if (drivingRouteResult == null || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(context, "抱歉，未找到结果",
                        Toast.LENGTH_SHORT).show();
            }
            if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                // result.getSuggestAddrInfo()
                return;
            }
            if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {

                // route = result.getRouteLines().get(0);
                drivingRouteOverlay = new MyDrivingRouteOverlay(baiduMap);
                //mBaiduMap.setOnMarkerClickListener(overlay);
                //    WalkingRouteOverlay routeOverlay = overlay;
                drivingRouteOverlay.setData(drivingRouteResult.getRouteLines().get(0));
                drivingRouteOverlay.addToMap();
                drivingRouteOverlay.zoomToSpan();

                MyDrivingRouteResult myDrivingRouteResult = new MyDrivingRouteResult();

                int distance = 0;
                int duration = 0;

                for(int i=0; i<drivingRouteResult.getRouteLines().size(); i++) {
                    System.out.println("方案"+ (i+1) +"：" + drivingRouteResult.getRouteLines().get(i).getAllStep().get(0).getExit().getTitle());

                    for (int j=0; j<drivingRouteResult.getRouteLines().get(i).getAllStep().size(); j++){
                        System.out.println("驾驶路段"+ j +"：" + drivingRouteResult.getRouteLines()
                                .get(i).getAllStep().get(j).getInstructions() + ", "
                                + "\n路段道路名称: " + drivingRouteResult.getRouteLines().get(i).getAllStep().get(j).getName()
                                + "\n路线长度: " + drivingRouteResult.getRouteLines().get(i).getAllStep().get(j).getDistance()
                                + "\n路段耗时: " + drivingRouteResult.getRouteLines().get(i).getAllStep().get(j).getDuration()
                                + "\n地理坐标集合: " + drivingRouteResult.getRouteLines().get(i).getAllStep().get(j).getWayPoints());

                        distance += drivingRouteResult.getRouteLines().get(i).getAllStep().get(j).getDistance();
                        duration += drivingRouteResult.getRouteLines().get(i).getAllStep().get(j).getDuration();

                        myDrivingRouteResult.setDistance(distance);
                        myDrivingRouteResult.setDuration(duration);
                    }
                }
                Log.e("驾驶总长度", String.valueOf(distance));
                Log.e("驾驶总耗时", String.valueOf(duration));
            }
        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
            // 获取步行线路规划结果
            if (bikingRouteResult == null || bikingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(context, "抱歉，未找到结果",
                        Toast.LENGTH_SHORT).show();
            }
            if (bikingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                // result.getSuggestAddrInfo()
                return;
            }
            if (bikingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {

                // route = result.getRouteLines().get(0);
                bikingRouteOverlay = new MyBikingRouteOverlay(baiduMap);
                //mBaiduMap.setOnMarkerClickListener(overlay);
                //    WalkingRouteOverlay routeOverlay = overlay;
                bikingRouteOverlay.setData(bikingRouteResult.getRouteLines().get(0));
                bikingRouteOverlay.addToMap();
                bikingRouteOverlay.zoomToSpan();

                MyBikingRouteResult myBikingRouteResult = new MyBikingRouteResult();

                int distance = 0;
                int duration = 0;

                for(int i=0; i<bikingRouteResult.getRouteLines().size(); i++) {
                    System.out.println("方案"+ (i+1) +"：" + bikingRouteResult.getRouteLines().get(i).getAllStep().get(0).getExit().getTitle());

                    for (int j=0; j<bikingRouteResult.getRouteLines().get(i).getAllStep().size(); j++){
                        System.out.println("骑行路段"+ j +"：" + bikingRouteResult.getRouteLines()
                                .get(i).getAllStep().get(j).getInstructions() + ", "
                                + "\n路段道路名称: " + bikingRouteResult.getRouteLines().get(i).getAllStep().get(j).getName()
                                + "\n路线长度: " + bikingRouteResult.getRouteLines().get(i).getAllStep().get(j).getDistance()
                                + "\n路段耗时: " + bikingRouteResult.getRouteLines().get(i).getAllStep().get(j).getDuration()
                                + "\n地理坐标集合: " + bikingRouteResult.getRouteLines().get(i).getAllStep().get(j).getWayPoints());

                        String instructions = bikingRouteResult.getRouteLines()
                                .get(i).getAllStep().get(j).getInstructions();

                        distance += bikingRouteResult.getRouteLines().get(i).getAllStep().get(j).getDistance();
                        duration += bikingRouteResult.getRouteLines().get(i).getAllStep().get(j).getDuration();


                        myBikingRouteResult.setInstructions(instructions);
                        myBikingRouteResult.setDistance(distance);
                        myBikingRouteResult.setDuration(duration);

                    }
                }
                Log.e("骑行总长度", String.valueOf(distance));
                Log.e("骑行总耗时", String.valueOf(duration));
            }
        }
    }


    public void SearchDestory() {
        mSearch.destroy();
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

    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {
        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
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

    private class MyBikingRouteOverlay extends BikingRouteOverlay {
        public MyBikingRouteOverlay(BaiduMap baiduMap) {
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
}
