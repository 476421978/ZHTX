package com.example.user.zhtx.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.zhtx.LoginActivity;
import com.example.user.zhtx.R;
import com.example.user.zhtx.pojo.MessageInfo;
import com.example.user.zhtx.tools.Address;
import com.example.user.zhtx.tools.ShowToast;
import com.example.user.zhtx.tools.SingleErrDiaog;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText ed_newPwd,ed_confirmPwd;
    private Button btn_reset;
    private ImageView iv_back;
    private static final int SUCCESS =1;
    private static final int FAIL = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initView();
    }

    private void initView(){
        ed_confirmPwd = (EditText)findViewById(R.id.activity_reset_password_ed_confirmPwd);
        ed_newPwd = (EditText)findViewById(R.id.activity_reset_password_ed_confirmPwd);

        btn_reset = (Button)findViewById(R.id.activity_reset_password_btn_reset);
        btn_reset.setOnClickListener(this);

        iv_back = (ImageView)findViewById(R.id.activity_reset_password_iv_back);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_reset_password_btn_reset:
                if (!checkPwd()){
                    return;
                }
                updatePwd();
                break;
            case R.id.activity_reset_password_iv_back:
                finish();
                break;
        }
    }

    private void updatePwd(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                FormBody body = new FormBody.Builder()
                    .add("newpassword",ed_newPwd.getText().toString())
                    .add("phonenum",getIntent().getStringExtra("phone"))
                    .build();

                    Log.i("new",ed_newPwd.getText().toString()+"--------------");
                Log.i("new",getIntent().getStringExtra("phone")+"------------------");


                final Request request = new Request.Builder()
                    .url(Address.ResetPassword)
                    .post(body)
                    .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        return;
                    }

                    @Override
                    public  void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        Log.i("result",result+"--------------------------");

                        Gson gson = new Gson();
                        MessageInfo m = gson.fromJson(result,MessageInfo.class);

                        if (m.getSuccess().equals("true")){
                            Message message = new Message();
                            message.what = SUCCESS;
                            message.obj=m.getMessage();
                            handler.sendMessage(message);
                        }if (m.getSuccess().equals("false")){
                            Message message = new Message();
                            message.what = FAIL;
                            message.obj = m.getMessage();
                            handler.sendMessage(message);
                        }
                    }
                });



            }
        }).start();
    }

    private boolean checkPwd(){
        if (ed_newPwd.getText().toString().length()<6||ed_newPwd.getText().toString().length()>16){
            SingleErrDiaog.show(ResetPasswordActivity.this,"找回密码失败","请输入6-16位密码");
            return false;
        }
        if (ed_newPwd.getText().toString().equals(ed_confirmPwd.getText().toString())){
            return true;
        }
        return false;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if (msg.what==SUCCESS){
                ShowToast.show(ResetPasswordActivity.this,msg.obj+"");
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }else if(msg.what == FAIL){
                ShowToast.show(ResetPasswordActivity.this,msg.obj+"");
            }
        }
    };
}
