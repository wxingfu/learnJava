package com.weixf.utils;

import com.weixf.utils.poi.ExcelUtils;
import com.weixf.utils.poi.dto.SlrEmpSalary;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyTest {

    @Test
    public void getExcelData() throws Exception {
        // 导出数据
        List<SlrEmpSalary> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SlrEmpSalary slrEmpSalary = new SlrEmpSalary();
            slrEmpSalary.setEmployeeName("aaa");
            slrEmpSalary.setEmployeeCode("190378091");
            slrEmpSalary.setSeqNumber(123 + i);
            dataList.add(slrEmpSalary);
        }
        // 导出
        File file = new File("ttttt.xls");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ExcelUtils.exportExcel(fileOutputStream, dataList, SlrEmpSalary.class, null, "aaaaa");
    }
}
