package com.example.user.zhtx.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.user.zhtx.R;
import com.example.user.zhtx.adapter.CareListAdapter;
import com.example.user.zhtx.pojo.FriendsGPS;
import com.example.user.zhtx.tools.FriendsGPSList;
import com.example.user.zhtx.tools.GetGPS;
import com.example.user.zhtx.tools.ShowToast;

import java.util.ArrayList;

public class FriendsManagementActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;
    private ImageView iv_back;
    private Button btn_myCare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_management);
        fulshView();

        iv_back = (ImageView)findViewById(R.id.activity_friends_management_iv_back);
        iv_back.setOnClickListener(this);

        btn_myCare = (Button)findViewById(R.id.activity_friends_management_btn_myCare);
        btn_myCare.setOnClickListener(this);

    }

    private void fulshView(){
        listView = (ListView)findViewById(R.id.activity_friends_management_listview);
        ArrayList<FriendsGPS> list = new FriendsGPSList(FriendsManagementActivity.this).getAll();

        if (list!=null || list.size()>0){
            CareListAdapter careListAdapter = new CareListAdapter(FriendsManagementActivity.this,list);
            careListAdapter.notifyDataSetChanged();
            listView.setAdapter(careListAdapter);
            careListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_friends_management_btn_myCare:
                Intent intent = new Intent(FriendsManagementActivity.this,CareListActivity.class);
                startActivityForResult(intent,1);
                break;
            case  R.id.activity_friends_management_iv_back:
                finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (data!=null){
            fulshView();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        GetGPS getGPS = new GetGPS(getBaseContext());
        getGPS.getFriendsGps();
    }

    @Override
    public void onResume(){
        super.onResume();
        fulshView();
    }

    @Override
    public void onStart(){
        super.onStart();
        fulshView();
    }
}
