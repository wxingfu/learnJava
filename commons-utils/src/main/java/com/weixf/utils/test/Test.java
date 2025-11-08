package com.weixf.utils.test;


import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;

/**
 *
 *
 * @since 2022-06-28
 */
public class Test {

    public static void main(String[] args) throws Exception {
        String base = "C:\\Users\\weixf\\Desktop\\B树和B+树.assets\\";
        File imageFile = new File(base + "image-20220816214607760.png");
        FileInputStream fis = new FileInputStream(imageFile);
        byte[] fileBytes = new byte[(int) imageFile.length()];
        int read = fis.read(fileBytes);
        fis.close();
        String imageBase64String = Base64.getEncoder().encodeToString(fileBytes);
        String imageBase64 = "data:image;base64," + imageBase64String;
        System.out.println(imageBase64);
    }
}
