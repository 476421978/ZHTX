package com.UI.change;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.zhtx.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.adapter.EaseContactAdapter;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.widget.EaseSidebar;
import com.hyphenate.exceptions.HyphenateException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GroupPickContactsActivity extends BaseActivity {
    /** if this is a new group */
    protected boolean isCreatingNewGroup;
    private PickContactAdapter contactAdapter;
    /** members already in the group */
    private List<String> existMembers;
    private List<EaseUser> alluserList;
    private Handler handler;
    private String groupId;
    private List<String> var;
    private  String[] vars;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_group_pick_contacts);

        handler = new g_Handler();

        groupId = getIntent().getStringExtra("groupId");

        if (groupId == null) {// create new group
            isCreatingNewGroup = true;
        } else {
            // get members of the group
            EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
            existMembers = group.getMembers();
            existMembers.add(group.getOwner());
            existMembers.addAll(group.getAdminList());
        }
        if(existMembers == null)
            existMembers = new ArrayList<String>();

        // get contact list
        alluserList = new ArrayList<EaseUser>();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    List<String> userNames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    for (String userId : userNames) {
//                KLog.e("好友列表中有 : " + userId);
                        EaseUser user = new EaseUser(userId);
                        if (!user.getUsername().equals(Constant.NEW_FRIENDS_USERNAME) & !user.getUsername().equals(Constant.GROUP_USERNAME) & !user.getUsername().equals(Constant.CHAT_ROOM) & !user.getUsername().equals(Constant.CHAT_ROBOT))
                            alluserList.add(user);
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                /** 封装消息 */
                Message msg = handler.obtainMessage();
                msg.what = 1;
                msg.obj = alluserList;
                /* 发送消息 */
                handler.sendMessage(msg);
            }
        }.start();
    }

    public void get_finish(){
        // sort the list
        Collections.sort(alluserList, new Comparator<EaseUser>() {
            @Override
            public int compare(EaseUser lhs, EaseUser rhs) {
                if(lhs.getInitialLetter().equals(rhs.getInitialLetter())){
                    return lhs.getNick().compareTo(rhs.getNick());
                }else{
                    if("#".equals(lhs.getInitialLetter())){
                        return 1;
                    }else if("#".equals(rhs.getInitialLetter())){
                        return -1;
                    }
                    return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
                }
            }
        });

        ListView listView = (ListView) findViewById(R.id.list);
        contactAdapter = new PickContactAdapter(this, R.layout.em_row_contact_with_checkbox, alluserList);
        listView.setAdapter(contactAdapter);
        ((EaseSidebar) findViewById(R.id.sidebar)).setListView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
                checkBox.toggle();
                System.out.println("点击选择用户");

            }
        });
    }

    public class g_Handler extends Handler{
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    get_finish();
                    break;
                case 2:
                    System.out.println("数据库创建群聊成功");

                    var = getToBeAddMembers();
                    vars = var.toArray(new String[var.size()]);
                    setResult(RESULT_OK, new Intent().putExtra("newmembers", var.toArray(new String[var.size()])));

                    Toast.makeText(getApplicationContext(),"保存群聊成功",Toast.LENGTH_SHORT).show();
                    //登陆用户ID
                    finish();
                    break;
                default:
                    break;
            }
        }
    }



    /**
     * save selected members
     *保存选择的联系人  发送邀请 创建群聊
     * @param v
     */
    public void save(View v) {
        //通知数据库 成功创建群聊 保存群聊
        Thread th = new MyThread();
        th.start();
    }

    //通知数据库创建群聊
    class MyThread extends Thread{
        @Override
        public void run() {
            super.run();
            //群聊ID
            /*String id = getIntent().getStringExtra("groupId");*/
            //群名
            String name = getIntent().getStringExtra("groupName");
            //登陆用户ID
            SharedPreferences user= getSharedPreferences("user", 0);
            int userID = user.getInt("id",0);
            //群简介
            String g_deatil = getIntent().getStringExtra("g_deatil");

            //保存群聊
            String url = "http://172.17.146.102:8080/txzh/insertGroup";
            OkHttpClient okHttpClient = new OkHttpClient();


            RequestBody body = new FormBody.Builder()
                    .add("name",name)
                    .add("ownerid",userID+"")
                    .add("detail",g_deatil)
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
                    msg.what=2;
                    handler.sendMessage(msg);
                }
            });
        }
    }

    /**
     * get selected members
     *
     * @return
     */
    private List<String> getToBeAddMembers() {
        List<String> members = new ArrayList<String>();
        int length = contactAdapter.isCheckedArray.length;
        for (int i = 0; i < length; i++) {
            String username = contactAdapter.getItem(i).getUsername();
            if (contactAdapter.isCheckedArray[i] && !existMembers.contains(username)) {
                members.add(username);
            }
        }
        return members;
    }

    /**
     * adapter
     */
    private class PickContactAdapter extends EaseContactAdapter {

        private boolean[] isCheckedArray;

        public PickContactAdapter(Context context, int resource, List<EaseUser> users) {
            super(context, resource, users);
            isCheckedArray = new boolean[users.size()];
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            final String username = getItem(position).getUsername();

            final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            ImageView avatarView = (ImageView) view.findViewById(R.id.avatar);
            TextView nameView = (TextView) view.findViewById(R.id.name);

            if (checkBox != null) {
                if(existMembers != null && existMembers.contains(username)){
                    checkBox.setButtonDrawable(R.drawable.em_checkbox_bg_gray_selector);
                }else{
                    checkBox.setButtonDrawable(R.drawable.em_checkbox_bg_selector);
                }

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // check the exist members
                        if (existMembers.contains(username)) {
                            isChecked = true;
                            checkBox.setChecked(true);
                        }
                        isCheckedArray[position] = isChecked;

                    }
                });
                // keep exist members checked
                if (existMembers.contains(username)) {
                    checkBox.setChecked(true);
                    isCheckedArray[position] = true;
                } else {
                    checkBox.setChecked(isCheckedArray[position]);
                }
            }

            return view;
        }
    }

    public void back(View view){
        finish();
    }

}
