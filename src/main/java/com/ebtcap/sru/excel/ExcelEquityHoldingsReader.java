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

public class ExcelEquityHoldingsReader {
    protected static final Logger logger = LogManager.getLogger();
    public static List<ExcelEquityHolding> readFromFile(FileInputStream file) throws IOException {
        List<ExcelEquityHolding> excelEquityHoldings = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        int rowNumber = 0;
        for (Row row : sheet) {
            rowNumber++;
            if (rowNumber == 1) {
                continue;
            }
            logger.debug("\tReading row " + rowNumber);
            ExcelEquityHolding excelEquityHolding = new ExcelEquityHolding();

            String name = row.getCell(0).getStringCellValue();
            double sharesAmount = row.getCell(1).getNumericCellValue();
            double sekCostBase = row.getCell(2).getNumericCellValue();

            excelEquityHolding.setAccount("");
            excelEquityHolding.setName(name);
            excelEquityHolding.setAmount(sharesAmount);
            excelEquityHolding.setCostBaseSEK(sekCostBase);

            excelEquityHoldings.add(excelEquityHolding);
        }



        return excelEquityHoldings;
    }
}
