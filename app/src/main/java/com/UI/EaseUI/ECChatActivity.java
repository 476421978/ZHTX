package com.UI.EaseUI;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.user.zhtx.R;
import com.hyphenate.easeui.EaseConstant;


public class ECChatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_cchat);

        //new出EaseChatFragment或其子类的实例
        MyChatFragment chatFragment = new MyChatFragment();
        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);

        args.putString(EaseConstant.EXTRA_USER_ID, getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID));
        chatFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_chat_content, chatFragment).commit();
    }
}
