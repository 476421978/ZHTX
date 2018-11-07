package com.example.user.zhtx.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.zhtx.R;
import com.example.user.zhtx.tools.ShowToast;

public class FriendInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back,iv_head;
    private TextView tv_name,tv_address,tv_phone;
    private Button btn_add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        ShowToast.show(FriendInfoActivity.this,phone);

        initView();
    }
    private void initView(){
        iv_head = (ImageView)findViewById(R.id.activity_friendInfo_iv_head);
        iv_back = (ImageView)findViewById(R.id.activity_friendInfo_iv_back);
        iv_back.setOnClickListener(this);

        tv_name = (TextView)findViewById(R.id.activity_friendInfo_tv_name);
        tv_address = (TextView)findViewById(R.id.activity_friendInfo_tv_address);
        tv_phone = (TextView)findViewById(R.id.activity_friendInfo_tv_phone);

        btn_add = (Button)findViewById(R.id.activity_friendInfo_btn_add);
        btn_add.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_friendInfo_iv_back:
                finish();
                break;
            case R.id.activity_friendInfo_btn_add:
                ShowToast.show(FriendInfoActivity.this,"已发送请求");
                break;
        }
    }
}
