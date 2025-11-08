package com.weixf.behavioral.observer.old;

/**
 *
 *
 * @since 2022-08-23
 */
public class Main {
    public static void main(String[] args) {
        Subject subject = new SubjectImpl();
        subject.registerObserver(new OrderObserver());
        subject.registerObserver(new StockObserver());
        subject.notifyAllObserver("001");

        NewSubject newSubject = new NewSubject() {
        };
        newSubject.registerObserver((String orderNo) -> System.out.println("订单 " + orderNo + " 状态更新为【已支付】"));
        newSubject.registerObserver((String orderNo) -> System.out.println("订单 " + orderNo + " 已通知库房发货！"));
        newSubject.notifyAllObserver("002");
    }
}
