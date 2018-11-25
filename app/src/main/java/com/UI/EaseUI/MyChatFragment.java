package com.UI.EaseUI;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.UI.change.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {
    private SharedPreferences sharedPreferences;

    public MyChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setChatFragmentHelper(this);
        sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        return super.onCreateView(inflater, container, savedInstanceState);
    }



    @Override
    public void onSetMessageAttributes(EMMessage message) {
        System.out.println("发送扩展信息");
       //set message extension
        message.setAttribute("em_robot_message", EMClient.getInstance().getCurrentUser());
        //设置要发送扩展消息昵称
        message.setAttribute(Constant.USER_NAME,"大王");
        //用户ID
        message.setAttribute(Constant.USER, EMClient.getInstance().getCurrentUser());
        //设置要发送扩展消息用户头像
        message.setAttribute(Constant.HEAD_IMAGE_URL,"http://editerupload.eepw.com.cn/201809/61001537857032.jpg");

    }

    @Override
    public void onEnterToChatDetails() {

    }

    @Override
    public void onAvatarClick(String username) {
        Toast.makeText(getActivity(),"头像被点击了",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAvatarLongClick(String username) {

    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }
}
