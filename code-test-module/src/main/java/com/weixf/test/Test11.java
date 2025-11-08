package com.weixf.test;

import java.util.concurrent.Callable;

/**
 *
 *
 * @since 2022-06-13
 */
public class Test11 {
    public static void main(String[] args) throws Exception {

        // String date = "2022-06-13";
        // String replace = date.replaceAll("-", "").substring(0,6);
        // System.out.println(replace);

        // String str = "";
        //
        // JSONObject jsonObject = JSONObject.parseObject(str);
        // String responseCode = (String) JSONPath.read(jsonObject.toJSONString(), "$header.responseCode");
        // System.out.println(responseCode);

        // MyThread mt1 = new MyThread();
        // MyThread mt2 = new MyThread();
        // FutureTask<String> task1 = new FutureTask<String>(mt1);
        // FutureTask<String> task2 = new FutureTask<String>(mt2);
        //
        // //FutureTask是Runnable接口的实现类/子类，所以Thread构造方法可以接收其对象
        // new Thread(task1).start();
        // new Thread(task2).start();
        //
        // System.out.println("B" + task2.get());
        // //FutureTask同时是Future接口的实现类/子类，父接口的get方法可以获取其值
        // System.out.println("A" + task1.get());

        // long l = System.currentTimeMillis();
        // System.out.println(l);

        // System.out.println();
    }

    static class MyThread implements Callable<String> {
        private final int ticket = 10;

        @Override
        public String call() {
            // for (int i = 0; i < 200; i++) {
            //     if (this.ticket > 0) {
            //         System.out.println("ticket=" + ticket--);
            //     }
            // }
            return "ticket sold out!";
        }
    }

}
