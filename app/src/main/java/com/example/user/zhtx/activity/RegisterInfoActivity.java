package com.example.user.zhtx.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.example.user.zhtx.LoginActivity;
import com.example.user.zhtx.R;
import com.example.user.zhtx.tools.ShowToast;


public class RegisterInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_birthday,btn_commit;
    private ImageView iv_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);

        initView();
    }

    private void initView(){
        btn_birthday = (Button)findViewById(R.id.activity_registerInfo_btn_birthday);
        btn_birthday.setOnClickListener(this);
        btn_commit = (Button)findViewById(R.id.activity_registerInfo_btn_commit);
        btn_commit.setOnClickListener(this);

        iv_back = (ImageView) findViewById(R.id.activity_registerInfo_iv_back);
        iv_back.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_registerInfo_btn_birthday:
                DatePickerDialog dialog = new DatePickerDialog(RegisterInfoActivity.this,DatePickerDialog.THEME_HOLO_DARK,dateSetListener,2018,10,28);
                dialog.show();
                break;
            case R.id.activity_registerInfo_btn_commit:
                Intent intent = new Intent(RegisterInfoActivity.this, LoginActivity.class);
                startActivity(intent);
                startActivity(intent);
                break;
            case R.id.activity_registerInfo_iv_back:
                Intent intent1= new Intent(RegisterInfoActivity.this,RegisterActivity.class);
                startActivity(intent1);
                break;
        }
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            ShowToast.show(RegisterInfoActivity.this,i+" "+(i1+1)+" "+i2);
            btn_birthday.setText(i+"年"+(i1+1)+"月"+i2+"日");
        }
    };
}
