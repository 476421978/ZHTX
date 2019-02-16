package com.example.user.zhtx.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.user.zhtx.R;
import com.example.user.zhtx.pojo.MessageInfo;
import com.example.user.zhtx.tools.Address;
import com.example.user.zhtx.tools.ShowToast;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SeeAreaActivity extends AppCompatActivity implements View.OnClickListener {
    private RadioButton rb_all,rb_allNot;
    private RadioGroup radioGroup;
    private ImageView iv_back;
    private SharedPreferences sp;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_area);
        initView();
    }

    private void initView(){
        iv_back = (ImageView) findViewById(R.id.activity_see_area_iv_back);
        iv_back.setOnClickListener(this);

        sp= getSharedPreferences("user",MODE_PRIVATE);
        int isView = sp.getInt("isView",2);

        radioGroup = (RadioGroup) findViewById(R.id.activity_see_area_rg);
        rb_all = (RadioButton) findViewById(R.id.activity_see_area_rb_all);
        rb_allNot = (RadioButton) findViewById(R.id.activity_see_area_rb_allNot);

        if (isView==0){
            rb_allNot.setChecked(true);
        }else if (isView ==1){
            rb_all.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener(new MyRdiaoGroupChangedListener());


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_see_area_iv_back:
                finish();
                break;
        }

    }

    class MyRdiaoGroupChangedListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            if(rb_all.getId() == checkedId ){
                ShowToast.show(SeeAreaActivity.this,"全部可见");
                i=1;
                changeIsView(i);
            }else {
                i=0;
                ShowToast.show(SeeAreaActivity.this,"全部不可见");
                changeIsView(i);
            }

        }
    }

    private void changeIsView(final int i){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                FormBody body = new FormBody.Builder()
                        .add("userid",sp.getInt("id",0)+"")
                        .add("isview",i+"")
                        .add("uuid",sp.getString("uuid",""))
                        .build();

                Request request = new Request.Builder()
                        .url(Address.ChangeSeeArea)
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
                        if ("true".equals(m.getSuccess())){
                            SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putInt("isView",i);
                            editor.commit();
                        }
                    }
                });
            }
        }).start();
    }
}
