package com.example.user.zhtx.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBEngineInitListener;
import com.baidu.mapapi.bikenavi.adapter.IBRoutePlanListener;
import com.baidu.mapapi.bikenavi.model.BikeRoutePlanError;
import com.baidu.mapapi.bikenavi.params.BikeNaviLaunchParam;
import com.baidu.mapapi.model.LatLng;

public class Navigation {

    private BikeNaviLaunchParam Bikeparam;
    private static final String LTAG = "导航回应++++++++++";
    private static Navigation navigation;
    private Context context;

    public Navigation(Context context) {
        this.context = context;
    }

    public static Navigation newInstance(Context context) {
        if(navigation ==null) {
            navigation = new Navigation(context);
        }
        return navigation;
    }

    public void Bike_initNavi(final LatLng startPt, final LatLng endPt) {
        // 获取导航控制类
        // 引擎初始化
        BikeNavigateHelper.getInstance().initNaviEngine((Activity) context, new IBEngineInitListener() {
            @Override
            public void engineInitSuccess() {
                Log.d(LTAG, "引擎初始化成功");
                routePlanWithParam(context, startPt, endPt);
            }

            @Override
            public void engineInitFail() {
                Log.d(LTAG, "引擎初始化失败");
            }
        });
    }

    /**
     * 开始算路
     */
    @SuppressLint("ResourceType")
    public void routePlanWithParam(final Context context, LatLng startPt, LatLng endPt) {
        Bikeparam = new BikeNaviLaunchParam().stPt(startPt).endPt(endPt).vehicle(0);
        BikeNavigateHelper.getInstance().routePlanWithParams(Bikeparam, new IBRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                Log.d(LTAG, "开始算路");
            }

            @Override
            public void onRoutePlanSuccess() {
                Log.d(LTAG, "算路成功,跳转至诱导页面");
                Intent intent = new Intent();
                intent.setClass(context, BNaviGuideActivity.class);
                context.startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(BikeRoutePlanError error) {
                Log.d(LTAG, "算路失败");
                Log.e("算路失败：", error.name());
            }

        });
    }
}
