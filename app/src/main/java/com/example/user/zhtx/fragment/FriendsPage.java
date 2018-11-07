package com.example.user.zhtx.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.user.zhtx.R;
import com.example.user.zhtx.activity.AddFriendActivity;
import com.example.user.zhtx.adapter.FriendsExpandableListViewAdapter;
import com.example.user.zhtx.pojo.FriendAndGroupMessageBean;

import java.util.ArrayList;
import java.util.List;

public class FriendsPage extends Fragment implements View.OnClickListener {
    private ExpandableListView expandableListView;
    private List<FriendAndGroupMessageBean> bean;
    private RelativeLayout rl_add;
    private ImageView iv_back;
    private View v;

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle) {
        v = inflater.inflate(R.layout.fragment_friends, group, false);
        initView();

        return v;
    }

    private void initView(){
        rl_add = (RelativeLayout)v.findViewById(R.id.fragment_friends_rl_add);
        rl_add.setOnClickListener(this);

        updateInfo();
    }
    private void updateInfo(){
        expandableListView = (ExpandableListView)v.findViewById(R.id.fragment_friends_expandableListview);
        bean = new ArrayList<>();
        FriendAndGroupMessageBean mm = new FriendAndGroupMessageBean();
        ArrayList<String> list = new ArrayList<>();
        list.add("邱志鹏");
        list.add("黄俊贤");
        list.add("都是爸爸");
        mm.setName("家人");
        mm.setList(list);
        bean.add(mm);

        mm = new FriendAndGroupMessageBean();
        list = new ArrayList<>();
        list.add("邱志鹏");
        list.add("黄俊贤");
        list.add("123");
        mm.setName("同事");
        mm.setList(list);
        bean.add(mm);

        expandableListView.setAdapter(new FriendsExpandableListViewAdapter(this.getActivity(),bean));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_friends_rl_add:
                Intent intent = new Intent(getContext(), AddFriendActivity.class);
                startActivity(intent);
                break;
        }


    }
}
