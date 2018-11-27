package com.example.user.zhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.example.user.zhtx.R;
import com.example.user.zhtx.fragment.FriendsPage;
import com.example.user.zhtx.fragment.SettingPage;
import com.example.user.zhtx.fragment.MapPage;
import com.example.user.zhtx.fragment.MessagePage;
import com.example.user.zhtx.service.UpdateGpsService;
import com.example.user.zhtx.tools.GetGroupMemberGps;
import com.example.user.zhtx.tools.GroupMemberList;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout map,message,friends,group;
    private ImageView imageMap,imageMessage,imageFriends,imageGroup;

    private MapPage mapPage;
    private FriendsPage friendsPage;
    private MessagePage messagePage;
    private SettingPage settingPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        initView();
        initMap();

        // 拿到GPS信息与好友信息的例子
        /*
        FriendsGPSList getFriendsGPS = new FriendsGPSList(MainPageActivity.this);
        ArrayList<FriendsGPS> list =  getFriendsGPS.get();
        FriendsList friendsList = new FriendsList(MainPageActivity.this);
        ArrayList<Friend> list1 = friendsList.get();
        */
        Intent intent = new Intent(MainPageActivity.this, UpdateGpsService.class);
        startService(intent);

        GroupMemberList g = new GroupMemberList(MainPageActivity.this);
        g.get();

    }

    private void initView(){
        map = (RelativeLayout)findViewById(R.id.activity_main_page_rl_map);
        message = (RelativeLayout)findViewById(R.id.activity_main_page_rl_message);
        friends = (RelativeLayout)findViewById(R.id.activity_main_page_rl_friends);
        group = (RelativeLayout)findViewById(R.id.activity_main_page_rl_group);

        imageMap = (ImageView)findViewById(R.id.activity_main_page_iv_map);
        imageMessage = (ImageView)findViewById(R.id.activity_main_page_iv_message);
        imageFriends = (ImageView)findViewById(R.id.activity_main_page_iv_friends);
        imageGroup = (ImageView)findViewById(R.id.activity_main_page_iv_group);
        imageMap.setImageResource(R.drawable.map_blue);


        map.setOnClickListener(this);
        message.setOnClickListener(this);
        friends.setOnClickListener(this);
        group.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_main_page_rl_map:
                initMap();
                exchangeImageColor(1);
                break;
            case R.id.activity_main_page_rl_message:
                initMessage();
                exchangeImageColor(2);
                break;
            case R.id.activity_main_page_rl_friends:
                exchangeImageColor(3);
                initFriends();
                break;
            case R.id.activity_main_page_rl_group:
                exchangeImageColor(4);
                initGroup();
                break;
        }
    }

    public void exchangeImageColor(int i){
        imageMap.setImageResource(R.drawable.map_black);
        imageMessage.setImageResource(R.drawable.messages_black);
        imageFriends.setImageResource(R.drawable.person_black);
        imageGroup.setImageResource(R.drawable.group_black);

        switch (i){
            case 1:
                imageMap.setImageResource(R.drawable.map_blue);
                break;
            case 2:
                imageMessage.setImageResource(R.drawable.message_blue);
                break;
            case 3:
                imageFriends.setImageResource(R.drawable.person_blue);
                break;
            case 4:
                imageGroup.setImageResource(R.drawable.group_blue);
                break;
        }
    }

    private void initMap(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        if (mapPage == null){
            mapPage = new MapPage();
            transaction.add(R.id.activity_main_page_fragment,mapPage);
            transaction.show(mapPage);
        }else {
            transaction.show(mapPage);
        }

        transaction.commit();
    }

    private void initMessage(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        if (messagePage == null){
            messagePage = new MessagePage(MainPageActivity.this);
            transaction.add(R.id.activity_main_page_fragment,messagePage);
            transaction.show(messagePage);
        }else {
            transaction.show(messagePage);
        }

        transaction.commit();
    }
    private void initFriends(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        if (friendsPage == null){
            friendsPage = new FriendsPage();
            transaction.add(R.id.activity_main_page_fragment,friendsPage);
            transaction.show(friendsPage);
        }else {
            transaction.show(friendsPage);
        }

        transaction.commit();
    }
    private void initGroup(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        if (settingPage == null){
            settingPage = new SettingPage();
            transaction.add(R.id.activity_main_page_fragment, settingPage);
            transaction.show(settingPage);
        }else {
            transaction.show(settingPage);
        }

        transaction.commit();
    }


    private void hideFragment(FragmentTransaction transaction){
        if (mapPage != null){
            transaction.hide(mapPage);
        }
        if (messagePage != null){
            transaction.hide(messagePage);
        }
        if (friendsPage != null){
            transaction.hide(friendsPage);
        }
        if (settingPage != null){
            transaction.hide(settingPage);
        }
    }


}
