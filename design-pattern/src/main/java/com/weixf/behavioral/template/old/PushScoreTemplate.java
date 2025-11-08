package com.weixf.behavioral.template.old;

/**
 *
 *
 * @since 2022-08-23
 */
public class PushScoreTemplate extends AbstractPushTemplate {
    @Override
    protected void execute(int customerId, String shopName) {
        System.out.println("会员:" + customerId + ",你好，" + shopName + "送您10个积分");
    }
}
