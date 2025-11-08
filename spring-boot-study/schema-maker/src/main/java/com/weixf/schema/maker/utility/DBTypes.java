package com.weixf.schema.maker.utility;

import org.springframework.stereotype.Component;

/**
 *
 * @since 2022-01-21
 */
@Component
public class DBTypes {

    public final static int PrimaryKey = 1;
    public final static int UniqueKey = 2;
    public final static int ForeignKey = 3;
    public final static int Delete_None = 0;
    public final static int Delete_Restrict = 1;
    public final static int Delete_Cascade = 2;
    public final static int Delete_SetNull = 3;
    public final static int Delete_SetDefault = 4;
    public final static int Update_None = 0;
    public final static int Update_Restrict = 1;
    public final static int Update_Cascade = 2;
    public final static int Update_SetNull = 3;
    public final static int Update_SetDefault = 4;

    // Prevent instantiation
    private DBTypes() {
    }

    public static int getDeleteType(String type) {
        if (type.equalsIgnoreCase("NO ACTION") || type.equalsIgnoreCase("A")) {
            return Delete_None;
        }
        if (type.equalsIgnoreCase("CASCADE") || type.equalsIgnoreCase("C")) {
            return Delete_Cascade;
        }
        if (type.equalsIgnoreCase("SET NULL")) // DB2不可用
        {
            return Delete_SetNull;
        }
        // 没有测试，要在外键触发器实现时使用
        if (type.equalsIgnoreCase("SET DEFAULT")) {
            return Delete_SetDefault;
        }
        // Restrict | R
        return Delete_Restrict;
    }

    public static String getDeleteTypeStr(int type) {
        switch (type) {
            case Delete_None:
                return "NO ACTION";
            case Delete_Restrict:
                return "Restrict";
            case Delete_Cascade:
                return "CASCADE";
            case Delete_SetNull:
                return "SET NULL";
            case Delete_SetDefault: // 没有测试，要在外键触发器实现时使用
                return "SET DEFAULT";
            default:
                return null;
        }
    }

    public static int getUpdateType(String type) {
        if (type.equalsIgnoreCase("NO ACTION") || type.equalsIgnoreCase("A")) {
            return Update_None;
        }
        if (type.equalsIgnoreCase("CASCADE") || type.equalsIgnoreCase("C")) {
            return Update_Cascade;
        }
        if (type.equalsIgnoreCase("SET NULL")) // DB2不可用
        {
            return Update_SetNull;
        }
        // 没有测试，要在外键触发器实现时使用
        if (type.equalsIgnoreCase("SET DEFAULT")) {
            return Update_SetDefault;
        }
        // Restrict | R
        return Update_Restrict;
    }

    public static String getUpdateTypeStr(int type) {
        switch (type) {
            case Update_None:
                return "NO ACTION";
            case Update_Restrict:
                return "Restrict";
            case Update_Cascade:
                return "CASCADE";
            case Update_SetNull:
                return "SET NULL";
            case Update_SetDefault: // 没有测试，要在外键触发器实现时使用
                return "SET DEFAULT";
            default:
                return null;
        }
    }

}
