package com.weixf.test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class FileTypeCounter {

    public static void main(String[] args) {
        // 指定要统计的文件夹路径
        String folderPath = "D:/WorkSpaces/ideaProjects/huaguilife/xxxxxxxxxxx/new_sit/lis";

        // 统计文件类型
        Set<String> fileTypes = countFileTypes(folderPath);

        // 输出结果
        System.out.println("文件夹中共有 " + fileTypes.size() + " 种文件类型:");
        for (String fileType : fileTypes) {
            System.out.println(fileType);
        }
    }

    /**
     * 统计文件夹下的文件类型
     * @param folderPath 文件夹路径
     * @return 文件类型的集合
     */
    public static Set<String> countFileTypes(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("指定的路径不是一个有效的文件夹: " + folderPath);
        }

        Set<String> fileTypes = new HashSet<>();
        countFileTypesRecursive(folder, fileTypes);
        return fileTypes;
    }

    /**
     * 递归遍历文件夹，统计文件类型
     * @param folder 文件夹对象
     * @param fileTypes 文件类型的集合
     */
    private static void countFileTypesRecursive(File folder, Set<String> fileTypes) {
        File[] files = folder.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                countFileTypesRecursive(file, fileTypes);
            } else {
                String extension = getFileExtension(file);
                if (extension != null && !extension.isEmpty()) {
                    fileTypes.add(extension);
                }
            }
        }
    }

    /**
     * 获取文件的扩展名
     * @param file 文件对象
     * @return 文件扩展名
     */
    private static String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return null;
    }
}
