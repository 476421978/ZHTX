package com.example.user.zhtx.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

public class ChangePasswordByPwd extends AppCompatActivity implements View.OnClickListener {
    private EditText ed_oldPwd,ed_newPwd,ed_confirmPwd;
    private TextView tv_toVerification;
    private Button btn_change;
    private ImageView iv_back;
    private static final int SUCCESS = 1;
    private static final int FAIL = 2;
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
                if (checkPwd()){
                    changePwd();
                }

                break;
            case R.id.activity_change_passwordByPwd_iv_back:
                Intent intent3 = new Intent(ChangePasswordByPwd.this,MainPageActivity.class);
                startActivity(intent3);
                break;
        }
    }

    private boolean checkPwd(){
        String odlPwd = ed_oldPwd.getText().toString();
        String newPwd = ed_newPwd.getText().toString();
        String confirmPwd = ed_confirmPwd.getText().toString();

        if (odlPwd.length()<1){
            SingleErrDiaog.show(ChangePasswordByPwd.this,"修改密码失败","请输入6-16位密码");
            return false;
        }
        if (newPwd.length()<6||newPwd.length()>16){
            SingleErrDiaog.show(ChangePasswordByPwd.this,"修改密码失败","请输入6-16位密码");
            return false;
        }
        if (!newPwd.equals(confirmPwd)){
            SingleErrDiaog.show(ChangePasswordByPwd.this,"修改密码失败","两次密码输入不一致");
            return false;
        }
        return true;
    }

    private void changePwd(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
                FormBody body = new FormBody.Builder()
                        .add("id",sp.getInt("id",1)+"")
                        .add("oldpassword",ed_oldPwd.getText().toString())
                        .add("newpassword",ed_newPwd.getText().toString())
                        .add("uuid",sp.getString("uuid",""))
                        .build();

                final Request request = new Request.Builder()
                        .url(Address.ChangePasswordByPwd)
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
                        MessageInfo m = gson.fromJson(result,MessageInfo.class);
                        if (m.getSuccess().equals("true")){
                            Message message = new Message();
                            message.what = SUCCESS;
                            message.obj = m.getMessage();
                            handler.sendMessage(message);
                        }else {
                            Message message1 = new Message();
                            message1.obj = m.getMessage();
                            message1.what = FAIL;
                            handler.sendMessage(message1);
                        }
                    }
                });

            }
        }).start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if (msg.what==SUCCESS){
                ShowToast.show(ChangePasswordByPwd.this,msg.obj+"");
                SharedPreferences  sp = getSharedPreferences("user",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("password",ed_newPwd.getText().toString());
                Intent intent = new Intent(ChangePasswordByPwd.this,MainPageActivity.class);
                startActivity(intent);
            }else if (msg.what==FAIL){
                SingleErrDiaog.show(ChangePasswordByPwd.this,"修改密码失败",msg.obj+"");
            }
        }

    };

}
