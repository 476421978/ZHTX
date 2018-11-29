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


public class CareListActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_list);

        ArrayList<FriendsGPS> list = new FriendsGPSList(CareListActivity.this).getcare();
        listView=(ListView)findViewById(R.id.activity_care_list_listview);
        CareListAdapter adapter  = new CareListAdapter(CareListActivity.this,list);
        listView.setAdapter(adapter);

        iv_back = (ImageView)findViewById(R.id.activity_care_list_iv_back);
        iv_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
