package com.example.user.zhtx.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import com.example.user.zhtx.R;
import com.example.user.zhtx.adapter.FriendsExpandableListViewAdapter;
import com.example.user.zhtx.pojo.FriendAndGroupMessageBean;

import java.util.ArrayList;
import java.util.List;

public class FriendsPage extends Fragment {
    private ExpandableListView expandableListView;
    private List<FriendAndGroupMessageBean> bean;
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle) {
        View v = inflater.inflate(R.layout.fragment_friends, group, false);
        expandableListView = (ExpandableListView)v.findViewById(R.id.fragment_friends_expandableListview);

        bean = new ArrayList<>();
        FriendAndGroupMessageBean mm = new FriendAndGroupMessageBean();
        ArrayList<String> list = new ArrayList<>();
        list.add("垃圾邱志鹏");
        list.add("垃圾黄俊贤");
        list.add("都是垃圾");
        mm.setName("垃圾");
        mm.setList(list);
        bean.add(mm);

        mm = new FriendAndGroupMessageBean();
        list = new ArrayList<>();
        list.add("垃圾邱志鹏");
        list.add("垃圾黄俊贤");
        list.add("都是垃圾");
        mm.setName("很垃圾");
        mm.setList(list);
        bean.add(mm);

        expandableListView.setAdapter(new FriendsExpandableListViewAdapter(this.getActivity(),bean));
        return v;
    }
}
