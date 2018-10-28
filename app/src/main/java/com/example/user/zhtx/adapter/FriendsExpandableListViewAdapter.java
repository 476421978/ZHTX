package com.example.user.zhtx.adapter;

/**
 * Created by user on 2018/10/29.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.user.zhtx.R;

import com.example.user.zhtx.pojo.FriendAndGroupMessageBean;

import java.util.List;

public class FriendsExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Activity activity;
    private List<FriendAndGroupMessageBean> list;
    public FriendsExpandableListViewAdapter(Activity activity, List<FriendAndGroupMessageBean> list){
        this.list = list;
        this.activity = activity;
    }
    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return list.get(i).getList().size();
    }

    @Override
    public Object getGroup(int i) {
        return list.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return list.get(i).getList().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder groupViewHolder;
        if(view == null){
            view = LayoutInflater.from(activity).inflate(R.layout.expandablelistview_group,viewGroup,false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.text = (TextView)view.findViewById(R.id.expandable_listview_group_tv_content);
            view.setTag(groupViewHolder);
        }else{
            groupViewHolder = (GroupViewHolder)view.getTag();
        }
        groupViewHolder.text.setText(list.get(i).getName());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder groupViewHolder;
        if(view == null){
            view = LayoutInflater.from(activity).inflate(R.layout.expandablelistview_child,viewGroup,false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.text = (TextView)view.findViewById(R.id.expandable_listview_child_tv_name);
            view.setTag(groupViewHolder);
        }else{
            groupViewHolder = (GroupViewHolder)view.getTag();
        }
        groupViewHolder.text.setText(list.get(i).getList().get(i1));
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    static class GroupViewHolder {
        TextView text;
    }
    static class ChildViewHolder {
        TextView text;
        ImageView picture;
    }

}
