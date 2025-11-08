package com.weixf.behavioral.template.old;

/**
 *
 *
 * @since 2022-08-23
 * 核心思路是把一些通用的标准方法，在抽象父类里仅定义方法签名，实现逻辑交给子类。
 */
public abstract class AbstractPushTemplate {

    public void push(int customerId, String shopName) {
        System.out.println("准备推送...");
        execute(customerId, shopName);
        System.out.println("推送完成\n");
    }

    abstract protected void execute(int customerId, String shopName);
}
