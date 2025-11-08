package com.weixf.behavioral.strategy.old;

/*
 *
 *
 * @since 2022-08-23
 * 策略实现2
 */
public class MySqlSaveOrderStrategy implements OrderService {
    @Override
    public void saveOrder(String orderNo) {
        System.out.println("order:" + orderNo + " save to mysql");
    }
}
