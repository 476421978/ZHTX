package com.example.user.zhtx.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.TextView;
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

    public PointConverge(Context context, BaiduMap baiduMap) {
        this.context = context;
        this.baiduMap = baiduMap;
        clusterManager = new ClusterManager<MyItem>(context, baiduMap);
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

    public void addMarkers(LatLng latLng, Bitmap bitmap, String name,String phonenum) {
        items = new ArrayList<MyItem>();
        items.add(new MyItem(context, latLng, bitmap,name,phonenum));

        clusterManager.addItems(items);
        clusterManager.cluster();
    }

    public void pointConverge(final TextView name_txt, final TextView phone_text) {
        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {

            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                return false;
            }
        });

        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {

            @Override
            public boolean onClusterItemClick(MyItem item) {

                if(item.getName()!=null && item.getPhonenum()!=null) {
                    name_txt.setText(item.getName());
                    phone_text.setText(item.getPhonenum());
                }
                return false;
            }
        });
    }

    public void setListener() {
        baiduMap.setOnMapStatusChangeListener(clusterManager);
        baiduMap.setOnMarkerClickListener(clusterManager);
    }
}
