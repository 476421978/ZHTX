package com.example.user.zhtx.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.baidu.mapapi.clusterutil.clustering.Cluster;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class PointConverge {

    private List<MyItem> items;
    private static PointConverge pointConverge;
    private ClusterManager<MyItem> clusterManager;

    private Context context;
    private BaiduMap baiduMap;
    private int count = 0;

    public PointConverge(Context context, BaiduMap baiduMap) {
        this.context = context;
        this.baiduMap = baiduMap;

    }

    public static PointConverge newInstance(Context context, BaiduMap baiduMap) {
        if(pointConverge == null) {
            pointConverge = new PointConverge(context, baiduMap);
        }
        return new PointConverge(context, baiduMap);
    }

    public void clearMarkers() {
        clusterManager.clearItems();
        clusterManager.getMarkerCollection().clear();
        clusterManager.getClusterMarkerCollection().clear();
    }

    public void addMarkers(LatLng latLng, Drawable drawable) {
        items = new ArrayList<MyItem>();
        items.add(new MyItem(context, latLng, drawable));

        clusterManager.addItems(items);
        clusterManager.cluster();
    }

    public void pointConverge() {
        clusterManager = new ClusterManager<MyItem>(context, baiduMap);

        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {

            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                Toast.makeText(context,"有" + cluster.getSize() + "个点", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {

            @Override
            public boolean onClusterItemClick(MyItem item) {
                Toast.makeText(context, "点击单个Item", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    public void setListener() {
        baiduMap.setOnMapStatusChangeListener(clusterManager);
        baiduMap.setOnMarkerClickListener(clusterManager);
    }
}
