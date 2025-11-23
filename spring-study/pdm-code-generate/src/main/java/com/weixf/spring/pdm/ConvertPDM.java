package com.weixf.spring.pdm;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;


@Slf4j
public class ConvertPDM {

    private int DBMSType = DBConst.DB_UnSupported;
    private boolean AllowErrorInPDM = false; // 是否允许PDM上有错误信息是继续转换
    private boolean AllowJavaType = false; // 是否允许Integer或Double等Java类型
    private boolean AllowJavaMath = false; // 是否允许使用BigDecimal代替Double类型

    public static String getPKWhereClause(ArrayList<PDMColumn> pkList) {
        // 生成按主键操作的WHERE子句
        StringBuilder PKWhereClause = new StringBuilder(50);
        if (pkList != null && pkList.size() > 0) {
            for (PDMColumn pk : pkList) {
                PKWhereClause.append(" AND ").append(pk.getCode()).append(" = ?");
            }
            PKWhereClause.delete(0, 4);
        }
        if (PKWhereClause.length() == 0) {
            PKWhereClause.append("1 = 1");
        }
        return PKWhereClause.toString().trim();
    }


    public void setDBMSType(int DBMSType) {
        this.DBMSType = DBMSType;
    }


    public void setAllowErrorInPDM(boolean AllowErrorInPDM) {
        this.AllowErrorInPDM = AllowErrorInPDM;
    }


    public void setAllowJavaType(boolean AllowJavaType) {
        this.AllowJavaType = AllowJavaType;
    }


    public void setAllowJavaMath(boolean AllowJavaMath) {
        this.AllowJavaMath = AllowJavaMath;
        if (AllowJavaMath) {
            AllowJavaType = AllowJavaMath;
        }
    }

    public ArrayList<PDMColumn> ConvertColumnType(String tableName, ArrayList<PDMColumn> columnArrayList) throws Exception {
        ArrayList<PDMColumn> pdmColumnArrayList = new ArrayList<>();
        for (PDMColumn column : columnArrayList) {
            String id = column.getId();
            String name = column.getName();
            String code = column.getCode();

            String dataType = column.getDataType();
            String newDataType = changeType(dataType, tableName, name);

            int length = column.getLength();
            int precision = column.getPrecision();
            int mandatory = column.getMandatory();
            String defaultValue = column.getDefaultValue();
            String lowValue = column.getLowValue();
            String highValue = column.getHighValue();
            String comment = column.getComment();
            PDMTable table = column.getTable();

            PDMColumn pdmColumn = new PDMColumn(id, name, code, newDataType, length, precision, mandatory, defaultValue, lowValue, highValue, comment, table);
            pdmColumnArrayList.add(pdmColumn);
        }

        return pdmColumnArrayList;
    }


    private String changeType(String oldType, String tabName, String colName) throws Exception {
        oldType = oldType.toLowerCase().trim();
        String regex = null;
        if (oldType.indexOf("int") >= 0) {
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
        if (oldType.indexOf("dec") >= 0 || oldType.indexOf("num") >= 0) {
            if (oldType.indexOf("number") >= 0 && DBMSType != DBConst.DB_Oracle) {
                dealDbError(oldType, tabName, colName);
            }
            if (oldType.indexOf("numeric") < 0 && oldType.indexOf("number") < 0 && oldType.indexOf("num") >= 0 && DBMSType != DBConst.DB_DB2) {
                dealDbError(oldType, tabName, colName);
            }
            regex = "(dec|decimal|numeric|num|number)\\s*(\\(\\s*[0-9]+\\s*(,\\s*[0-9]+|)\\s*\\)|)";
            checkDataType(regex, oldType, tabName, colName);
            // 符合dec,dec(*),dec(*,0),decimal,decimal(*),decimal(*,0)
            // numeric,numeric(*),numeric(*,0),num,num(*),num(*,0),number(*),number(*,0)的都为整数
            // 注意number类型不为整数
            String regexI = null;
            if (oldType.indexOf("number") >= 0) {
                regexI = "number\\s*\\(\\s*[0-9]+\\s*(,\\s*[0]\\s*|)\\)";
            } else {
                regexI = "(dec|decimal|numeric|num)\\s*(\\(\\s*[0-9]+\\s*(,\\s*[0]|)\\s*\\)|)";
            }
            if (oldType.matches(regexI)) {
                // 如果定义的整数范围大于2147483647可能会出现溢出
                if (AllowJavaType) {
                    if (AllowJavaMath) {
                        String regexB = "(dec|decimal|numeric|num|number)\\s*(\\(\\s*[1-9][0-9]\\s*(,\\s*[0-9]+|)\\s*\\)|)";
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
        if (oldType.indexOf("float") >= 0) {
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
        if (oldType.indexOf("char") >= 0) {
            if (oldType.indexOf("varchar2") >= 0 && DBMSType != DBConst.DB_Oracle) {
                dealDbError(oldType, tabName, colName);
            }
            if (oldType.indexOf("nvarchar") >= 0 && (DBMSType != DBConst.DB_Oracle && DBMSType != DBConst.DB_SQLServer)) {
                dealDbError(oldType, tabName, colName);
            }
            regex = "(char|varchar|varchar2|nvarchar|nvarchar2)(|\\s*\\(\\s*[0-9]+\\s*\\))";
            checkDataType(regex, oldType, tabName, colName);
            return "String";
        }
        // 不推荐使用timestamp。timestamp在Microsoft SQL Server JDBC中没有对应的java.sql.Types类型
        if (oldType.indexOf("timestamp") >= 0 || oldType.indexOf("date") >= 0) {
            if (oldType.indexOf("datetime") >= 0 && DBMSType != DBConst.DB_SQLServer) {
                dealDbError(oldType, tabName, colName);
            }
            if (oldType.indexOf("datetime") < 0 && oldType.indexOf("date") >= 0 && DBMSType != DBConst.DB_Oracle && DBMSType != DBConst.DB_DB2) {
                dealDbError(oldType, tabName, colName);
            }
            regex = "(datetime|smalldatetime|timestamp|date)";
            checkDataType(regex, oldType, tabName, colName);
            if (oldType.indexOf("datetime") < 0 && oldType.indexOf("date") >= 0) {
                return "Date";
            } else {
                return "String";
            }
        }
        if (oldType.indexOf("text") >= 0) {
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
        if (oldType.indexOf("image") >= 0) {
            if (DBMSType != DBConst.DB_SQLServer) {
                dealDbError(oldType, tabName, colName);
            }
            regex = "image";
            checkDataType(regex, oldType, tabName, colName);
            return "InputStream";
        }
        if (oldType.indexOf("blob") >= 0) {
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


    private void checkDataType(String regex, String dataType, String tabName, String colName) throws Exception {
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


    public String getInsertColumnClause(ArrayList<PDMColumn> newColumns) {
        StringBuilder InsertColumnClause = new StringBuilder(300);
        for (int i = 0; i < newColumns.size(); i++) {
            InsertColumnClause.append(" , ?");
        }
        InsertColumnClause.delete(0, 2);
        return InsertColumnClause.toString().trim();
    }


    public String getUpdateColumnClause(ArrayList<PDMColumn> newColumns) {
        // 生成UPDATE操作所需要的所有字段的字句。例如，ContNo = ? , PolNo = ?
        StringBuilder UpdateColumnClause = new StringBuilder(300);
        for (PDMColumn column : newColumns) {
            UpdateColumnClause.append(" , ").append(column.getCode()).append(" = ?");
        }
        UpdateColumnClause.delete(0, 2);
        return UpdateColumnClause.toString().trim();
    }
}
