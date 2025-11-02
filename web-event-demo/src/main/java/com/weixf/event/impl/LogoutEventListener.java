package com.weixf.event.impl;

import com.weixf.event.EventListener;

import java.util.Date;


public class LogoutEventListener implements EventListener<UserLogoutEvent> {


    public void onEvent(UserLogoutEvent event) {
        System.out.println("用户 " + event.getUsername() + " 已登出，时间: " + new Date(event.getTimestamp()));
        // 可以在这里添加清理会话等逻辑
    }

}
