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

import com.example.user.zhtx.R;
import com.example.user.zhtx.tools.ShowToast;
import com.example.user.zhtx.tools.SingleErrDiaog;
import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;


import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText ed_phone,ed_pwd,ed_confirmPwd,ed_verification;
    private Button btn_getVerification,btn_register;
    private ImageView iv_back;

    /*------------------验证码---------------------*/
    private TimerTask timerTask;
    private Timer timer;
    private int TIME = 60;                          //倒计时时间
    public String country="86";                     //国家区号，中国为86

    private static final int CODE_REPEAT = 1;       //重新发送
    private final String appkty = "286309d7a4904";
    private final String appSecret = "bae047d3b2f375d802dfe3fb1d778efa";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // 你在mob创建项目的appkey和app secret
        MobSDK.init(RegisterActivity.this,appkty,appSecret);
        SMSSDK.registerEventHandler(eh);
        initView();
    }

    EventHandler eh=new EventHandler(){
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                //    handleResult();
                    Log.i("test","发送答应");
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){       //获取验证码成功
                    Log.i("data","获取验证码成功");
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//如果你调用了获取国家区号类表会在这里回调
                    //返回支持发送验证码的国家列表
                }
            }else{//错误等在这里（包括验证失败）
                //错误码请参照http://wiki.mob.com/android-api-错误码参考/这里我就不再继续写了
                Log.i("data","验证码错误");

                SingleErrDiaog.show(RegisterActivity.this,"注册失败","验证码错误");
            }
        }
    };

    private void initView(){
        ed_phone = (EditText) findViewById(R.id.activity_register_ed_phoneNumber);
        ed_pwd = (EditText) findViewById(R.id.activity_register_ed_pwd);
        ed_confirmPwd = (EditText) findViewById(R.id.activity_register_ed_confirmPwd);
        ed_verification = (EditText) findViewById(R.id.activity_register_ed_verification);

        btn_register = (Button) findViewById(R.id.activity_register_btn_register);
        btn_getVerification = (Button) findViewById(R.id.activity_register_btn_getVerification);

        btn_getVerification.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        iv_back = (ImageView)findViewById(R.id.activity_register_iv_back);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String phone =  ed_phone.getText().toString().trim().replace("/s","");
        String pwd = ed_pwd.getText().toString();
        String confirmPwd = ed_confirmPwd.getText().toString();
        String verification = ed_verification.getText().toString().replaceAll("/s","");
   //     String code = ed_registerCode.getText().toString().replaceAll("/s","");

        switch (view.getId()){
            case R.id.activity_register_btn_register:
                ShowToast.show(RegisterActivity.this,"注册按钮点击");
                if (checkPhone(phone)){
                    if (checkPwd(pwd,confirmPwd)){
                        if (!TextUtils.isEmpty(verification)){
                            SMSSDK.submitVerificationCode(country,ed_phone.getText().toString().trim().replace("/s",""),verification);
                        }else {
                            SingleErrDiaog.show(RegisterActivity.this,"注册失败","验证码不能为空");
                        }
                    }
                }

                break;
            case R.id.activity_register_btn_getVerification:
                ShowToast.show(RegisterActivity.this,"获取验证码按钮点击");
                if (checkPhone(phone)){
                    //定义需要匹配的正则表达式的规则
                    String REGEX_MOBILE_SIMPLE =  "[1][358]\\d{9}";
                    //把正则表达式的规则编译成模板
                    Pattern pattern = Pattern.compile(REGEX_MOBILE_SIMPLE);
                    //把需要匹配的字符给模板匹配，获得匹配器
                    Matcher matcher = pattern.matcher(phone);
                    // 通过匹配器查找是否有该字符，不可重复调用重复调用matcher.find()
                    if (matcher.find()) {//匹配手机号是否存在
                        alterWarning(phone);
                        //    showDialog("提示","真的要发送吗");
                    } else {
                        ShowToast.show(RegisterActivity.this,"手机号格式错误");
                    }
                }
                break;
            case R.id.activity_register_iv_back:
                finish();
                break;
        }
    }

    private void handleResult(final String phone, final String pwd){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                FormBody formBody = new FormBody.Builder()
                        .add("phone",phone)
                        .add("pwd",pwd)
                        .build();
            }
        }).start();

        Intent intent = new Intent(RegisterActivity.this,RegisterInfoActivity.class);
        startActivity(intent);
    }

    private boolean checkPwd(String pwd,String confirmPwd){
        if(pwd.length()<6){
            SingleErrDiaog.show(RegisterActivity.this,"密码错误","密码不得少于6位");
            return false;
        }else if(pwd.length()>16){
            SingleErrDiaog.show(RegisterActivity.this,"密码错误","密码不得多于16位");
            return false;
        }else{
            if (pwd.equals(confirmPwd)){
                return true;
            }else {
                SingleErrDiaog.show(RegisterActivity.this,"注册失败","密码输入不一致");
                return false;
            }
        }
    }

    private boolean checkPhone(String phone){
        boolean result = false;
        if (phone.length() == 11){
            result = true;
        }else {
            SingleErrDiaog.show(RegisterActivity.this,"手机错误","请输入中国大陆11位电话号码");
        }
        return result;
    }


    //销毁短信注册
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销回调接口registerEventHandler必须和unregisterEventHandler配套使用，否则可能造成内存泄漏。
        SMSSDK.unregisterEventHandler(eh);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if (msg.what == CODE_REPEAT){
                btn_getVerification.setEnabled(true);
                timer.cancel();
                timerTask.cancel();
                TIME=60;
                btn_getVerification.setText("重新发送验证码");
            }else{
                btn_getVerification.setText(TIME+" 重新发送验证码");
            }
        }
    };

    private void alterWarning(final String phone){
        new AlertDialog.Builder(RegisterActivity.this)
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
                    ShowToast.show(RegisterActivity.this,"已取消");
                }
            })
            .show();
    }

    // 获取定位
    private void getLocation(){}

}
