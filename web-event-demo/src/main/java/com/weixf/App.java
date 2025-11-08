package com.weixf;



public class App {

    public static void main(String[] args) {


        /*
         * 最佳实践：5条生存法则
         *
         * 1.单一职责原则一个监听器只做一件事：如 PaymentListener 只处理支付，CouponListener 只发券
         * 2.事件轻量化禁止在事件中携带 HttpSession 等重型对象（建议只传ID）
         * 3.异常隔离舱： 异步事件必须独立捕获异常
         * 4.版本兼容设计: 事件类预留版本字段
         * 5.监控三件套： 监控处理时长/失败率/QPS
         *
         */
    }

}
