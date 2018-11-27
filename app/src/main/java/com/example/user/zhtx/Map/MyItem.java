package com.example.user.zhtx.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
    private Drawable drawable;
    private CircleImageView circleImageView;
    private Bitmap bitmap;

    public MyItem(Context context, LatLng latLng, Drawable drawable) {
        this.context = context;
        this.latLng = latLng;
        this.drawable = drawable;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        view = View.inflate(context, R.layout.head_mark, null);
    //    drawable = new BitmapDrawable(bitmap);

        circleImageView = (CircleImageView) view.findViewById(R.id.shape);
        circleImageView.setImageDrawable(drawable);

        return BitmapDescriptorFactory
                .fromView(view);
    }


}
