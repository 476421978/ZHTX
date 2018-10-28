package com.example.user.zhtx.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.example.user.zhtx.R;
import com.example.user.zhtx.adapter.FramentStatePageAdapter;
import com.example.user.zhtx.fragment.FriendsPage;
import com.example.user.zhtx.fragment.GroupPage;
import com.example.user.zhtx.fragment.Map;
import com.example.user.zhtx.fragment.MessagePage;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private ArrayList<Fragment> fragmentArrayList;
    private RelativeLayout map,message,friends,group;
    private ImageView imageMap,imageMessage,imageFriends,imageGroup;
    private Handler handler;
    private Message msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        viewPager = (ViewPager)findViewById(R.id.activity_main_page_vp_fourview);
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

        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new Map());
        fragmentArrayList.add(new MessagePage(MainPageActivity.this));
        fragmentArrayList.add(new FriendsPage());
        fragmentArrayList.add(new GroupPage());
        FramentStatePageAdapter adapter = new FramentStatePageAdapter(getSupportFragmentManager(),fragmentArrayList);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);

        handler = new Handler(){
            public void handleMessage(android.os.Message msg) {
                switch (msg.what){
                    case 0:
                        exchangeImageColor(1);
                        break;
                    case 1:
                        exchangeImageColor(2);
                        break;
                    case 2:
                        exchangeImageColor(3);
                        break;
                    case 3:
                        exchangeImageColor(4);
                        break;
                    default:
                        break;
                }
            }
        };

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_main_page_rl_map:
                viewPager.setCurrentItem(0);
                msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
                break;
            case R.id.activity_main_page_rl_message:
                viewPager.setCurrentItem(1);
                msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                break;
            case R.id.activity_main_page_rl_friends:
                viewPager.setCurrentItem(2);
                msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
                break;
            case R.id.activity_main_page_rl_group:
                viewPager.setCurrentItem(3);
                msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        switch (position){
            case 0:
                msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
                break;
            case 1:
                msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                break;
            case 2:
                msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
                break;
            case 3:
                msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
