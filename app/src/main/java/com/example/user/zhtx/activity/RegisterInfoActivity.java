package com.example.user.zhtx.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.user.zhtx.LoginActivity;
import com.example.user.zhtx.R;
import com.example.user.zhtx.tools.Address;
import com.example.user.zhtx.tools.ImageFormat;
import com.example.user.zhtx.tools.PerssionControl;
import com.example.user.zhtx.tools.ShowToast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RegisterInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_birthday,btn_commit,btn_changeHead;
    private ImageView iv_back,iv_head;
    private EditText ed_name,ed_address;
    private RadioGroup rg_gender;
    private RadioButton rb_man,rb_women;
    private int which;
    private Uri imageUri;
    private String photoPath;
    private Bitmap bitmap;
    private String gender = "0";
    private String birthday ;

    private String pic;

    private final static int REGISTER_SUCCESS = 1;      //注册成功
    private final static int REGISTER_FAIL =  2;        //注册失败
    private final static int REGISTER_ERROR = 3;        //出现未知错误

    private Message message;

    //相册
    public static final int IMAGE_REQUEST_CODE = 101;
    //手机
    public static final int RESULT_REQUEST_CODE = 102;

    private String[] perssions ={   Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);

        PerssionControl control = PerssionControl.getInstance();
        control.getPermission(this,perssions);



        initView();
    }

    // 实例化各种组件
    private void initView(){
        ed_name = (EditText) findViewById(R.id.activity_registerInfo_ed_name);
        ed_address = (EditText) findViewById(R.id.activity_registerInfo_ed_address);

        btn_birthday = (Button)findViewById(R.id.activity_registerInfo_btn_birthday);
        btn_birthday.setOnClickListener(this);
        btn_commit = (Button)findViewById(R.id.activity_registerInfo_btn_commit);
        btn_commit.setOnClickListener(this);

        iv_back = (ImageView) findViewById(R.id.activity_registerInfo_iv_back);
        iv_back.setOnClickListener(this);

        btn_changeHead = (Button)findViewById(R.id.activity_registerInfo_btn_changeHead);
        btn_changeHead.setOnClickListener(this);

        iv_head = (ImageView)findViewById(R.id.activity_registerInfo_iv_head);

        rg_gender = (RadioGroup)findViewById(R.id.activity_registerInfo_rg_gender);
        rb_man = (RadioButton)findViewById(R.id.activity_registerInfo_rb_man);
        rb_women = (RadioButton)findViewById(R.id.activity_registerInfo_rb_women);
        rg_gender.setOnCheckedChangeListener(new MyRadioButtonListener());

        message = new Message();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_registerInfo_btn_birthday:
                DatePickerDialog dialog = new DatePickerDialog(RegisterInfoActivity.this,DatePickerDialog.THEME_HOLO_DARK,dateSetListener,2018,10,28);
                dialog.show();
                break;
            case R.id.activity_registerInfo_btn_commit:
                saveInfo();
            //     后台我自己写来测试的，注释掉savaInfo，直接Internt
            /*
                Intent intent = new Intent(RegisterInfoActivity.this, LoginActivity.class);
                startActivity(intent);
            */
                break;
            case R.id.activity_registerInfo_iv_back:
                Intent intent1 = new Intent(RegisterInfoActivity.this,LoginActivity.class);
                startActivity(intent1);
                break;
            case R.id.activity_registerInfo_btn_changeHead:
                which = 0;
                choseWay();
                break;
        }
    }

    //选择相片方式
    private void choseWay(){
        final String items[] = {"相册","拍照"};
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterInfoActivity.this);
        builder.setTitle("选择图片源");
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ShowToast.show(RegisterInfoActivity.this, items[i]);
                        which=i;
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                ShowToast.show(RegisterInfoActivity.this,"选择了"+items[which]);
                if (items[which].equals("相册")){
                    dialogInterface.dismiss();
                    openAlbum();
                }else{
                    dialogInterface.dismiss();
                    // 调用系统的拍照功能
                    takePhone();
                }
            }
        });
        builder.create().show();
    }

    //打开相册
    private void openAlbum() {
        // 图库选择
        // 激活系统图库，选择一张图片
        Intent intent_gallery = new Intent(Intent.ACTION_PICK);
        intent_gallery.setType("image/*");
        startActivityForResult(intent_gallery,IMAGE_REQUEST_CODE);
    }

    //使用相机
    private void takePhone(){
        Toast.makeText(RegisterInfoActivity.this,"功能未完善",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        startActivityForResult(intent,RESULT_REQUEST_CODE);
    }

    private boolean checkInfo(){
        if (TextUtils.isEmpty(ed_name.getText().toString())){
            ShowToast.show(RegisterInfoActivity.this,"昵称不能为空");
            return false;
        }
        if (TextUtils.isEmpty(ed_address.getText().toString())){
            ShowToast.show(RegisterInfoActivity.this,"地址不能为空");
            return false;
        }
        if (btn_birthday.getText().toString().equals("请选择出生日期")){
            ShowToast.show(RegisterInfoActivity.this,"请选择出生日期");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE) {//相册
            imageUri = data.getData();
            //获取照片路径
            String[] filePathColumn = {MediaStore.Audio.Media.DATA};
            Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            photoPath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));

            setImageBitmap();

            iv_head.setBackground(null);
    //        bitmapToString = BitmapStringTool.BitmapToString(bitmap);

            /**已经把所选的相片变成String**/
            //         Log.i("bitmap",bitmapToString);
            //         Log.i("bitmap",bitmapToString.length()+"");
        }
    }

    private void setImageBitmap() {
        //获取imageview的宽和高
        int targetWidth = iv_head.getWidth();
        int targetHeight = iv_head.getHeight();

        //根据图片路径，获取bitmap的宽和高
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, options);
        int photoWidth = options.outWidth;
        int photoHeight = options.outHeight;

        //获取缩放比例
        int inSampleSize = 1;
        if (photoWidth > targetWidth || photoHeight > targetHeight) {
            int widthRatio = Math.round((float) photoWidth / targetWidth);
            int heightRatio = Math.round((float) photoHeight / targetHeight);
            inSampleSize = Math.min(widthRatio, heightRatio);
        }

        //使用现在的options获取Bitmap
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(photoPath, options);

        ByteArrayOutputStream byteArrayOutputStream =new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);

        ImageFormat format = ImageFormat.getInstance();
        pic = format.bitmapToBase64(bitmap);

        iv_head.setImageBitmap(bitmap);
    }

    // 性别选择
    class MyRadioButtonListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            switch (i){
                case R.id.activity_registerInfo_rb_man:
                    gender = "0";
                    ShowToast.show(RegisterInfoActivity.this,rb_man.getText().toString());
                    break;
                case R.id.activity_registerInfo_rb_women:
                    ShowToast.show(RegisterInfoActivity.this,rb_women.getText().toString());
                    gender = "1";
                    break;
            }
        }
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        //    ShowToast.show(RegisterInfoActivity.this,i+" "+(i1+1)+" "+i2);
            btn_birthday.setText(i+"-"+(i1+1)+"-"+i2);
            birthday = i+"-"+(i1+1)+"-"+i2;
        }
    };

    private void saveInfo(){
        if (!checkInfo()){
            return;
        }
        final String phone = getIntent().getStringExtra("phone");
        final String password = getIntent().getStringExtra("password");
        final String name = ed_name.getText().toString();
        final String address = ed_address.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                FormBody body = new FormBody.Builder()
                        .add("phonenum",phone)
                        .add("password",password)
                        .add("name",name)
                        .add("birthday",birthday)
                        .add("address",address)
                        .add("gender",gender)
                     //   .add("pic",pic)
                        .add("pic","01")
                        .build();

                Log.i("result",phone+" "+password+"  "+name+"  "+birthday+"  "+address+"  "+gender+"  pic");

                final Request request = new Request.Builder()
                        .url(Address.Register)
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

                        Log.i("a","----------------"+result);
                        if (result.equals("注册完成")){
                            message.what = REGISTER_SUCCESS;
                            handler.sendMessage(message);
                            Intent intent = new Intent(RegisterInfoActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }else if(result.equals("注册失败")){
                            message.what = REGISTER_FAIL;
                            handler.sendMessage(message);
                            Log.i("result","注册失败");
                        }else{
                            message.what = REGISTER_ERROR;
                            handler.sendMessage(message);
                            Log.i("result","出现未知错误");
                        }

                    }
                });
            }
        }).start();
    }

    Handler handler = new Handler(){
      @Override
      public void handleMessage(Message msg){
          if (msg.what == REGISTER_SUCCESS){
              ShowToast.show(RegisterInfoActivity.this,"注册成功");
          }else if(msg.what == REGISTER_ERROR){
              ShowToast.show(RegisterInfoActivity.this,"出现未知错误");
          }
          else if(msg.what == REGISTER_FAIL){
              ShowToast.show(RegisterInfoActivity.this,"注册失败");
          }
      }
    };
}
