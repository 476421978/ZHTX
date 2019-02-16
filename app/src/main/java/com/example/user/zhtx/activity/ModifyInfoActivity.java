package com.example.user.zhtx.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

public class ModifyInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private RadioGroup rg_sex;
    private RadioButton rb_man,rb_women;
    private EditText ed_name,ed_address;
    private Button btn_birthday,btn_modify;
    private int gender;
    private static final int SUCCESS = 1;
    private static final int FAIL=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);

        initView();
    }

    private void initView(){
        SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);

        iv_back = (ImageView)findViewById(R.id.activity_modify_info_iv_back);
        iv_back.setOnClickListener(this);

        rg_sex = (RadioGroup)findViewById(R.id.activity_modify_info_rg_sexRadio);
        rb_man = (RadioButton)findViewById(R.id.activity_modify_info_rb_man);
        rb_women = (RadioButton)findViewById(R.id.activity_modify_info_rb_women);
        rg_sex.setOnCheckedChangeListener(new MyRadioButtonListener());
        gender = sp.getInt("gender",1);
        if (gender==1){
            rb_man.setChecked(true);
        }else {
            rb_women.setChecked(true);
        }

        ed_address = (EditText)findViewById(R.id.activity_modify_info_ed_address);
        ed_address.setText(sp.getString("address","未找到"));

        ed_name = (EditText)findViewById(R.id.activity_modify_info_ed_name);
        ed_name.setText(sp.getString("name","未找到"));

        btn_birthday = (Button)findViewById(R.id.activity_modify_info_btn_birthday);
        btn_birthday.setOnClickListener(this);
        btn_birthday.setText(sp.getString("birthday","错误"));

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
                updateInfo();
                ShowToast.show(ModifyInfoActivity.this,"修改信息成功");
                break;
            case R.id.activity_modify_info_btn_birthday:
                DatePickerDialog dialog = new DatePickerDialog(ModifyInfoActivity.this,DatePickerDialog.THEME_HOLO_DARK,dateSetListener,2018,10,28);
                dialog.show();
                break;
        }

    }

    class MyRadioButtonListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            switch (i){
                case R.id.activity_modify_info_rb_man:
                    gender = 0;
                    ShowToast.show(ModifyInfoActivity.this,rb_man.getText().toString());
                    Log.i("gender",rb_man.getText().toString());
                    break;
                case R.id.activity_modify_info_rb_women:
                    ShowToast.show(ModifyInfoActivity.this,rb_women.getText().toString());
                    Log.i("gender",rb_women.getText().toString());
                    gender = 1;
                    break;
            }
        }
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            ShowToast.show(ModifyInfoActivity.this,i+" "+(i1+1)+" "+i2);
            btn_birthday.setText(i+"-"+(i1+1)+"-"+i2);
        }
    };

    private void updateInfo(){
        Log.i("info",gender+"  "+ed_name.getText().toString()+"  "+ed_address.getText().toString()+"  "+btn_birthday.getText().toString());

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);

                FormBody body = new FormBody.Builder()
                        .add("birthday",btn_birthday.getText().toString())
                        .add("userid",sp.getInt("id",1)+"")
                        .add("name",ed_name.getText().toString())
                        .add("gender",gender+"")
                        .add("address",ed_address.getText().toString())
                        .add("uuid",sp.getString("uuid",""))
                        .build();

                Request request = new Request.Builder()
                        .url(Address.ChangeMessage)
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
                        Log.i("result",result+"------------------");

                        Gson gson = new Gson();
                        MessageInfo m = gson.fromJson(result,MessageInfo.class);
                        if (m.getSuccess().equals("true")){
                            Message message = new Message();
                            message.what=SUCCESS;
                            message.obj=m.getMessage();
                            handler.sendMessage(message);
                        }else if(m.getSuccess().equals("false")){
                            Message message = new Message();
                            message.what=FAIL;
                            message.obj=m.getMessage();
                            handler.sendMessage(message);
                        }

                    }
                });

            }
        }).start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if (msg.what == SUCCESS){
                ShowToast.show(ModifyInfoActivity.this,"个人信息修改完成");
                updateSP();

                Intent intent = new Intent(ModifyInfoActivity.this,SelfInfoActivity.class);
                startActivity(intent);
            }else if (msg.what == FAIL){
                ShowToast.show(ModifyInfoActivity.this,"修改失败+  "+msg.obj+"");
            }
        }
    };

    private void updateSP(){
        SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name",ed_name.getText().toString());
        editor.putString("address",ed_address.getText().toString());
        editor.putInt("gender",gender);
        editor.putString("birthday",btn_birthday.getText().toString());
        editor.commit();

        Log.i("sp",sp.getString("name",""));
        Log.i("sp",sp.getString("address",""));
        Log.i("sp",sp.getInt("gender",1)+"");
        Log.i("sp",sp.getString("birthday",""));
    }
}
