package com.weixf.schema.maker.table;


import com.weixf.schema.maker.utility.DBConst;
import com.weixf.schema.maker.utility.DBTypes;
import com.weixf.schema.maker.utility.SqlTypes;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @since 2022-01-21
 */
@Component
public class Table {
    @Setter
    @Getter
    private String Name; // 表名称
    @Setter
    @Getter
    private String Code; // 表代码

    private final ArrayList<Column> Columns = new ArrayList<>(); // 表字段
    @Getter
    private int ColumnNum = 0;

    private final ArrayList<Key> Keys = new ArrayList<>(); // 表中的Key
    @Getter
    private int KeyNum = 0;
    @Setter
    private boolean LargeObj = false; // 是否有BLOB等二进制大对象的字段
    private boolean LargeObj_PK = false; // 是否有BLOB等二进制大对象的主键字段
    @Setter
    private int DBType = DBConst.DB_UnSupported;
    @Setter
    private int JDBCVendor = DBConst.Vendor_UnSupported;

    public Table() {
    }

    public void addColumn(Column column) {
        Columns.add(column);
        ColumnNum++;
    }

    public Column getColumn(int i) {
        return Columns.get(i);
    }

    public Column getColumn(String tCode) {
        for (int i = 0; i < ColumnNum; i++) {
            if (getColumn(i).getCode().equalsIgnoreCase(tCode)) {
                return getColumn(i);
            }
        }
        return null;
    }

    public void addKey(Key key) {
        Keys.add(key);
        KeyNum++;
    }

    public Key getKey(int i) {
        return Keys.get(i);
    }

    //************以下代码用于和数据库交互************//

    public Key getKey(String ConstraintName) {
        for (int i = 0; i < KeyNum; i++) {
            if (getKey(i).getConstraintName().equalsIgnoreCase(ConstraintName)) {
                return getKey(i);
            }
        }
        return null;
    }

    public Key getPrimaryKey() {
        for (int i = 0; i < KeyNum; i++) {
            if (getKey(i).getKeyType() == DBTypes.PrimaryKey) {
                return getKey(i);
            }
        }
        return null;
    }

    public int getUniqueKeyNum() {
        int a = 0;
        for (int i = 0; i < KeyNum; i++) {
            if (getKey(i).getKeyType() == DBTypes.UniqueKey) {
                a++;
            }
        }
        return a;
    }

    public int getForeignKeyNum() {
        int a = 0;
        for (int i = 0; i < KeyNum; i++) {
            if (getKey(i).getKeyType() == DBTypes.ForeignKey) {
                a++;
            }
        }
        return a;
    }

    public boolean haveLargeObj() {
        return LargeObj;
    }

    public boolean haveLargeObjInPK() {
        return LargeObj_PK;
    }

    public void setLargeObjInPK(boolean LargeObj_PK) {
        this.LargeObj_PK = LargeObj_PK;
    }


    public boolean getDBInfo(Connection con, String schema) throws Exception {
        if (DBType == DBConst.DB_UnSupported) {
            throw new Exception("请先设置数据库类型！");
        }
        ResultSet rs = null;
        try {
            PreparedStatement pstmt = null;
            if (DBType == DBConst.DB_SQLServer) {
                pstmt = con.prepareStatement(String.format("select Top 1 * from %s", Code), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            }
            if (DBType == DBConst.DB_Oracle) {
                pstmt = con.prepareStatement(String.format("select * from %s where rownum <= 1", Code), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            }
            if (DBType == DBConst.DB_DB2) {
                pstmt = con.prepareStatement(String.format("select * from %s fetch first 1 rows only", Code), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            }
            rs = pstmt.executeQuery();
        } catch (Exception e) {
            throw new Exception(Code + "表在数据库中不存在");
        }
        ResultSetMetaData rsmd = rs.getMetaData();
        int n2 = rsmd.getColumnCount();
        if (ColumnNum != n2) {
            throw new Exception(Code + "表在数据库中字段个数和pdm不一致,数据库:" + n2 + ";PDM:" + ColumnNum);
        } else {
            // 除了个数的校验，还需要添加字段顺序的校验，这样就可以在后面的获取中使用索引而非字段
            for (int i = 1; i <= ColumnNum; i++) {
                Column f = getColumn(i - 1);
                String tPDM_Col_Code = f.getCode(); // pdm中字段
                String tDB_Col_Code = rsmd.getColumnName(i); // 数据库中字段
                // 设置数据库字段的类型及长度
                f.setDBSqlType(rsmd.getColumnType(i));

                f.setDBLength(rsmd.getColumnDisplaySize(i));
                // 如果同顺位的字段不同名，则表示pdm中的表结构顺序＆库中不同，需要查证。
                if (!tPDM_Col_Code.equalsIgnoreCase(tDB_Col_Code)) {
                    throw new Exception(Code + "表在数据库中的字段" + tDB_Col_Code + "和pdm中该字段的'位置'属性不一致:PDM此位置字段为:" + tPDM_Col_Code);
                }
                // 如果检查PDM中字段的isNull属性和数据库是否一致。（不是必须的）
                if (f.getNullable() != rsmd.isNullable(i)) {
                    throw new Exception(Code + "表在数据库中的字段" + tPDM_Col_Code + "和pdm中该字段的'是否为空'属性不一致:PDM为" + f.getNullable() + ",数据库中为" + rsmd.isNullable(i));
                }
                if (f.getDataType().equals("InputStream")) {
                    LargeObj = true;
                }
                // 比较数据类型
                if (!compareDataType(f.getDataType(), rsmd.getColumnType(i), f.getPDMDataType())) {
                    throw new Exception(Code + "表在数据库中的字段" + tPDM_Col_Code +
                            "和pdm中该字段的'数据类型'属性不一致:PDM类型为" +
                            f.getPDMDataType() + ",数据库类型为" +
                            SqlTypes.getSQLTypeStr(rsmd.getColumnType(i)) + ",Java类型为:" + f.getDataType());
                }
                // 比较数据长度和精度（不是必须的）
                if (!compareLength(rsmd, i, f)) {
                    throw new Exception(Code + "表在数据库中的字段" + tPDM_Col_Code +
                            "和pdm中该字段的'长度或精度'属性不一致:PDM长度为" +
                            f.getLength() + ",数据库长度为" +
                            rsmd.getPrecision(i) + ",PDM精度为" +
                            f.getPrecision() + ",数据库精度为" +
                            rsmd.getScale(i));
                }
                // 比较Char数据类型的长度（不是必须的）
                compareOther(rsmd, i, f);//                    throw new Exception(Code + "表在数据库中的字段" + tPDM_Col_Code + "的Char类型长度应该小于等于2:现在长度为"+rsmd.getPrecision(i));
            }
            rs.close();
            DatabaseMetaData dbmd = con.getMetaData();
            // 纯粹的不晓得，第二个参数的用法，db2的话，没有第二个参数才可以正常的得到pk
            // pkRS = dbmd.getPrimaryKeys(null, null, strTableCode);
            ResultSet pkRS = null;
            if (DBType == DBConst.DB_Oracle) {
                pkRS = dbmd.getPrimaryKeys(null, schema.toUpperCase(), Code.toUpperCase());
            }
            if (DBType == DBConst.DB_SQLServer || DBType == DBConst.DB_DB2) {
                pkRS = dbmd.getPrimaryKeys(null, null, Code.toUpperCase());
            }
            int DBPKNum = 0;
            Key PrimaryKey = getPrimaryKey();
            while (pkRS.next()) {
                DBPKNum++;
                String col_PK = pkRS.getString(4);
                if (getColumn(col_PK).getDataType().equals("InputStream")) {
                    LargeObj_PK = true;
                }
                boolean havefield = false;
                for (int i = 0; i < PrimaryKey.getColumnNum(); i++) {
                    String FieldCode = PrimaryKey.getColumn(i);
                    if (FieldCode.equalsIgnoreCase(col_PK)) {
                        havefield = true;
                        break;
                    }
                }
                if (!havefield) {
                    throw new Exception(Code + "表在数据库中的主键字段" + col_PK + "pdm在中没有出现");
                }
            }
            pkRS.close();
        }
        return true;
    }

    private boolean compareOther(ResultSetMetaData rsmd, int i, Column f) throws SQLException {
        if (rsmd.getColumnType(i) == SqlTypes.CHAR) {
            return rsmd.getPrecision(i) <= 2;
        }
        return true;
    }

    private boolean compareLength(ResultSetMetaData rsmd, int i, Column f) throws
            SQLException {
        if (f.getPDMDataType().contains("(") || f.getPDMDataType().contains(")")) {
            String regex = "\\s*[a-zA-Z]+[0-9]?\\s*\\(\\s*[0-9]+\\s*,?\\s*[0-9]*\\s*\\)\\s*";
            if (f.getPDMDataType().matches(regex)) {
                if (f.getPDMDataType().toLowerCase().contains("float")) {
                    return true;
                }
                return f.getLength() == rsmd.getPrecision(i) && f.getPrecision() == rsmd.getScale(i);
            }
            return false;
        }
        // 如果误写双字节的括号，则报错
        return !f.getPDMDataType().contains("（") && !f.getPDMDataType().contains("）");
    }

    private boolean compareDataType(String JavaType, int SQLType, String PDMType) throws Exception {
        if (DBType == DBConst.DB_Oracle) {
            return compareDataTypeForOracle(JavaType, SQLType);
        }
        if (DBType == DBConst.DB_SQLServer) {
            return compareDataTypeForSQLServer(JavaType, SQLType, PDMType);
        }
        if (DBType == DBConst.DB_DB2) {
            return compareDataTypeForDB2(JavaType, SQLType, PDMType);
        }
        return false;
    }

    private boolean compareDataTypeForDB2(String JavaType, int SQLType, String PDMType) {
        if ((PDMType.toLowerCase().contains("dec") || PDMType.toLowerCase().contains("num"))) {
            if (SQLType == SqlTypes.DECIMAL) {
                // 符合dec,dec(*),dec(*,0),decimal,decimal(*),decimal(*,0),num,num(*),num(*,0)的都为整数
                String regex = "\\s*(dec|decimal|num|numeric)\\s*(\\(\\s*[0-9]+\\s*(,\\s*[0]|)\\s*\\)|)\\s*";
                if (PDMType.toLowerCase().matches(regex)) {
                    return JavaType.equals("int") || JavaType.equals("Integer") || JavaType.equals("BigInteger");
                } else {
                    return JavaType.equals("double") || JavaType.equals("Double") || JavaType.equals("BigDecimal");
                }
            }
        } else if (PDMType.toLowerCase().contains("float")) {
            if (JavaType.equals("double") || JavaType.equals("Double") || JavaType.equals("BigDecimal")) {
                String regex = "\\s*float\\s*";
                if (PDMType.toLowerCase().matches(regex)) {
                    return SQLType == SqlTypes.DOUBLE;
                } else {
                    return SQLType == SqlTypes.REAL;
                }
            }
        } else {
            if (JavaType.equals("String") && (SQLType == SqlTypes.CHAR || SQLType == SqlTypes.VARCHAR || SQLType == SqlTypes.TIMESTAMP)) {
                return true;
            }
            if ((JavaType.equals("int") || JavaType.equals("Integer")) && (SQLType == SqlTypes.INTEGER || SQLType == SqlTypes.SMALLINT)) {
                return true;
            }
            if (JavaType.equals("Date") && (SQLType == SqlTypes.DATE)) {
                return true;
            }
            return JavaType.equals("InputStream") && SQLType == SqlTypes.BLOB;
        }
        return false;
    }

    private boolean compareDataTypeForSQLServer(String JavaType, int SQLType, String PDMType) throws Exception {
        if (JDBCVendor != DBConst.Vendor_Microsoft && JDBCVendor != DBConst.Vendor_SF_jDTS) {
            throw new Exception("请先设置SQL Server数据库JDBC类型！");
        }

        if (PDMType.toLowerCase().contains("dec")) {
            if (SQLType == SqlTypes.DECIMAL) {
                String regex =
                        "\\s*(dec|decimal)\\s*(\\(\\s*[0-9]+\\s*(,\\s*[0]|)\\s*\\)|)\\s*";
                if (PDMType.toLowerCase().matches(regex)) {
                    return JavaType.equals("int") || JavaType.equals("Integer") || JavaType.equals("BigInteger");
                } else {
                    return JavaType.equals("double") || JavaType.equals("Double") || JavaType.equals("BigDecimal");
                }
            }
        } else if (PDMType.toLowerCase().contains("numeric")) {
            if (SQLType == SqlTypes.NUMERIC) {
                // 符合numeric,numeric(*),numeric(*,0)的都为整数
                String regex = "\\s*numeric\\s*(\\(\\s*[0-9]+\\s*(,\\s*[0]|)\\s*\\)|)\\s*";
                if (PDMType.toLowerCase().matches(regex)) {
                    return JavaType.equals("int") || JavaType.equals("Integer") || JavaType.equals("BigInteger");
                } else {
                    return JavaType.equals("double") || JavaType.equals("Double") || JavaType.equals("BigDecimal");
                }
            }
        } else if (PDMType.toLowerCase().contains("float")) {
            if (JavaType.equals("double") || JavaType.equals("Double") || JavaType.equals("BigDecimal")) {
                String regex = "\\s*float\\s*";
                if (PDMType.toLowerCase().matches(regex)) {
                    return SQLType == SqlTypes.DOUBLE;
                } else {
                    return SQLType == SqlTypes.REAL;
                }
            }
        } else if (PDMType.toLowerCase().contains("text")) {
            if (JavaType.equals("String")) {
                // MS JDBC 2005中对应LONGVARCHAR
                if (JDBCVendor == DBConst.Vendor_Microsoft) {
                    if (SQLType == SqlTypes.LONGVARCHAR) {
                        return true;
                    }
                }
                // 在jTds中对应LONGVARBINARY
                if (JDBCVendor == DBConst.Vendor_SF_jDTS) {
                    // Java类型不确定
                    return SQLType == SqlTypes.CLOB;
                }
            }
        } else if (PDMType.toLowerCase().contains("image")) // image类型不确定
        {
            if (JavaType.equals("InputStream")) {
                // 在MS JDBC 2005中对应LONGVARCHAR
                if (JDBCVendor == DBConst.Vendor_Microsoft) {
                    if (SQLType == SqlTypes.LONGVARBINARY) {
                        return true;
                    }
                }
                // 在jTds中对应BLOB
                if (JDBCVendor == DBConst.Vendor_SF_jDTS) {
                    return SQLType == SqlTypes.BLOB;
                }
            }
        } else {
            if (JavaType.equals("String") &&
                    (SQLType == SqlTypes.CHAR || SQLType == SqlTypes.VARCHAR || SQLType == SqlTypes.TIMESTAMP)) {
                return true;
            }
            return (JavaType.equals("int") || JavaType.equals("Integer")) && (SQLType == SqlTypes.INTEGER || SQLType == SqlTypes.SMALLINT || SQLType == SqlTypes.TINYINT);
        }
        return false;
    }

    private boolean compareDataTypeForOracle(String JavaType, int SQLType) {
        if (JavaType.equals("String") && (SQLType == SqlTypes.CHAR || SQLType == SqlTypes.VARCHAR || SQLType == SqlTypes.LONGVARCHAR)) {
            return true;
        }
        if ((JavaType.equals("int") || JavaType.equals("Integer") || JavaType.equals("BigInteger") || JavaType.equals("double") || JavaType.equals("Double") || JavaType.equals("BigDecimal")) && SQLType == SqlTypes.NUMERIC) {
            return true;
        }
        if (JavaType.equals("Date") && SQLType == SqlTypes.DATE) {
            return true;
        }
        return JavaType.equals("InputStream") && SQLType == SqlTypes.BLOB;
    }
}
