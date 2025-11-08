package com.weixf.utils;

import com.weixf.utils.cache.CacheUtils;
import com.weixf.utils.file.FileUtils;

public class UtilsApp {

    public static void main(String[] args) throws InterruptedException {
        // 写入缓存数据
        CacheUtils.put("userName", "张三", 3);

        // 读取缓存数据
        Object value1 = CacheUtils.get("userName");
        System.out.println("第一次查询结果：" + value1);

        // 停顿4秒
        Thread.sleep(4000);

        // 读取缓存数据
        Object value2 = CacheUtils.get("userName");
        System.out.println("第二次查询结果：" + value2);

        FileUtils.searchFile("D:\\WorkSpace\\ideaProjects\\learnJava\\commons-util", "FileUtils.java");
    }

}
