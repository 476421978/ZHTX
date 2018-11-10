package com.example.user.zhtx.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.example.user.zhtx.R;
import com.example.user.zhtx.fragment.FriendsPage;
import com.example.user.zhtx.fragment.GroupPage;
import com.example.user.zhtx.fragment.MapPage;
import com.example.user.zhtx.fragment.MessagePage;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout map,message,friends,group;
    private ImageView imageMap,imageMessage,imageFriends,imageGroup;

    private MapPage mapPage;
    private FriendsPage friendsPage;
    private MessagePage messagePage;
    private GroupPage groupPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        initView();
        initMap();



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
                break;
            case R.id.activity_main_page_rl_message:
                initMessage();
                break;
            case R.id.activity_main_page_rl_friends:
                initFriends();
                break;
            case R.id.activity_main_page_rl_group:
                initGroup();
                break;
        }
    }

    private void iconColor(int i){
        ArrayList<ImageView> icons = new ArrayList<ImageView>();
        icons.add(imageMap);
        icons.add(imageMessage);
        icons.add(imageFriends);
        icons.add(imageGroup);




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
        if (groupPage == null){
            groupPage = new GroupPage();
            transaction.add(R.id.activity_main_page_fragment,groupPage);
            transaction.show(groupPage);
        }else {
            transaction.show(groupPage);
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
        if (groupPage != null){
            transaction.hide(groupPage);
        }
    }


}
