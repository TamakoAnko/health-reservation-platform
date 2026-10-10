package com.liusp.text;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class POIText3 {
    public static void main(String[] args) throws IOException {
        //在内存中创建一个Excel文件
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表，指定工作表名称
        XSSFSheet sheet = workbook.createSheet("测试");
        //创建行，0表示第一行
        XSSFRow row = sheet.createRow(0);
        //创建单元格，0表示第一个单元格
        row.createCell(0).setCellValue("编号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("年龄");

        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("1");
        row1.createCell(1).setCellValue("张三");
        row1.createCell(2).setCellValue("18");

        XSSFRow row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("2");
        row2.createCell(1).setCellValue("李四");
        row2.createCell(2).setCellValue("19");

        //通过输出流将workbook对象下载到磁盘
        FileOutputStream out = new FileOutputStream("C:\\Users\\xzj12\\Desktop\\测试.xlsx");
        workbook.write(out);
        out.flush();
        out.close();
        workbook.close();
    }

}
