package com.UI;

import android.content.Context;

import com.UI.db.UserDao;
import com.UI.utils.PreferenceManager;
import com.hyphenate.easeui.domain.EaseUser;

import java.security.Key;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huang on 2018/11/8.
 */

public class DemoModel {
    UserDao dao = null;
    protected Context context = null;
    protected Map<Key,Object> valueCache = new HashMap<Key,Object>();


    public DemoModel(Context ctx){
        context = ctx;
        PreferenceManager.init(context);
    }

    /*
    * 保存联系人列表
    * */
    public boolean saveContactList(List<EaseUser> contactList) {
        UserDao dao = new UserDao(context);
        dao.saveContactList(contactList);
        return true;
    }

    public Map<String, EaseUser> getContactList() {
        UserDao dao = new UserDao(context);
        return dao.getContactList();
    }

    public String getCurrentUsernName(){
        return PreferenceManager.getInstance().getCurrentUsername();
    }

    public void saveContact(EaseUser user){
        UserDao dao = new UserDao(context);
        dao.saveContact(user);
    }
    public boolean isMsgRoaming() {
        return PreferenceManager.getInstance().isMsgRoaming();
    }
    public boolean isShowMsgTyping() {
        return PreferenceManager.getInstance().isShowMsgTyping();
    }
}
