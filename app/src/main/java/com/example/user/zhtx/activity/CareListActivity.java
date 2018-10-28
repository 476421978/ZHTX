package com.example.user.zhtx.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import com.example.user.zhtx.R;
import com.example.user.zhtx.adapter.CareListAdapter;



public class CareListActivity extends AppCompatActivity implements View.OnClickListener {

    private int[] pic = {R.drawable.head,R.drawable.touxiang,R.drawable.head,R.drawable.touxiang,R.drawable.touxiang,R.drawable.touxiang,R.drawable.touxiang,R.drawable.touxiang,R.drawable.touxiang};
    private String[] names= {"zdz44","李达波","黄立根","1","2","22","22","2","2","2"};
    private ListView listView;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_list);

        listView=(ListView)findViewById(R.id.activity_care_list_listview);
        CareListAdapter adapter  = new CareListAdapter(this,pic,names);
        listView.setAdapter(adapter);

        iv_back = (ImageView)findViewById(R.id.activity_care_list_iv_back);
        iv_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
