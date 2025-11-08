package com.weixf.test;

import org.apache.commons.lang.RandomStringUtils;

import java.util.Random;

/**
 *
 */
public class Test4 {

    public static void main(String[] args) {
        // String ss = "520102198010014796, 520102198010019538, 520102198010013654, 520102198010019992, 520102198010019650, 520102198010013435, 520102198010013312, 520102198010014833, 520102198010013590, 520102198010011237";
        // if (ss.contains("520102198010019538")) {
        //     System.out.println("ooooooooooo");
        // }

        // String FORMATMODOL = "0.00"; //保费保额计算出来后的精确位数
        // DecimalFormat mDecimalFormat = new DecimalFormat(FORMATMODOL);
        // String format = mDecimalFormat.format(1.0E9);
        // System.out.println(format);
        // Double aa = 1000000.0 * 1000;
        // System.out.println(aa);
        // System.out.println(Double.parseDouble("0") * 100.0);

        // String transTime = CommonUtil.getCurrentDate("yyyyMMdd") + CommonUtil.getCurrentTime().replace(":", "");
        // System.out.println(transTime);
        //
        // String randomNickname = getRandomNickname(18);
        // System.out.println(randomNickname);

        // byte[] values = new byte[128];
        // SecureRandom random = new SecureRandom();
        // random.nextBytes(values);

        String s = RandomStringUtils.randomNumeric(18);
        System.out.println(s);
    }

    public static String getRandomNickname(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }
}
