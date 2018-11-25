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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.UI.db.g_add;
import com.example.user.zhtx.R;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GroupSimpleDetailActivity extends BaseActivity {
    private Button btn_add_group;
    private TextView tv_admin;
    private TextView tv_name;
    private TextView tv_introduction;
    private EMGroup group;
    private String groupid;
    private ProgressBar progressBar;
    private EditText etReason;
    private Handler handler;
    private g_add g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_group_simle_details);
        tv_name = (TextView) findViewById(R.id.name);
        tv_admin = (TextView) findViewById(R.id.tv_admin);
        btn_add_group = (Button) findViewById(R.id.btn_add_to_group);
        tv_introduction = (TextView) findViewById(R.id.tv_introduction);
        progressBar = (ProgressBar) findViewById(R.id.loading);
        etReason = (EditText) findViewById(R.id.et_reason);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1://根据群ID 添加群成员
                        System.out.println("数据库添加群成员成功");
                        break;
                    case 2://根据群名 查找到群ID
                        Thread th = new MyThread();
                        th.start();
                        break;
                    default:
                        break;
                }
            }
        };

        EMGroupInfo groupInfo = (EMGroupInfo) getIntent().getSerializableExtra("groupinfo");
        String groupname = null;
        if(groupInfo != null){
            groupname = groupInfo.getGroupName();
            groupid = groupInfo.getGroupId();
        }else{
            group = PublicGroupsSeachActivity.searchedGroup;
            if(group == null)
                return;
            groupname = group.getGroupName();
            groupid = group.getGroupId();
        }

        tv_name.setText(groupname);


        if(group != null){
            showGroupDetail();
            return;
        }
        new Thread(new Runnable() {

            public void run() {
                //get detail from server
                try {
                    group = EMClient.getInstance().groupManager().getGroupFromServer(groupid);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            showGroupDetail();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    final String st1 = getResources().getString(R.string.Failed_to_get_group_chat_information);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(GroupSimpleDetailActivity.this, st1+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }).start();

    }

    //join the group
    //加入群聊
    public void addToGroup(View view){
        final String reason = TextUtils.isEmpty(etReason.getText().toString()) ? etReason.getHint().toString() : etReason.getText().toString();
        EMLog.i("AddToGroup", "reason: " + reason);
        String st1 = getResources().getString(R.string.Is_sending_a_request);
        final String st3 = getResources().getString(R.string.send_the_request_is);
        final String st4 = getResources().getString(R.string.Join_the_group_chat);
        final String st5 = getResources().getString(R.string.Failed_to_join_the_group_chat);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage(st1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    //if group is membersOnly，you need apply to join
                    if(group.isMembersOnly()){
                        EMClient.getInstance().groupManager().applyJoinToGroup(groupid, reason);
                    }else{
                        //直接申请加入
                        EMClient.getInstance().groupManager().joinGroup(groupid);
                        // 先查到群ID 再加人
                        Thread th2 =  new MyThread2();
                        th2.start();
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            if(group.isMembersOnly())
                                Toast.makeText(GroupSimpleDetailActivity.this, st3, Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(GroupSimpleDetailActivity.this, st4, Toast.LENGTH_SHORT).show();
                            btn_add_group.setEnabled(false);
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(GroupSimpleDetailActivity.this, st5+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }


    //根据群ID 添加群成员 到 群聊
    class MyThread extends Thread{
        public void run() {
            super.run();
            //登陆用户ID
            SharedPreferences user= getSharedPreferences("user", 0);
            int userID = user.getInt("id",0);

            //查询群聊
            String url = "http://172.17.146.102:8080/txzh/joinGroup";
            OkHttpClient okHttpClient = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .add("groupid",g.getId()+"")
                    .add("memberid",userID+"")
                    .build();

            final Request request = new Request.Builder()
                    .url(url)
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

                    System.out.println(response.body().string()+"-----");

                    Message msg = new Message();
                    msg.what=1;
                    handler.sendMessage(msg);
                }
            });
        }
    }

    //根据群聊名称 获取 群聊ID
    class MyThread2 extends Thread{
        public void run() {
            super.run();
            //保存群聊
            String url = "http://172.17.146.102:8080/txzh/searchGroup";
            OkHttpClient okHttpClient = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .add("name",tv_name.getText().toString().trim())
                    .build();

            final Request request = new Request.Builder()
                    .url(url)
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


                    Gson gson = new Gson();
                    g = gson.fromJson(response.body().string(),g_add.class);

                    Message msg = new Message();
                    msg.what=2;
                    msg.obj=g;
                    handler.sendMessage(msg);
                }
            });
        }
    }

    private void showGroupDetail() {
        progressBar.setVisibility(View.INVISIBLE);

        //get group detail, and you are not in, then show join button
        if(!group.getMembers().contains(EMClient.getInstance().getCurrentUser()))
            btn_add_group.setEnabled(true);
        tv_name.setText(group.getGroupName());
        tv_admin.setText(group.getOwner());
        tv_introduction.setText(group.getDescription());
    }

    public void back(View view){
        finish();
    }
}
