package com.weixf.test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 *
 *
 * @since 2023-04-26
 */
public class Test14 {

    public static void main(String[] args) {
        // boolean flag = true; // 设置成true，保证条件表达式的表达式二一定可以执行
        // boolean simpleBoolean = false; // 定义一个基本数据类型的boolean 变量
        // Boolean nullBoolean = null;// 定义一个包装类对象类型的Boolean 变量，值为null
        // boolean x = flag ? nullBoolean : simpleBoolean; // 使用三目运算符并给x 变量赋值
        // System.out.println(x);

        // Integer a = 1;
        // Integer b = 2;
        // Integer c = null;
        // Boolean flag = false;
        // Integer result = flag ? a * b : c;
        // System.out.println(result);


        // int aHundredMillion = 10000000;
        // Map<Integer, Integer> map = new HashMap<>();
        // long s1 = System.currentTimeMillis();
        // for (int i = 0; i < aHundredMillion; i++) {
        //     map.put(i, i);
        // }
        // long s2 = System.currentTimeMillis();
        // System.out.println(" 未初始化容量，耗时 ： " + (s2 - s1));
        // Map<Integer, Integer> map1 = new HashMap<>(aHundredMillion / 2);
        // long s5 = System.currentTimeMillis();
        // for (int i = 0; i < aHundredMillion; i++) {
        //     map1.put(i, i);
        // }
        // long s6 = System.currentTimeMillis();
        // System.out.println(" 初始化容量5000000，耗时 ： " + (s6 - s5));
        // Map<Integer, Integer> map2 = new HashMap<>(aHundredMillion);
        // long s3 = System.currentTimeMillis();
        // for (int i = 0; i < aHundredMillion; i++) {
        //     map2.put(i, i);
        // }
        // long s4 = System.currentTimeMillis();
        // System.out.println(" 初始化容量为10000000，耗时 ： " + (s4 - s3));


        BigDecimal g = BigDecimal.valueOf(0.1F);

        Map<String, Object> map = null;
        if (map.isEmpty()) {

        }

    }

}


class ExecutorsDemo {
    private static ExecutorService executor = Executors.newFixedThreadPool(15);

    public static void main(String[] args) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            executor.execute(new SubThread());
        }
    }
}

class SubThread implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // do nothing
        }
    }
}


