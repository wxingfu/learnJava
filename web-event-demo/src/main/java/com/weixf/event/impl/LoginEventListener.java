package com.weixf.event.impl;

import com.weixf.event.EventListener;

import java.util.Date;


public class LoginEventListener implements EventListener<UserLoginEvent> {

    public void onEvent(UserLoginEvent event) {

        System.out.println("用户 " + event.getUsername() + " 从IP " + event.getIpAddress() + " 登录，时间: " + new Date(event.getTimestamp()));
        // 可以在这里添加日志记录逻辑
    }

}
