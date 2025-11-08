package com.weixf.utils.http;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @since 2022-04-08
 */
public class UrlUtil {


    /**
     * 解析url
     *
     * @param url
     * @return
     */
    public static UrlEntity parse(String url) {
        UrlEntity entity = new UrlEntity();
        if (url == null) {
            return entity;
        }
        url = url.trim();
        if (url.equals("")) {
            return entity;
        }
        String[] urlParts = url.split("\\?");
        entity.baseUrl = urlParts[0];
        // 没有参数
        if (urlParts.length == 1) {
            return entity;
        }
        // 有参数
        String[] params = urlParts[1].split("&");
        entity.params = new HashMap<>();
        for (String param : params) {
            String[] keyValue = param.split("=");
            entity.params.put(keyValue[0], keyValue[1]);
        }

        return entity;
    }

    public static void main(String[] args) {
        UrlEntity entity = parse(null);
        // System.out.println(entity.baseUrl + "\n" + entity.params);
        //
        // entity = parse("http://www.123.com");
        // System.out.println(entity.baseUrl + "\n" + entity.params);
        //
        // entity = parse("http://www.123.com?id=1");
        // System.out.println(entity.baseUrl + "\n" + entity.params);
        //
        // entity = parse("http://www.123.com?id=1&name=小明");
        // System.out.println(entity.baseUrl + "\n" + entity.params);

        entity = parse("http://kutta-risk-manage-1254024480.cos.ap-beijing.myqcloud.com/cfcd208495d565ef66e7dff9f98764da/1649303609484340706198201014478.jpeg?sign=q-sign-algorithm%3Dsha1%26q-ak%3DAKID0oMegRP8anjOnwpQkna5JL6apoWx70cx%26q-sign-time%3D1649303616%3B1649390016%26q-key-time%3D1649303616%3B1649390016%26q-header-list%3Dhost%26q-url-param-list%3D%26q-signature%3D6308bfc36026035781c4d02ada6a53a11ca79faa");
        System.out.println(entity.baseUrl + "\n" + entity.params);
    }

    public static class UrlEntity {
        /**
         * 基础url
         */
        public String baseUrl;
        /**
         * url参数
         */
        public Map<String, String> params;
    }
}
