package com.example.user.zhtx.Map;

import android.content.Context;
import android.view.View;

import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.example.user.zhtx.R;

public class MyItem implements ClusterItem {

    private Context context;
    private LatLng latLng;
    private View view;

    public MyItem(Context context, LatLng latLng) {
        this.context = context;
        this.latLng = latLng;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        view = View.inflate(context, R.layout.head_mark, null);

        return BitmapDescriptorFactory
                .fromView(view);
    }
}
