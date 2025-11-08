package com.weixf.spring.utils;

/**
 *
 * @since 2022-03-07
 */
public class PubFun {

    public static String LCh(String sourString, String cChar, int cLen) {
        int tLen = sourString.length();
        int i, iMax;
        StringBuilder tReturn = new StringBuilder();
        if (tLen >= cLen) {
            return sourString;
        }
        iMax = cLen - tLen;
        for (i = 0; i < iMax; i++) {
            tReturn.append(cChar);
        }
        tReturn = new StringBuilder(tReturn.toString().trim() + sourString.trim());
        return tReturn.toString();
    }
}
