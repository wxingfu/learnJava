package com.weixf.demo.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weixf.demo.web.util.AesUtil;
import com.weixf.demo.web.util.Md5Util;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 *
 *
 * @since 2022-07-04
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,
                new SecretKeySpec(encryptKey.getBytes(), "AES"),
                new IvParameterSpec(new byte[16])
        );
        byte[] bytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
    }

    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        if (ObjectUtils.isEmpty(encryptStr)) {
            return null;
        }
        byte[] encryptBytes = org.apache.commons.codec.binary.Base64.decodeBase64(encryptStr);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,
                new SecretKeySpec(decryptKey.getBytes(), "AES"),
                new IvParameterSpec(new byte[16])
        );
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes, StandardCharsets.UTF_8);
    }

    @PostMapping(value = "/test")
    public String test(@RequestBody String param) {

        System.out.println("入参：" + param);
        JSONObject jsonObject = JSON.parseObject(param);
        String data = (String) jsonObject.get("data");
        String decrypt = AesUtil.decrypt(data, "ABCDEF");
        byte[] decode = Base64.getDecoder().decode(decrypt);
        String out = new String(decode);
        System.out.println("解密后：" + out);

        String base64String = Base64.getEncoder().encodeToString(out.getBytes());
        String aesString = AesUtil.encrypt(base64String, "ABCDEF");
        String signString = Md5Util.Md5(base64String + "123456");
        String params = "{\"data\":\"" + aesString + "\",\"sign\":\"" + signString + "\"}";
        System.out.println("出参：" + params);

        JSONObject parseObject = JSONObject.parseObject(params);
        System.out.println(parseObject);
        data = (String) jsonObject.get("data");
        decrypt = AesUtil.decrypt(data, "ABCDEF");
        decode = Base64.getDecoder().decode(decrypt);
        out = new String(decode);
        System.out.println("解密后：" + out);

        return params;
    }

    @PostMapping("/test2")
    public String test2(@RequestBody String param) {
        System.out.println("入参：" + param);
        String decrypt = null;
        try {
            decrypt = aesDecrypt(param, "c0d6720fdc834128");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("解密后：" + decrypt);

        Document document = new Document();
        Element tTranData = new Element("TranData");
        Element tHead = new Element("Head");
        tHead.addContent(new Element("Flag").setText("0"));
        tHead.addContent(new Element("Desc").setText("成功"));
        tTranData.addContent(tHead);
        document.addContent(tTranData);

        Format format = Format.getPrettyFormat();
        format.setEncoding("UTF-8");
        format.setIndent("  ");
        format.setExpandEmptyElements(true);
        XMLOutputter outputter = new XMLOutputter(format);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String res = "";
        try {
            outputter.output(document, baos);
            baos.close();
            res = baos.toString("UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String encrypt = null;
        try {
            encrypt = aesEncrypt(res, "c0d6720fdc834128");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("加密后：" + encrypt);
        return encrypt;
    }
}
