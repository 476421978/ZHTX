package com.example.user.zhtx.Map;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
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
import com.example.user.zhtx.R;

public class RoutePlan {

    private static RoutePlan routePlan;
    private RoutePlanSearch mSearch;
    private WalkingRoutePlanOption walkingRoutePlanOption;
    private WalkingRouteOverlay overlay;

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
        walkingRoutePlanOption = new WalkingRoutePlanOption();

        mSearch.setOnGetRoutePlanResultListener(new RoutePlanResult());
    }

    public void searchRoute(PlanNode stNode, PlanNode enNode) {
        walkingRoutePlanOption.from(stNode);
        walkingRoutePlanOption.to(enNode);
        mSearch.walkingSearch(walkingRoutePlanOption);
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
                // result.getSuggestAddrInfo()
                return;
            }
            if (walkingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {

                // route = result.getRouteLines().get(0);
                overlay = new MyWalkingRouteOverlay(baiduMap);
                //mBaiduMap.setOnMarkerClickListener(overlay);
                WalkingRouteOverlay routeOverlay = overlay;
                overlay.setData(walkingRouteResult.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
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
}
