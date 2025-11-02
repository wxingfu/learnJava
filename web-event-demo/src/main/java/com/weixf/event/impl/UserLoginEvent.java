package com.weixf.event.impl;


import com.weixf.event.Event;


/**
 * 事件类设计为final字段 + 无setter
 */
public class UserLoginEvent extends Event {

    private final String username;

    private final String ipAddress;

    public UserLoginEvent(Object source, String username, String ipAddress) {
        super(source);
        this.username = username;
        this.ipAddress = ipAddress;
    }

    public String getUsername() {
        return username;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}
