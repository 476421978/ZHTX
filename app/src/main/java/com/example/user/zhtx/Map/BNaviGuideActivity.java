package com.example.user.zhtx.Map;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBNaviStatusListener;
import com.baidu.mapapi.bikenavi.adapter.IBRouteGuidanceListener;
import com.baidu.mapapi.bikenavi.adapter.IBTTSPlayer;
import com.baidu.mapapi.bikenavi.model.BikeRouteDetailInfo;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.walknavi.model.RouteGuideKind;
import com.example.user.zhtx.R;

public class BNaviGuideActivity extends AppCompatActivity {
    private BikeNavigateHelper mNaviHelper;
    private View view;
    private LinearLayout parent;
    private IBNaviStatusListener mBNavigatorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bnavi_guide);

        /*LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        parent = (LinearLayout) inflater.inflate(R.layout.activity_bnavi_guide, null);
        setContentView(parent);*/

        mNaviHelper = BikeNavigateHelper.getInstance();

        view = mNaviHelper.onCreate(BNaviGuideActivity.this);

        if (view != null) {

            setContentView(view);
        }
        mNaviHelper.setTTsPlayer(new IBTTSPlayer() {
            @Override
            public int playTTSText(String s, boolean b) {
                return 0;
            }
        });
// 开始导航
        mNaviHelper.startBikeNavi(BNaviGuideActivity.this);

        mBNavigatorListener = new IBNaviStatusListener() {

            @Override
            public void onNaviExit() {
                //parent.removeView(view);
                Log.i("导航","退出导航");
            }
        };

        NaiHelperListener();
    }
    protected void onDestroy() {
        super.onDestroy();
        mNaviHelper.pause();
        mNaviHelper.quit();
        Log.i("导航","退出导航");
    }
    private void NaiHelperListener() {
        // 设置诱导监听, 具体诱导信息请参考javadoc
        mNaviHelper.setRouteGuidanceListener(this, new IBRouteGuidanceListener() {
            @Override
            public void onRouteGuideIconUpdate(Drawable icon) {
                Log.i("导航","complete");
            }

            @Override
            public void onRouteGuideKind(RouteGuideKind routeGuideKind) {
                Log.i("导航","complete");
            }

            @Override
            public void onRoadGuideTextUpdate(CharSequence charSequence, CharSequence charSequence1) {
                Toast.makeText(BNaviGuideActivity.this,"导航信息"+charSequence,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRemainDistanceUpdate(CharSequence charSequence) {
                Log.i("导航","complete");
            }

            @Override
            public void onRemainTimeUpdate(CharSequence charSequence) {
                Toast.makeText(BNaviGuideActivity.this,"时间跟新"+charSequence,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onGpsStatusChange(CharSequence charSequence, Drawable drawable) {
                Toast.makeText(BNaviGuideActivity.this,"GPS状态变化"+charSequence,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRouteFarAway(CharSequence charSequence, Drawable drawable) {
                Log.i("导航","complete");
            }

            @Override
            public void onRoutePlanYawing(CharSequence charSequence, Drawable drawable) {
                Log.i("导航","complete");
            }

            @Override
            public void onReRouteComplete() {
                Log.i("导航","complete");
            }

            @Override
            public void onArriveDest() {
                Log.i("导航","complete");
            }

            @Override
            public void onVibrate() {
                Toast.makeText(BNaviGuideActivity.this,"震颤",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onGetRouteDetailInfo(BikeRouteDetailInfo bikeRouteDetailInfo) {
                Log.i("导航","complete");
            }
        });
    }
}
