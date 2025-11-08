package com.weixf.test;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Test3 {

    public static void main(String[] args) {

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");

        // 每页显示多小数据
        Integer pageSize = 2;

        // 总页数
        Integer totalPage = list.size() / pageSize;

        // 余数计算
        Integer mod = list.size() % pageSize;

        // 如果有余数总页数+1
        if (mod > 0) {
            totalPage = totalPage + 1;
        }

        // 迭代取出每页内容
        for (int pageNo = 0; pageNo < totalPage; pageNo++) {
            Integer start = pageSize * pageNo;

            Integer end = pageSize * (pageNo + 1);

            // 避免超出列表最大界
            if (end > list.size()) {
                end = list.size();
            }

            System.out.println("start:" + start + ",end:" + end);

            List<String> subList = list.subList(start, end);

            System.out.println(StringUtils.join(subList, ","));
        }
    }
}
