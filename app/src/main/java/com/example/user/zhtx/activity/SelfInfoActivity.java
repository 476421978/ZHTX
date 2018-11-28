package com.example.user.zhtx.activity;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.zhtx.R;
import com.example.user.zhtx.pojo.MessageInfo;
import com.example.user.zhtx.tools.Address;
import com.example.user.zhtx.tools.SharedPreferencesControl;
import com.example.user.zhtx.tools.ShowToast;
import com.example.user.zhtx.tools.SingleErrDiaog;
import com.google.gson.Gson;
import com.loopj.android.image.SmartImageView;

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

public class SelfInfoActivity extends AppCompatActivity implements View.OnClickListener {
    //相册
    public static final int IMAGE_REQUEST_CODE = 101;
    //手机
    private static final int REQUEST_CAMERA = 102;//相机
    private static final int PICTURE_CUT =103 ;//相片截图
    private static final int CHANGE_HEAD_SUCCESS = 1;
    private static final int CHANGE_HEAD_FAILED = 2;
    private ImageView iv_bg,iv_back;
    private SmartImageView siv_head;
    private TextView tv_name,tv_phone,tv_address,tv_birthday;
    private Button btn_modify;
    private int gender;
    private Uri outputUri;
    private Uri CamarePhotoUri;
    private Uri imageUri;
    private int which;
    private File PhotoSavefile;
    private String photoPath;
    private boolean isClickCamera = false;
    private String imagePath;
    private boolean changePic = false;
    private Message message;
    private Bitmap bitmap;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_info);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView(){
        SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);

        iv_bg = (ImageView)findViewById(R.id.activity_self_info_iv_bg);
        siv_head = (SmartImageView)findViewById(R.id.activity_self_info_siv_head);
        siv_head.setBackground(null);
        getHead();
        siv_head.setOnClickListener(this);

        iv_back = (ImageView)findViewById(R.id.activity_self_info_iv_back);
        iv_back.setOnClickListener(this);

        tv_address = (TextView)findViewById(R.id.activity_self_info_tv_address);
        tv_address.setText(sp.getString("address","未找到"));

        tv_name = (TextView)findViewById(R.id.activity_self_info_tv_name);
        tv_name.setText(sp.getString("name","未找到"));
        tv_phone = (TextView)findViewById(R.id.activity_self_info_tv_phone);
        tv_phone.setText(sp.getString("phonenum","未找到"));
        tv_birthday = (TextView)findViewById(R.id.activity_self_info_tv_birthday);
        tv_birthday.setText(sp.getString("birthday","未找到"));

        btn_modify = (Button)findViewById(R.id.activity_self_info_btn_modify);
        btn_modify.setOnClickListener(this);
        message = new Message();
    }

    private void getHead(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);

                Request request = new Request.Builder()
                        .url(Address.title+sp.getString("phonenum","")+".jpeg")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        return;
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()){return;}

                        byte[] b = null;
                        try {
                            b = response.body().bytes();
                        }catch (IOException e){
                            e.printStackTrace();
                            return;
                        }
                        bitmap = BitmapFactory.decodeByteArray(b,0,b.length);
                        Message m = new Message();
                        m.what=44;
                        handler1.sendMessage(m);

                    }
                });


            }
        }).start();
    }

    Handler handler1 = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if (msg.what==44){
                siv_head.setImageBitmap(bitmap);
            }
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_self_info_iv_back:
                finish();
                break;
            case R.id.activity_self_info_btn_modify:
                Intent intent = new Intent(SelfInfoActivity.this,ModifyInfoActivity.class);
                startActivity(intent);
            case R.id.activity_self_info_siv_head:
                changePic = true;
                choseWay();
                break;
        }
    }
    private void choseWay(){
        final String items[] = {"相册","拍照"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SelfInfoActivity.this);
        builder.setTitle("选择图片源");
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ShowToast.show(SelfInfoActivity.this, items[i]);
                        which = i;
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                ShowToast.show(SelfInfoActivity.this,"选择了"+items[which]);
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



            if (resultCode == RESULT_OK){
                if (Build.VERSION.SDK_INT >= 19){
                    handleImageOnKitKat(data);

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

                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(outputUri));

                    siv_head.setImageBitmap(bitmap);
                    saveInfo();
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
        cropPhoto(uri);
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
        imagePath = file.getPath();
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
    private void saveInfo(){
        if (!changePic){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                MediaType TYPE = MediaType.parse("image/png");

                SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);

                RequestBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("pictureName",sp.getString("phonenum",""))
                        .addFormDataPart("pic","pic",RequestBody.create(TYPE,new File(imagePath)))
                        .build();

                final Request request = new Request.Builder()
                        .url(Address.ChangeHead)
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
                            message.what = CHANGE_HEAD_SUCCESS;
                            handler.sendMessage(message);
                        }else if("false".equals(m.getSuccess())){
                            message.what = CHANGE_HEAD_FAILED;
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

            if (msg.what == CHANGE_HEAD_SUCCESS){
                ShowToast.show(SelfInfoActivity.this,"头像更改上传成功");
                //志鹏注册成功 轮到我了
            }
        }
    };
}
