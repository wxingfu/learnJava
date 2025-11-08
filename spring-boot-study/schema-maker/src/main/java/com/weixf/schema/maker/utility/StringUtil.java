package com.weixf.schema.maker.utility;

import org.springframework.stereotype.Component;

/**
 *
 * @since 2022-01-21
 */
@Component
public class StringUtil {
    /**
     * 将字符串转换为Unicode字符串
     *
     * @param strOriginal String 原串
     * @return String 将原串由GBK编码转换为ISO8859_1(Unicode)编码
     */
    public static String GBKToUnicode(String strOriginal) {
        if (SysConst.CHANGECHARSET) {
            if (strOriginal != null) {
                try {
                    if (isGBKString(strOriginal)) {
                        return new String(strOriginal.getBytes("GBK"),
                                "ISO8859_1");
                    } else {
                        return strOriginal;
                    }
                } catch (Exception exception) {
                    return strOriginal;
                }
            } else {
                return null;
            }
        } else {
            if (strOriginal == null) {
                return "";
            } else {
                return strOriginal;
            }
        }
    }

    /**
     * 判断是否是GBK编码
     *
     * @param tStr String
     * @return boolean
     */
    public static boolean isGBKString(String tStr) {
        int tlength = tStr.length();
        int t1 = 0;
        for (int i = 0; i < tlength; i++) {
            t1 = Integer.parseInt(Integer.toOctalString(tStr.charAt(i)));
            if (t1 > 511) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将输入的字符串进行转换，如果为空，则返回""，如果不空，则返回该字符串去掉前后空格
     */
    public static String cTrim(String tStr) {
        String ttStr = "";
        if (tStr != null) {
            ttStr = tStr.trim();
        }
        return ttStr;
    }

    /**
     * 以指定内容生成给定长度的字符串,不足以空格补齐,超长截去
     *
     * @param strValue  String 指定内容
     * @param intLength int 字符串长度
     * @return String
     */
    public static String space(String strValue, int intLength) {
        int strLen = strValue.length();
        StringBuilder strReturn = new StringBuilder();
        if (strLen > intLength) {
            strReturn.append(strValue, 0, intLength);
        } else {
            if (strLen == 0) {
                strReturn.append(" ");
            } else {
                strReturn.append(strValue);
            }

            for (int i = strLen; i < intLength; i++) {
                strReturn.append(" ");
            }
        }
        return strReturn.toString();
    }

    /**
     * 该函数得到c_Str中的第c_i个以c_Split分割的字符串
     *
     * @param c_Str   目标字符串
     * @param c_i     位置
     * @param c_Split 分割符
     * @return 如果发生异常，则返回空
     */
    public static String getStr(String c_Str, int c_i, String c_Split) {
        String t_Str1 = "", t_Str2 = "", t_strOld = "";
        int i = 0, i_Start = 0;
//        int j_End = 0;
        t_Str1 = c_Str;
        t_Str2 = c_Split;
        try {
            while (i < c_i) {
                i_Start = t_Str1.indexOf(t_Str2);
                if (i_Start >= 0) {
                    i += 1;
                    t_strOld = t_Str1;
                    t_Str1 = t_Str1.substring(i_Start + t_Str2.length());
                } else {
                    if (i != c_i - 1) {
                        t_Str1 = "";
                    }
                    break;
                }
            }
            if (i_Start >= 0) {
                t_Str1 = t_strOld.substring(0, i_Start);
            }
        } catch (Exception ex) {
            t_Str1 = "";
        }
        return t_Str1;
    }

    /**
     * 字符串替换函数
     *
     * @param strMain        String 原串
     * @param strFind        String 查找字符串
     * @param strReplaceWith String 替换字符串
     * @return String 替换后的字符串，如果原串为空或者为""，则返回""
     */
    public static String replace(String strMain, String strFind, String strReplaceWith) {
        StringBuilder tSBql = new StringBuilder();
        int intStartIndex = 0;
        int intEndIndex = 0;
        if (strMain == null || "".equals(strMain)) {
            return "";
        }
        while ((intEndIndex = strMain.indexOf(strFind, intStartIndex)) > -1) {
            tSBql.append(strMain, intStartIndex, intEndIndex);
            tSBql.append(strReplaceWith);
            intStartIndex = intEndIndex + strFind.length();
        }
        tSBql.append(strMain.substring(intStartIndex));
        return tSBql.toString();
    }

}

