package com.weixf.event.impl;

import com.weixf.event.EventListener;

import java.util.Date;


/**
 * 错误做法：在事件处理中发布新事件
 */
public class LoginEventListener implements EventListener<UserLoginEvent> {

    public void onEvent(UserLoginEvent event) {

        System.out.println("用户 " + event.getUsername() + " 从IP " + event.getIpAddress() + " 登录，时间: " + new Date(event.getTimestamp()));
        // 可以在这里添加日志记录逻辑
    }

}
