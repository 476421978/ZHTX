package com.example.user.zhtx.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.zhtx.R;
import com.example.user.zhtx.pojo.add;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;
import com.loopj.android.image.SmartImageView;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by huang on 2018/11/17.
 */

public class AddFriendsAdapter  extends BaseAdapter {
    private Context mContext;
    private Handler handler;
    private LinkedList<add> adds;
    byte[] Picture_bt;
    public AddFriendsAdapter(LinkedList<add> data,Context mContext) {
        this.adds = data;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return adds.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_friend_invite,parent,false);


        TextView txt_aName = (TextView) convertView.findViewById(R.id.activity_friend_invite_tv_inviter);
        Button btn_arr = (Button) convertView.findViewById(R.id.activity_friend_invite_btn_agree);
        Button btn_rej = (Button) convertView.findViewById(R.id.activity_friend_invite_btn_refuse);

        //设置申请人头像
        String pic = adds.get(position).getPic();
        String pic_img = "http://172.17.146.102:8080/txzh/pic/"+pic+".jpeg";
        SmartImageView img_icon = (SmartImageView) convertView.findViewById(R.id.activity_friend_invite_iv_head);
        img_icon.setImageUrl(pic_img);

        //设置申请人用户名
        txt_aName.setText(adds.get(position).getInviterName());

        //同意
        btn_arr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                        Thread th = new MyThread(position);
                        th.start();
                }
        });

        //拒绝
        btn_rej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread th2 = new MyThread2(position);
                th2.start();
            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        Toast.makeText(mContext,"同意添加好友",Toast.LENGTH_SHORT).show();
                        //好友请求被同意
                        try {
                            EMClient.getInstance().contactManager().acceptInvitation(adds.get(position).getInviterPhone());
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                        //需要添加的好友账号
                        Map<String, EaseUser> toAddUsers = new HashMap<String, EaseUser>();
                        EaseUser user = new EaseUser(adds.get(position).getInviterPhone());
                        toAddUsers.put(adds.get(position).getInviterPhone(), user);
                        //删除该好友申请
                        Thread th2 = new MyThread2(position);
                        th2.start();
                        break;

                    case 2:
                        //删除该好友申请
                        notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        };

        return convertView;
    }

    //同意添加好友
    class MyThread extends Thread{
        int position;
        public MyThread(int id) {
            this.position = id;
        }

        @Override
        public void run() {
            super.run();
            System.out.println(position+"位置");
            //获取用户自己的信息
            String url = "http://172.17.146.102:8080/txzh/addFriends";
            OkHttpClient okHttpClient = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .add("inviterid",adds.get(position).getInviterid()+"")
                    .add("inviteeid",adds.get(position).getInviteeid()+"")
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Call call = okHttpClient.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                        System.out.println(response.body().string());

                    Message msg = new Message();
                    msg.what=1;
                    handler.sendMessage(msg);
                }
            });
        }
    }

    //拒绝添加好友  删除申请信息
    class MyThread2 extends Thread{
        int position;
        public MyThread2(int id) {
            this.position = id;
        }

        @Override
        public void run() {
            super.run();
            String url = "http://172.17.146.102:8080/txzh/deleteInvitation";
            OkHttpClient okHttpClient = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .add("invitationid",adds.get(position).getId()+"")
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Call call = okHttpClient.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    System.out.println(response.body().string());

                    Message msg = new Message();
                    msg.what=2;
                    handler.sendMessage(msg);
                }
            });
        }
    }


}
