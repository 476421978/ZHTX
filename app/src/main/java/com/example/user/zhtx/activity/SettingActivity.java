package com.example.user.zhtx.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.zhtx.R;
import com.example.user.zhtx.tools.ShowToast;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout lin_changePassword,lin_seeArea,lin_selfInfo;
    private Button btn_loginOut;
    private TextView tv_phoneNumber,tv_name;
    private ImageView iv_headPic;
    private ImageView iv_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView(){
        lin_selfInfo = (LinearLayout)findViewById(R.id.activity_setting_lin_selfInfo);
        lin_selfInfo.setOnClickListener(this);
        lin_seeArea = (LinearLayout)findViewById(R.id.activity_setting_lin_seeArea);
        lin_seeArea.setOnClickListener(this);
        lin_changePassword = (LinearLayout)findViewById(R.id.activity_setting_lin_changePassword);
        lin_changePassword.setOnClickListener(this);

        btn_loginOut= (Button)findViewById(R.id.activity_setting_btn_loginOut);
        btn_loginOut.setOnClickListener(this);

        tv_name = (TextView)findViewById(R.id.activity_setting_tv_name);
        tv_phoneNumber = (TextView) findViewById(R.id.activity_setting_tv_phoneNumber);

        iv_back = (ImageView) findViewById(R.id.activity_setting_iv_back);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_setting_lin_selfInfo:
                ShowToast.show(SettingActivity.this,"个人信息");
                break;
            case R.id.activity_setting_lin_seeArea:
                ShowToast.show(SettingActivity.this,"可见范围");
                Intent intent2 = new Intent(SettingActivity.this,SeeAreaActivity.class);
                startActivity(intent2);
                break;
            case R.id.activity_setting_lin_changePassword:
                ShowToast.show(SettingActivity.this,"修改密码");
                break;
            case R.id.activity_setting_btn_loginOut:
                ShowToast.show(SettingActivity.this,"注销");
                break;
            case R.id.activity_setting_iv_back:
                Intent intent5 = new Intent(SettingActivity.this,MainPageActivity.class);
                startActivity(intent5);
                break;

        }
    }
}

