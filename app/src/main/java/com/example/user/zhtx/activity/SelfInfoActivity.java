package com.example.user.zhtx.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.zhtx.R;
import com.example.user.zhtx.tools.SharedPreferencesControl;
import com.loopj.android.image.SmartImageView;

public class SelfInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_bg,iv_back;
    private SmartImageView siv_head;
    private TextView tv_name,tv_phone,tv_address,tv_birthday;
    private Button btn_modify;
    private int gender;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_info);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView(){
        SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);

        iv_bg = (ImageView)findViewById(R.id.activity_self_info_iv_bg);
        siv_head = (SmartImageView)findViewById(R.id.activity_self_info_siv_head);
        siv_head.setBackground(null);
        siv_head.setImageUrl(sp.getString("picPath",""),R.mipmap.ic_launcher);

        iv_back = (ImageView)findViewById(R.id.activity_self_info_iv_back);
        iv_back.setOnClickListener(this);

        tv_address = (TextView)findViewById(R.id.activity_self_info_tv_address);
        tv_address.setText(sp.getString("address","未找到"));

        tv_name = (TextView)findViewById(R.id.activity_self_info_tv_name);
        tv_name.setText(sp.getString("name","未找到"));
        tv_phone = (TextView)findViewById(R.id.activity_self_info_tv_phone);
        tv_phone.setText(sp.getString("phonenum","未找到"));
        tv_birthday = (TextView)findViewById(R.id.activity_self_info_tv_birthday);
        tv_birthday.setText(sp.getString("birthday","未找到"));

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
