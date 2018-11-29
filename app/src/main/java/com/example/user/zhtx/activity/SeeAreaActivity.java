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
import com.example.user.zhtx.tools.Address;
import com.example.user.zhtx.tools.ShowToast;

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
            }else {
                ShowToast.show(SeeAreaActivity.this,"全部不可见");
            }
        }
    }

    private void changeIsView(int i){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                FormBody body = new FormBody.Builder()
                        .add("userid",sp.getInt("userid",0)+"")
                        .add("isView",sp.getInt("isView",2)+"")
                        .build();

                Request request = new Request.Builder()
                        .url(Address.ChangeSeeArea)
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

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
