package com.example.user.zhtx.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.example.user.zhtx.R;

public class SeeAreaActivity extends AppCompatActivity implements View.OnClickListener {
    private RadioButton rb_all,rb_allNot,rb_care;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_area);
        initView();
    }

    private void initView(){
        rb_all = (RadioButton)findViewById(R.id.activity_see_area_rb_all);
        rb_all.setOnClickListener(this);
        rb_allNot = (RadioButton)findViewById(R.id.activity_see_area_rb_allNot);
        rb_allNot.setOnClickListener(this);
        rb_care = (RadioButton)findViewById(R.id.activity_see_area_rb_care);
        rb_care.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_see_area_rb_all:

        }

    }
}
