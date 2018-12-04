package com.UI.EaseUI; /**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;

import com.example.user.zhtx.pojo.User;
import com.example.user.zhtx.tools.Address;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DemoApplication extends Application {

    public static Context applicationContext;
    private static DemoApplication instance;
    private static Handler handler;
    private static List<User> jsonListObject;
    private static EaseUI easeUI;
    private static Map<String, EaseUser> contactList = new HashMap<>();

    //用户 昵称 头像
    private String own_name;
    private String own_avater;

    //好友 昵称 头像
    private int i=0;

    private Context mcontext;

    public DemoApplication(){

    }

    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;

        init(applicationContext);
    }

    public static DemoApplication getInstance() {
        return instance;
    }

     public void reSet(Context context){
        this.mcontext = context;
        //重置头像 昵称
       System.out.println("进来设置头像");
      /*  Thread th_own = new MyThread();
        th_own.start();*/
        Thread th_friends = new MyThread2();
        th_friends.start();

        handler = new Handler(){
             @Override
             public void handleMessage(Message msg) {
                 super.handleMessage(msg);
                 switch (msg.what){
                     case 1:
                         //好友
                         break;
                     case 2:
                         jsonListObject = (List<User>) msg.obj;

                         System.out.println("123");

                         setEaseUIProviders();
                         break;
                     default:
                         break;
                 }
             }
         };
    }

    //初始化
    public void init(Context context) {
/*        DemoModel demoModel = new DemoModel(context);*/
        EMOptions options = initChatOptions();
        //use default options if options is null
        if (EaseUI.getInstance().init(context, options)) {
            applicationContext = context;
            //获取easeui实例
            easeUI = EaseUI.getInstance();
            //初始化easeui
            easeUI.init(applicationContext,options);
            //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
            EMClient.getInstance().setDebugMode(true);
        }


    }


    @Override
    //去除警告的错误
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    /*设置昵称和头像的方法*/
    private void setEaseUIProviders() {
        // set profile provider if you want easeUI to handle avatar and nickname
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });

    }

    //设置头像
    private EaseUser getUserInfo(String username){
        EaseUser user = null;
        //用户
        SharedPreferences userM= mcontext.getSharedPreferences("user", 0);

        //如果用户是本人，就设置自己的头像
        if(username.equals(EMClient.getInstance().getCurrentUser())){
            user=new EaseUser(username);
            user.setAvatar(Address.title + userM.getString("phonenum","") + ".jpeg");
            user.setNick(userM.getString("name",""));
            return user;
        }

        //设置联系人数据
        new Thread() {//需要在子线程中调用
            @Override
            public void run() {
                //需要设置联系人列表才能启动fragment
                contactList = getContact();
            }
        }.start();

       //收到别人的消息
        if (contactList!=null && contactList.containsKey(username)){
            user=contactList.get(username);
            //根据用户ID 设置会话头像 ，设置好友列表的头像
            for (User p : jsonListObject) {
                String Id = p.getPhonenum();
                if(username.equals(Id)){
                    String pic = p.getPic();
                    String f_pic = Address.title + pic + ".jpeg";
                    user.setNick(p.getName());
                    user.setAvatar(f_pic);
                }
            }
        }
        return user;
    }

    //设置
    private EMOptions initChatOptions(){
        EMOptions options = new EMOptions();
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执TODO 这个暂时有bug，上层收不到发送回执
        options.setRequireDeliveryAck(false);
        return options;
    }




    //获取环信好友体系
    private static Map<String, EaseUser> getContact() {
        Map<String, EaseUser> map = new HashMap<>();
        try {
            List<String> userNames = EMClient.getInstance().contactManager().getAllContactsFromServer();
//            KLog.e("......有几个好友:" + userNames.size());
            for (String userId : userNames) {
//                KLog.e("好友列表中有 : " + userId);
                EaseUser user = new EaseUser(userId);
                map.put(userId,user);
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }


        return map;
    }


    /*根据登陆用户 获取好友的昵称 头像*/
    static class MyThread2 extends Thread{
        public void run(){
     /*       String url = "http://172.17.146.102:8080/txzh/getFriendsList";*/
            OkHttpClient okHttpClient = new OkHttpClient();
            //登陆用户ID
            SharedPreferences user= applicationContext.getSharedPreferences("user", 0);
            int id = user.getInt("id",0);
            //3, 发起新的请求,获取返回信息
            RequestBody body = new FormBody.Builder()
                    .add("userid",id+"")//添加键值对
                    .add("uuid",user.getString("uuid",""))
                    .build();

            Request request = new Request.Builder()
                    .url(Address.GetFriends)
                    .post(body)
                    .build();

            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                 /*   System.out.println("返回:"+response.body().string());*/
                    Gson gson = new Gson();
                    List<User> jsonListObject = gson.fromJson(response.body().string(), new TypeToken<List<User>>(){}.getType());//把JSON格式的字
                    Message msg = new Message();
                    msg.what=2;
                    msg.obj=jsonListObject;
                    handler.sendMessage(msg);
                }
            });
        }
    }

}
