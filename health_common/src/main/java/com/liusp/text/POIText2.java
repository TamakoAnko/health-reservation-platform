package com.liusp.text;


import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

public class POIText2 {

    public static void main(String[] args) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook("C:\\Users\\xzj12\\Desktop\\新建 Microsoft Excel 工作表.xlsx");
        XSSFSheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        for (int i =0;i<=lastRowNum;i++){
            XSSFRow row = sheet.getRow(i);
            short lastCellNum = row.getLastCellNum();
            for (int j = 0;j<lastCellNum;j++){
                String value = row.getCell(j).getStringCellValue();
                System.out.println(value);
            }
        }
        workbook.close();
    }

}
