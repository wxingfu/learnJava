package com.weixf.event;


public abstract class Event {

    private final long timestamp;

    private final Object source;


    public Event(Object source) {
        this.source = source;
        this.timestamp = System.currentTimeMillis();
    }


    public Object getSource() {
        return source;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
