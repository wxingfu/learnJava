package com.weixf.behavioral.template.old;

import java.util.function.Consumer;

/**
 *
 *
 * @since 2022-08-23
 * 如果模板的实现方式越多，子类就越多。使用java8重构后，可以把上面的3个模板（包括抽象类模板）减少到1个
 * 借助Consumer<T>这个function interface，可以省去实现子类，具体的实现留到使用时再来决定
 */
public class PushTemplateLambda {

    public void push(int customerId, String shopName, Consumer<Object[]> execute) {
        System.out.println("准备推送...");
        Object[] param = new Object[]{customerId, shopName};
        execute.accept(param);
        System.out.println("推送完成\n");
    }
}
