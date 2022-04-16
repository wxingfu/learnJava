package com.wxf.utils.secret;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import sun.security.provider.Sun;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.Security;


public class AESUtil {


    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String KEY_AES = "AES";

    /**
     * 加密
     *
     * @param data 需要加密的内容
     * @param key 加密密码
     * @return
     */
    public static String encryptForOpen(String data, String key) {

        return doAESForOpen(data, key, Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密
     *
     * @param data 待解密内容
     * @param key 解密密钥
     * @return
     */
    public static String decryptForOpen(String data, String key) {

        return doAESForOpen(data, key, Cipher.DECRYPT_MODE);
    }

    /**
     * 加解密
     *
     * @param data
     * @param key
     * @param mode
     * @return
     */

    private static String doAESForOpen(String data, String key, int mode) {

        try {
            if (StringUtils.isBlank(data) || StringUtils.isBlank(key)) {
                return null;
            }

            boolean encrypt = mode == Cipher.ENCRYPT_MODE;
            byte[] content;
            if (encrypt) {
                content = data.getBytes(DEFAULT_CHARSET);

            } else {
                content = Base64.decodeBase64(data.getBytes(DEFAULT_CHARSET));

            }
            SecretKey secretKey = getSecretKey(key);
            // 创建密钥
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getEncoded(), KEY_AES);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(KEY_AES);
            // 初始化
            cipher.init(mode, keySpec);

            byte[] result = cipher.doFinal(content);

            if (encrypt) {
                return new String(Base64.encodeBase64(result));

            } else {
                return new String(result, DEFAULT_CHARSET);

            }
        } catch (Exception e) {
            System.err.println("AES密文处理异常" + e);
            e.printStackTrace();

        }
        return null;
    }

    public static SecretKey getSecretKey(String key) {
        try {
            KeyGenerator gen = KeyGenerator.getInstance(KEY_AES);
            if (null == Security.getProvider("SUN")) {
                Security.addProvider(new Sun());
            }
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
            secureRandom.setSeed(key.getBytes());
            gen.init(128, secureRandom);
            return gen.generateKey();
        } catch (Exception e) {
            throw new RuntimeException("初始化秘钥出现异常 ");
        }
    }

    public static void main(String[] args) {
        // String encrypt = AESUtil.encryptForOpen(
        //         "{\"body\":{\"orderNo\":\"SD1542888157671918564334\",\"insuredUsers\":[{\"name\":\"杜克\",\"idCode\":\"510922198305214127\",\"idType\":1}]},\"supplierNo\":\"payb\"}",
        //         "0123456789ABCDEF");
        // System.out.println(encrypt);
        // String third = "ooAjaaOKU23wXU1KIsEMbkn38bgtta84/wYO3CIEvprcOm/Rzhje0p0aOemmz8SS2jS+lpge6R84uT3EETynDqwOZvSVSB9uQaoXv2LfbAzh/+vvR2t6/lv4bY5uPq7pAxYdnEJhYNafC1u4U+i0wUaoDpJ+KJlSeecwbtLi9oC4tDULq3lM7mUnPIqhgUzO";
        // System.out.println(encrypt.equals(third));
        //
        // String result = "N4+GmYYgP0WN3zgh8J5qcNzhLkWis2gsbZad7eEhczqHW9yF6bDZcofRQQKTSYI6v24BYuDoFDbtcPj/PUF0zR8PVdzbZsGOMl9K4S25Rfm0IHPKYNDedPqUk8IRtU+abmVm4AZLWXWZfW0lSWolxg==";
        // String decrypt = AESUtil.decryptForOpen(result, "0123456789ABCDEF");
        // System.out.println(decrypt);

        String s = AESUtil.encryptForOpen("{\"flag\":\"1\",\"tranNo\":\"SD_INTEL_16500031800081569428\"}",
                "0123456789ABCDEF");
        System.out.println(s);

        String s1 = AESUtil.decryptForOpen("7nLjiDAvi2lGz2dmwbdpIbPa0ac8awwt+uYjFgc4z7YLQwbaqlSCQovqgdsrNL0fOVBeKorVX8b75OO0jUZa2A==",
                "0123456789ABCDEF");
        System.out.println(s1);

    }
}
