package com.example.user.zhtx.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.zhtx.R;
import com.example.user.zhtx.pojo.Friend;
import com.example.user.zhtx.pojo.FriendsGPS;
import com.example.user.zhtx.tools.Address;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;

/**
 * Created by user on 2018/11/3.
 */
public class CareListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FriendsGPS> list;
    public CareListAdapter(Context context,ArrayList<FriendsGPS> list){
        this.context=context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context,R.layout.listview_setting_care,null);
        SmartImageView siv = (SmartImageView)v.findViewById(R.id.listview_setting_care_siv_head);
        siv.setImageUrl(Address.title+list.get(i).getPhonenum()+".jpeg");


        TextView textView = (TextView)v.findViewById(R.id.listview_setting_care_tv_name);
        textView.setText(list.get(i).getName());
        Button button = (Button)v.findViewById(R.id.listview_setting_care_btn_quxiao);

        return v;
    }

}