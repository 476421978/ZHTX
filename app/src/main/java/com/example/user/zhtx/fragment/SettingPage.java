package com.example.user.zhtx.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.zhtx.LoginActivity;
import com.example.user.zhtx.R;
import com.example.user.zhtx.activity.ChangePasswordByPwd;
import com.example.user.zhtx.activity.FriendsManagementActivity;
import com.example.user.zhtx.activity.SeeAreaActivity;
import com.example.user.zhtx.activity.SelfInfoActivity;
import com.example.user.zhtx.tools.SharedPreferencesControl;
import com.example.user.zhtx.tools.ShowToast;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.loopj.android.image.SmartImageView;

public class SettingPage extends Fragment implements View.OnClickListener {
    private LinearLayout lin_changePassword,lin_seeArea,lin_selfInfo,lin_friendsManagement;
    private Button btn_loginOut;
    private TextView tv_phoneNumber,tv_name;
    private SmartImageView siv_head;
    private View v;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle) {
        v = inflater.inflate(R.layout.activity_setting, group, false);
        initView();

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView(){
        lin_selfInfo = (LinearLayout)v.findViewById(R.id.activity_setting_lin_selfInfo);
        lin_selfInfo.setOnClickListener(this);
        lin_seeArea = (LinearLayout)v.findViewById(R.id.activity_setting_lin_seeArea);
        lin_seeArea.setOnClickListener(this);
        lin_changePassword = (LinearLayout)v.findViewById(R.id.activity_setting_lin_changePassword);
        lin_changePassword.setOnClickListener(this);
        lin_friendsManagement = (LinearLayout)v.findViewById(R.id.activity_setting_lin_friendsManage);
        lin_friendsManagement.setOnClickListener(this);

        btn_loginOut= (Button)v.findViewById(R.id.activity_setting_btn_loginOut);
        btn_loginOut.setOnClickListener(this);


        SharedPreferences sp = getActivity().getSharedPreferences("user",getActivity().MODE_PRIVATE);
        tv_name = (TextView)v.findViewById(R.id.activity_setting_tv_name);
        tv_name.setText(sp.getString("name","未找到"));
        tv_phoneNumber = (TextView) v.findViewById(R.id.activity_setting_tv_phoneNumber);
        tv_phoneNumber.setText("手机号："+sp.getString("phonenum","未找到"));

        siv_head = (SmartImageView)v.findViewById(R.id.activity_setting_siv_head);
        siv_head.setBackground(null);
        siv_head.setImageUrl(sp.getString("picPath",""),R.mipmap.ic_launcher);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_setting_lin_selfInfo:
                ShowToast.show(getActivity(),"个人信息");
                Intent intent1 = new Intent(getActivity(),SelfInfoActivity.class);
                startActivity(intent1);
                break;
            case R.id.activity_setting_lin_seeArea:
                ShowToast.show(getActivity(),"可见范围");
                Intent intent2 = new Intent(getActivity(),SeeAreaActivity.class);
                startActivity(intent2);
                break;
            case R.id.activity_setting_lin_changePassword:
                ShowToast.show(getActivity(),"修改密码");
                Intent intent3 = new Intent(getActivity(),ChangePasswordByPwd.class);
                startActivity(intent3);
                break;
            case R.id.activity_setting_btn_loginOut:
                ShowToast.show(getActivity(),"注销");
                signOut();
                SharedPreferencesControl control = new SharedPreferencesControl(getActivity());
                control.loginOut();
                Intent intent4 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent4);
                break;
            case R.id.activity_setting_lin_friendsManage:
                Intent intent5 = new Intent(getActivity(), FriendsManagementActivity.class);
                startActivity(intent5);
                break;

        }
    }

    /**
     * 退出登录
     */
    private void signOut() {
        // 调用sdk的退出登录方法，第一个参数表示是否解绑推送的token，没有使用推送或者被踢都要传false
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override public void onSuccess() {
                Log.i("用户退出", "logout success");
                // 调用退出成功，结束app
            }

            @Override public void onError(int i, String s) {
                Log.i("用户退出", "logout error " + i + " - " + s);
            }

            @Override public void onProgress(int i, String s) {
                Log.i("用户退出", "logout error " + i + " - " + s);
            }
        });
    }

}