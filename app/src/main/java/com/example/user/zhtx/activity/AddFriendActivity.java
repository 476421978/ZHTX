package com.example.user.zhtx.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.example.user.zhtx.R;
import com.example.user.zhtx.tools.ShowToast;

public class AddFriendActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back,iv_head;
    private Button btn_search,btn_see,btn_add;
    private EditText ed_phone;
    private TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        initView();
    }

    private void initView(){
        ed_phone = (EditText)findViewById(R.id.activity_add_friend_ed_phone);

        tv_name = (TextView)findViewById(R.id.activity_add_friend_tv_name);

        iv_head = (ImageView)findViewById(R.id.activity_add_friend_iv_head);
        iv_back = (ImageView)findViewById(R.id.activity_add_friend_iv_back);
        iv_back.setOnClickListener(this);

        btn_add = (Button)findViewById(R.id.activity_add_friend_btn_add);
        btn_add.setOnClickListener(this);
        btn_search = (Button)findViewById(R.id.activity_add_friend_btn_search);
        btn_search.setOnClickListener(this);
        btn_see = (Button)findViewById(R.id.activity_add_friend_btn_see);
        btn_see.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_add_friend_btn_add:
                ShowToast.show(AddFriendActivity.this,"添加好友");
                break;
            case R.id.activity_add_friend_btn_see:
                ShowToast.show(AddFriendActivity.this,"查看信息");
                Intent intent = new Intent(AddFriendActivity.this,FriendInfoActivity.class);
                intent.putExtra("phone",ed_phone.getText().toString());
                startActivity(intent);
                break;
            case R.id.activity_add_friend_btn_search:
                ShowToast.show(AddFriendActivity.this,ed_phone.getText().toString());
                break;
            case R.id.activity_add_friend_iv_back:
                finish();
                break;
        }
    }
}
