package com.liusp.text;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

public class POIText1 {

    public static void main(String[] args) throws IOException {
        //创建工作簿

        XSSFWorkbook workbook = new XSSFWorkbook("C:\\Users\\xzj12\\Desktop\\新建 Microsoft Excel 工作表.xlsx");
        //获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
        XSSFSheet sheet = workbook.getSheetAt(0);
        //遍历工作表获得行对象
        for(Row row : sheet){
            //遍历行对象获取单元格对象
            for (Cell cell: row){
                //获得单元格中的值
                String value = cell.getStringCellValue();
                System.out.println(value);
            }
        }
        workbook.close();
    }
}
