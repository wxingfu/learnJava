package com.weixf.schema.maker.utility;

import org.springframework.stereotype.Component;

/**
 *
 * @since 2022-01-21
 */
@Component
public class CommonUtils {
    /**
     * 如果一个字符串数字中小数点后全为零，则去掉小数点及零
     *
     * @param Value String
     * @return String
     */
    public static String getInt(String Value) {

        if (Value == null) {
            return null;
        }
        // 查询时对于没有数据的数字型数据Value传入的时null字符串，因此特殊处理一下
        // 朱向峰 2005-07-26 修改
        if (Value.equals("null")) {
            return "";
        }
        String result = "";
        boolean mflag = true;
        int m = Value.lastIndexOf(".");
        if (m == -1) {
            result = Value;
        } else {
            for (int i = m + 1; i <= Value.length() - 1; i++) {
                if (Value.charAt(i) != '0') {
                    result = Value;
                    mflag = false;
                    break;
                }
            }
            if (mflag) {
                result = Value.substring(0, m);
            }
        }
        return result;
    }
}
