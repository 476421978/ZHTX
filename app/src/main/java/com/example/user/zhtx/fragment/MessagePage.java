package com.example.user.zhtx.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.user.zhtx.R;
import com.example.user.zhtx.activity.SettingActivity;

public class MessagePage extends Fragment implements View.OnClickListener {
    private Button btn_setting;
    private View view;
    private Context context;

    public MessagePage(Context context){
        this.context = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle) {
        view = inflater.inflate(R.layout.fragment, group, false);
        initView();
        return view;
    }

    private void initView(){
        btn_setting = (Button)view.findViewById(R.id.fragment_btn_setting);
        btn_setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, SettingActivity.class);
        startActivity(intent);
    }
}
