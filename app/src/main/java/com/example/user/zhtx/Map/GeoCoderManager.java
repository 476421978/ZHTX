package com.example.user.zhtx.Map;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class GeoCoderManager {

    private static GeoCoderManager geoCoderManager;
    private Context context;

    public GeoCoderManager(Context context) {
        this.context = context;
    }

    public static GeoCoderManager newInstance(Context context) {
        if(geoCoderManager == null) {
            geoCoderManager = new GeoCoderManager(context);
        }
        return geoCoderManager;
    }

    public void PositionChange(LatLng latLng) {
        // 创建地理编码检索实例
        GeoCoder geoCoder = GeoCoder.newInstance();

        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            // 反地理编码查询结果回调函数
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null
                        || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                }
                String addressText = result.getAddress();//这里的addressText就是我们要的地址
            //    showAddress = addressText;
                Log.e("address","地址详细细信息"+addressText);
                Toast.makeText(context,"地址："+addressText, Toast.LENGTH_SHORT).show();
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
}
