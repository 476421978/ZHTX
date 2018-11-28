package com.example.user.zhtx.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.UI.EaseUI.ChatActivity;
import com.UI.EaseUI.DemoApplication;
import com.UI.change.AddContactActivity;
import com.UI.change.GroupsActivity;
import com.UI.change.NewFriendsMsgActivity;
import com.UI.widget.ContactItemView;
import com.example.user.zhtx.R;
import com.example.user.zhtx.pojo.add;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/*
* 好友页面
* */

public class FriendsPage extends EaseContactListFragment {
   private ContactItemView applicationItem;
    private View loadingView;
    private List<add> adds = new ArrayList<add>();;


    private Map<String, EaseUser> contactList = new HashMap<>();

    private Handler handler;
    private DemoApplication demoApplication = new DemoApplication();
    @Override
    protected void initView() {
        super.initView();
        System.out.println("initView");
    }

    protected void setUpView() {
        super.setUpView();
        System.out.println("setUpView");
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        //设置
                        demoApplication.reSet();
                        break;
                }
            }
        };
        //设置
        demoApplication.reSet();
        //好友列表的头部
        @SuppressLint("InflateParams") View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.em_contacts_header, null);
        HeaderItemClickListener clickListener = new HeaderItemClickListener();

        //监听点击
        applicationItem = (ContactItemView) headerView.findViewById(R.id.application_item);
        applicationItem.setOnClickListener(clickListener);

        headerView.findViewById(R.id.group_item).setOnClickListener(clickListener);
        listView.addHeaderView(headerView);

        //add loading view 添加好友列表头部
        loadingView = LayoutInflater.from(getActivity()).inflate(R.layout.em_layout_loading_data, null);
        contentContainer.addView(loadingView);

        //添加好友加号
        titleBar.setRightImageResource(com.hyphenate.easeui.R.drawable.em_add);
        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), AddContactActivity.class));
                NetUtils.hasDataConnection(getActivity());
            }
        });
        // 进入添加好友页
        titleBar.getRightLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddContactActivity.class));
                //*跳转页面*//*
                System.out.println("点击加号");
            }
        });

       //设置联系人数据
        new Thread() {//需要在子线程中调用
            @Override
            public void run() {
                //需要设置联系人列表才能启动fragment
                setContactsMap(getContact());
            }
        }.start();

        //添加好友后重新获取好友列表
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String s) {
                //同意好友申请
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        setContactsMap(getContact());
                        refresh();
                    }
                }).start();
            } @Override
            public void onContactDeleted(String s) {

            }
            @Override
            public void onContactInvited(String s, String s1) { }
            @Override
            public void onFriendRequestAccepted(String s) { }
            @Override public void onFriendRequestDeclined(String s) { }
        });


         /*点击好友 打开聊天界面*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EaseUser user = (EaseUser)listView.getItemAtPosition(position);
                if (user != null) {
                    String username = user.getUsername();
                    // demo中直接进入聊天页面
                    startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userId", username));
                }
            }
        });
    }

    //点击申请 群聊事件
    protected class HeaderItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.application_item:
                    // 进入申请与通知页面
                    Intent intentPut = new Intent(getActivity(), NewFriendsMsgActivity.class);
                    startActivity(intentPut);
                    break;
                case R.id.group_item:
                    // 进入群聊列表页面
                    startActivity(new Intent(getActivity(), GroupsActivity.class));
                    break;
                default:
                    break;
            }
        }
    }

    //获取环信好友体系
    private Map<String, EaseUser> getContact() {
        Map<String, EaseUser> map = new HashMap<>();
        try {
            List<String> userNames = EMClient.getInstance().contactManager().getAllContactsFromServer();
//            KLog.e("......有几个好友:" + userNames.size());
            for (String userId : userNames) {
//                KLog.e("好友列表中有 : " + userId);
                EaseUser user = new EaseUser(userId);
                map.put(userId,user);
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }

        Message msg = new Message();
        msg.what=1;
        handler.sendMessage(msg);

        return map;
    }

}