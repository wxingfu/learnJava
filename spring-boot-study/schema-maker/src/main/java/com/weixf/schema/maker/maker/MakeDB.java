package com.weixf.schema.maker.maker;

import com.weixf.schema.maker.table.Column;
import com.weixf.schema.maker.table.Key;
import com.weixf.schema.maker.table.Table;
import com.weixf.schema.maker.utility.DBConst;
import com.weixf.schema.maker.utility.SqlTypes;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 *
 * @since 2022-01-21
 */
@Component
public class MakeDB {

    private static final String space4_1 = "    ";
    private static final String space4_2 = space4_1 + space4_1;
    private static final String space4_3 = space4_2 + space4_1;
    private static final String space4_4 = space4_3 + space4_1;
    private static final String space4_5 = space4_4 + space4_1;
    private static final String space4_6 = space4_5 + space4_1;
    private final int DBType = DBConst.DB_UnSupported;
    public boolean addOrderBy = false;
    private String packageName;
    private String schemaOutputPATH;
    private String outputPackagePATH;
    private String DBName;
    @Setter
    private boolean UserInfo = false;
    private boolean MultiCloseConn = false;
    private boolean hasTimeStamp = false;


    public MakeDB() {
    }


    public MakeDB(String packageName, String schemaOutputPATH, String outputPackagePATH, String DBName) {
        this.packageName = packageName;
        this.schemaOutputPATH = schemaOutputPATH;
        this.outputPackagePATH = outputPackagePATH;
        this.DBName = DBName;
    }

    public void isMultiCloseConn(boolean MultiCloseConn) {
        this.MultiCloseConn = MultiCloseConn;
    }

    public boolean hasTimeStamp(Table tTable) {
        for (int i = 0; i < tTable.getColumnNum(); i++) {
            if (tTable.getColumn(i).getDBSqlType() == SqlTypes.TIMESTAMP) {
                return true;
            }
        }
        return false;
    }

    private String getTimestamp() {
        String pattern = "yyyy-MM-dd HH:mm:ss SSS";
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date today = new Date();
        return df.format(today);
    }

    public void create(Table tTable) throws Exception {
        hasTimeStamp = hasTimeStamp(tTable);
        String TableName = tTable.getCode();
        PrintWriter out = null;
        String Path = null;
        Path = schemaOutputPATH + outputPackagePATH + "db/";
        String ClassName = TableName + "DB";
        String FileName = ClassName + ".java";
        String SchemaName = TableName + "Schema";
        String SetName = TableName + "Set";
        try {
            // 创建目录
            File dir = new File(Path);
            if (!dir.exists() || !dir.isDirectory()) {
                boolean b = dir.mkdirs();
            }
            // out = new PrintWriter(new FileWriter(new File(Path + FileName)), true);
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    Files.newOutputStream(new File(Path + FileName).toPath()), "GBK")),
                    true);
            // 文件头信息
            out.println("/**");
            out.println(" * Copyright (c) " + getTimestamp().substring(0, 4) + " Sinosoft Co.,LTD.");
            out.println(" * All right reserved.");
            out.println(" */");
            out.println();
            // @Package && @Import
            out.println("package " + packageName + ".db;");
            out.println();
            out.println("import java.io.StringReader;");
            out.println("import java.sql.Connection;");
            out.println("import java.sql.ResultSet;");
            out.println("import java.sql.Statement;");
            out.println("import java.sql.PreparedStatement;");
            out.println("import java.sql.Date;");
            out.println("import java.sql.Types;");
            out.println("import java.util.ArrayList;");

            out.println("import java.util.logging.Logger;");

            out.println();
            out.println("import " + packageName + ".schema." + SchemaName + ";");
            out.println("import " + packageName + ".vschema." + SetName + ";");
            out.println("import com.sinosoft.utility.DBOper;");
            out.println("import com.sinosoft.utility.DBConnPool;");
            out.println("import com.sinosoft.utility.SQLString;");
            out.println("import com.sinosoft.utility.StrTool;");
            out.println("import com.sinosoft.utility.SysConst;");
            out.println("import com.sinosoft.utility.CError;");
            out.println("import com.sinosoft.utility.CErrors;");
            out.println("import com.sinosoft.lis.pubfun.FDate;");
            out.println("import com.sinosoft.lis.pubfun.PreValueBase;");
            out.println();
            // 类信息
            out.println("/**");
            out.println(" * <p>自动生成的文件，不可手工修改！</p>");
            out.println(" * <p>ClassName: " + ClassName + " </p>");
            out.println(" * <p>Description: DB层数据库操作类文件 </p>");
            out.println(" * <p>Company: Sinosoft Co.,LTD </p>");
            out.println(" * @Database: " + DBName);
            out.println(" * @author: Makerx");
            out.println(" * @CreateDatetime: " + getTimestamp());
            if (UserInfo) {
                Properties props = System.getProperties();
                out.println(" * @vm: " + props.getProperty("java.vm.name") + "(build " + props.getProperty("java.vm.version") + ", " + props.getProperty("java.vm.vendor") + ")");
                out.println(" * @os: " + props.getProperty("os.name") + "(" + props.getProperty("os.arch") + ")");
                out.println(" * @creator: " + props.getProperty("user.name") + "(" + props.getProperty("user.country") + ")");
            }
            out.println(" */");
            // @Begin
            out.println("public class " + ClassName + " extends " + SchemaName + "{");
            // 生成数据成员定义
            Field(out);
            out.println();
            // 生成构建器
            Constructor(TableName, out, ClassName);
            out.println();
            // 生成方法
            out.println(space4_1 + "// @Method");
            if (!tTable.haveLargeObjInPK()) {
                // 生成deleteSQL 方法
                deleteSQL(out, ClassName, SchemaName);
                out.println();
            }
            // 生成 getCount 方法
            getCount(out, ClassName, SchemaName);
            out.println();
            // 2005-11-11 Kevin
            // 生成按主键操作的WHERE子句，这个子句在下面的多个部分都会用到。
            // 生成UPDATE操作所需要的所有字段的字句。例如，ContNo = ? , PolNo = ?
            StringBuffer sbPKWhereClause = new StringBuffer(50);
            StringBuffer sbInsertColumnClause = new StringBuffer(300);
            StringBuffer sbUpdateColumnClause = new StringBuffer(300);
            for (int i = 0; i < tTable.getColumnNum(); i++) {
                Column f = tTable.getColumn(i);
                sbInsertColumnClause.append(" , ?");
                sbUpdateColumnClause.append(" , ").append(f.getCode()).append(" = ?");
            }
            Key t_PK = tTable.getPrimaryKey();
            if (t_PK != null) {
                for (int i = 0; i < t_PK.getColumnNum(); i++) {
                    String f = t_PK.getColumn(i);
                    sbPKWhereClause.append(" AND ").append(f).append(" = ?");
                }
                // get rid of the first " AND" part.
                sbPKWhereClause.delete(0, 4);
            }
            if (sbPKWhereClause.length() == 0) {
                sbPKWhereClause.append("1=1");
            }
            // get rid of the first " ," part.
            sbInsertColumnClause.delete(0, 2);
            // get rid of the first " ," part.
            sbUpdateColumnClause.delete(0, 2);
            // 2005-11-14 Kevin
            // 修改insert方法，使用preparedStatement方法。
            if (!tTable.haveLargeObj()) {
                insert(out, tTable, ClassName, sbInsertColumnClause);
                out.println();
            }
            if (t_PK != null && t_PK.getColumnNum() > 0) {
                // 2005-11-14 Kevin
                // 修改delete方法，使用preparedStatement方法。
                if (!tTable.haveLargeObjInPK()) {
                    delete(out, tTable, ClassName, sbPKWhereClause);
                    out.println();
                }
                // 2005-11-14 Kevin
                // 修改update方法，使用preparedStatement方法。
                if (!tTable.haveLargeObj()) {
                    update(out, tTable, ClassName, sbPKWhereClause, sbUpdateColumnClause);
                    out.println();
                }
                // 生成 getInfo方法
                getInfo(out, tTable, ClassName, sbPKWhereClause);
                out.println();
            }
            // 生成 query 方法
            query(out, TableName, ClassName, SchemaName, SetName);
            out.println();
            // 生成 query(start, count) 方法
            queryWithNO(out, TableName, ClassName, SchemaName, SetName);
            out.println();
            // 生成 executeQuery 方法
            executeQuery(out, ClassName, SchemaName, SetName);
            out.println();
            // 生成 executeQuery(sql, start, count) 方法
            executQueryWithNO(out, ClassName, SchemaName, SetName);
            out.println();
            // 生成 executeQueryAsBindVariable(sql, pvbList) 方法
            executeQueryAsBindVariable(out, ClassName, SchemaName, SetName);
            out.println();
            // 生成 executeQueryAsBindVariable(sql, start, count, pvbList) 方法
            executeQueryAsBindVariableWithNO(out, ClassName, SchemaName, SetName);
            out.println();
            // 生成按条件update的方法
            if (!tTable.haveLargeObj()) {
                updateWithStr(out, TableName, ClassName);
                out.println();
            }
            // 生成prepareData方法
            prepareData(out, ClassName);
            out.println();
            // 生成prepareDataAsBindVariable方法
            prepareDataAsBindVariable(out, ClassName);
            out.println();
            // 生成hasMoreData方法
            hasMoreData(out, ClassName);
            out.println();
            // 生成getData方法
            getData(out, TableName, ClassName);
            out.println();
            // 生成closeData方法
            closeData(out, ClassName);
            // xjh Add 2006-10-29 生成addOrderBy方法
            if (addOrderBy) {
                addOrderBy(out, tTable);
            }
            // 生成结尾
            out.println("}");
        } // end of try
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void addOrderBy(PrintWriter out, Table tTable) {
        out.println(space4_1 + "private String OrderByCondition = null;");
        for (int i = 0; i < tTable.getColumnNum(); i++) {
            Column f = tTable.getColumn(i);
            String FieldCode = f.getCode();
            boolean direction = true;
            for (int j = 0; j < 2; j++) {
                if (j == 1) {
                    direction = false;
                }
                out.println(space4_1 + "/**");
                if (direction) {
                    out.println(space4_1 + " * 按照字段" + FieldCode + "(" + f.getName() + ")进行ASE排序");
                } else {
                    out.println(space4_1 + " * 按照字段" + FieldCode + "(" + f.getName() + ")进行排序，");
                    out.println(space4_1 + " * 如果direction为true,则进行ASE排序，否则进行DESC排序");
                    out.println(space4_1 + " *");
                    out.println(space4_1 + " * @param direction boolean");
                }
                out.println(space4_1 + " */");
                out.println(space4_1 + "public void addOrderBy" + FieldCode + "(" + (direction ? "" : "boolean direction") + ")");
                out.println(space4_1 + "{");
                if (direction) {
                    out.println(space4_2 + "if(OrderByCondition == null)");
                    out.println(space4_2 + "{");
                    out.println(space4_3 + "OrderByCondition = \" order by " + FieldCode + "\";");
                    out.println(space4_2 + "}");
                    out.println(space4_2 + "else");
                    out.println(space4_2 + "{");
                    out.println(space4_3 + " OrderByCondition += \"," + FieldCode + "\";");
                    out.println(space4_2 + "}");
                } else {
                    out.println(space4_2 + "addOrderBy" + FieldCode + "();");
                }
                if (!direction) {
                    out.println(space4_2 + "if(direction)");
                    out.println(space4_2 + "{");
                    out.println(space4_3 + "OrderByCondition += \" ASC\";");
                    out.println(space4_2 + "}");
                    out.println(space4_2 + "else");
                    out.println(space4_2 + "{");
                    out.println(space4_3 + "OrderByCondition += \" DESC\";");
                    out.println(space4_2 + "}");
                }
                out.println(space4_1 + "}");
            }
        }
    }


    private void Field(PrintWriter out) {

        out.println(space4_1 + "private final Logger logger = Logger.getLogger(getClass().toString());");

        out.println(space4_1 + "// @Field");
        out.println(space4_1 + "private Connection con;");
        out.println(space4_1 + "private DBOper db;");
        out.println(space4_1 + "/**");
        out.println(space4_1 + " * flag = true: 传入Connection");
        out.println(space4_1 + " * flag = false: 不传入Connection");
        out.println(space4_1 + " **/");
        out.println(space4_1 + "private boolean mflag = false;");
        out.println(space4_1 + "/**");
        out.println(space4_1 + " * 为批量操作而准备的语句和游标对象");
        out.println(space4_1 + " */");
        out.println(space4_1 + "private ResultSet mResultSet = null;");
        out.println(space4_1 + "private PreparedStatement mStatement = null;");
        if (hasTimeStamp) {
            out.println();
            out.println(space4_1 + "private FDate fDate = new FDate(); // 处理日期");
        }
    }


    private void Constructor(String TableName, PrintWriter out, String ClassName) {
        out.println(space4_1 + "// @Constructor");
        out.println(space4_1 + "public " + ClassName + "(Connection cConnection){");
        out.println(space4_2 + "con = cConnection;");
        out.println(space4_2 + "db = new DBOper(con, \"" + TableName + "\");");
        out.println(space4_2 + "mflag = true;");
        out.println(space4_1 + "}");
        out.println();
        out.println(space4_1 + "public " + ClassName + "(){");
        out.println(space4_2 + "con = null;");
        out.println(space4_2 + "db = new DBOper(\"" + TableName + "\");");
        out.println(space4_2 + "mflag = false;");
        out.println(space4_1 + "}");
    }


    private void deleteSQL(PrintWriter out, String ClassName, String SchemaName) {
        out.println(space4_1 + "public boolean deleteSQL(){");
        out.println(space4_2 + SchemaName + " tSchema = this.getSchema();");
        out.println(space4_2 + "if(db.deleteSQL(tSchema))");
        out.println(space4_3 + "return true;");
        out.println(space4_2 + "else{");
        out.println(space4_3 + "// @@错误处理");
        out.println(space4_3 + "this.mErrors.copyAllErrors(db.mErrors);");
        out.println(space4_3 + "CError tError = new CError();");
        out.println(space4_3 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_3 + "tError.functionName = \"deleteSQL\";");
        out.println(space4_3 + "tError.errorMessage = \"操作失败!\";");
        out.println(space4_3 + "this.mErrors.addOneError(tError);");
        out.println(space4_3 + "return false;");
        out.println(space4_2 + "}");
        out.println(space4_1 + "}");
    }


    private void getCount(PrintWriter out, String ClassName, String SchemaName) {
        out.println(space4_1 + "public int getCount(){");
        out.println(space4_2 + SchemaName + " tSchema = this.getSchema();");
        out.println(space4_2 + "int tCount = db.getCount(tSchema);");
        out.println(space4_2 + "if(tCount < 0){");
        out.println(space4_3 + "// @@错误处理");
        out.println(space4_3 + "this.mErrors.copyAllErrors(db.mErrors);");
        out.println(space4_3 + "CError tError = new CError();");
        out.println(space4_3 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_3 + "tError.functionName = \"getCount\";");
        out.println(space4_3 + "tError.errorMessage = \"操作失败!\";");
        out.println(space4_3 + "this.mErrors.addOneError(tError);");
        out.println(space4_3 + "return -1;");
        out.println(space4_2 + "}");
        out.println(space4_2 + "return tCount;");
        out.println(space4_1 + "}");
    }


    private void delete(PrintWriter out, Table tTable,
                        String ClassName, StringBuffer sbPKWhereClause) throws Exception {
        out.println(space4_1 + "/**");
        out.println(space4_1 + " * 删除操作");
        out.println(space4_1 + " * 删除条件：主键");
        out.println(space4_1 + " * @return boolean");
        out.println(space4_1 + " **/");
        out.println(space4_1 + "public boolean delete(){");
        out.println(space4_2 + "PreparedStatement pstmt = null;");
        out.println(space4_2 + "if(!mflag){");
        out.println(space4_3 + "con = DBConnPool.getConnection();");
        out.println(space4_2 + "}");
        out.println(space4_2 + "try{");
        out.println(space4_3 + "String sql = \"DELETE FROM " + tTable.getCode() + " WHERE " + sbPKWhereClause + "\";");
        out.println(space4_3 + "pstmt = con.prepareStatement(sql);");

        out.println(space4_3 + "StringBuilder paramStringBuilder = new StringBuilder();");
        out.println(space4_3 + "paramStringBuilder.append(\"(\");");

        Key t_PK = tTable.getPrimaryKey();
        if (t_PK != null) {
            // 生成设置参数的部分。
            int nParamIndex = 1;
            for (int i = 0; i < t_PK.getColumnNum(); i++) {
                String f_PK = t_PK.getColumn(i);
                for (int j = 0; j < tTable.getColumnNum(); j++) {
                    if (f_PK.equals(tTable.getColumn(j).getCode())) {
                        Column f = tTable.getColumn(j);
                        genSetParamString(f, nParamIndex++, "pstmt", out, space4_3, true);
                        break;
                    }
                }
            }
        }

        out.println(space4_3 + "String params = paramStringBuilder.substring(0, paramStringBuilder.lastIndexOf(\",\"));");
        out.println(space4_3 + "params += \")\";");
        out.println(space4_3 + "logger.info(\"exeSql: \" + sql + \"\\n\" + \"exeSqlParam: \" + params);");

        out.println(space4_3 + "pstmt.executeUpdate();");
        out.println(space4_3 + "pstmt.close();");
        out.println(space4_2 + "}");
        out.println(space4_2 + "catch(Exception ex){");
        out.println(space4_3 + "ex.printStackTrace();");
        out.println(space4_3 + "// @@错误处理");
        out.println(space4_3 + "this.mErrors.copyAllErrors(db.mErrors);");
        out.println(space4_3 + "CError tError = new CError();");
        out.println(space4_3 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_3 + "tError.functionName = \"delete()\";");
        out.println(space4_3 + "tError.errorMessage = ex.toString();");
        out.println(space4_3 + "this.mErrors.addOneError(tError);");
        out.println(space4_3 + "// 输出出错Sql语句");
        out.println(space4_3 + "SQLString sqlObj = new SQLString(\"" + tTable.getCode() + "\");");
        out.println(space4_3 + "sqlObj.setSQL(4, this.getSchema());");
        out.println(space4_3 + "System.out.println(\"出错Sql为：\" + sqlObj.getSQL());");
        out.println(space4_3 + "try{pstmt.close();}catch(Exception e){e.printStackTrace();}");
        out.println(space4_3 + "return false;");
        out.println(space4_2 + "}");
        out.println(space4_2 + "finally{");
        out.println(space4_3 + "if(!mflag){");
        out.println(space4_4 + "try{");
        // xijiahui 2006-07-07 finally里重复关闭数据库连接在有些JDBC驱动下会报错，例如Sql Server的jDts驱动
        if (MultiCloseConn) {
            out.println(space4_5 + "con.close();");
        } else {
            out.println(space4_5 + "if(con.isClosed() == false){");
            out.println(space4_6 + "con.close();");
            out.println(space4_5 + "}");
        }
        out.println(space4_4 + "}");
        out.println(space4_4 + "catch(Exception e){");
        out.println(space4_5 + "e.printStackTrace();");
        out.println(space4_4 + "}");
        out.println(space4_3 + "}");
        out.println(space4_2 + "}");
        out.println(space4_2 + "return true;");
        out.println(space4_1 + "}");
    }


    private void update(PrintWriter out, Table tTable, String ClassName,
                        StringBuffer sbPKWhereClause, StringBuffer sbUpdateColumnClause) throws Exception {
        out.println(space4_1 + "/**");
        out.println(space4_1 + " * 更新操作");
        out.println(space4_1 + " * 更新条件：主键");
        out.println(space4_1 + " * @return boolean");
        out.println(space4_1 + " */");
        out.println(space4_1 + "public boolean update(){");
        out.println(space4_2 + "PreparedStatement pstmt = null;");
        out.println(space4_2 + "if(!mflag){");
        out.println(space4_3 + "con = DBConnPool.getConnection();");
        out.println(space4_2 + "}");
        out.println(space4_2 + "try{");
        out.println(space4_3 + "String sql = \"UPDATE " + tTable.getCode() + " SET " + sbUpdateColumnClause + " WHERE " + sbPKWhereClause + "\";");
        out.println(space4_3 + "pstmt = con.prepareStatement(sql);");

        out.println(space4_3 + "StringBuilder paramStringBuilder = new StringBuilder();");
        out.println(space4_3 + "paramStringBuilder.append(\"(\");");

        // 生成设置数据的部分。
        int nParamIndex = 1;
        for (int i = 0; i < tTable.getColumnNum(); i++) {
            Column f = tTable.getColumn(i);
            genSetParamString(f, nParamIndex++, "pstmt", out, space4_3, false);
        }
        out.println(space4_3 + "// set where condition");
        Key t_PK = tTable.getPrimaryKey();
        if (t_PK != null) {
            for (int i = 0; i < t_PK.getColumnNum(); i++) {
                String f_PK = t_PK.getColumn(i);
                for (int j = 0; j < tTable.getColumnNum(); j++) {
                    if (f_PK.equals(tTable.getColumn(j).getCode())) {
                        Column f = tTable.getColumn(j);
                        genSetParamString(f, nParamIndex++, "pstmt", out, space4_3, true);
                        break;
                    }
                }
            }
        }

        out.println(space4_3 + "String params = paramStringBuilder.substring(0, paramStringBuilder.lastIndexOf(\",\"));");
        out.println(space4_3 + "params += \")\";");
        out.println(space4_3 + "logger.info(\"exeSql: \" + sql + \"\\n\" + \"exeSqlParam: \" + params);");

        out.println(space4_3 + "pstmt.executeUpdate();");
        out.println(space4_3 + "pstmt.close();");
        out.println(space4_2 + "}");
        out.println(space4_2 + "catch(Exception ex){");
        out.println(space4_3 + "ex.printStackTrace();");
        out.println(space4_3 + "// @@错误处理");
        out.println(space4_3 + "this.mErrors.copyAllErrors(db.mErrors);");
        out.println(space4_3 + "CError tError = new CError();");
        out.println(space4_3 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_3 + "tError.functionName = \"update()\";");
        out.println(space4_3 + "tError.errorMessage = ex.toString();");
        out.println(space4_3 + "this.mErrors.addOneError(tError);");
        out.println(space4_3 + "// 输出出错Sql语句");
        out.println(space4_3 + "SQLString sqlObj = new SQLString(\"" + tTable.getCode() + "\");");
        out.println(space4_3 + "sqlObj.setSQL(2, this.getSchema());");
        out.println(space4_3 + "System.out.println(\"出错Sql为：\" + sqlObj.getSQL());");
        out.println(space4_3 + "try{pstmt.close();}catch(Exception e){e.printStackTrace();}");
        out.println(space4_3 + "return false;");
        out.println(space4_2 + "}");
        out.println(space4_2 + "finally{");
        out.println(space4_3 + "if(!mflag){");
        out.println(space4_4 + "try{");
        // xijiahui 2006-07-07 finally里重复关闭数据库连接在有些JDBC驱动下会报错，例如Sql Server的jDts驱动
        if (MultiCloseConn) {
            out.println(space4_5 + "con.close();");
        } else {
            out.println(space4_5 + "if(con.isClosed() == false){");
            out.println(space4_6 + "con.close();");
            out.println(space4_5 + "}");
        }
        out.println(space4_4 + "}");
        out.println(space4_4 + "catch(Exception e){");
        out.println(space4_5 + "e.printStackTrace();");
        out.println(space4_4 + "}");
        out.println(space4_3 + "}");
        out.println(space4_2 + "}");
        out.println(space4_2 + "return true;");
        out.println(space4_1 + "}");
    }


    private void insert(PrintWriter out, Table tTable,
                        String ClassName, StringBuffer sbInsertColumnClause) throws Exception {
        out.println(space4_1 + "/**");
        out.println(space4_1 + " * 新增操作");
        out.println(space4_1 + " * @return boolean");
        out.println(space4_1 + " */");
        out.println(space4_1 + "public boolean insert(){");
        out.println(space4_2 + "PreparedStatement pstmt = null;");
        out.println(space4_2 + "if(!mflag){");
        out.println(space4_3 + "con = DBConnPool.getConnection();");
        out.println(space4_2 + "}");
        out.println(space4_2 + "try{");
        out.println(space4_3 + "String sql = \"INSERT INTO " + tTable.getCode() + " VALUES (" + sbInsertColumnClause + ")\";");
        out.println(space4_3 + "pstmt = con.prepareStatement(sql);");

        out.println(space4_3 + "StringBuilder paramStringBuilder = new StringBuilder();");
        out.println(space4_3 + "paramStringBuilder.append(\"(\");");

        // 生成设置数据的部分。
        int nParamIndex = 1;
        for (int i = 0; i < tTable.getColumnNum(); i++) {
            Column f = tTable.getColumn(i);
            genSetParamString(f, nParamIndex++, "pstmt", out, space4_3, false);
        }

        out.println(space4_3 + "String params = paramStringBuilder.substring(0, paramStringBuilder.lastIndexOf(\",\"));");
        out.println(space4_3 + "params += \")\";");
        out.println(space4_3 + "logger.info(\"exeSql: \" + sql + \"\\n\" + \"exeSqlParam: \" + params);");

        out.println(space4_3 + "// execute sql");
        out.println(space4_3 + "pstmt.executeUpdate();");
        out.println(space4_3 + "pstmt.close();");
        out.println(space4_2 + "}");
        out.println(space4_2 + "catch(Exception ex){");
        out.println(space4_3 + "ex.printStackTrace();");
        out.println(space4_3 + "// @@错误处理");
        out.println(space4_3 + "this.mErrors.copyAllErrors(db.mErrors);");
        out.println(space4_3 + "CError tError = new CError();");
        out.println(space4_3 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_3 + "tError.functionName = \"insert()\";");
        out.println(space4_3 + "tError.errorMessage = ex.toString();");
        out.println(space4_3 + "this.mErrors.addOneError(tError);");
        out.println(space4_3 + "// 输出出错Sql语句");
        out.println(space4_3 + "SQLString sqlObj = new SQLString(\"" + tTable.getCode() + "\");");
        out.println(space4_3 + "sqlObj.setSQL(1, this.getSchema());");
        out.println(space4_3 + "System.out.println(\"出错Sql为：\" + sqlObj.getSQL());");
        out.println(space4_3 + "try{pstmt.close();}catch(Exception e){e.printStackTrace();}");
        out.println(space4_3 + "return false;");
        out.println(space4_2 + "}");
        out.println(space4_2 + "finally{");
        out.println(space4_3 + "if(!mflag){");
        out.println(space4_4 + "try{");
        // xijiahui 2006-07-07 finally里重复关闭数据库连接在有些JDBC驱动下会报错，例如Sql Server的jDts驱动
        if (MultiCloseConn) {
            out.println(space4_5 + "con.close();");
        } else {
            out.println(space4_5 + "if(con.isClosed() == false){");
            out.println(space4_6 + "con.close();");
            out.println(space4_5 + "}");
        }
        out.println(space4_4 + "}");
        out.println(space4_4 + "catch(Exception e){");
        out.println(space4_5 + "e.printStackTrace();");
        out.println(space4_4 + "}");
        out.println(space4_3 + "}");
        out.println(space4_2 + "}");
        out.println(space4_2 + "return true;");
        out.println(space4_1 + "}");
    }


    private void getInfo(PrintWriter out, Table tTable,
                         String ClassName, StringBuffer sbPKWhereClause) throws Exception {
        out.println(space4_1 + "/**");
        out.println(space4_1 + " * 查询操作");
        out.println(space4_1 + " * 查询条件：主键");
        out.println(space4_1 + " * @return boolean");
        out.println(space4_1 + " */");
        out.println(space4_1 + "public boolean getInfo(){");
        out.println(space4_2 + "PreparedStatement pstmt = null;");
        out.println(space4_2 + "ResultSet rs = null;");
        out.println(space4_2 + "if(!mflag){");
        out.println(space4_3 + "con = DBConnPool.getConnection();");
        out.println(space4_2 + "}");
        out.println(space4_2 + "try{");
        out.println(space4_3 + "String sql = \"SELECT * FROM " + tTable.getCode() + " WHERE " + sbPKWhereClause + "\";");
        out.println(space4_3 + "pstmt = con.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);");

        out.println(space4_3 + "StringBuilder paramStringBuilder = new StringBuilder();");
        out.println(space4_3 + "paramStringBuilder.append(\"(\");");

        int nParamIndex = 1;
        Key t_PK = tTable.getPrimaryKey();
        if (t_PK != null) {
            for (int i = 0; i < t_PK.getColumnNum(); i++) {
                String f_PK = t_PK.getColumn(i);
                for (int j = 0; j < tTable.getColumnNum(); j++) {
                    if (f_PK.equals(tTable.getColumn(j).getCode())) {
                        Column f = tTable.getColumn(j);
                        genSetParamString(f, nParamIndex++, "pstmt", out, space4_3, true);
                        break;
                    }
                }
            }
        }

        out.println(space4_3 + "String params = paramStringBuilder.substring(0, paramStringBuilder.lastIndexOf(\",\"));");
        out.println(space4_3 + "params += \")\";");
        out.println(space4_3 + "logger.info(\"exeSql: \" + sql + \"\\n\" + \"exeSqlParam: \" + params);");

        out.println(space4_3 + "rs = pstmt.executeQuery();");
        out.println(space4_3 + "int i = 0;");
        out.println(space4_3 + "if(rs.next()){");
        out.println(space4_4 + "i++;");
        out.println(space4_4 + "if(!this.setSchema(rs, i)){");
        out.println(space4_5 + "// @@错误处理");
        out.println(space4_5 + "CError tError = new CError();");
        out.println(space4_5 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_5 + "tError.functionName = \"getInfo\";");
        out.println(space4_5 + "tError.errorMessage = \"取数失败!\";");
        out.println(space4_5 + "this.mErrors.addOneError(tError);");
        out.println(space4_5 + "i = 0;");
        out.println(space4_4 + "}");
        out.println(space4_3 + "}");
        out.println(space4_3 + "try{rs.close();}catch(Exception e1){e1.printStackTrace();}");
        out.println(space4_3 + "try{pstmt.close();}catch(Exception e2){e2.printStackTrace();}");
        out.println(space4_3 + "if(i == 0){");
        out.println(space4_4 + "return false;");
        out.println(space4_3 + "}");
        out.println(space4_2 + "}");
        out.println(space4_2 + "catch(Exception e){");
        out.println(space4_3 + "e.printStackTrace();");
        out.println(space4_3 + "CError tError = new CError();");
        out.println(space4_3 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_3 + "tError.functionName = \"getInfo\";");
        out.println(space4_3 + "tError.errorMessage = e.toString();");
        out.println(space4_3 + "this.mErrors.addOneError(tError);");
        out.println(space4_3 + "// 输出出错Sql语句");
        out.println(space4_3 + "SQLString sqlObj = new SQLString(\"" + tTable.getCode() + "\");");
        out.println(space4_3 + "sqlObj.setSQL(6, this.getSchema());");
        out.println(space4_3 + "System.out.println(\"出错Sql为：\" + sqlObj.getSQL());");
        out.println(space4_3 + "try{rs.close();}catch(Exception e1){e1.printStackTrace();}");
        out.println(space4_3 + "try{pstmt.close();}catch(Exception e2){e2.printStackTrace();}");
        out.println(space4_3 + "return false;");
        out.println(space4_2 + "}");
        out.println(space4_2 + "finally{");
        out.println(space4_3 + "// 断开数据库连接");
        out.println(space4_3 + "if(!mflag){");
        out.println(space4_4 + "try{");
        // xijiahui 2006-07-07 finally里重复关闭数据库连接在有些JDBC驱动下会报错，例如Sql Server的jDts驱动
        if (MultiCloseConn) {
            out.println(space4_5 + "con.close();");
        } else {
            out.println(space4_5 + "if(con.isClosed() == false){");
            out.println(space4_6 + "con.close();");
            out.println(space4_5 + "}");
        }
        out.println(space4_4 + "}");
        out.println(space4_4 + "catch(Exception e){");
        out.println(space4_5 + "e.printStackTrace();");
        out.println(space4_4 + "}");
        out.println(space4_3 + "}");
        out.println(space4_2 + "}");
        out.println(space4_2 + "return true;");
        out.println(space4_1 + "}");
    }


    private void queryWithNO(PrintWriter out, String TableName,
                             String ClassName, String SchemaName, String SetName) {
        out.println(space4_1 + "/**");
        out.println(space4_1 + " * 查询操作");
        out.println(space4_1 + " * 查询条件：传入的schema中有值的字段");
        out.println(space4_1 + " * @return boolean");
        out.println(space4_1 + " */");
        out.println(space4_1 + "public " + SetName + " query(int nStart, int nCount){");
        out.println(space4_2 + "SQLString sqlObj = new SQLString(\"" + TableName + "\");");
        out.println(space4_2 + "sqlObj.setSQL(8, this.getSchema());");
        out.println(space4_2 + "String sql = sqlObj.getSQL();");
        out.println(space4_2 + "ArrayList bindVarAL = sqlObj.getBindVarAL();");
        if (addOrderBy) {
            out.println(space4_2 + "if(OrderByCondition != null){");
            out.println(space4_3 + "sql += OrderByCondition;");
            out.println(space4_2 + "}");
        }
        out.println(space4_2 + "return executeQueryAsBindVariable(sql,nStart,nCount,bindVarAL);");
        out.println(space4_1 + "}");
    }


    private void executeQuery(PrintWriter out, String ClassName, String SchemaName, String SetName) {
        out.println(space4_1 + "public " + SetName + " executeQuery(String sql){");
        out.println(space4_2 + "return executeQuery(sql,-1,-1);");
        out.println(space4_1 + "}");
    }


    private void executQueryWithNO(PrintWriter out, String ClassName, String SchemaName, String SetName) {
        out.println(space4_1 + "public " + SetName + " executeQuery(String sql, int nStart, int nCount){");
        out.println(space4_2 + "return executeQueryAsBindVariable(sql,nStart,nCount,null);");
        out.println(space4_1 + "}");
    }


    private void executeQueryAsBindVariable(PrintWriter out, String ClassName, String SchemaName, String SetName) {
        out.println(space4_1 + "public " + SetName + " executeQueryAsBindVariable(String sql,ArrayList pvbList){");
        out.println(space4_2 + "return executeQueryAsBindVariable(sql,-1,-1,pvbList);");
        out.println(space4_1 + "}");
    }


    private void executeQueryAsBindVariableWithNO(PrintWriter out, String ClassName, String SchemaName, String SetName) {
        out.println(space4_1 + "public " + SetName + " executeQueryAsBindVariable(String sql, int nStart, int nCount,ArrayList pvbList){");
        out.println(space4_2 + "PreparedStatement pstmt = null;");
        out.println(space4_2 + "ResultSet rs = null;");
        out.println(space4_2 + SetName + " a" + SetName + " = new " + SetName + "();");
        out.println(space4_2 + "if(!mflag){");
        out.println(space4_3 + "con = DBConnPool.getConnection();");
        out.println(space4_2 + "}");
        out.println(space4_2 + "try{");
        out.println(space4_3 + "pstmt = con.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);");

        out.println(space4_3 + "String params = \"\";");

        out.println(space4_3 + "if (pvbList != null && pvbList.size() > 0){");

        out.println(space4_4 + "StringBuilder paramStringBuilder = new StringBuilder();");
        out.println(space4_4 + "paramStringBuilder.append(\"(\");");

        out.println(space4_4 + "for (int i = 0; i < pvbList.size(); i++){");
        out.println(space4_5 + "PreValueBase tPvb = (PreValueBase) pvbList.get(i);");
        out.println(space4_5 + "pstmt.setObject(tPvb.getLocation(),tPvb.getValue(),tPvb.getValueType());");

        out.println(space4_5 + "paramStringBuilder.append(tPvb.getValue()).append(\",\");");

        out.println(space4_4 + "}");

        out.println(space4_4 + "params = paramStringBuilder.substring(0, paramStringBuilder.lastIndexOf(\",\"));");
        out.println(space4_4 + "params += \")\";");

        out.println(space4_3 + "}");

        out.println(space4_3 + "logger.info(\"exeSql: \" + sql + \"\\n\" + \"exeSqlParam: \" + params);");

        out.println(space4_3 + "rs = pstmt.executeQuery();");
        out.println(space4_3 + "int i = 0;");
        out.println(space4_3 + "while(rs.next()){");
        out.println(space4_4 + "i++;");
        out.println(space4_4 + "//如果nStart=-1，表示全部查询");
        // 小于起始值，继续
        out.println(space4_4 + "if(nStart != -1 && i < nStart){");
        out.println(space4_5 + "continue;");
        out.println(space4_4 + "}");
        // 大于结束值，退出
        out.println(space4_4 + "if(nStart != -1 && i >= nStart + nCount){");
        out.println(space4_5 + "break;");
        out.println(space4_4 + "}");
        out.println(space4_4 + SchemaName + " s1 = new " + SchemaName + "();");
        out.println(space4_4 + "if(!s1.setSchema(rs, i)){");
        out.println(space4_5 + "// @@错误处理");
        out.println(space4_5 + "CError tError = new CError();");
        out.println(space4_5 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_5 + "tError.functionName = \"executeQuery\";");
        out.println(space4_5 + "tError.errorMessage = \"sql语句有误，请查看表名及字段名信息!\";");
        out.println(space4_5 + "this.mErrors.addOneError(tError);");
        out.println(space4_4 + "}");
        out.println(space4_4 + "a" + SetName + ".add(s1);");
        out.println(space4_3 + "}");
        out.println(space4_3 + "try{rs.close();}catch(Exception e1){e1.printStackTrace();}");
        out.println(space4_3 + "try{pstmt.close();}catch(Exception e2){e2.printStackTrace();}");
        out.println(space4_2 + "}");
        out.println(space4_2 + "catch(Exception e)");
        out.println(space4_2 + "{");
        out.println(space4_3 + "System.out.println(\"##### Error Sql in " + ClassName + " at query(): \" + sql);");
        out.println(space4_3 + "// @@错误处理");
        out.println(space4_3 + "CError tError = new CError();");
        out.println(space4_3 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_3 + "tError.functionName = \"query\";");
        out.println(space4_3 + "tError.errorMessage = e.toString();");
        out.println(space4_3 + "this.mErrors.addOneError(tError);");
        out.println(space4_3 + "try{rs.close();}catch(Exception e1){e1.printStackTrace();}");
        out.println(space4_3 + "try{pstmt.close();}catch(Exception e2){e2.printStackTrace();}");
        out.println(space4_2 + "}");
        out.println(space4_2 + "finally{");
        out.println(space4_3 + "if(!mflag){");
        out.println(space4_4 + "try{");
        // xijiahui 2006-07-07 finally里重复关闭数据库连接在有些JDBC驱动下会报错，例如Sql Server的jDts驱动
        if (MultiCloseConn) {
            out.println(space4_5 + "con.close();");
        } else {
            out.println(space4_5 + "if(con.isClosed() == false){");
            out.println(space4_6 + "con.close();");
            out.println(space4_5 + "}");
        }
        out.println(space4_4 + "}");
        out.println(space4_4 + "catch(Exception e){");
        out.println(space4_5 + "e.printStackTrace();");
        out.println(space4_4 + "}");
        out.println(space4_3 + "}");
        out.println(space4_2 + "}");
        out.println(space4_2 + "return a" + SetName + ";");
        out.println(space4_1 + "}");
    }


    private void query(PrintWriter out, String TableName, String ClassName, String SchemaName, String SetName) {
        out.println(space4_1 + "public " + SetName + " query(){");
        out.println(space4_2 + "return query(-1,-1);");
        out.println(space4_1 + "}");

    }


    private void updateWithStr(PrintWriter out, String TableName, String ClassName) {
        out.println(space4_1 + "public boolean update(String strWherePart){");
        out.println(space4_2 + "Statement stmt = null;");
        out.println(space4_2 + "if(!mflag){");
        out.println(space4_3 + "con = DBConnPool.getConnection();");
        out.println(space4_2 + "}");
        out.println(space4_2 + "SQLString sqlObj = new SQLString(\"" + TableName + "\");");
        out.println(space4_2 + "sqlObj.setSQL(2, this.getSchema());");
        out.println(space4_2 + "String sql = \"update " + TableName + " \"" + " + sqlObj.getUpdPart() + \" where \" + strWherePart;");
        out.println(space4_2 + "try{");
        out.println(space4_3 + "stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);");

        out.println(space4_3 + "logger.info(\"exeSql: \" + sql);");

        out.println(space4_3 + "int operCount = stmt.executeUpdate(sql);");
        out.println(space4_3 + "if(operCount == 0){");
        out.println(space4_4 + "// @@错误处理");
        out.println(space4_4 + "CError tError = new CError();");
        out.println(space4_4 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_4 + "tError.functionName = \"update\";");
        out.println(space4_4 + "tError.errorMessage = \"更新数据失败!\";");
        out.println(space4_4 + "this.mErrors.addOneError(tError);");
        out.println(space4_4 + "return false;");
        out.println(space4_3 + "}");
        out.println(space4_2 + "}");
        out.println(space4_2 + "catch(Exception e){");
        out.println(space4_3 + "System.out.println(\"##### Error Sql in " + ClassName + " at update(String strWherePart): \" + sql);");
        out.println(space4_3 + "// @@错误处理");
        out.println(space4_3 + "CError tError = new CError();");
        out.println(space4_3 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_3 + "tError.functionName = \"update\";");
        out.println(space4_3 + "tError.errorMessage = e.toString();");
        out.println(space4_3 + "this.mErrors.addOneError(tError);");
        out.println(space4_3 + "try{stmt.close();}catch(Exception ex1){ex1.printStackTrace();}");
        out.println(space4_3 + "return false;");
        out.println(space4_2 + "}");
        out.println(space4_2 + "finally{");
        out.println(space4_3 + "// 断开数据库连接");
        out.println(space4_3 + "if(!mflag){");
        out.println(space4_4 + "try{");
        // xijiahui 2006-07-07 finally里重复关闭数据库连接在有些JDBC驱动下会报错，例如Sql Server的jDts驱动
        if (MultiCloseConn) {
            out.println(space4_5 + "con.close();");
        } else {
            out.println(space4_5 + "if(con.isClosed() == false){");
            out.println(space4_6 + "con.close();");
            out.println(space4_5 + "}");
        }
        out.println(space4_4 + "}");
        out.println(space4_4 + "catch(Exception e){");
        out.println(space4_5 + "e.printStackTrace();");
        out.println(space4_4 + "}");
        out.println(space4_3 + "}");
        out.println(space4_2 + "}");
        out.println(space4_2 + "return true;");
        out.println(space4_1 + "}");
    }


    private void prepareData(PrintWriter out, String ClassName) {
        out.println(space4_1 + "/**");
        out.println(space4_1 + " * 准备数据集");
        out.println(space4_1 + " * @param strSQL String");
        out.println(space4_1 + " * @return boolean");
        out.println(space4_1 + " */");
        out.println(space4_1 + "public boolean prepareData(String strSQL){");
        out.println(space4_2 + "return prepareDataAsBindVariable(strSQL,null);");
        out.println(space4_1 + "}");
    }


    private void prepareDataAsBindVariable(PrintWriter out, String ClassName) {
        out.println(space4_1 + "/**");
        out.println(space4_1 + " * 准备数据集，使用绑定变量方式");
        out.println(space4_1 + " * @param strSQL String");
        out.println(space4_1 + " * @param pvbList ArrayList");
        out.println(space4_1 + " * @return boolean");
        out.println(space4_1 + " */");
        out.println(space4_1 + "public boolean prepareDataAsBindVariable(String strSQL, ArrayList pvbList){");
        out.println(space4_2 + "if(mResultSet != null){");
        out.println(space4_3 + "// @@错误处理");
        out.println(space4_3 + "CError tError = new CError();");
        out.println(space4_3 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_3 + "tError.functionName = \"prepareData\";");
        out.println(space4_3 + "tError.errorMessage = \"数据集非空，程序在准备数据集之后，没有关闭！\";");
        out.println(space4_3 + "this.mErrors.addOneError(tError);");
        out.println(space4_3 + "return false;");
        out.println(space4_2 + "}");
        out.println(space4_2 + "if(!mflag){");
        out.println(space4_3 + "con = DBConnPool.getConnection();");
        out.println(space4_2 + "}");
        out.println(space4_2 + "try{");
        out.println(space4_3 + "mStatement = con.prepareStatement(StrTool.GBKToUnicode(strSQL),ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);");

        out.println(space4_3 + "String params = \"\";");

        out.println(space4_3 + "if (pvbList != null && pvbList.size() > 0){");

        out.println(space4_4 + "StringBuilder paramStringBuilder = new StringBuilder();");
        out.println(space4_4 + "paramStringBuilder.append(\"(\");");

        out.println(space4_4 + "for (int i = 0; i < pvbList.size(); i++){");
        out.println(space4_5 + "PreValueBase tPvb = (PreValueBase) pvbList.get(i);");
        out.println(space4_5 + "mStatement.setObject(tPvb.getLocation(),tPvb.getValue(),tPvb.getValueType());");

        out.println(space4_5 + "paramStringBuilder.append(tPvb.getValue()).append(\",\");");

        out.println(space4_4 + "}");

        out.println(space4_4 + "params = paramStringBuilder.substring(0, paramStringBuilder.lastIndexOf(\",\"));");
        out.println(space4_4 + "params += \")\";");

        out.println(space4_3 + "}");

        out.println(space4_3 + "logger.info(\"exeSql: \" + StrTool.GBKToUnicode(strSQL) + \"\\n\" + \"exeSqlParam: \" + params);");

        out.println(space4_3 + "mResultSet = mStatement.executeQuery();");
        out.println(space4_2 + "}");
        out.println(space4_2 + "catch(Exception e){");
        out.println(space4_3 + "// @@错误处理");
        out.println(space4_3 + "CError tError = new CError();");
        out.println(space4_3 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_3 + "tError.functionName = \"prepareData\";");
        out.println(space4_3 + "tError.errorMessage = e.toString();");
        out.println(space4_3 + "this.mErrors.addOneError(tError);");
        out.println(space4_3 + "try{mResultSet.close();}catch(Exception ex2){ex2.printStackTrace();}");
        out.println(space4_3 + "try{mStatement.close();}catch(Exception ex3){ex3.printStackTrace();}");
        out.println(space4_3 + "try{");
        out.println(space4_4 + "if(con.isClosed() == false){");
        out.println(space4_5 + "con.close();");
        out.println(space4_4 + "}");
        out.println(space4_3 + "}");
        out.println(space4_3 + "catch(Exception e1){");
        out.println(space4_4 + "e1.printStackTrace();");
        out.println(space4_3 + "}");
        out.println(space4_3 + "return false;");
        out.println(space4_2 + "}");
        out.println(space4_2 + "return true;");
        out.println(space4_1 + "}");
    }


    private void hasMoreData(PrintWriter out, String ClassName) {
        out.println(space4_1 + "/**");
        out.println(space4_1 + " * 获取数据集");
        out.println(space4_1 + " * @return boolean");
        out.println(space4_1 + " */");
        out.println(space4_1 + "public boolean hasMoreData(){");
        out.println(space4_2 + "boolean flag = true;");
        out.println(space4_2 + "if(null == mResultSet){");
        out.println(space4_3 + "CError tError = new CError();");
        out.println(space4_3 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_3 + "tError.functionName = \"hasMoreData\";");
        out.println(space4_3 + "tError.errorMessage = \"数据集为空，请先准备数据集！\";");
        out.println(space4_3 + "this.mErrors.addOneError(tError);");
        out.println(space4_3 + "return false;");
        out.println(space4_2 + "}");
        out.println(space4_2 + "try{");
        out.println(space4_3 + "flag = mResultSet.next();");
        out.println(space4_2 + "}");
        out.println(space4_2 + "catch(Exception ex){");
        out.println(space4_3 + "CError tError = new CError();");
        out.println(space4_3 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_3 + "tError.functionName = \"hasMoreData\";");
        out.println(space4_3 + "tError.errorMessage = ex.toString();");
        out.println(space4_3 + "this.mErrors.addOneError(tError);");
        out.println(space4_3 + "try{mResultSet.close();mResultSet = null;}catch(Exception ex2){ex2.printStackTrace();}");
        out.println(space4_3 + "try{mStatement.close();mStatement = null;}catch(Exception ex3){ex3.printStackTrace();}");
        out.println(space4_3 + "if(!mflag){");
        out.println(space4_4 + "try{con.close();}catch(Exception et){et.printStackTrace();}");
        out.println(space4_3 + "}");
        out.println(space4_3 + "return false;");
        out.println(space4_2 + "}");
        out.println(space4_2 + "return flag;");
        out.println(space4_1 + "}");
    }


    private void getData(PrintWriter out, String TableName, String ClassName) {
        out.println(space4_1 + "/**");
        out.println(space4_1 + " * 获取定量数据");
        out.println(space4_1 + " * @return " + TableName + "Set");
        out.println(space4_1 + " */");
        out.println(space4_1 + "public " + TableName + "Set getData(){");
        out.println(space4_2 + "int tCount = 0;");
        out.println(space4_2 + TableName + "Set t" + TableName + "Set = new " + TableName + "Set();");
        out.println(space4_2 + TableName + "Schema t" + TableName + "Schema = null;");
        out.println(space4_2 + "if(null == mResultSet){");
        out.println(space4_3 + "CError tError = new CError();");
        out.println(space4_3 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_3 + "tError.functionName = \"getData\";");
        out.println(space4_3 + "tError.errorMessage = \"数据集为空，请先准备数据集！\";");
        out.println(space4_3 + "this.mErrors.addOneError(tError);");
        out.println(space4_3 + "return null;");
        out.println(space4_2 + "}");
        out.println(space4_2 + "try{");
        out.println(space4_3 + "tCount = 1;");
        out.println(space4_3 + "if(mResultSet.next()){");
        out.println(space4_4 + "t" + TableName + "Schema = new " + TableName + "Schema();");
        out.println(space4_4 + "t" + TableName + "Schema.setSchema(mResultSet, 1);");
        out.println(space4_4 + "t" + TableName + "Set.add(t" + TableName + "Schema);");
        out.println(space4_3 + "}");
        out.println(space4_3 + "//注意mResultSet.next()的作用");
        out.println(space4_3 + "while(tCount++ < SysConst.FETCHCOUNT && mResultSet.next()){");
//        out.println(space4_4 + "if(mResultSet.next())");
//        out.println(space4_4 + "{");
        out.println(space4_4 + "t" + TableName + "Schema = new " + TableName + "Schema();");
        out.println(space4_4 + "t" + TableName + "Schema.setSchema(mResultSet, 1);");
        out.println(space4_4 + "t" + TableName + "Set.add(t" + TableName + "Schema);");
//        out.println(space4_4 + "}");
        out.println(space4_3 + "}");
        out.println(space4_2 + "}");
        out.println(space4_2 + "catch(Exception ex){");
        out.println(space4_3 + "CError tError = new CError();");
        out.println(space4_3 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_3 + "tError.functionName = \"getData\";");
        out.println(space4_3 + "tError.errorMessage = ex.toString();");
        out.println(space4_3 + "this.mErrors.addOneError(tError);");
        out.println(space4_3 + "try{mResultSet.close();mResultSet = null;}catch(Exception ex2){ex2.printStackTrace();}");
        out.println(space4_3 + "try{mStatement.close();mStatement = null;}catch(Exception ex3){ex3.printStackTrace();}");
        out.println(space4_3 + "if(!mflag){");
        out.println(space4_4 + "try{con.close();}catch(Exception et){et.printStackTrace();}");
        out.println(space4_3 + "}");
        out.println(space4_3 + "return null;");
        out.println(space4_2 + "}");
        out.println(space4_2 + "return t" + TableName + "Set;");
        out.println(space4_1 + "}");
    }


    private void closeData(PrintWriter out, String ClassName) {
        out.println(space4_1 + "/**");
        out.println(space4_1 + " * 关闭数据集");
        out.println(space4_1 + " * @return boolean");
        out.println(space4_1 + " */");
        out.println(space4_1 + "public boolean closeData(){");
        out.println(space4_2 + "boolean flag = true;");
        out.println(space4_2 + "try{");
        out.println(space4_3 + "if(null == mResultSet){");
        out.println(space4_4 + "CError tError = new CError();");
        out.println(space4_4 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_4 + "tError.functionName = \"closeData\";");
        out.println(space4_4 + "tError.errorMessage = \"数据集已经关闭了！\";");
        out.println(space4_4 + "this.mErrors.addOneError(tError);");
        out.println(space4_4 + "flag = false;");
        out.println(space4_3 + "}");
        out.println(space4_3 + "else{");
        out.println(space4_4 + "mResultSet.close();");
        out.println(space4_4 + "mResultSet = null;");
        out.println(space4_3 + "}");
        out.println(space4_2 + "}");
        out.println(space4_2 + "catch(Exception ex2){");
        out.println(space4_3 + "CError tError = new CError();");
        out.println(space4_3 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_3 + "tError.functionName = \"closeData\";");
        out.println(space4_3 + "tError.errorMessage = ex2.toString();");
        out.println(space4_3 + "this.mErrors.addOneError(tError);");
        out.println(space4_3 + "flag = false;");
        out.println(space4_2 + "}");
        out.println(space4_2 + "try{");
        out.println(space4_3 + "if(null == mStatement){");
        out.println(space4_4 + "CError tError = new CError();");
        out.println(space4_4 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_4 + "tError.functionName = \"closeData\";");
        out.println(space4_4 + "tError.errorMessage = \"语句已经关闭了！\";");
        out.println(space4_4 + "this.mErrors.addOneError(tError);");
        out.println(space4_4 + "flag = false;");
        out.println(space4_3 + "}");
        out.println(space4_3 + "else{");
        out.println(space4_4 + "mStatement.close();");
        out.println(space4_4 + "mStatement = null;");
        out.println(space4_3 + "}");
        out.println(space4_2 + "}");
        out.println(space4_2 + "catch(Exception ex3){");
        out.println(space4_3 + "CError tError = new CError();");
        out.println(space4_3 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_3 + "tError.functionName = \"closeData\";");
        out.println(space4_3 + "tError.errorMessage = ex3.toString();");
        out.println(space4_3 + "this.mErrors.addOneError(tError);");
        out.println(space4_3 + "flag = false;");
        out.println(space4_2 + "}");
        out.println(space4_2 + "finally{");
        out.println(space4_3 + "if(!mflag){");
        out.println(space4_4 + "try{");
        // xijiahui 2006-07-07 finally里重复关闭数据库连接在有些JDBC驱动下会报错，例如Sql Server的jDts驱动
        if (MultiCloseConn) {
            out.println(space4_5 + "con.close();");
        } else {
            out.println(space4_5 + "if(con.isClosed() == false){");
            out.println(space4_6 + "con.close();");
            out.println(space4_5 + "}");
        }
        out.println(space4_4 + "}");
        out.println(space4_4 + "catch(Exception e){");
        out.println(space4_5 + "e.printStackTrace();");
        out.println(space4_4 + "}");
        out.println(space4_3 + "}");
        out.println(space4_2 + "}");
        out.println(space4_2 + "return flag;");
        out.println(space4_1 + "}");
    }


    private void setSchemaByRS(PrintWriter out, Table tTable, String ClassName, String SchemaName) {
        out.println(space4_1 + "/**");
        out.println(space4_1 + " * 使用 ResultSet 中的第 i 行给 Schema 赋值");
        out.println(space4_1 + " *");
        out.println(space4_1 + " * @param: rs ResultSet");
        out.println(space4_1 + " * @param: i int");
        out.println(space4_1 + " * @param schema " + SchemaName);
        out.println(space4_1 + " * @return: boolean");
        out.println(space4_1 + " **/");
        out.println(space4_1 + "private boolean setSchema(ResultSet rs, int i, " + SchemaName + " schema)");
        out.println(space4_1 + "{");
        out.println(space4_2 + "try");
        out.println(space4_2 + "{");
        out.println(space4_3 + "//rs.absolute(i); // 非滚动游标");
        for (int i = 0; i < tTable.getColumnNum(); i++) {
            // 采用索引返回数据信息，坏处和好处同样明显
            Column f = tTable.getColumn(i);
            String FieldCode = f.getCode();
            String FieldType = f.getDataType();
            if (FieldType.equals("float")) {
                out.println(space4_3 + "schema.set" + FieldCode + "(rs.getFloat(" + (i + 1) + "));");
            }
            if (FieldType.equals("double")) {
                out.println(space4_3 + "schema.set" + FieldCode + "(rs.getDouble(" + (i + 1) + "));");
            }
            if (FieldType.equals("int")) {
                out.println(space4_3 + "schema.set" + FieldCode + "(rs.getInt(" + (i + 1) + "));");
            }
            if (FieldType.equals("String")) {
                if (DBType == DBConst.DB_Oracle && f.getDBSqlType() == SqlTypes.LONGVARCHAR) { // 使用流传输，只能取一次
                    out.println(space4_3 + "schema.set" + FieldCode + "(rs.getString(" + (i + 1) + "));");
                } else if (f.getDBSqlType() == SqlTypes.TIMESTAMP) {
                    out.println(space4_3 + "schema.set" + FieldCode + "(fDate.getString(rs.getTimestamp(" + (i + 1) + ")));");
                } else {
                    out.println(space4_3 + "if(rs.getString(" + (i + 1) + ") == null)");
                    out.println(space4_4 + "schema.set" + FieldCode + "(null);");
                    out.println(space4_3 + "else");
                    out.println(space4_4 + "schema.set" + FieldCode + "(rs.getString(" + (i + 1) + ").trim());");
                }
            }
            if (FieldType.equals("Date")) {
                out.println(space4_3 + "schema.set" + FieldCode + "(rs.getDate(" + (i + 1) + "));");
            }
            if (FieldType.equals("InputStream")) {
                out.println(space4_3 + "schema.set" + FieldCode + "(rs.getBinaryStream(" + (i + 1) + "));");
            }
        }
        // end of for
        out.println(space4_2 + "}");
        out.println(space4_2 + "catch(SQLException sqle){");
        out.println(space4_3 + "System.out.println(\"数据库中表" + tTable.getCode() + "字段个数和Schema中的字段个数不一致，或执行db.executeQuery查询时未使用select * from tables\");");
        out.println(space4_3 + "// @@错误处理");
        out.println(space4_3 + "CError tError = new CError();");
        out.println(space4_3 + "tError.moduleName = \"" + ClassName + "\";");
        out.println(space4_3 + "tError.functionName = \"setSchema\";");
        out.println(space4_3 + "tError.errorMessage = sqle.toString();");
        out.println(space4_3 + "this.mErrors.addOneError(tError);");
        out.println(space4_3 + "return false;");
        out.println(space4_2 + "}");
        out.println(space4_2 + "return true;");
        out.println(space4_1 + "}");
    }


    public void genSetParamString(Column ColumnInfo, int nParamIndex, String strStatementVar, PrintWriter out, String strAppend, boolean bWherePart) throws Exception {
        int nColumnType = ColumnInfo.getDBSqlType();
        if (strStatementVar == null || strStatementVar.equals("")) {
            strStatementVar = "pstmt";
        }
        switch (nColumnType) {
            case SqlTypes.CHAR:
                out.println(strAppend + "if(this.get" + ColumnInfo.getCode() + "() == null || this.get" + ColumnInfo.getCode() + "().equals(\"null\")){");
                out.println(strAppend + space4_1 + strStatementVar + ".setNull(" + nParamIndex + ", " + "Types." + SqlTypes.getSQLTypeStr(nColumnType) + ");");

                out.println(strAppend + space4_1 + "paramStringBuilder.append(\"null\").append(\",\");");

                out.println(strAppend + "}");
                out.println(strAppend + "else{");
                if (bWherePart) {
                    out.println(strAppend + space4_1 + strStatementVar + ".setString(" + nParamIndex + ", StrTool.space(this.get" + ColumnInfo.getCode() + "(), " + ColumnInfo.getDBLength() + "));");

                    out.println(strAppend + space4_1 + "paramStringBuilder.append(StrTool.space(this.get(i).get" + ColumnInfo.getCode() + "()," + ColumnInfo.getDBLength() + ")).append(\",\");");

                } else {
                    out.println(strAppend + space4_1 + strStatementVar + ".setString(" + nParamIndex + ", this.get" + ColumnInfo.getCode() + "());");

                    out.println(strAppend + space4_1 + "paramStringBuilder.append(this.get" + ColumnInfo.getCode() + "()).append(\",\");");
                }
                out.println(strAppend + "}");
                break;
            case SqlTypes.VARCHAR:
            case SqlTypes.TIMESTAMP: // For SQL Server
                out.println(strAppend + "if(this.get" + ColumnInfo.getCode() + "() == null || this.get" + ColumnInfo.getCode() + "().equals(\"null\")){");
                out.println(strAppend + space4_1 + strStatementVar + ".setNull(" + nParamIndex + ", " + "Types." + SqlTypes.getSQLTypeStr(nColumnType) + ");");

                out.println(strAppend + space4_1 + "paramStringBuilder.append(\"null\").append(\",\");");

                out.println(strAppend + "}");
                out.println(strAppend + "else{");
                out.println(strAppend + space4_1 + strStatementVar + ".setString(" + nParamIndex + ", this.get" + ColumnInfo.getCode() + "());");

                out.println(strAppend + space4_1 + "paramStringBuilder.append(this.get" + ColumnInfo.getCode() + "()).append(\",\");");

                out.println(strAppend + "}");
                break;
            case SqlTypes.LONGVARCHAR: // For SQL Server的Text类型(MS SQL Server JDBC 2005驱动下)和Oracle下的Long类型
            case SqlTypes.CLOB: // For SQL Server的Text类型(jTds驱动下)
                out.println(strAppend + "if(this.get" + ColumnInfo.getCode() + "() == null || this.get" + ColumnInfo.getCode() + "().equals(\"null\")){");
                out.println(strAppend + space4_1 + strStatementVar + ".setNull(" + nParamIndex + ", " + "Types." + SqlTypes.getSQLTypeStr(nColumnType) + ");");

                out.println(strAppend + space4_1 + "paramStringBuilder.append(\"null\").append(\",\");");

                out.println(strAppend + "}");
                out.println(strAppend + "else{");
                if (DBType == DBConst.DB_SQLServer) {
                    out.println(strAppend + space4_1 + strStatementVar + ".setString(" + nParamIndex + ", this.get" + ColumnInfo.getCode() + "());");

                    out.println(strAppend + space4_1 + "paramStringBuilder.append(this.get" + ColumnInfo.getCode() + "()).append(\",\");");

                } else if (DBType == DBConst.DB_Oracle) {
                    out.println(strAppend + space4_1 + "StringReader strReader = new StringReader(this.get" + ColumnInfo.getCode() + "());");
                    out.println(strAppend + space4_1 + strStatementVar + ".setCharacterStream(" + nParamIndex + ",strReader,this.get" + ColumnInfo.getCode() + "().length());");

                    out.println(strAppend + space4_1 + "paramStringBuilder.append(this.get" + ColumnInfo.getCode() + "()).append(\",\");");
                }
                out.println(strAppend + "}");
                break;
            case SqlTypes.DATE:
                out.println(strAppend + "if(this.get" + ColumnInfo.getCode() + "() == null || this.get" + ColumnInfo.getCode() + "().equals(\"null\")){");
                out.println(strAppend + space4_1 + strStatementVar + ".setNull(" + nParamIndex + ", " + "Types." + SqlTypes.getSQLTypeStr(nColumnType) + ");");

                out.println(strAppend + space4_1 + "paramStringBuilder.append(\"null\").append(\",\");");

                out.println(strAppend + "}");
                out.println(strAppend + "else{");
                out.println(strAppend + space4_1 + strStatementVar + ".setDate(" + nParamIndex + ", Date.valueOf(this.get" + ColumnInfo.getCode() + "()));");

                out.println(strAppend + space4_1 + "paramStringBuilder.append(Date.valueOf(this.get" + ColumnInfo.getCode() + "())).append(\",\");");

                out.println(strAppend + "}");
                break;
            case SqlTypes.INTEGER:
            case SqlTypes.SMALLINT:
            case SqlTypes.TINYINT:
                out.println(strAppend + strStatementVar + ".setInt(" + nParamIndex + ", this.get" + ColumnInfo.getCode() + "());");

                out.println(strAppend + "paramStringBuilder.append(this.get" + ColumnInfo.getCode() + "()).append(\",\");");

                break;
            case SqlTypes.NUMERIC:
            case SqlTypes.DOUBLE:
            case SqlTypes.DECIMAL:
            case SqlTypes.REAL:
                out.println(strAppend + strStatementVar + ".setDouble(" + nParamIndex + ", this.get" + ColumnInfo.getCode() + "());");

                out.println(strAppend + "paramStringBuilder.append(this.get" + ColumnInfo.getCode() + "()).append(\",\");");

                break;
            default:
                System.out.println(ColumnInfo.getCode() + " is unsupported Column type");
        }
    }
}
