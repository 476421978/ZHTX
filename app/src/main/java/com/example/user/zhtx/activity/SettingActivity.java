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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.zhtx.LoginActivity;
import com.example.user.zhtx.R;
import com.example.user.zhtx.pojo.User;
import com.example.user.zhtx.tools.SharedPreferencesControl;
import com.example.user.zhtx.tools.ShowToast;
import com.loopj.android.image.SmartImageView;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout lin_changePassword,lin_seeArea,lin_selfInfo;
    private Button btn_loginOut;
    private TextView tv_phoneNumber,tv_name;
    private SmartImageView siv_head;
    private ImageView iv_back;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView(){
        lin_selfInfo = (LinearLayout)findViewById(R.id.activity_setting_lin_selfInfo);
        lin_selfInfo.setOnClickListener(this);
        lin_seeArea = (LinearLayout)findViewById(R.id.activity_setting_lin_seeArea);
        lin_seeArea.setOnClickListener(this);
        lin_changePassword = (LinearLayout)findViewById(R.id.activity_setting_lin_changePassword);
        lin_changePassword.setOnClickListener(this);

        btn_loginOut= (Button)findViewById(R.id.activity_setting_btn_loginOut);
        btn_loginOut.setOnClickListener(this);


        iv_back = (ImageView) findViewById(R.id.activity_setting_iv_back);
        iv_back.setOnClickListener(this);

        SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
        tv_name = (TextView)findViewById(R.id.activity_setting_tv_name);
        tv_name.setText(sp.getString("name","未找到"));
        tv_phoneNumber = (TextView) findViewById(R.id.activity_setting_tv_phoneNumber);
        tv_phoneNumber.setText("手机号："+sp.getString("phonenum","未找到"));

        siv_head = (SmartImageView)findViewById(R.id.activity_setting_siv_head);
        siv_head.setBackground(null);
        siv_head.setImageUrl(sp.getString("picPath",""),R.mipmap.ic_launcher);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_setting_lin_selfInfo:
                ShowToast.show(SettingActivity.this,"个人信息");
                Intent intent1 = new Intent(SettingActivity.this,SelfInfoActivity.class);
                startActivity(intent1);
                break;
            case R.id.activity_setting_lin_seeArea:
                ShowToast.show(SettingActivity.this,"可见范围");
                Intent intent2 = new Intent(SettingActivity.this,SeeAreaActivity.class);
                startActivity(intent2);
                break;
            case R.id.activity_setting_lin_changePassword:
                ShowToast.show(SettingActivity.this,"修改密码");
                Intent intent3 = new Intent(SettingActivity.this,ChangePasswordByPwd.class);
                startActivity(intent3);
                break;
            case R.id.activity_setting_btn_loginOut:
                ShowToast.show(SettingActivity.this,"注销");
                SharedPreferencesControl control = new SharedPreferencesControl(SettingActivity.this);
                control.loginOut();
                Intent intent4 = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent4);
                break;
            case R.id.activity_setting_iv_back:
                finish();
                break;
        }
    }
}

