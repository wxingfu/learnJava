package com.weixf.schema.maker.utility;

import org.springframework.stereotype.Component;

import java.sql.Types;

/**
 *
 * @since 2022-01-21
 */
@Component
public class SqlTypes {

    // 下面类型作为String处理
    public final static int CHAR = Types.CHAR;
    public final static int VARCHAR = Types.VARCHAR;
    public final static int TIMESTAMP = Types.TIMESTAMP;
    // 下面类型默认作为String处理，但是也可以作为流处理
    public final static int LONGVARCHAR = Types.LONGVARCHAR;
    public final static int CLOB = Types.CLOB;
    // 下面类型作为Date处理
    public final static int DATE = Types.DATE;
    // 下面类型作为int处理
    public final static int INTEGER = Types.INTEGER;
    public final static int SMALLINT = Types.SMALLINT;
    public final static int TINYINT = Types.TINYINT;
    // 下面类型作为double处理
    public final static int NUMERIC = Types.NUMERIC;
    public final static int DECIMAL = Types.DECIMAL;
    public final static int DOUBLE = Types.DOUBLE;
    public final static int REAL = Types.REAL;
    // 下面类型作为InputStream处理
    public final static int BLOB = Types.BLOB;
    public final static int LONGVARBINARY = Types.LONGVARBINARY;

    private SqlTypes() {
    }

    public static String getSQLTypeStr(int sqlType) {
        switch (sqlType) {
            case CHAR:
                return "CHAR";
            case VARCHAR:
                return "VARCHAR";
            case TIMESTAMP:
                return "TIMESTAMP";
            case LONGVARCHAR:
                return "LONGVARCHAR";
            case DATE:
                return "DATE";
            case INTEGER:
                return "INTEGER";
            case SMALLINT:
                return "SMALLINT";
            case TINYINT:
                return "TINYINT";
            case NUMERIC:
                return "NUMERIC";
            case DOUBLE:
                return "DOUBLE";
            case DECIMAL:
                return "DECIMAL";
            case REAL:
                return "REAL";
            case BLOB:
                return "BLOB";
            case CLOB:
                return "CLOB";
            case LONGVARBINARY:
                return "LONGVARBINARY";
            default:
                return null;
        }
    }
}
