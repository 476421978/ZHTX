package com.UI.change;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.zhtx.R;
import com.example.user.zhtx.tools.Address;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseAlertDialog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AddContactActivity extends BaseActivity{
    private EditText editText;
    private RelativeLayout searchedUserLayout;
    private TextView nameText;
    private Button searchBtn;
    private String toAddUsername;
    private ProgressDialog progressDialog;
    private Handler handler;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        TextView mTextView = (TextView) findViewById(R.id.add_list_friends);
        editText = (EditText) findViewById(R.id.edit_note);
        String strAdd = getResources().getString(R.string.add_friend);
        mTextView.setText(strAdd);
        String strUserName = getResources().getString(R.string.user_name);
        editText.setHint(strUserName);
        searchedUserLayout = (RelativeLayout) findViewById(R.id.ll_user);
        nameText = (TextView) findViewById(R.id.name);
        searchBtn = (Button) findViewById(R.id.search);


        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        Toast.makeText(getApplicationContext(),"发送好友申请成功",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    /*
    * 查找好友
    * */
    public void searchContact(View v) {
        final String name = editText.getText().toString().toLowerCase();
        String saveText = searchBtn.getText().toString();
        if (getString(R.string.button_search).equals(saveText)) {
            toAddUsername = name;
            if(TextUtils.isEmpty(name)) {
                new EaseAlertDialog(this, R.string.Please_enter_a_username).show();
                return;
            }
            // TODO you can search the user from your app server here.
            //show the userame and add button if user exist
            searchedUserLayout.setVisibility(View.VISIBLE);
            nameText.setText(toAddUsername);
        }
    }

    public void addContact(View view){
        System.out.println("添加好友");
        //不能添加自己
        if(EMClient.getInstance().getCurrentUser().equals(nameText.getText().toString())){
            new EaseAlertDialog(this, R.string.not_add_myself).show();
            return;
        }


        /*弹出发送 添加好友 信息提示*/
        progressDialog = new ProgressDialog(this);
        String stri = getResources().getString(R.string.Is_sending_a_request);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        //发送添加好友请求
       new Thread(new Runnable() {
            public void run() {
                try {
                    //demo use a hardcode reason here, you need let user to input if you like
                    String s = getResources().getString(R.string.Add_a_friend);
                    EMClient.getInstance().contactManager().addContact(toAddUsername, s);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s1 = getResources().getString(R.string.send_successful);
                            /*Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();*/
                            //发送好友申请
                            Thread th = new MyThread();
                            th.start();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();



    }

    //发送好友申请到数据库
    class MyThread extends Thread{
        @Override
        public void run() {
            super.run();

            SharedPreferences user= getSharedPreferences("user", 0);

            //登陆用户I
            String userName = EMClient.getInstance().getCurrentUser();

            //获取用户自己的信息
            String url = "http://172.17.146.102:8080/txzh/insertInvitation";
            OkHttpClient okHttpClient = new OkHttpClient();


            RequestBody body = new FormBody.Builder()
                    .add("myphone",userName)
                    .add("friendsphone",editText.getText().toString().trim())
                    .add("uuid",user.getString("uuid",""))
                    .build();

            Request request = new Request.Builder()
                    .url(Address.setInvitation)
                    .post(body)
                    .build();

            Call call = okHttpClient.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                 /*   System.out.println("返回:"+response.body().string());*/
                    Message msg = new Message();
                    msg.what=1;
                    handler.sendMessage(msg);
                }
            });
        }
    }

    public void back(View v) {
        finish();
    }
}
