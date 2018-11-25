package com.example.user.zhtx.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.example.user.zhtx.pojo.MessageInfo;
import com.example.user.zhtx.tools.Address;
import com.example.user.zhtx.tools.ImageFormat;
import com.example.user.zhtx.tools.PerssionControl;
import com.example.user.zhtx.tools.ShowToast;
import com.example.user.zhtx.tools.SingleErrDiaog;
import com.google.gson.Gson;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegisterInfoActivity extends AppCompatActivity implements View.OnClickListener {
    // 弹出框
    private ProgressDialog mDialog;

    private boolean isClickCamera =false;//判断是否使用了相机拍照
    private Button btn_birthday,btn_commit,btn_changeHead;
    private ImageView iv_back,iv_head;
    private EditText ed_name,ed_address;
    private RadioGroup rg_gender;
    private RadioButton rb_man,rb_women;
    private int which;
    private File PhotoSavefile;
    private Uri imageUri,CamarePhotoUri,outputUri;
    private String photoPath;
    private Bitmap bitmap;
    private String gender = "0";
    private String birthday ;

    private String pic;

    private final static int REGISTER_SUCCESS = 1;      //注册成功
    private final static int REGISTER_FAIL =  2;        //注册失败
    private final static int REGISTER_ERROR = 3;        //出现未知错误

    private String imagePath = "";

    private Message message;

    //相册
    public static final int IMAGE_REQUEST_CODE = 101;
    //手机
    private static final int REQUEST_CAMERA = 102;//相机
    private static final int PICTURE_CUT =103 ;//相片截图

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
        PhotoSavefile = new File(getExternalCacheDir(), "outputImage.jpg");
        try {
            if (PhotoSavefile.exists()) {
                PhotoSavefile.delete();
            }
            PhotoSavefile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            CamarePhotoUri = FileProvider.getUriForFile(this, "com.example.user.zhtx.fileprovider", PhotoSavefile);
        } else {
            CamarePhotoUri = Uri.fromFile(PhotoSavefile);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, CamarePhotoUri);
        startActivityForResult(intent, REQUEST_CAMERA);
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
            if (resultCode == RESULT_OK){
                if (Build.VERSION.SDK_INT >= 19){
                    handleImageOnKitKat(data);
                    cropPhoto(imageUri);
                }
            }
        }


        if (requestCode == REQUEST_CAMERA) {
            if(resultCode == RESULT_OK) {

                cropPhoto(CamarePhotoUri);//裁剪图片
            }
        }

        if (requestCode == PICTURE_CUT) { //裁剪完成
            isClickCamera = true;
            Bitmap bitmap = null;
            try {
                if (isClickCamera) {

                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));

                    iv_head.setImageBitmap(bitmap);

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data){
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }
        Log.i("path",imagePath+"------------------------------------------");
    }

    private String getImagePath(Uri uri,String selection){
        String Path=null;
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                Path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return Path;
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

                MediaType TYPE = MediaType.parse("image/png");

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phonenum",phone)
                .addFormDataPart("password",password)
                .addFormDataPart("name",name)
                .addFormDataPart("birthday",birthday)
                .addFormDataPart("address",address)
                .addFormDataPart("gender",gender)
                .addFormDataPart("pic","pic",RequestBody.create(TYPE,new File(imagePath)))
                .build();

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

                        Gson gson = new Gson();
                        MessageInfo m = gson.fromJson(result,MessageInfo.class);

                        if ("true".equals(m.getSuccess())){
                            message.what = REGISTER_SUCCESS;
                            handler.sendMessage(message);
                        }else if("false".equals(m.getSuccess())){
                            message.what = REGISTER_FAIL;
                            handler.sendMessage(message);
                        }else{
                            message.what = REGISTER_ERROR;
                            message.obj = m.getMessage();
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
          if (msg.what == REGISTER_SUCCESS){
              ShowToast.show(RegisterInfoActivity.this,"注册成功");
              //志鹏注册成功 轮到我了
              signUp();
          }else if(msg.what == REGISTER_ERROR){
              ShowToast.show(RegisterInfoActivity.this,"出现未知错误");
          }
          else if(msg.what == REGISTER_FAIL){
              SingleErrDiaog.show(RegisterInfoActivity.this,"注册失败",msg.obj+"");
          }
      }
    };
    private void cropPhoto(Uri uri) {
        // 创建File对象，用于存储裁剪后的图片，避免更改原图
        File file = new File(getExternalCacheDir(), "crop_image.jpg");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        outputUri = Uri.fromFile(file);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        //裁剪图片的宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("crop", "true");//可裁剪
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("scale", true);//支持缩放
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出图片格式
        intent.putExtra("noFaceDetection", true);//取消人脸识别
        startActivityForResult(intent, PICTURE_CUT);
    }


    /**
     * 注册方法
     */
    private void signUp() {
        // 注册是耗时过程，所以要显示一个dialog来提示下用户
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("注册中，请稍后...");
        mDialog.show();

        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    String username = getIntent().getStringExtra("phone").trim();
                    String password = getIntent().getStringExtra("password").trim();
                    EMClient.getInstance().createAccount(username, password);
                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            if (!RegisterInfoActivity.this.isFinishing()) {
                                mDialog.dismiss();
                            }
                            Toast.makeText(RegisterInfoActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterInfoActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            if (!RegisterInfoActivity.this.isFinishing()) {
                                mDialog.dismiss();
                            }
                            /**
                             * 关于错误码可以参考官方api详细说明
                             * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                             */
                            int errorCode = e.getErrorCode();
                            String message = e.getMessage();
                            Log.d("lzan13",
                                    String.format("sign up - errorCode:%d, errorMsg:%s", errorCode,
                                            e.getMessage()));
                            switch (errorCode) {
                                // 网络错误
                                case EMError.NETWORK_ERROR:
                                    Toast.makeText(RegisterInfoActivity.this,
                                            "网络错误 code: " + errorCode + ", message:" + message,
                                            Toast.LENGTH_LONG).show();
                                    break;
                                // 用户已存在
                                case EMError.USER_ALREADY_EXIST:
                                    Toast.makeText(RegisterInfoActivity.this,
                                            "用户已存在 code: " + errorCode + ", message:" + message,
                                            Toast.LENGTH_LONG).show();
                                    break;
                                // 参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册
                                case EMError.USER_ILLEGAL_ARGUMENT:
                                    Toast.makeText(RegisterInfoActivity.this,
                                            "参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册 code: "
                                                    + errorCode
                                                    + ", message:"
                                                    + message, Toast.LENGTH_LONG).show();
                                    break;
                                // 服务器未知错误
                                case EMError.SERVER_UNKNOWN_ERROR:
                                    Toast.makeText(RegisterInfoActivity.this,
                                            "服务器未知错误 code: " + errorCode + ", message:" + message,
                                            Toast.LENGTH_LONG).show();
                                    break;
                                case EMError.USER_REG_FAILED:
                                    Toast.makeText(RegisterInfoActivity.this,
                                            "账户注册失败 code: " + errorCode + ", message:" + message,
                                            Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(RegisterInfoActivity.this,
                                            "ml_sign_up_failed code: " + errorCode + ", message:" + message,
                                            Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
