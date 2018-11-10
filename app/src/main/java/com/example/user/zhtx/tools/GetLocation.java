package com.example.user.zhtx.tools;

import com.baidu.mapapi.model.LatLng;

public class GetLocation {

    public double latitude;
    public double longitude;

    public static  GetLocation instance = new GetLocation();
    public static   GetLocation getInstance(){return instance; }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(LatLng myLL) {
        this.latitude = myLL.latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(LatLng myLL) {
        this.longitude = myLL.longitude;
    }
}
