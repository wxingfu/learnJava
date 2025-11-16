package com.weixf.test;

import org.apache.log4j.Logger;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class GbkToUtf8Converter {

    private static final Logger logger = Logger.getLogger(GbkToUtf8Converter.class);

    public static void main(String[] args) throws IOException {
        String inFilePath = "D:/WorkSpaces/ideaProjects/huaguilife/xxxxxxxxxxx/new_sit/lis/";
        String outFilePath = "D:/WorkSpaces/ideaProjects/huaguilife/xxxxxxxxxxx_new/new_sit/lis/";
        converter(inFilePath, outFilePath);

        logger.debug("转换完成");

        Files.walk(Paths.get(outFilePath))
                .filter(Files::isRegularFile)
                .filter(path -> Stream.of(".jsp", ".xml", ".html", ".htm").anyMatch(path.toString()::endsWith))
                .forEach(path -> {
                    try {
                        List<String> lines = Files.readAllLines(Paths.get(String.valueOf(path)));
                        for (int i = 0; i < lines.size(); i++) {
                            String line = lines.get(i);
                            line = line.replaceAll("charset=GBK", "charset=UTF-8")
                                    .replaceAll("charset='GBK'", "charset=UTF-8")
                                    .replaceAll("charset=gbk", "charset=UTF-8")
                                    .replaceAll("charset='gbk'", "charset=UTF-8")
                                    .replaceAll("charset=GB2312", "charset=UTF-8")
                                    .replaceAll("charset='GB2312'", "charset=UTF-8")
                                    .replaceAll("charset=gb2312", "charset=UTF-8")
                                    .replaceAll("charset='gb2312'", "charset=UTF-8")
                                    .replaceAll("charset=ISO-8859-1", "charset=UTF-8")
                                    .replaceAll("charset='ISO-8859-1'", "charset=UTF-8")
                                    .replaceAll("<?xml version=\"1.0\" encoding=\"GB2312\"?>", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                                    .replaceAll("<?xml version=\"1.0\" encoding=\"gb2312\"?>", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                                    .replaceAll("<?xml version=\"1.0\" encoding=\"GBK\"?>", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                                    .replaceAll("<?xml version=\"1.0\" encoding=\"gbk\"?>", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                            ;
                            lines.set(i, line);
                        }
                        Files.write(Paths.get(String.valueOf(path)), lines);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        logger.debug("转换完成");
    }

    private static final List<String> list = Arrays.asList(".css", ".lic", ".cert", ".bak", ".p12", ".java", ".r416", ".bat", ".xml", ".dat",
            ".tmp", ".pem", ".mf", ".html", ".propertise", ".jar", ".xml20170613", ".r659", ".drl", ".zip", ".rar", ".ocx", ".pspimage",
            ".ktr", ".png", ".jbf", ".bak20160607", ".r29", ".jsb", ".doc", ".psd", ".jbx", ".cur", ".pfx", ".bakbak", ".gif", ".cab",
            ".jsp", ".js", ".sql", ".xsl", ".cer", ".sh", ".htc", ".map", ".class", ".ftl", ".policy", ".htm", ".jpg", ".mine", ".xlsx",
            ".fts", ".ini", ".svg", ".lpk", ".ttf", ".bcmap", ".store", ".wsdd", ".r42", ".txt", ".r", ".pdf", ".vts", ".config",
            ".properties", ".xls");

    private static final List<String> suffixList = Arrays.asList(".css", ".lic", ".java", ".bat", ".xml",
            ".dat", ".pem", ".mf", ".html", ".propertise", ".drl", ".ktr", ".jsb", ".jbx", ".jsp", ".js", ".sql",
            ".xsl", ".sh", ".htc", ".map", ".ftl", ".policy", ".htm", ".ini", ".wsdd", ".txt", ".r", ".config", ".properties");

    private static final List<String> deelteSuffixList = Arrays.asList(".bak", ".tmp", ".xml20170613", ".r659", ".r416",
            ".pspimage", ".bak20160607", ".r29", ".psd", ".bakbak", ".class", ".mine", ".r42");

    private static void converter(String inFilePath, String outFilePath) throws IOException {
        Files.walk(Paths.get(inFilePath))
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    File inFile = new File(String.valueOf(path));
                    String inFileAbsolutePath = inFile.getAbsolutePath();
                    logger.debug("开始处理: " + inFileAbsolutePath);
                    String inFileName = inFile.getName();
                    // logger.debug("开始处理: " + listFileName);

                    String inFileSuffix = "";
                    if (inFileName.lastIndexOf(".") != -1) {
                        inFileSuffix = inFileName.substring(inFileName.lastIndexOf("."));
                    }

                    // 获取文件的编码
                    String encoding = detectFileEncoding(inFile);
                    logger.debug("文件编码: " + encoding);
                    if (suffixList.contains(inFileSuffix.toLowerCase())) {
                        try {
                            convertFileEncoding(inFileAbsolutePath, encoding, "UTF-8", inFileAbsolutePath.replace("xxxxxxxxxxx", "xxxxxxxxxxx_new"));
                            logger.debug("文件 " + inFileAbsolutePath + " 转换成功");
                        } catch (IOException e) {
                            logger.error("文件 " + inFileAbsolutePath + " 转换失败: " + e.getMessage(), e);
                        }
                    } else if (deelteSuffixList.contains(inFileSuffix.toLowerCase())) {
                        logger.debug("应删除: " + inFileAbsolutePath);
                    } else {
                        try {
                            File outFile = new File(inFileAbsolutePath.replace("xxxxxxxxxxx", "xxxxxxxxxxx_new"));
                            if (!outFile.getParentFile().exists()) {
                                boolean mkdirs = outFile.getParentFile().mkdirs();
                            }
                            Files.copy(Paths.get(inFileAbsolutePath), Paths.get(outFile.getAbsolutePath()));
                            logger.debug("文件 " + inFileAbsolutePath + " 复制成功");
                        } catch (IOException e) {
                            logger.error("文件 " + inFileAbsolutePath + " 复制失败: " + e.getMessage(), e);
                        }
                    }
                });
    }

    /**
     * 将文件从一种编码转换为另一种编码
     *
     * @param inFilePath  文件路径
     * @param fromCharset 源文件编码
     * @param toCharset   目标文件编码
     * @param outFilePath 目标文件路径
     * @throws IOException 如果读写文件时发生错误
     */
    public static void convertFileEncoding(String inFilePath, String fromCharset, String toCharset, String outFilePath) throws IOException {
        File inFile = new File(inFilePath);
        if (!inFile.exists()) {
            throw new FileNotFoundException("文件 " + inFilePath + " 不存在");
        }

        // 读取文件内容
        String content = readFile(inFile, fromCharset);

        // 写入文件内容
        writeFile(content, toCharset, outFilePath);
    }


    /**
     * 读取文件内容
     *
     * @param file    文件对象
     * @param charset 文件编码
     * @return 文件内容
     * @throws IOException 如果读取文件时发生错误
     */
    private static String readFile(File file, String charset) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), charset))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        }
    }

    /**
     * 写入文件内容
     *
     * @param content     文件内容
     * @param charset     文件编码
     * @param outFilePath 目标文件路径
     * @throws IOException 如果写入文件时发生错误
     */
    private static void writeFile(String content, String charset, String outFilePath) throws IOException {
        File outFile = new File(outFilePath);
        File outParentFile = outFile.getParentFile();
        if (!outParentFile.exists()) {
            boolean mkdirs = outParentFile.mkdirs();
        }
        if (!outFile.exists()) {
            boolean newFile = outFile.createNewFile();
        }
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outFile.toPath()), charset))) {
            writer.write(content);
        }
    }


    public static String detectFileEncoding(File file) {
        byte[] buf = new byte[4096];
        UniversalDetector detector = new UniversalDetector(null);
        try (FileInputStream fis = new FileInputStream(file)) {
            int nread;
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
            detector.dataEnd();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String encoding = detector.getDetectedCharset();
        detector.reset();

        return encoding == null ? "GBK" : encoding;
    }

}
