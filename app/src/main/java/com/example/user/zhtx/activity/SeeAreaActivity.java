package com.example.user.zhtx.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.user.zhtx.R;

public class SeeAreaActivity extends AppCompatActivity implements View.OnClickListener {
    private RadioButton rb_all,rb_allNot,rb_care;
    private Button btn_myCare;
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

        btn_myCare = (Button)findViewById(R.id.activity_see_area_btn_myCare);
        btn_myCare.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_see_area_iv_back:
                finish();
                break;
            case R.id.activity_see_area_btn_myCare:
                Intent intent = new Intent(SeeAreaActivity.this,CareListActivity.class);
                startActivity(intent);
                break;

        }

    }
}
