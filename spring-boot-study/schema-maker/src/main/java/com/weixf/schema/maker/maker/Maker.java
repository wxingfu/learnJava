package com.weixf.schema.maker.maker;

import com.weixf.schema.maker.pdm.PDM;
import com.weixf.schema.maker.pdm.Parser;
import com.weixf.schema.maker.repository.MyRepository;
import com.weixf.schema.maker.table.Convert;
import com.weixf.schema.maker.table.Schema;
import com.weixf.schema.maker.utility.DBConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *
 * @since 2022-06-20
 */
@Slf4j
@Component
public class Maker {
    @Value("${schema.output-path}")
    public String schemaOutputPATH;
    // 单表操作输出路径
    @Value("${schema.output-path}")
    public String oneTableOprOutputPATH;
    // 输出实体类包名称
    @Value("${schema.package-name}")
    public String packageName;
    // 输出实体类存放路径
    @Value("${schema.output-package-path}")
    public String outputPackagePATH;
    // 数据库用户名
    @Value("${schema.db-username}")
    public String DBUserName;
    // 标示生成的文件类型 DB -只生成表实体 OPR -只生成单表操作文件 ALL -生成表实体和单表操作文件
    @Value("${schema.op-flag}")
    public String OPFlag;
    // PDM文件名(含全路径)
    @Value("${schema.pdm-filename}")
    public String PDMFileName;
    // 表名，如果指定了表名，则指生成指定表的相关文件
    @Value("${schema.table-name}")
    public String tableName;
    @Resource
    private MyRepository myRepository;

    public Maker() {
    }

    private static void checkDBType(Schema schema) {
        Pattern pc = Pattern.compile("\\s*(microsoft\\s+sql\\s+server|ibm\\s+db2\\s+udb|oracle)\\s+.*");
        Matcher m = pc.matcher(schema.getDBMSName().toLowerCase());
        if (!m.matches()) {
            try {
                throw new Exception("你正在处理的数据库为" + schema.getDBMSName() + "，为不支持的数据库类型，目前仅支持：Oracle,DB2 UDB,SQL Server");
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    /**
     * 递归删除目录及下面的所有文件
     */
    private static void deleteDirectory(File dir) {
        if (dir == null || !dir.isDirectory()) {
            throw new IllegalArgumentException("Argument " + dir + " is not a directory. ");
        } else {
            File[] entries = dir.listFiles();
            int sz = 0;
            if (entries != null) {
                sz = entries.length;
            }
            for (int i = 0; i < sz; i++) {
                if (entries[i].isDirectory()) {
                    deleteDirectory(entries[i]);
                } else {
                    log.debug("正在删除文件:" + entries[i].getPath());
                    boolean delete = entries[i].delete();
                }
            }
        }
    }

    public void createJavaFile() throws Exception {
        log.info(" validate at " + getTimestamp());
        validate();
        // 解析PDM文件
        log.info("PDMParser at " + getTimestamp());
        Parser tPDMParser = new Parser();
        tPDMParser.setAllowErrorInPDM(false); // PDM上不能有错误
        tPDMParser.setParserForeignKey(false); // 生成Schema类不需要外键
        tPDMParser.setPrimaryKeyIsNull(false); // 生成Schema类必须有主键(推荐)
        PDM tPDM = tPDMParser.readPDMFile(PDMFileName);
        if (tPDM == null) {
            throw new Exception("解析PDM文件出错");
        }
        // 将PDM格式转换成MEM(内存)格式
        log.info("ConvertPDM at " + getTimestamp());
        Convert tConvertPDM = new Convert();
        tConvertPDM.setDBMSType(DBConst.DB_Oracle);
        tConvertPDM.setAllowErrorInPDM(false); // 转换过程不允许跳过错误
        tConvertPDM.setAllowJavaType(false); // 转换时使用基本类型
        tConvertPDM.setAllowJavaMath(false); // 转换时不使用java.math类型
        Schema tSchema = tConvertPDM.ConvertPDM(tPDM);
        // 检查PDM的数据库类型
        checkDBType(tSchema);
        // 删除以前生成的文件
        log.info("initOutputDir at " + getTimestamp());
        initOutputDir();
        // 循环处理Schema中的每个表
        // 删除以前生成的文件
        log.info(" MakeFile at " + getTimestamp());
        for (int i = 0; i < tSchema.getTabNum(); i++) {
            log.info("<Table " + (i + 1) + "> " + tSchema.getTable(i).getCode() + "(" + tSchema.getTable(i).getName() + ")");
            log.debug("Begin at " + getTimestamp());
            // 获得数据库的信息并作一些简单的检查
            log.debug("getDBInfo at " + getTimestamp());
            tSchema.getTable(i).setDBType(DBConst.DB_Oracle);
            tSchema.getTable(i).setJDBCVendor(DBConst.Vendor_Oracle);
            tSchema.getTable(i).getDBInfo(myRepository.getConnection(), DBUserName.toUpperCase());
            // 对有大对象字段时不让生成某些函数
            tSchema.getTable(i).setLargeObj(false);
            // 对有大对象主键字段时不让生成某些函数
            tSchema.getTable(i).setLargeObjInPK(false);
            if ("DB".equals(OPFlag) || "ALL".equals(OPFlag)) {
                // 生成Schema.java
                log.debug("MakeSchema at " + getTimestamp());
                MakeSchema t1 = new MakeSchema(packageName, schemaOutputPATH, outputPackagePATH, tSchema.getName());
                t1.setUserInfo(true); // 输出创建者个人信息
                t1.create(tSchema.getTable(i));
                // 生成DB.java
                log.debug("MakeDB at " + getTimestamp());
                MakeDB t2 = new MakeDB(packageName, schemaOutputPATH, outputPackagePATH, tSchema.getName());
                t2.setUserInfo(true);
                t2.isMultiCloseConn(false);
                t2.create(tSchema.getTable(i));
                // 生成Set.java
                log.debug("MakeSet at " + getTimestamp());
                MakeSet t3 = new MakeSet(packageName, schemaOutputPATH, outputPackagePATH, tSchema.getName());
                t3.setUserInfo(true);
                t3.create(tSchema.getTable(i));
                // 生成DBSet.java
                log.debug("MakeDBSet at " + getTimestamp());
                MakeDBSet t4 = new MakeDBSet(packageName, schemaOutputPATH, outputPackagePATH, tSchema.getName());
                t4.setUserInfo(true);
                t4.isMultiCloseConn(false);
                t4.create(tSchema.getTable(i));
            }
            log.debug("End at " + getTimestamp());
        }
    }

    private void validate() throws Exception {
        if (PDMFileName == null || "".equals(PDMFileName) || !(new File(PDMFileName)).exists()) {
            throw new Exception("PDM文件名为空或文件不存在:" + PDMFileName);
        }
        if (schemaOutputPATH == null || "".equals(schemaOutputPATH)) {
            throw new Exception("输出路径不能为空:" + schemaOutputPATH);
        }
        if (packageName == null && !Pattern.matches("[a-zA-Z.]+", packageName)) {
            throw new Exception("Package Name Error:" + packageName);
        }
        if (!"DB".equals(OPFlag) && !"OPR".equals(OPFlag) && !"ALL".equals(OPFlag)) {
            throw new Exception("操作类型不合法：" + OPFlag);
        }
        if (DBUserName == null || "".equals(DBUserName)) {
            throw new Exception("数据库用户名不能为空：" + DBUserName);
        }
    }

    private String getTimestamp() {
        String pattern = "yyyy-MM-dd HH:mm:ss SSS";
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date today = new Date();
        return df.format(today);
    }

    private void initOutputDir() {
        File schemaOutputDir = new File(schemaOutputPATH);
        File oneTableOprOutputDir = new File(oneTableOprOutputPATH);
        if (schemaOutputDir.exists()) {
            deleteDirectory(schemaOutputDir);
        } else if (!schemaOutputDir.mkdirs()) {
            throw new IllegalArgumentException("创建输出目录" + schemaOutputPATH + "失败。");
        }
    }
}
