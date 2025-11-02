package com.weixf.test;

import java.io.*;
import java.util.*;

public class FileCopyUtil {

    /**
     * 从A文件夹查找与B文件夹同名但后缀不同的文件，并复制到C文件夹
     *
     * @param folderA 源文件夹A
     * @param folderB 对比文件夹B
     * @param folderC 目标文件夹C
     * @throws IOException IO异常
     */
    public static void copyDifferentExtensionFiles(String folderA, String folderB, String folderC) throws IOException {
        File dirA = new File(folderA);
        File dirB = new File(folderB);
        File dirC = new File(folderC);

        // 检查输入参数
        if (!dirA.exists() || !dirA.isDirectory()) {
            throw new IllegalArgumentException("文件夹A不存在或不是目录: " + folderA);
        }

        if (!dirB.exists() || !dirB.isDirectory()) {
            throw new IllegalArgumentException("文件夹B不存在或不是目录: " + folderB);
        }

        // 创建目标文件夹C（如果不存在）
        if (!dirC.exists()) {
            boolean mkdirs = dirC.mkdirs();
        }

        // 获取B文件夹中的所有文件名（不包含扩展名）
        Map<String, String> bFileNames = getFileBaseNames(dirB);

        // 遍历A文件夹中的文件
        File[] filesA = dirA.listFiles();
        if (filesA != null) {
            for (File fileA : filesA) {
                if (fileA.isFile()) {
                    String baseName = getBaseName(fileA.getName());

                    // 检查B文件夹中是否存在同名但不同后缀的文件
                    if (bFileNames.containsKey(baseName)) {
                        // 复制文件到C文件夹
                        File targetFile = new File(dirC, fileA.getName());
                        copyFile(fileA, targetFile);
                        System.out.println("已复制文件: " + fileA.getName());
                    }
                }
            }
        }
    }

    /**
     * 获取文件夹中所有文件的基本名称（不含扩展名）和扩展名映射
     *
     * @param directory 文件夹
     * @return 基本名称到扩展名的映射
     */
    private static Map<String, String> getFileBaseNames(File directory) {
        Map<String, String> baseNames = new HashMap<String, String>();
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String baseName = getBaseName(file.getName());
                    String extension = getExtension(file.getName());
                    baseNames.put(baseName, extension);
                }
            }
        }

        return baseNames;
    }

    /**
     * 获取文件名的基本名称（不含扩展名）
     *
     * @param fileName 文件名
     * @return 基本名称
     */
    private static String getBaseName(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            return fileName.substring(0, dotIndex);
        }
        return fileName;
    }

    /**
     * 获取文件的扩展名
     *
     * @param fileName 文件名
     * @return 扩展名
     */
    private static String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

    /**
     * 复制文件
     *
     * @param source 源文件
     * @param target 目标文件
     * @throws IOException IO异常
     */
    private static void copyFile(File source, File target) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(target);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // 忽略
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // 忽略
                }
            }
        }
    }

    // 使用示例
    public static void main(String[] args) {
        try {
            // 调用方法示例
            FileCopyUtil.copyDifferentExtensionFiles("D:\\WorkSpace\\vscode\\test\\A", "D:\\WorkSpace\\vscode\\test\\B", "D:\\WorkSpace\\vscode\\test\\C");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
