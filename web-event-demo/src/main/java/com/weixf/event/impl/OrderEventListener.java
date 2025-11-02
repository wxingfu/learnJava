package com.weixf.event.impl;

import com.weixf.event.EventListener;


public class OrderEventListener implements EventListener<OrderCreatedEvent> {


    public void onEvent(OrderCreatedEvent event) {
        // 处理订单创建后的业务逻辑
        System.out.println("订单 " + event.getOrderId() + " 已创建，客户: " + event.getCustomerId() + "，金额: " + event.getAmount());
        // 可以发送邮件通知、更新库存等
    }

}

