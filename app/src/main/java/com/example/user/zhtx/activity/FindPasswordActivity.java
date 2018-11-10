package com.example.user.zhtx.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.zhtx.R;
import com.example.user.zhtx.tools.Address;
import com.example.user.zhtx.tools.ShowToast;
import com.example.user.zhtx.tools.SingleErrDiaog;
import com.mob.MobSDK;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FindPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_findPwd,btn_getVerification;
    private EditText ed_phone,ed_verification;
    private ImageView iv_back;

    private Message message;
    private TimerTask timerTask;
    private Timer timer;
    private int TIME = 60;                          //倒计时时间
    public String country="86";                     //国家区号，中国为86

    private static final int CODE_REPEAT = 1;       //重新发送
    private static final int CODE_ERROR = 2;        //验证码错误
    private static final int PHONE_ERROR = 3;       //电话号码不存在
    private static final int ERROR =4;              //未知错误

    private final String appkty = "286309d7a4904";
    private final String appSecret = "bae047d3b2f375d802dfe3fb1d778efa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        MobSDK.init(FindPasswordActivity.this,appkty,appSecret);
        SMSSDK.registerEventHandler(eh);
        initView();
    }

    EventHandler eh=new EventHandler(){
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    handleResult();
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){       //获取验证码成功
                    Log.i("data","获取验证码成功");
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//如果你调用了获取国家区号类表会在这里回调
                    //返回支持发送验证码的国家列表
                }
            }else{//错误等在这里（包括验证失败）
                //错误码请参照http://wiki.mob.com/android-api-错误码参考/这里我就不再继续写了
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        message.what = CODE_ERROR;
                        handler.sendMessage(message);
                    }
                }).start();

            }
        }
    };

    private void initView(){
        btn_findPwd = (Button)findViewById(R.id.activity_find_password_btn_findPwd);
        btn_getVerification = (Button)findViewById(R.id.activity_find_password_btn_getVerification);
        btn_findPwd.setOnClickListener(this);
        btn_getVerification.setOnClickListener(this);

        ed_phone = (EditText)findViewById(R.id.activity_find_password_ed_phoneNumber);
        ed_verification = (EditText)findViewById(R.id.activity_find_password_ed_verification);

        iv_back = (ImageView)findViewById(R.id.activity_find_password_iv_back);
        iv_back.setOnClickListener(this);

        message = new Message();
    }

    @Override
    public void onClick(View view) {
        String verification = ed_verification.getText().toString();
        String phone =  ed_phone.getText().toString().trim().replace("/s","");

        switch (view.getId()){
            case R.id.activity_find_password_btn_findPwd:
                ShowToast.show(FindPasswordActivity.this,"找回密码");
                if (checkInfo()){
                    if (!TextUtils.isEmpty(ed_verification.getText().toString())){
                        SMSSDK.submitVerificationCode(country,phone,verification);
                    }else {
                        SingleErrDiaog.show(FindPasswordActivity.this,"注册失败","验证码不能为空");
                    }
                }
            //    Intent intent = new Intent(FindPasswordActivity.this,ResetPasswordActivity.class);
           //     startActivity(intent);
                break;
            case R.id.activity_find_password_btn_getVerification:
                ShowToast.show(FindPasswordActivity.this,"获取验证码");
                //15014635129
                if (phone.length() == 11){
                    //定义需要匹配的正则表达式的规则
                    String REGEX_MOBILE_SIMPLE =  "[1][358]\\d{9}";
                    //把正则表达式的规则编译成模板
                    Pattern pattern = Pattern.compile(REGEX_MOBILE_SIMPLE);
                    //把需要匹配的字符给模板匹配，获得匹配器
                    Matcher matcher = pattern.matcher(phone);
                    // 通过匹配器查找是否有该字符，不可重复调用重复调用matcher.find()
                    if (matcher.find()) {//匹配手机号是否存在
                        alterWarning(phone);
                    } else {
                        ShowToast.show(FindPasswordActivity.this,"手机号码不存在");
                    }
                }
                break;

            case R.id.activity_find_password_iv_back:
                finish();

                break;
        }
    }

    private boolean checkInfo(){
        if (TextUtils.isEmpty(ed_phone.getText().toString())){
            SingleErrDiaog.show(FindPasswordActivity.this,"错误","电话号码为空");
            return false;
        }else if (ed_phone.getText().toString().length() != 11){
            SingleErrDiaog.show(FindPasswordActivity.this,"错误","电话号码格式错误");
            return false;
        }
        return true;
    }

    private void handleResult(){
        final String phone = ed_phone.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                FormBody body = new FormBody.Builder()
                    .add("phone",phone)
                    .build();

                final Request request = new Request.Builder()
                    .url(Address.CheckPhone)
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
                        if (result.equals("手机可注册")){
                            Intent intent = new Intent(FindPasswordActivity.this,ResetPasswordActivity.class);
                            intent.putExtra("phone",ed_phone.getText().toString());
                            startActivity(intent);
                        }else if ("电话不存在".equals(result)){
                            message.what =  PHONE_ERROR;
                            handler.sendMessage(message);
                        }

                    }
                });

            }
        }).start();

    }

    private void alterWarning(final String phone){
        new AlertDialog.Builder(FindPasswordActivity.this)
                .setTitle("发送验证码")
                .setMessage("将验证码发送至"+phone)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        SMSSDK.getVerificationCode(country,phone);
                        btn_getVerification.setEnabled(false);
                        timer = new Timer();
                        timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                handler.sendEmptyMessage(TIME--);
                            }
                        };
                        timer.schedule(timerTask,0,1000);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ShowToast.show(FindPasswordActivity.this,"已取消");
                    }
                })
                .show();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CODE_REPEAT){
                btn_getVerification.setEnabled(true);
                timer.cancel();
                timerTask.cancel();
                TIME=60;
                btn_getVerification.setText("重新发送验证码");
            }if (msg.what == CODE_ERROR){
                handleResult();
            }
        }
    };

    //销毁短信注册
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销回调接口registerEventHandler必须和unregisterEventHandler配套使用，否则可能造成内存泄漏。
        SMSSDK.unregisterEventHandler(eh);
    }

}
