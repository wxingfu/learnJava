package com.weixf.schema.maker.utility;

import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 *
 * @since 2022-01-21
 */
@Component
public class SysConst {

    /* 系统变量 */
    public static final boolean CHANGECHARSET = false; // Unicode to GBK
    /* 信息分隔符 */
    public static final String PACKAGESPILTER = "|";
    public static final String RECORDSPLITER = "^";
    // 大批量数据查询时，使用的缓冲区大小
    public static final int FETCHCOUNT = 5000;
    public static Properties sysProperties;

    public static String getProperties(String propName) {
        if (sysProperties == null) {
            try {
                sysProperties = new Properties();
                InputStream in = new BufferedInputStream(
                        Files.newInputStream(Paths.get(
                                (ClassLoader.getSystemResource("").toString())
                                        .replaceAll("file:/", "") + "SysConst.properties")));
                sysProperties.load(in);
                in.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return sysProperties.getProperty(propName);
    }
}
