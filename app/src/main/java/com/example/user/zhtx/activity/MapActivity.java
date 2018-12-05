package com.example.user.zhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.UI.EaseUI.DemoApplication;
import com.example.user.zhtx.R;
import com.example.user.zhtx.fragment.FriendsPage;
import com.example.user.zhtx.fragment.GroupMapPage;
import com.example.user.zhtx.fragment.MapPage;
import com.example.user.zhtx.fragment.MessagePage;
import com.example.user.zhtx.fragment.SettingPage;
import com.example.user.zhtx.pojo.Friend;
import com.example.user.zhtx.pojo.FriendsGPS;
import com.example.user.zhtx.service.UpdateGpsService;
import com.example.user.zhtx.tools.FriendsGPSList;
import com.example.user.zhtx.tools.FriendsList;
import com.example.user.zhtx.tools.GroupMemberList;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity{
    private LinearLayout activity_main_page_ll_map;
    private GroupMapPage groupMapPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        initView();

        initGroupMap();
    }

    private void initView() {
        activity_main_page_ll_map = (LinearLayout) findViewById(R.id.activity_main_page_ll_map);
    }

    private void initGroupMap() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

//        if (groupMapPage.isAdded()) {
//            transaction.remove(groupMapPage);
//        }

        if (groupMapPage == null) {
            groupMapPage = new GroupMapPage(activity_main_page_ll_map);
            groupMapPage.Destroy();
            transaction.add(R.id.activity_main_page_fragment, groupMapPage);
            transaction.show(groupMapPage);
        } else {
            transaction.show(groupMapPage);
        }
        transaction.commit();
    }
}
