package com.example.user.zhtx.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import com.example.user.zhtx.R;
import com.example.user.zhtx.adapter.CareListAdapter;
import com.example.user.zhtx.pojo.FriendsGPS;
import com.example.user.zhtx.tools.FriendsGPSList;
import com.example.user.zhtx.tools.GetGPS;

import java.util.ArrayList;


public class CareListActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_list);

        listView=(ListView)findViewById(R.id.activity_care_list_listview);
        fulshView();

        iv_back = (ImageView)findViewById(R.id.activity_care_list_iv_back);
        iv_back.setOnClickListener(this);

    }

    public void fulshView(){
        ArrayList<FriendsGPS> list = new FriendsGPSList(CareListActivity.this).getcare();
        if (list!=null||list.size()>0){
            CareListAdapter adapter  = new CareListAdapter(CareListActivity.this,list);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        fulshView();
    }

    @Override
    public void onPause(){
        super.onPause();
        GetGPS getGPS = new GetGPS(getBaseContext());
        getGPS.getFriendsGps();
    }



    @Override
    public void onClick(View view) {
        Intent intent = new Intent(CareListActivity.this,FriendsManagementActivity.class);
        intent.putExtra("result","true");
        setResult(200,intent);
        finish();
    }
}
