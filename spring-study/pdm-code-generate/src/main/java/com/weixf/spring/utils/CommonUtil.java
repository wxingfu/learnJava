package com.weixf.spring.utils;

import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * <p>
 * 这个类功能：
 * 1. 用来判断包路径是否存在，若不存在，即创建文件夹；
 * 2. 将配好的模板文件写出到指定路径下的文件
 * 3. 将配好的模板文件写出到控制台
 */
public class CommonUtil {


    /**
     * 判断包路径是否存在
     *
     * @param path 路径
     */
    private static void pathJudgeExist(String path) {
        File file = new File(path);
        if (!file.exists()) {
            boolean b = file.mkdirs();
        }
    }


    /**
     * @param root     要在 Templet 中替换的内容
     * @param template Templete 实例
     * @param filePath 生成文件的路径
     * @param fileName 生成文件的名字
     * @throws Exception 异常
     */
    public static void printFile(Map<String, Object> root, Template template, String filePath, String fileName) throws Exception {
        pathJudgeExist(filePath);
        File file = new File(filePath, fileName);
        if (!file.exists()) {
            boolean b = file.createNewFile();
        }
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));
        template.process(root, out);
        out.close();
    }


    /**
     * 输出到控制台
     *
     * @param root     要在 Templet 中替换的内容
     * @param template Templete 实例
     */
    public static void printConsole(Map<String, Object> root, Template template) throws TemplateException, IOException {
        StringWriter out = new StringWriter();
        template.process(root, out);
        System.out.println(out);
    }


    /**
     * 首字母大写
     */
    public static String cap_first(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }


    /***
     * 下划线命名转为驼峰命名
     *
     * @param para 下划线命名的字符串
     */
    public static String underlineToHump(String para) {
        StringBuilder result = new StringBuilder();
        String[] a = para.split("_");
        for (String s : a) {
            if (result.length() == 0) {
                result.append(s);
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }


    /**
     * 将[数据库类型]转换成[Java类型],如果遇到没有写的类型,会出现Undefine,在后面补充即可
     *
     * @param columnType 数据库类型
     * @return Java类型
     */
    public static String convert2Java(String columnType) {
        String result;
        switch (columnType) {
            case "VARCHAR":
            case "CHAR": {
                result = "String";
                break;
            }
            case "INT":
            case "INTEGER": {
                result = "Integer";
                break;
            }
            case "BIGINT": {
                result = "Long";
                break;
            }
            case "FLOAT": {
                result = "Float";
                break;
            }
            case "DOUBLE": {
                result = "Double";
                break;
            }
            case "DATETIME": {
                result = "Date";
                break;
            }
            case "BIT": {
                result = "Boolean";
                break;
            }
            default: {
                result = "Undefine";
                break;
            }
        }
        return result;
    }


    /***
     * 驼峰命名转为下划线命名
     *
     * @param para
     *        驼峰命名的字符串
     */

    public String humpToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;
        for (int i = 0; i < para.length(); i++) {
            if (Character.isUpperCase(para.charAt(i))) {
                sb.insert(i + temp, "_");
                temp += 1;
            }
        }
        return sb.toString().toUpperCase();
    }

}
