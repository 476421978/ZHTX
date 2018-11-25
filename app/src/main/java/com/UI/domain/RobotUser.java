package com.UI.domain;

import com.hyphenate.easeui.domain.EaseUser;

/**
 * Created by huang on 2018/11/8.
 */

public class RobotUser extends EaseUser {
    public RobotUser(String username) {
        super(username.toLowerCase());
    }
}