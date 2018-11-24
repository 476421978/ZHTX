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
import android.widget.TextView;

import com.example.user.zhtx.R;


public class MessagePage extends Fragment {
    private View view;
    private TextView tv_setting;
    private Context context;

    public MessagePage(Context context){
        this.context = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle) {
        view = inflater.inflate(R.layout.fragment, group, false);
        return view;
    }
}
