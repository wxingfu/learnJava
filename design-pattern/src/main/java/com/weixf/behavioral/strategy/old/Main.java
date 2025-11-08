package com.weixf.behavioral.strategy.old;

/**
 *
 *
 * @since 2022-08-23
 */
public class Main {
    public static void main(String[] args) {

        OrderServiceStrategy executor1 = new OrderServiceStrategy(new MySqlSaveOrderStrategy());
        executor1.save("001");
        OrderServiceStrategy executor2 = new OrderServiceStrategy(new NoSqlSaveOrderStrategy());
        executor2.save("002");

        OrderServiceStrategy executor3 = new OrderServiceStrategy(
                (String orderNo) -> System.out.println("order:" + orderNo + " save to mysql"));
        executor3.save("001");

        OrderServiceStrategy executor4 = new OrderServiceStrategy(
                (String orderNo) -> System.out.println("order:" + orderNo + " save to nosql"));
        executor4.save("002");
    }
}
