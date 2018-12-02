package com.example.user.zhtx.Map;

import android.content.Context;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.user.zhtx.tools.GetGeoCoderResult;

public class GeoCoderManager {

    private static GeoCoderManager geoCoderManager;
    private Context context;
    private GeoCoder geoCoder;

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
        geoCoder = GeoCoder.newInstance();

        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            // 反地理编码查询结果回调函数
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null
                        || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                }

                GetGeoCoderResult getGeoCoderResult = GetGeoCoderResult.newInstance();

                String address = result.getAddressDetail().province + result.getAddressDetail().city
                        + result.getAddressDetail().district + result.getAddressDetail().town
                        + result.getAddressDetail().street + result.getAddressDetail().streetNumber;

                String addressSematic = result.getSematicDescription();

                getGeoCoderResult.setAddress(address);
                getGeoCoderResult.setAddressSematic(addressSematic);

                addressListner.getGetGeoCoderResult(getGeoCoderResult);

                /*Log.e("", result.getPoiList().get(0).getAddress());*/

            //    showAddress = addressText;
/*                Log.e("address","地址详细细信息"+result.getAddressDetail().district);
                Toast.makeText(context,"地址："+result.getSematicDescription(), Toast.LENGTH_SHORT).show();*/
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
    }

    public void GeoCoderDestroy() {
        // 释放地理编码检索实例
        if(geoCoder != null) {
            geoCoder.destroy();
        }
    }

    private AddressListner addressListner;

    public void setAddressListner(AddressListner addressListner) {
        this.addressListner = addressListner;
    }

    public interface AddressListner {
        void getGetGeoCoderResult(GetGeoCoderResult getGeoCoderResult);
    }
}
