package com.example.user.zhtx.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.user.zhtx.R;

public class SeeAreaActivity extends AppCompatActivity implements View.OnClickListener {
    private RadioButton rb_all,rb_allNot,rb_care;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_area);
        initView();
    }

    private void initView(){
        iv_back = (ImageView) findViewById(R.id.activity_see_area_iv_back);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_see_area_iv_back:
                Intent intent = new Intent(SeeAreaActivity.this,SettingActivity.class);
                startActivity(intent);
                break;

        }

    }
}
