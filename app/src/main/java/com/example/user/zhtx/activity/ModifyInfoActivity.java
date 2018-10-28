package com.example.user.zhtx.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.user.zhtx.R;
import com.example.user.zhtx.tools.ShowToast;

public class ModifyInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private RadioGroup rg_sex;
    private RadioButton rb_man,rb_women;
    private EditText ed_name,ed_address;
    private Button btn_birthday,btn_modify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);

        initView();
    }

    private void initView(){
        iv_back = (ImageView)findViewById(R.id.activity_modify_info_iv_back);
        iv_back.setOnClickListener(this);

        rg_sex = (RadioGroup)findViewById(R.id.activity_modify_info_rg_sexRadio);
        rb_man = (RadioButton)findViewById(R.id.activity_modify_info_rb_man);
        rb_women = (RadioButton)findViewById(R.id.activity_modify_info_rb_women);

        ed_address = (EditText)findViewById(R.id.activity_modify_info_ed_address);
        ed_name = (EditText)findViewById(R.id.activity_modify_info_ed_name);

        btn_birthday = (Button)findViewById(R.id.activity_modify_info_btn_birthday);
        btn_birthday.setOnClickListener(this);
        btn_modify = (Button)findViewById(R.id.activity_modify_info_btn_modify);
        btn_modify.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_modify_info_iv_back:
                finish();
                break;
            case R.id.activity_modify_info_btn_modify:
                Intent intent = new Intent(ModifyInfoActivity.this,SelfInfoActivity.class);
                startActivity(intent);
                ShowToast.show(ModifyInfoActivity.this,"修改信息成功");
                break;
            case R.id.activity_modify_info_btn_birthday:
                DatePickerDialog dialog = new DatePickerDialog(ModifyInfoActivity.this,DatePickerDialog.THEME_HOLO_DARK,dateSetListener,2018,10,28);
                dialog.show();
                break;
        }

    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            ShowToast.show(ModifyInfoActivity.this,i+" "+(i1+1)+" "+i2);
            btn_birthday.setText(i+"年"+(i1+1)+"月"+i2+"日");
        }
    };
}
