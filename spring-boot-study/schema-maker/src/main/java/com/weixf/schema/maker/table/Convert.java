package com.weixf.schema.maker.table;


import com.weixf.schema.maker.pdm.PDM;
import com.weixf.schema.maker.pdm.PDMColumn;
import com.weixf.schema.maker.pdm.PDMKey;
import com.weixf.schema.maker.pdm.PDMReference;
import com.weixf.schema.maker.pdm.PDMReferenceJoin;
import com.weixf.schema.maker.pdm.PDMTable;
import com.weixf.schema.maker.utility.DBConst;
import com.weixf.schema.maker.utility.DBTypes;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 * @since 2022-01-21
 */
@Slf4j
@Component
public class Convert {

    @Setter
    private int DBMSType = DBConst.DB_UnSupported;
    @Setter
    private boolean AllowErrorInPDM = false; // 是否允许PDM上有错误信息是继续转换
    @Setter
    private boolean AllowJavaType = false; // 是否允许Integer或Double等Java类型
    private boolean AllowJavaMath = false; // 是否允许使用BigDecimal代替Double类型

    public Convert() {
    }

    public void setAllowJavaMath(boolean AllowJavaMath) {
        this.AllowJavaMath = AllowJavaMath;
        if (AllowJavaMath) {
            this.AllowJavaType = AllowJavaMath;
        }
    }

// PDM中数据库一些长度及约束的命名(PowerDesigner 12)
//
//  模型: IBM DB2 UDB 8.x Common Server
//     表名: 最长128位;   字段: 最长30位;  键: 最长18位
//     主键约束: "P_" + 16位键名
//     唯一约束: "A_" + 16位键名
//     外键约束: "F_" + 16位键名
//  模型: Oracle Version 9i2
//     表名: 最长30位;    字段: 最长30位;  键: 最长30位
//     主键约束: "PK_" + 27位表名
//     唯一约束: "AK_" + 18位键名 + "_" + 8位表名
//     外键约束: "FK_" + 8位子表+ "_" + 9位键名 + "_" + 8位父表
//  模型: Microsoft SQL Server 2000
//     表名: 最长128位;   字段: 最长30位;  键: 最长30位
//     主键约束: "PK_" + 27位表名
//     唯一约束: "AK_" + 18位键名 + "_" + 8位表名
//     外键约束: "FK_" + 8位子表+ "_" + 9位键名 + "_" + 8位父表

    public Schema ConvertPDM(PDM cPDM) throws Exception {
        if (DBMSType == DBConst.DB_UnSupported) {
            throw new Exception("请先设置+数据库类型！");
        }
        Schema tSchema = new Schema();
        tSchema.setCode(cPDM.getCode());
        tSchema.setName(cPDM.getName());
        tSchema.setDBMSCode(cPDM.getDBMSCode());
        tSchema.setDBMSName(cPDM.getDBMSName());

        for (int i = 0; i < cPDM.getTabNum(); i++) {
            PDMTable tPDMTable = cPDM.getTable(i);
            Table tTable = new Table();
            String tableCode = tPDMTable.getCode().trim();
            if (DBMSType == DBConst.DB_Oracle && tableCode.length() > 30) {
                tableCode = tableCode.substring(0, 30);
            }
            if ((DBMSType == DBConst.DB_DB2 || DBMSType == DBConst.DB_SQLServer) && tableCode.length() > 128) {
                tableCode = tableCode.substring(0, 128);
            }
            tTable.setCode(tableCode);
            tTable.setName(tPDMTable.getName());
            for (int j = 0; j < tPDMTable.getColNum(); j++) {
                PDMColumn tPDMColumn = tPDMTable.getColumn(j);
                String colCode = tPDMColumn.getCode().trim();
                if (colCode.length() > 30) {
                    colCode = colCode.substring(0, 30);
                }
                Column tColumn = new Column();
                tColumn.setName(tPDMColumn.getName());
                tColumn.setCode(colCode);
                tColumn.setDataType(changeType(tPDMColumn.getDataType(), tableCode, colCode));
                tColumn.setLength(tPDMColumn.getLength());
                tColumn.setPrecision(tPDMColumn.getPrecision());
                tColumn.setNullable(tPDMColumn.getMandatory() == 0 ? 1 : 0);
                tColumn.setDefaultValue(tPDMColumn.getDefaultValue());
                tColumn.setPDMDataType(tPDMColumn.getDataType());
                tTable.addColumn(tColumn);
            }
            for (int j = 0; j < tPDMTable.getKeyNum(); j++) {
                PDMKey tPDMKey = tPDMTable.getKey(j);
                Key tKey = new Key();
                // 设置PK
                if (tPDMKey.getId().equals(tPDMTable.getPrimaryKey())) {
                    tKey.setKeyType(DBTypes.PrimaryKey);
                } else {
                    tKey.setKeyType(DBTypes.UniqueKey);
                }
                String cn = tPDMKey.getConstraintName();
                if (cn == null || "".equals(cn)) {
                    if (tPDMKey.getId().equals(tPDMTable.getPrimaryKey())) {
                        if (DBMSType == DBConst.DB_DB2) {
                            cn = "P_" + (tPDMKey.getCode().trim().length() > 16 ? tPDMKey.getCode().substring(0, 16) : tPDMKey.getCode().trim());
                        }
                        if (DBMSType == DBConst.DB_SQLServer || DBMSType == DBConst.DB_Oracle) {
                            cn = "PK_" + (tableCode.length() > 27 ? tableCode.substring(0, 27) : tableCode);
                        }
                    } else {
                        if (DBMSType == DBConst.DB_DB2) {
                            cn = "A_" +
                                    (tPDMKey.getCode().trim().length() > 16 ?
                                            tPDMKey.getCode().substring(0, 16) :
                                            tPDMKey.getCode().trim());
                        }
                        if (DBMSType == DBConst.DB_SQLServer || DBMSType == DBConst.DB_Oracle) {
                            cn = "AK_" + (tPDMKey.getCode().trim().length() > 18 ?
                                    tPDMKey.getCode().substring(0, 18) :
                                    tPDMKey.getCode().trim()) + "_" + (tableCode.trim().length() > 8 ? tableCode.trim().substring(0, 8) : tableCode.trim());
                        }
                    }
                }
                tKey.setConstraintName(cn);
                for (int k = 0; k < tPDMKey.getColNum(); k++) {
                    String Columns_Ref = tPDMKey.getColumn(k);
                    for (int l = 0; l < tPDMTable.getColNum(); l++) {
                        PDMColumn tPDMColumn = tPDMTable.getColumn(l);
                        if (tPDMColumn.getId().equals(Columns_Ref)) {
                            tKey.addColumn(tPDMTable.getColumn(l).getCode(), null);
                            break;
                        }
                    }
                }
                tTable.addKey(tKey);
            }
            for (int j = 0; j < cPDM.getRefNum(); j++) {
                PDMReference tPDMRef = cPDM.getReference(j);
                if (tPDMRef.getChildTable().equals(tPDMTable.getId())) {
                    Key tFK = new Key();
                    tFK.setKeyType(DBTypes.ForeignKey);
                    tFK.setDeleteType(tPDMRef.getDeleteConstraint());
                    tFK.setUpdateType(tPDMRef.getUpdateConstraint());
                    for (int k = 0; k < cPDM.getTabNum(); k++) {
                        PDMTable aPMDTable = cPDM.getTable(k);
                        if (aPMDTable.getId().equals(tPDMRef.getParentTable())) {
                            tFK.setRefTable(aPMDTable.getCode());
                            String cn = tPDMRef.getConstraintName();
                            if (cn == null || "".equals(cn)) {
                                if (DBMSType == DBConst.DB_DB2) {
                                    cn = "F_" + (tPDMRef.getCode().trim().length() > 16 ? tPDMRef.getCode().substring(0, 16) : tPDMRef.getCode().trim());
                                }
                                if (DBMSType == DBConst.DB_SQLServer || DBMSType == DBConst.DB_Oracle) {
                                    cn = "FK_" + ((tableCode.trim().length() > 8) ? tableCode.trim().substring(0, 8) : tableCode.trim()) + "_" + (tPDMRef.getCode().trim().length() > 9 ? tPDMRef.getCode().substring(0, 9) : tPDMRef.getCode().trim()) + "_" + ((aPMDTable.getCode().trim().length() > 8) ? aPMDTable.getCode().trim().substring(0, 8) : aPMDTable.getCode().trim());
                                }
                            }
                            tFK.setConstraintName(cn);
                            for (int l = 0; l < aPMDTable.getKeyNum(); l++) {
                                PDMKey aPDMKey = cPDM.getTable(k).getKey(l);
                                if (aPDMKey.getId().equals(tPDMRef.getParentKey())) {
                                    tFK.setRefKey(aPDMKey.getCode());
                                    for (int m = 0; m < tPDMRef.getJoinNum(); m++) {
                                        PDMReferenceJoin tPDMRefJoin = tPDMRef.getJoin(m);
                                        String PC = null;
                                        String CC = null;
                                        for (int n = 0; n < tPDMTable.getColNum(); n++) {
                                            PDMColumn aPDMColumn = tPDMTable.getColumn(n);
                                            if (aPDMColumn.getId().equals(tPDMRefJoin.getChildTableCol())) {
                                                CC = aPDMColumn.getCode();
                                                break;
                                            }
                                        }
                                        for (int n = 0; n < aPMDTable.getColNum(); n++) {
                                            PDMColumn aPDMColumn = aPMDTable.getColumn(n);
                                            if (aPDMColumn.getId().equals(tPDMRefJoin.getParentTableCol())) {
                                                PC = aPDMColumn.getCode();
                                                break;
                                            }
                                        }
                                        tFK.addColumn(CC, PC);
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    tTable.addKey(tFK);
                }
            }
            tSchema.addTable(tTable);
        }
        return tSchema;
    }

    private void checkDataType(String regex, String dataType, String tabName, String colName) throws Exception {
//        Pattern pc = null;
//        Matcher m = null;
//        pc = Pattern.compile(regex);
//        m = pc.matcher(dataType);
//        if(!m.matches())
        if (!dataType.matches(regex)) {
            if (AllowErrorInPDM) {
                log.error("表" + tabName + "的字段" + colName + "的类型错误:" + dataType);
            } else {
                throw new Exception("表" + tabName + "的字段" + colName + "的类型错误:" + dataType);
            }
        }
    }

    private void dealDbError(String oldType, String tabName, String colName) throws Exception {
        if (AllowErrorInPDM) {
            log.error("表" + tabName + "的字段" + colName + "的类型错误:" + oldType + "(" + DBConst.getDBName(DBMSType) + ")");
        } else {
            throw new Exception("表" + tabName + "的字段" + colName + "的数据类型不支持或错误:" + oldType);
        }
    }

    private String changeType(String oldType, String tabName, String colName) throws Exception {
        oldType = oldType.toLowerCase().trim();
        String regex = null;
        if (oldType.contains("int")) {
            if (oldType.equalsIgnoreCase("tinyint") && DBMSType != DBConst.DB_SQLServer) {
                dealDbError(oldType, tabName, colName);
            }
            regex = "(int|integer|smallint|tinyint)";
            checkDataType(regex, oldType, tabName, colName);
            if (AllowJavaType) {
                return "Integer";
            } else {
                return "int";
            }
        }
        if (oldType.contains("dec") || oldType.contains("num")) {
            if (oldType.contains("number") && DBMSType != DBConst.DB_Oracle) {
                dealDbError(oldType, tabName, colName);
            }
            if (!oldType.contains("numeric") && !oldType.contains("number") && oldType.contains("num") && DBMSType != DBConst.DB_DB2) {
                dealDbError(oldType, tabName, colName);
            }
            regex = "(dec|decimal|numeric|num|number)\\s*(\\(\\s*[0-9]+\\s*(,\\s*[0-9]+|)\\s*\\)|)";
            checkDataType(regex, oldType, tabName, colName);
            // 符合dec,dec(*),dec(*,0),decimal,decimal(*),decimal(*,0)
            // numeric,numeric(*),numeric(*,0),num,num(*),num(*,0),number(*),number(*,0)的都为整数
            // 注意number类型不为整数
            String regexI = null;
            if (oldType.contains("number")) {
                regexI = "number\\s*\\(\\s*[0-9]+\\s*(,\\s*[0]\\s*|)\\)";
            } else {
                regexI = "(dec|decimal|numeric|num)\\s*(\\(\\s*[0-9]+\\s*(,\\s*[0]|)\\s*\\)|)";
            }
//            Pattern pc = Pattern.compile(regexI);
//            Matcher m = pc.matcher(oldType);
//            if(m.matches())
            if (oldType.matches(regexI)) {
                // 如果定义的整数范围大于2147483647可能会出现溢出
                if (AllowJavaType) {
                    if (AllowJavaMath) {
                        String regexB = "(dec|decimal|numeric|num|number)\\s*(\\(\\s*[1-9][0-9]\\s*(,\\s*[0-9]+|)\\s*\\)|)";
//                        Pattern pcB = Pattern.compile(regexB);
//                        Matcher mB = pcB.matcher(oldType);
//                        if(mB.matches())
                        if (oldType.matches(regexB)) {
                            return "BigInteger";
                        } else {
                            return "Integer";
                        }
                    } else {
                        return "Integer";
                    }
                } else {
                    return "int";
                }
            } else {
                if (AllowJavaType) {
                    if (AllowJavaMath) {
                        return "BigDecimal";
                    } else {
                        return "Double";
                    }
                } else {
                    return "double";
                }
            }
        }
        if (oldType.contains("float")) {
            regex = "float(|\\s*\\(\\s*[0-9]+\\s*\\))";
            checkDataType(regex, oldType, tabName, colName);
            if (AllowJavaType) {
                if (AllowJavaMath) {
                    return "BigDecimal";
                } else {
                    return "Double";
                }
            } else {
                return "double";
            }
        }
        if (oldType.contains("char")) {
            if (oldType.contains("varchar2") &&
                    DBMSType != DBConst.DB_Oracle) {
                dealDbError(oldType, tabName, colName);
            }
            if (oldType.contains("nvarchar") &&
                    (DBMSType != DBConst.DB_Oracle &&
                            DBMSType != DBConst.DB_SQLServer)) {
                dealDbError(oldType, tabName, colName);
            }
            regex =
                    "(char|varchar|varchar2|nvarchar|nvarchar2)(|\\s*\\(\\s*[0-9]+\\s*\\))";
            checkDataType(regex, oldType, tabName, colName);
            return "String";
        }
        // 不推荐使用timestamp。timestamp在Microsoft SQL Server JDBC中没有对应的java.sql.Types类型
        if (oldType.contains("timestamp") ||
                oldType.contains("date")) {
            if (oldType.contains("datetime") &&
                    DBMSType != DBConst.DB_SQLServer) {
                dealDbError(oldType, tabName, colName);
            }
            if (!oldType.contains("datetime") &&
                    oldType.contains("date") &&
                    DBMSType != DBConst.DB_Oracle &&
                    DBMSType != DBConst.DB_DB2) {
                dealDbError(oldType, tabName, colName);
            }
            regex = "(datetime|smalldatetime|timestamp|date)";
            checkDataType(regex, oldType, tabName, colName);
            if (!oldType.contains("datetime") &&
                    oldType.contains("date")) {
                return "Date";
            } else {
                return "String";
            }
        }
        if (oldType.contains("text")) {
            if (DBMSType != DBConst.DB_SQLServer) {
                dealDbError(oldType, tabName, colName);
            }
            regex = "text";
            checkDataType(regex, oldType, tabName, colName);
            return "String"; // SQL Server用String
        }
        if (oldType.equalsIgnoreCase("long")) {
            if (DBMSType != DBConst.DB_Oracle) {
                dealDbError(oldType, tabName, colName);
            }
            regex = "long";
            checkDataType(regex, oldType, tabName, colName);
            return "String";
        }
        if (oldType.contains("image")) {
            if (DBMSType != DBConst.DB_SQLServer) {
                dealDbError(oldType, tabName, colName);
            }
            regex = "image";
            checkDataType(regex, oldType, tabName, colName);
            return "InputStream";
        }
        if (oldType.contains("blob")) {
            if (DBMSType != DBConst.DB_Oracle && DBMSType != DBConst.DB_DB2) {
                dealDbError(oldType, tabName, colName);
            }
            // regex = "blob(|\\s*\\(\\s*[0-9]+\\s*\\)\\s*)";
            regex = "blob";
            checkDataType(regex, oldType, tabName, colName);
            return "InputStream";
        }
        return null;
    }
}
