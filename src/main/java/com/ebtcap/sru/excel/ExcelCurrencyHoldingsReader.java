package com.ebtcap.sru.excel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelCurrencyHoldingsReader {
    protected static final Logger logger = LogManager.getLogger();

    public static List<ExcelCurrencyHolding> readFromFile(FileInputStream file) throws IOException {
        List<ExcelCurrencyHolding> excelCurrencyHoldings = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        int rowNumber = 0;
        for (Row row : sheet) {
            rowNumber++;
            if (rowNumber == 1) {
                continue;
            }
            logger.info("\tReading row " + rowNumber);
            ExcelCurrencyHolding excelCurrencyHolding = new ExcelCurrencyHolding();

            String currencyCode = row.getCell(0).getStringCellValue();
            double currencyAmount = row.getCell(1).getNumericCellValue();
            double sekCostBase = row.getCell(2).getNumericCellValue();

            excelCurrencyHolding.setAccountName("");
            excelCurrencyHolding.setCurrencyCode(currencyCode);
            excelCurrencyHolding.setAmount(currencyAmount);
            excelCurrencyHolding.setCostBaseInSEK(sekCostBase);

            excelCurrencyHoldings.add(excelCurrencyHolding);
        }


        return excelCurrencyHoldings;
    }
}
