package com.example.user.zhtx.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.zhtx.R;

public class SelfInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_bg,iv_head,iv_back;
    private TextView tv_name,tv_phone,tv_address,tv_birthday;
    private Button btn_modify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_info);
        initView();
    }

    private void initView(){
        iv_bg = (ImageView)findViewById(R.id.activity_self_info_iv_bg);
        iv_head = (ImageView)findViewById(R.id.activity_self_info_iv_head);
        iv_back = (ImageView)findViewById(R.id.activity_self_info_iv_back);
        iv_back.setOnClickListener(this);

        tv_address = (TextView)findViewById(R.id.activity_self_info_tv_address);
        tv_name = (TextView)findViewById(R.id.activity_self_info_tv_name);
        tv_phone = (TextView)findViewById(R.id.activity_self_info_tv_phone);
        tv_birthday = (TextView)findViewById(R.id.activity_self_info_tv_birthday);

        btn_modify = (Button)findViewById(R.id.activity_self_info_btn_modify);
        btn_modify.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_self_info_iv_back:
                finish();
                break;
            case R.id.activity_self_info_btn_modify:
                Intent intent = new Intent(SelfInfoActivity.this,ModifyInfoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
