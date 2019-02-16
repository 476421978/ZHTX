package com.example.user.zhtx.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.example.user.zhtx.R;

public class MyItem implements ClusterItem {

    private String phonenum;
    private String name;
    private Context context;
    private LatLng latLng;
    private View view;
    private Drawable drawable;
    private CircleImageView circleImageView;
    private Bitmap bitmap;


    public MyItem(Context context, LatLng latLng, Bitmap bitmap,String name,String phonenum) {
        this.context = context;
        this.latLng = latLng;
        this.bitmap = bitmap;
        this.name = name;
        this.phonenum = phonenum;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        view = View.inflate(context, R.layout.head_mark, null);
        if(bitmap!=null) {
            drawable = new BitmapDrawable(bitmap);


        circleImageView = (CircleImageView) view.findViewById(R.id.shape);
        circleImageView.setImageDrawable(drawable);
        }

        return BitmapDescriptorFactory
                .fromView(view);
    }
    public String getName(){

            return name;
    }
    public String getPhonenum(){
        return phonenum;
    }
}
