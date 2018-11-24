package com.example.user.zhtx.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ChangePasswordByVeri extends AppCompatActivity implements View.OnClickListener {
    private EditText ed_pwd,ed_confirmPwd,ed_verification;
    private Button btn_getVerification,btn_change;
    private ImageView iv_back;
    private String phone;

    private TimerTask timerTask;
    private Timer timer;
    private int TIME = 60;                          //倒计时时间
    public String country="86";                     //国家区号，中国为86

    private static final int CODE_REPEAT = 1;       //重新发送
    private static final int CODE_ERROR = 2;        //验证码错误
    private static final int PHONE_ERROR = 3;       //电话号码不存在

    private final String appkty = "286309d7a4904";
    private final String appSecret = "bae047d3b2f375d802dfe3fb1d778efa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_by_veri);
        initView();
        MobSDK.init(ChangePasswordByVeri.this,appkty,appSecret);
        SMSSDK.registerEventHandler(eh);

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
            /*    new Thread(new Runnable() {
                    @Override
                    public void run() {
                        message.what = CODE_ERROR;
                        handler.sendMessage(message);
                    }
                }).start();*/
                handleResult();
            }
        }
    };

    private void initView(){
        ed_confirmPwd = (EditText)findViewById(R.id.activity_change_passwordByVeri_ed_confirmPwd);
        ed_pwd = (EditText)findViewById(R.id.activity_change_passwordByVeri_ed_pwd);
        btn_getVerification = (Button) findViewById(R.id.activity_change_passwordByVeri_btn_getVerification);
        btn_change = (Button)findViewById(R.id.activity_change_passwordByVeri_btn_change);

        btn_change.setOnClickListener(this);
        btn_getVerification.setOnClickListener(this);

        iv_back = (ImageView)findViewById(R.id.activity_change_passwordByVeri_iv_back);
        iv_back.setOnClickListener(this);
        ed_verification = (EditText)findViewById(R.id.activity_change_passwordByVeri_ed_verification);

        SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
        phone = sp.getString("phonenum","");
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_change_passwordByVeri_btn_getVerification:
                ShowToast.show(ChangePasswordByVeri.this,"获取验证码");
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
                        ShowToast.show(ChangePasswordByVeri.this,"手机号码不存在");
                    }
                }
                break;
            case R.id.activity_change_passwordByVeri_btn_change:
                String verification = ed_verification.getText().toString().trim();
                if (checkPwd()){
                    if (!TextUtils.isEmpty(ed_verification.getText().toString())){
                        SMSSDK.submitVerificationCode(country,phone,verification);
                    }else {
                        SingleErrDiaog.show(ChangePasswordByVeri.this,"注册失败","验证码不能为空");
                    }
                }
            /*    ShowToast.show(ChangePasswordByVeri.this,"密码修改成功");
                Intent intent = new Intent(ChangePasswordByVeri.this,MainPageActivity.class);
                startActivity(intent);*/
                break;
            case R.id.activity_change_passwordByVeri_iv_back:
                finish();
                break;
        }
    }

    private boolean checkPwd(){
        String newPwd = ed_pwd.getText().toString();
        String confirmPwd = ed_confirmPwd.getText().toString();

        if (newPwd.length()<6||newPwd.length()>16){
            SingleErrDiaog.show(ChangePasswordByVeri.this,"修改密码失败","请输入6-16位密码");
            return false;
        }
        if (!newPwd.equals(confirmPwd)){
            SingleErrDiaog.show(ChangePasswordByVeri.this,"修改密码失败","两次密码输入不一致");
            return false;
        }
        if (ed_verification.getText().toString().length()<1){
            SingleErrDiaog.show(ChangePasswordByVeri.this,"修改密码失败","验证码不能为空");
        }

        return true;
    }

    private void alterWarning(final String phone){
        new AlertDialog.Builder(ChangePasswordByVeri.this)
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
                        ShowToast.show(ChangePasswordByVeri.this,"已取消");
                    }
                })
                .show();
    }

    private void handleResult(){

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){

        }
    };
}
