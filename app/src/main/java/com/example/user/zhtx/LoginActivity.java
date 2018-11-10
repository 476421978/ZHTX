package com.example.user.zhtx;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.zhtx.activity.FindPasswordActivity;
import com.example.user.zhtx.activity.MainPageActivity;
import com.example.user.zhtx.activity.RegisterActivity;
import com.example.user.zhtx.internet.CheckNetwork;
import com.example.user.zhtx.tools.Address;
import com.example.user.zhtx.tools.PerssionControl;
import com.example.user.zhtx.tools.ShowToast;
import com.example.user.zhtx.tools.SingleErrDiaog;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class  LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_register,tv_resetPwd;
    private EditText ed_phoneNum,ed_pwd;
    private Button btn_login;
    private Message message;
    private final static int LOGIN_SUCCESS =1;
    private final static int LOGIN_FAIL = 2;

    private String[] permissions = {Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

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

        ed_phoneNum = (EditText) findViewById(R.id.activity_login_ed_phoneNumber);
        ed_pwd = (EditText) findViewById(R.id.activity_login_ed_pwd);

        message = new Message();
    }

    @Override
    public void onClick(View view) {
        if (CheckNetwork.NetworkState(LoginActivity.this)){
            switch (view.getId()){
                case R.id.activity_login_btn_login:
                    check();
                    break;
                case R.id.activity_login_tv_register:
                    Intent intent2 = new Intent(LoginActivity.this,RegisterActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.activity_login_tv_resetPwd:
                    ShowToast.show(LoginActivity.this,"找回密码");
                    Intent intent3 = new Intent(LoginActivity.this, FindPasswordActivity.class);
                    startActivity(intent3);
                    break;
            }
        }
    }

    private void check(){
        Log.i("login","已点击登陆");
        String phone = ed_phoneNum.getText().toString().trim();
        String password = ed_pwd.getText().toString().trim();
        if ((phone.length()==0) || (phone==null)){
            SingleErrDiaog.show(LoginActivity.this,"登陆失败","电话号码不能为空");
        }else {
            if ((password.length()==0) || (password==null)){
                SingleErrDiaog.show(LoginActivity.this,"登陆失败","密码不能为空");
            }else {
                checkLogin(phone,password);
                Intent intent1 = new Intent(LoginActivity.this, MainPageActivity.class);
                startActivity(intent1);
            }
        }
    }

    private void checkLogin(final String phone,final String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                FormBody formBody = new FormBody.Builder()
                        .add("phone",phone)
                        .add("password",password)
                        .build();

                final Request request = new Request.Builder()
                    .url(Address.Test)
                    .post(formBody)
                    .build();

                Log.i("login","已发送");

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        return;
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        Log.i("login",result);
                        if (result.equals("true")){
                            message.what=LOGIN_SUCCESS;
                            handler.sendMessage(message);
                        }else{
                            message.what=LOGIN_FAIL;
                            message.obj = result;
                        }
                    }
                });
            }
        }).start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == LOGIN_SUCCESS){
                Intent intent = new Intent(LoginActivity.this,MainPageActivity.class);
                startActivity(intent);
            }
            else {
                SingleErrDiaog.show(LoginActivity.this,"登录失败",message.obj+"");
            }
        }
    };

    private void getUserInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                FormBody body = new FormBody.Builder()
                    .add("phonenum",ed_phoneNum.getText().toString())
                    .build();

                Request request = new Request.Builder()
                    .url(Address.getInfo)
                    .post(body)
                    .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        return;
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();


                    }
                });
            }
        }).start();
    }


}
