package com.pay.tg;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;

public class SaveImgFromExcelUtils {



	public static void main(String[] args) throws Exception{
        FileInputStream file = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\a.xls"));
        Workbook workbook = new HSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            for (Cell cell : row) {
                //System.out.println(cell.getRowIndex()+"_"+cell.getCellType());
                if(cell.getCellType() == CellType.STRING){
                    System.out.print(cell.getStringCellValue());
                }
                System.out.print("__");

                if(cell.getCellType() == CellType.FORMULA){
                    String cellFormula = cell.getCellFormula();
                }

                /*if (cell.getCellType() == CellType.PICTURE) {
                    byte[] pictureData = cell.getDrawingPatriarch().getPictures().get(0).getData();
                    // 处理图片数据
                }*/
            }
            System.out.println("==========");
        }


    }





}

