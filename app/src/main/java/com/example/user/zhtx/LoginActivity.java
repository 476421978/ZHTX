package com.example.user.zhtx;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.zhtx.activity.RegisterActivity;
import com.example.user.zhtx.tools.PerssionControl;
import com.example.user.zhtx.tools.ShowToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_register,tv_resetPwd;
    private EditText ed_phoneName,ed_pwd;
    private Button btn_login;

    private String[] permissions = {Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        PerssionControl control = PerssionControl.getInstance();
        control.getPermission(LoginActivity.this,permissions);


        initView();


    }

    private void initView(){
        tv_register = (TextView) findViewById(R.id.activity_login_tv_register);
        tv_resetPwd = (TextView) findViewById(R.id.activity_login_tv_resetPwd);
        tv_register.setOnClickListener(this);
        tv_resetPwd.setOnClickListener(this);

        btn_login = (Button) findViewById(R.id.activity_login_btn_login);
        btn_login.setOnClickListener(this);

        ed_phoneName = (EditText) findViewById(R.id.activity_login_ed_phoneNumber);
        ed_pwd = (EditText) findViewById(R.id.activity_login_ed_pwd);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_login_btn_login:
                ShowToast.show(LoginActivity.this,"denglu");
                break;
            case R.id.activity_login_tv_register:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_login_tv_resetPwd:
                ShowToast.show(LoginActivity.this,"找回密码");
                break;
        }
    }
}
