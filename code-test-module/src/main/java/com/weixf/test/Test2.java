package com.weixf.test;

import org.jdom2.JDOMException;

import java.io.IOException;

/**
 * @author wxf
 */
public class Test2 {


    public static void main(String[] args) throws IOException, JDOMException {
        System.out.printf("%s返回电视剧看风景的康师傅好烦好烦好烦好烦好烦好烦好烦好烦好烦好烦好烦好烦好烦好金克斯KIKIKIKIKIKIKIKI看的房间", "dfeqwfdewfdscjdsifj");
    }


    // 字符串转换unicode
    public static String string2Unicode(String string) {
        StringBuilder unicode = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u").append(Integer.toHexString(c));
        }
        return unicode.toString();
    }

    // unicode 转字符串
    public static String unicode2String(String unicode) {
        StringBuilder string = new StringBuilder();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }
        return string.toString();
    }

    public static String convert(String str) {
        str = (str == null ? "" : str);
        String tmp;
        StringBuilder sb = new StringBuilder(1000);
        char c;
        int i, j;
        sb.setLength(0);
        for (i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            sb.append("\\u");
            j = (c >>> 8); // 取出高8位
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
            j = (c & 0xFF); // 取出低8位
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);

        }
        return (new String(sb));
    }
}
