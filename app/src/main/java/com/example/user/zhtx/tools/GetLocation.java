package com.example.user.zhtx.tools;

import com.baidu.mapapi.model.LatLng;

public class GetLocation {

    private LatLng myLL;
    private double latitude;
    private double longitude;

    private static GetLocation getLocation;

    public static GetLocation newInstance() {
        if(getLocation == null) {
            getLocation = new GetLocation();
        }
        return getLocation;
    }

    public LatLng getMyLL() {
        return myLL;
    }

    public void setMyLLs(LatLng myLL) {
        this.latitude = myLL.latitude;
        this.longitude = myLL.longitude;
        this.myLL = myLL;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
