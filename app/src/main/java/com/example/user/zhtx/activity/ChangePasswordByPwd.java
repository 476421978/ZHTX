package com.example.user.zhtx.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.zhtx.R;

public class ChangePasswordByPwd extends AppCompatActivity implements View.OnClickListener {
    private EditText ed_oldPwd,ed_newPwd,ed_confirmPwd;
    private TextView tv_toVerification;
    private Button btn_change;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_by_pwd);
        initView();
    }

    private void initView(){
        ed_confirmPwd = (EditText)findViewById(R.id.activity_change_passwordByPwd_ed_confirmPwd);
        ed_oldPwd = (EditText)findViewById(R.id.activity_change_passwordByPwd_ed_oldPwd);
        ed_newPwd = (EditText)findViewById(R.id.activity_change_passwordByPwd_ed_newPwd);

        tv_toVerification = (TextView)findViewById(R.id.activity_change_passwordByPwd_tv_toVerification);
        tv_toVerification.setOnClickListener(this);


        btn_change = (Button)findViewById(R.id.activity_change_passwordByPwd_btn_changePwd);
        btn_change.setOnClickListener(this);

        iv_back = (ImageView)findViewById(R.id.activity_change_passwordByPwd_iv_back);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_change_passwordByPwd_tv_toVerification:
                Intent intent1 = new Intent(ChangePasswordByPwd.this,ChangePasswordByVeri.class);
                startActivity(intent1);
                break;
            case R.id.activity_change_passwordByPwd_btn_changePwd:
                Intent intent2 = new Intent(ChangePasswordByPwd.this,SettingActivity.class);
                startActivity(intent2);
                break;
            case R.id.activity_change_passwordByPwd_iv_back:
                finish();
                break;

        }
    }

    private void checkPwd(){
        String newPwd = ed_newPwd.getText().toString();
        String comfirePwd = ed_confirmPwd.getText().toString();

        
    }
}
