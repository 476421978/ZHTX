package com.example.user.zhtx.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.user.zhtx.R;
import com.example.user.zhtx.tools.ShowToast;

public class ChangePasswordByVeri extends AppCompatActivity implements View.OnClickListener {
    private EditText ed_pwd,ed_confirmPwd;
    private Button btn_getVerification,btn_change;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_by_veri);

        initView();
    }

    private void initView(){
        ed_confirmPwd = (EditText)findViewById(R.id.activity_change_passwordByVeri_ed_confirmPwd);
        ed_pwd = (EditText)findViewById(R.id.activity_change_passwordByVeri_ed_pwd);
        btn_getVerification = (Button) findViewById(R.id.activity_change_passwordByVeri_btn_getVerification);
        btn_change = (Button)findViewById(R.id.activity_change_passwordByVeri_btn_change);

        btn_change.setOnClickListener(this);
        btn_getVerification.setOnClickListener(this);

        iv_back = (ImageView)findViewById(R.id.activity_change_passwordByVeri_iv_back);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_change_passwordByVeri_btn_getVerification:
                ShowToast.show(ChangePasswordByVeri.this,"获取验证码");
                break;
            case R.id.activity_change_passwordByVeri_btn_change:
                ShowToast.show(ChangePasswordByVeri.this,"密码修改成功");
                Intent intent = new Intent(ChangePasswordByVeri.this,SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_change_passwordByVeri_iv_back:
                finish();
                break;
        }
    }
}
