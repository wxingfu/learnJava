package com.weixf.utils.test;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;

/**
 *
 *
 * @since 2022-06-22
 */
public class Md5Util {

    public static String Md5(String data) {
        String charset = "UTF-8";
        return Md5(data, charset);
    }

    public static String Md5(String data, String charset) {
        try {
            return DigestUtils.md5Hex(data.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
