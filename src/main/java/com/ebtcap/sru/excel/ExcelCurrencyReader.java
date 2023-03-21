package com.ebtcap.sru.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelCurrencyReader {
    public static Map<String, Double> readXlxsFile(FileInputStream file) throws IOException {
        Map<String, Double>  conversionMap = new TreeMap<>();

        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {

            String currencyCode = row.getCell(0).getStringCellValue();
            Double currencyFactor = row.getCell(1).getNumericCellValue();

            conversionMap.put(currencyCode, currencyFactor);
        }
        return conversionMap;
    }
}
