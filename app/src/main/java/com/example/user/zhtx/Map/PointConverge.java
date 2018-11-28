package com.example.user.zhtx.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.clusterutil.clustering.Cluster;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.model.LatLng;
import com.example.user.zhtx.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PointConverge {

    private TextView name_txt,phone_text;
    private List<MyItem> items;
    private static PointConverge pointConverge;
    private ClusterManager<MyItem> clusterManager;

    private Context context;
    private BaiduMap baiduMap;
    private int count = 0;
    private Bitmap bitmap;

    public PointConverge(Context context, BaiduMap baiduMap,TextView name_txt, TextView phone_text) {
        this.context = context;
        this.baiduMap = baiduMap;
        this.name_txt =name_txt;
        this.phone_text = phone_text;
    }

    public static PointConverge newInstance(Context context, BaiduMap baiduMap, TextView name_txt, TextView phone_text) {
        if(pointConverge == null) {
            pointConverge = new PointConverge(context, baiduMap,name_txt,phone_text);
        }
        return new PointConverge(context, baiduMap,name_txt,phone_text);
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

                if(item.getName()!=null && item.getPhonenum()!=null) {
                    name_txt.setText(item.getName());
                    phone_text.setText(item.getPhonenum());
                }
                Toast.makeText(context, "点击单个Item"+String.valueOf(item.getName()), Toast.LENGTH_SHORT).show();
                System.out.println("点击单个Item"+String.valueOf(item.getName()));
                return false;
            }
        });
    }

    public void setListener() {
        baiduMap.setOnMapStatusChangeListener(clusterManager);
        baiduMap.setOnMarkerClickListener(clusterManager);
    }
}
