package com.weixf.event.impl;


import com.weixf.event.Event;


public class UserLogoutEvent extends Event {
    private final String username;

    public UserLogoutEvent(Object source, String username) {
        super(source);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

