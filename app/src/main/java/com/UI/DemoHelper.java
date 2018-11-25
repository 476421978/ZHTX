package com.UI;


import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by huang on 2018/11/8.
 */
public class DemoHelper {
    private Map<String, EaseUser> contactList;
    private DemoModel demoModel = null;
    private static DemoHelper instance = null;
    private String username;//当前用户名


    public synchronized static DemoHelper getInstance() {
        if (instance == null) {
            instance = new DemoHelper();
        }
        return instance;
    }


    /**
     * if ever logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    /**
     * get contact list
     *
     * @return
     */
    public Map<String, EaseUser> getContactList() {
        if (isLoggedIn() && contactList == null) {
            contactList = demoModel.getContactList();
        }
        // return a empty non-null object to avoid app crash
        if(contactList == null){
            return new Hashtable<String, EaseUser>();
        }
        return contactList;
    }

    /**
     * get current user's id
     */
    public String getCurrentUsernName(){
        if(username == null){
            username = demoModel.getCurrentUsernName();
        }
        return username;
    }




    /**
     * save single contact
     */
    public void saveContact(EaseUser user){
        contactList.put(user.getUsername(), user);
        demoModel.saveContact(user);
    }

    public DemoModel getModel(){
        return (DemoModel) demoModel;
    }
}
