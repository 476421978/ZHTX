package com.example.user.zhtx.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.zhtx.R;
import com.example.user.zhtx.tools.ShowToast;

public class FindPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_findPwd,btn_getVerifiacation;
    private EditText ed_phoneName,ed_verification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        initView();
    }

    private void initView(){
        btn_findPwd = (Button)findViewById(R.id.activity_find_password_btn_findPwd);
        btn_getVerifiacation = (Button)findViewById(R.id.activity_find_password_btn_getVerification);
        btn_findPwd.setOnClickListener(this);
        btn_getVerifiacation.setOnClickListener(this);

        ed_phoneName = (EditText)findViewById(R.id.activity_find_password_ed_phoneNumber);
        ed_verification = (EditText)findViewById(R.id.activity_find_password_ed_verification);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_find_password_btn_findPwd:
                ShowToast.show(FindPasswordActivity.this,"找回密码");
                Intent intent = new Intent(FindPasswordActivity.this,ResetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_find_password_btn_getVerification:
                ShowToast.show(FindPasswordActivity.this,"获取验证码");
                break;

        }
    }
}
