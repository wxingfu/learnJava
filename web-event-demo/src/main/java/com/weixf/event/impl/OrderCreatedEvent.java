package com.weixf.event.impl;

import com.weixf.event.Event;


public class OrderCreatedEvent extends Event {
    private final String orderId;
    private final String customerId;
    private final double amount;

    public OrderCreatedEvent(Object source, String orderId, String customerId, double amount) {
        super(source);
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
    }

    // getter方法
    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getAmount() {
        return amount;
    }
}
