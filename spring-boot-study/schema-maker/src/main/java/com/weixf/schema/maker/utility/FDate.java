package com.weixf.schema.maker.utility;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @since 2022-01-21
 */
@Component
public class FDate implements Cloneable {
    public Errors mErrors = new Errors(); // 错误信息
    private final SimpleDateFormat df;
    private final SimpleDateFormat df1;

    public FDate() {
        String pattern = "yyyy-MM-dd";
        df = new SimpleDateFormat(pattern);
        String pattern1 = "yyyyMMdd";
        df1 = new SimpleDateFormat(pattern1);
    }

    public Object clone() throws CloneNotSupportedException {
        FDate cloned = (FDate) super.clone();
        cloned.mErrors = (Errors) mErrors.clone();
        return cloned;
    }

    /**
     * 输入符合格式要求的日期字符串，返回日期类型变量
     * getDate("2002-10-8") returns "Tue Oct 08 00:00:00 CST 2002"
     */
    public Date getDate(String dateString) {
        Date tDate = null;
        try {
            if (dateString.contains("-")) {
                tDate = df.parse(dateString);
            } else {
                tDate = df1.parse(dateString);
            }
        } catch (Exception e) {
            Error tError = new Error();
            tError.moduleName = "FDate";
            tError.functionName = "getDate";
            tError.errorMessage = e.toString();
            this.mErrors.addOneError(tError);
        }
        return tDate;
    }

    /**
     * 输入日期类型变量，返回日期字符串
     * getString("Tue Oct 08 00:00:00 CST 2002") returns "2002-10-8"
     */
    public String getString(Date mDate) {
        String tString = null;
        if (mDate != null) {
            tString = df.format(mDate);
        }
        return tString;
    }
}
