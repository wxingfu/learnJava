package com.weixf.spring;

import com.weixf.spring.pdm.ConvertPDM;
import com.weixf.spring.pdm.DBConst;
import com.weixf.spring.pdm.PDM;
import com.weixf.spring.pdm.PDMColumn;
import com.weixf.spring.pdm.PDMKey;
import com.weixf.spring.pdm.PDMTable;
import com.weixf.spring.pdm.Parser;
import com.weixf.spring.utils.CommonUtil;
import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@SpringBootTest
public class MyTest {

    @Resource
    private Configuration configuration;

    @Test
    public void executeAll() throws Exception {
        createSchema();
        // createSet();
        // createDbSet();
        // createDb();
    }


    public Map<String, Object> getSystemProperties() {
        Properties properties = System.getProperties();
        Map<String, Object> propMap = new HashMap<>();
        propMap.put("javaVmName", properties.getProperty("java.vm.name"));
        propMap.put("javaVmVersion", properties.getProperty("java.vm.version"));
        propMap.put("javaVmVendor", properties.getProperty("java.vm.vendor"));
        propMap.put("osName", properties.getProperty("os.name"));
        propMap.put("osArch", properties.getProperty("os.arch"));
        propMap.put("userName", properties.getProperty("user.name"));
        propMap.put("userCountry", properties.getProperty("user.country"));
        return propMap;
    }

    @Test
    public void createSchema() throws Exception {
        String rootPath = "D:\\MyWork\\Schema\\";
        try {
            Parser parser = new Parser();
            // PDM pdm = parser.pdmParser("D:\\MyWork\\Schema\\LDPrdDeploy.pdm");
            // PDM pdm = parser.pdmParser("D:\\MyWork\\Schema\\NoRealBlcDtl.pdm");
            PDM pdm = parser.pdmParser(rootPath + "LifeInsurancePersonal.pdm");

            String dataBaseName = pdm.getName();
            Map<String, Object> systemProperties = getSystemProperties();

            ConvertPDM convertPDM = new ConvertPDM();
            convertPDM.setDBMSType(DBConst.DB_Oracle);
            convertPDM.setAllowErrorInPDM(false); // 转换过程不允许跳过错误
            convertPDM.setAllowJavaType(false); // 转换时使用基本类型
            convertPDM.setAllowJavaMath(false); // 转换时不使用java.math类型

            ArrayList<PDMTable> tables = pdm.getTables();
            for (PDMTable table : tables) {

                String tableCode = table.getCode();
                PDMKey primaryKey = table.getPrimaryKey();

                ArrayList<PDMColumn> pkList = primaryKey.getColumns();
                ArrayList<PDMColumn> newPKList = convertPDM.ConvertColumnType(tableCode, pkList);

                ArrayList<PDMColumn> columns = table.getColumns();
                ArrayList<PDMColumn> newColumns = convertPDM.ConvertColumnType(tableCode, columns);

                String PKWhereClause = ConvertPDM.getPKWhereClause(pkList);
                String InsertColumnClause = convertPDM.getInsertColumnClause(newColumns);
                String UpdateColumnClause = convertPDM.getUpdateColumnClause(newColumns);

                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("sysProp", systemProperties);
                dataMap.put("tableName", tableCode);
                dataMap.put("dataBase", dataBaseName);
                dataMap.put("tableColumns", newColumns);
                dataMap.put("pkList", newPKList);
                dataMap.put("PKWhereClause", PKWhereClause);
                dataMap.put("InsertColumnClause", InsertColumnClause);
                dataMap.put("UpdateColumnClause", UpdateColumnClause);

                CommonUtil.printFile(dataMap, configuration.getTemplate("schema.ftl"), rootPath, tableCode + "Schema.java");
                CommonUtil.printFile(dataMap, configuration.getTemplate("set.ftl"), rootPath, tableCode + "Set.java");
                CommonUtil.printFile(dataMap, configuration.getTemplate("dbset.ftl"), rootPath, tableCode + "DBSet.java");
                CommonUtil.printFile(dataMap, configuration.getTemplate("db.ftl"), rootPath, tableCode + "DB.java");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
