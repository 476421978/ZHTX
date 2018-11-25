package com.example.user.zhtx;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.zhtx.activity.FindPasswordActivity;
import com.example.user.zhtx.activity.MainPageActivity;
import com.example.user.zhtx.activity.RegisterActivity;
import com.example.user.zhtx.internet.CheckNetwork;
import com.example.user.zhtx.pojo.MessageInfo;
import com.example.user.zhtx.pojo.User;
import com.example.user.zhtx.tools.Address;
import com.example.user.zhtx.tools.PerssionControl;
import com.example.user.zhtx.tools.SharedPreferencesControl;
import com.example.user.zhtx.tools.ShowToast;
import com.example.user.zhtx.tools.SingleErrDiaog;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;

import java.io.IOException;

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
    private final static int LOGIN_SUCCESS =1;
    private final static int LOGIN_FAIL = 2;
    // 弹出框
    private ProgressDialog mDialog;



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
            }
        }
    }

    private void checkLogin(final String phone,final String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                FormBody formBody = new FormBody.Builder()
                        .add("phonenum",phone)
                        .add("password",password)
                        .build();

                final Request request = new Request.Builder()
                    .url(Address.longin)
                    .post(formBody)
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

                        Log.i("result",result);

                        Gson gson = new Gson();
                        MessageInfo m = gson.fromJson(result,MessageInfo.class);

                        Log.i("login",result);
                        if ("true".equals(m.getSuccess())){
                            Message message1 = new Message();
                            message1.what=LOGIN_SUCCESS;
                            handler.sendMessage(message1);
                        }else{
                            Message message2 = new Message();
                            message2.what=LOGIN_FAIL;
                            message2.obj = m.getMessage();
                            handler.sendMessage(message2);
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
                getUserInfo();


                //志鹏验证成功 轮到我了
                signIn();

            }
            else {
                SingleErrDiaog.show(LoginActivity.this,"登录失败",msg.obj+"");
            }
        }
    };


    /**
     * 登录方法
     */
    private void signIn() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("正在登陆，请稍后...");
        mDialog.show();
        String username = ed_phoneNum.getText().toString().trim();
        String password = ed_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "用户名和密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        EMClient.getInstance().login(username, password, new EMCallBack() {
            /**
             * 登陆成功的回调
             */
            @Override public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        mDialog.dismiss();
                        // 加载所有会话到内存
                        EMClient.getInstance().chatManager().loadAllConversations();
                        // 加载所有群组到内存，如果使用了群组的话
                        EMClient.getInstance().groupManager().loadAllGroups();

/*                        SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
                        sharedPreferences.edit().putString("user",ed_phoneNum.getText().toString().trim()).commit();
                        sharedPreferences.edit().putString("pwd",ed_pwd.getText().toString().trim()).commit();
                        sharedPreferences.edit().putString("nick","昵称").commit();*/
                       /* sharedPreferences.edit().putString("url","").commit();*/
                        // 登录成功跳转界面
                        Intent intent = new Intent(LoginActivity.this,MainPageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            /**
             * 登陆错误的回调
             * @param i
             * @param s
             */
            @Override public void onError(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        mDialog.dismiss();
                        Log.d("lzan13", "登录失败 Error code:" + i + ", message:" + s);
                        /**
                         * 关于错误码可以参考官方api详细说明
                         * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                         */
                        switch (i) {
                            // 网络异常 2
                            case EMError.NETWORK_ERROR:
                                Toast.makeText(LoginActivity.this,
                                        "网络错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的用户名 101
                            case EMError.INVALID_USER_NAME:
                                Toast.makeText(LoginActivity.this,
                                        "无效的用户名 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的密码 102
                            case EMError.INVALID_PASSWORD:
                                Toast.makeText(LoginActivity.this,
                                        "无效的密码 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 用户认证失败，用户名或密码错误 202
                            case EMError.USER_AUTHENTICATION_FAILED:
                                Toast.makeText(LoginActivity.this,
                                        "用户认证失败，用户名或密码错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG)
                                        .show();
                                break;
                            // 用户不存在 204
                            case EMError.USER_NOT_FOUND:
                                Toast.makeText(LoginActivity.this,
                                        "用户不存在 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无法访问到服务器 300
                            case EMError.SERVER_NOT_REACHABLE:
                                Toast.makeText(LoginActivity.this,
                                        "无法访问到服务器 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 等待服务器响应超时 301
                            case EMError.SERVER_TIMEOUT:
                                Toast.makeText(LoginActivity.this,
                                        "等待服务器响应超时 code: " + i + ", message:" + s, Toast.LENGTH_LONG)
                                        .show();
                                break;
                            // 服务器繁忙 302
                            case EMError.SERVER_BUSY:
                                Toast.makeText(LoginActivity.this,
                                        "服务器繁忙 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 未知 Server 异常 303 一般断网会出现这个错误
                            case EMError.SERVER_UNKNOWN_ERROR:
                                Toast.makeText(LoginActivity.this,
                                        "未知的服务器异常 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(LoginActivity.this,
                                        "ml_sign_in_failed code: " + i + ", message:" + s,
                                        Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
            }

            @Override public void onProgress(int i, String s) {

            }
        });
    }

    private void getUserInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                FormBody body = new FormBody.Builder()
                    .add("phonenum",ed_phoneNum.getText().toString())
                    .build();

                Request request = new Request.Builder()
                    .url(Address.getUser)
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

                        Gson gson = new Gson();
                        User user = gson.fromJson(result,User.class);
                        SharedPreferencesControl control = new SharedPreferencesControl(LoginActivity.this);
                        control.saveUser(user);
                        User user2 = control.getUser();

                    }
                });
            }
        }).start();
    }
}
