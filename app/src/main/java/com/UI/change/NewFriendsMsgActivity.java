package com.UI.change;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.example.user.zhtx.R;
import com.example.user.zhtx.adapter.AddFriendsAdapter;
import com.example.user.zhtx.pojo.add;
import com.example.user.zhtx.tools.Address;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.LinkedList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
*
* 申请与通知界面
*
* */

public class NewFriendsMsgActivity extends BaseActivity implements View.OnClickListener {
    private Context mContext;
    private Handler handler;
    private LinkedList<add> adds;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_new_friends_msg);
        mContext = this;

        //好友申请处理
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        System.out.println("获取好友申请列表"+adds.size());
                        for (int i = 0; i <adds.size() ; i++) {
                            System.out.println(adds.get(i).getId()+"------");
                        }
                        ListView listView = (ListView) findViewById(R.id.list_item_show);

                        AddFriendsAdapter adapter = new AddFriendsAdapter(adds,mContext);

                        listView.setAdapter(adapter);

                        break;
                    default:
                        break;
                }
            }
        };


        Thread th = new MyThread();
        th.start();
    }



    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
      /*  Log.i("同意添加好友", "onClick: " + mm.get(i));
        *//*好友管理*//*
        try {
            EMClient.getInstance().contactManager().acceptInvitation(mm.get(i));
        } catch (HyphenateException e) {
            e.printStackTrace();
        }*/
    }

    /*根据登陆用户 得到好友申请事件*/
    class MyThread extends Thread{
        @Override
        public void run() {
            super.run();

            //登陆用户ID
            SharedPreferences user= getSharedPreferences("user", 0);
            int userID = user.getInt("id",0);
            System.out.println(userID);


            //获取用户自己的信息
            /*String url = "http://172.17.146.102:8080/txzh/selectInvitation";*/
            OkHttpClient okHttpClient = new OkHttpClient();


            RequestBody body = new FormBody.Builder()
                    .add("userid",userID+"")
                    .add("uuid",user.getString("uuid",""))
                    .build();

            final Request request = new Request.Builder()
                    .url(Address.getInvitation)
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
                    Gson gson = new Gson();


                /*    System.out.println(response.body().string());*/

                    adds = gson.fromJson(response.body().string(), new TypeToken<LinkedList<add>>(){}.getType());

                    System.out.println(adds);

                    Message msg = new Message();
                    msg.what=1;
                    handler.sendMessage(msg);
                }
            });
        }
    }
}
