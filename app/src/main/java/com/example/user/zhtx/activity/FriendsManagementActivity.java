package com.example.user.zhtx.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.user.zhtx.R;
import com.example.user.zhtx.adapter.CareListAdapter;
import com.example.user.zhtx.pojo.FriendsGPS;
import com.example.user.zhtx.tools.FriendsGPSList;

import java.util.ArrayList;

public class FriendsManagementActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_management);

        listView = (ListView)findViewById(R.id.activity_friends_management_listview);
        ArrayList<FriendsGPS> list = new FriendsGPSList(FriendsManagementActivity.this).getAll();
        CareListAdapter careListAdapter = new CareListAdapter(FriendsManagementActivity.this,list);
        listView.setAdapter(careListAdapter);

        iv_back = (ImageView)findViewById(R.id.activity_friends_management_iv_back);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
