package com.weixf.schema.maker.utility;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Vector;

/**
 *
 * @since 2022-01-21
 */
@Component
public class DataCollection {

    @Getter
    public int MaxCol = 0;
    @Getter
    public int MaxRow = 0;
    @Getter
    public int MaxNumber = 0;
    public Errors mErrors = new Errors(); // 错误信息
    // false没有错，true有错
    public boolean ErrorFlag = false;
    private final Vector<String> RData = new Vector<>();

    public DataCollection() {
    }

    public DataCollection(int n) {
        MaxCol = n;
    }

    public void SetText(String strValue) {
        RData.addElement(strValue);
        MaxNumber = RData.size();
        if ((MaxNumber % MaxCol) == 0) {
            MaxRow = MaxNumber / MaxCol;
        } else {
            MaxRow = MaxNumber / MaxCol + 1;
        }

    }

    public String GetText(int cRow, int cCol) {
        String Result = "";
        int Number = (cRow - 1) * MaxCol + cCol - 1;
        if (Number <= MaxNumber) {
            Result = RData.get(Number);
        } else {
            // @@错误处理
            Error tError = new Error();
            tError.moduleName = "SSRS";
            tError.functionName = "GetText";
            tError.errorMessage = "指定的位置在结果集中没有数据";
            this.mErrors.addOneError(tError);
            this.ErrorFlag = true;
        }
        return Result;
    }

    public void Clear() {
        RData.clear();
        MaxCol = 0;
        MaxNumber = 0;
    }

    public String encode() {
        StringBuilder strReturn = new StringBuilder();
        if (MaxNumber != 0) {
            strReturn = new StringBuilder("0" + SysConst.PACKAGESPILTER + MaxRow + SysConst.RECORDSPLITER);
            for (int i = 1; i <= MaxRow; i++) {
                for (int j = 1; j <= MaxCol; j++) {
                    if (j != MaxCol) {
                        strReturn.append(GetText(i, j)).append(SysConst.PACKAGESPILTER);
                    } else {
                        strReturn.append(GetText(i, j));
                    }
                }
                if (i != MaxRow) {
                    strReturn.append(SysConst.RECORDSPLITER);
                }
            }
        }
        return strReturn.toString();
    }
}
