package com.example.user.zhtx.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.zhtx.R;

/**
 * Created by user on 2018/11/3.
 */
public class CareListAdapter extends BaseAdapter {
    private Context context;
    private int[] pic;
    private String[] names;
    private boolean isCare = true;
    public CareListAdapter(Context context,int[] pic,String[] names){
        this.context=context;
        this.pic=pic;
        this.names=names;
    }
    @Override
    public int getCount() {
        return pic.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context,R.layout.listview_setting_care,null);
        ImageView imageView = (ImageView)v.findViewById(R.id.listview_setting_care_iv_head);
        imageView.setBackgroundResource(pic[i]);
        final TextView textView = (TextView)v.findViewById(R.id.listview_setting_care_tv_name);
        textView.setText(names[i]);
        final Button button = (Button)v.findViewById(R.id.listview_setting_care_btn_quxiao);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCare){
                    Toast.makeText(context,textView.getText(),Toast.LENGTH_SHORT).show();
                    button.setBackgroundColor(Color.parseColor("#40E0D0"));
                    button.setText("未关注");
                    Log.i("aa",textView.getText()+"已不再关注");
                    isCare = !isCare;
                }else {
                    Toast.makeText(context,textView.getText(),Toast.LENGTH_SHORT).show();
                    button.setBackgroundColor(Color.parseColor("#ff0000"));
                    button.setText("已关注");
                    Log.i("aa",textView.getText()+"已关注");
                    isCare = !isCare;
                }
            }
        });

        return v;
    }

}