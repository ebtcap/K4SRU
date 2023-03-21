package com.ebtcap.sru.excel;

import com.ebtcap.sru.transactions.CurrencySale;
import com.ebtcap.sru.transactions.FinancialYear;
import com.ebtcap.sru.transactions.SecuritySale;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelTradesWriter {
    public static void writeTradesToFile(FinancialYear financialYear, File file) throws IOException {
        if (file.exists()) {
           boolean result = file.delete();
           if (!result) {
               throw new RuntimeException("Cannot delete " + file.getAbsolutePath());
           }
        }
        XSSFWorkbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("K4");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 6000);
        sheet.setColumnWidth(4, 6000);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Beteckning");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Antal");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Försäljningspris");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("Omkostnadsbelopp");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(4);
        headerCell.setCellValue("Vinst/Förlust");
        headerCell.setCellStyle(headerStyle);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        int i = 1;
        for(SecuritySale securitySale : financialYear.getSecuritySales().values()) {
            Row row = sheet.createRow(i);
            Cell cell = row.createCell(0);
            cell.setCellValue(securitySale.getName());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(securitySale.getAmount());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellValue(securitySale.getSalePriceSEK().getNumber().longValue());
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellValue(securitySale.getCostPriceSEK().getNumber().longValue());
            cell.setCellStyle(style);

            cell = row.createCell(4);
            cell.setCellValue(securitySale.getProfit().getNumber().longValue());
            cell.setCellStyle(style);

            i++;
        }
        sheet.createRow(i);
        i++;
        sheet.createRow(i);
        i++;
        header = sheet.createRow(i);
        i++;
        headerCell = header.createCell(0);
        headerCell.setCellValue("Belopp");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Valutakod");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Försäljningspris SEK");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("Omkostnadsbelopp SEK");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(4);
        headerCell.setCellValue("Vinst/Förlust");
        headerCell.setCellStyle(headerStyle);

        for(CurrencySale currencySale : financialYear.getCurrencySales().values()) {
            Row row = sheet.createRow(i);
            Cell cell = row.createCell(0);
            cell.setCellValue(currencySale.getSoldCurrency().getNumber().longValue());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(currencySale.getSoldCurrency().getCurrency().getCurrencyCode());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellValue(currencySale.getReceivedCurrencySEK().getNumber().longValue());
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellValue(currencySale.getCostCurrencySEK().getNumber().longValue());
            cell.setCellStyle(style);

            cell = row.createCell(4);
            cell.setCellValue(currencySale.getProfit().getNumber().longValue());
            cell.setCellStyle(style);

            i++;
        }


        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        workbook.close();
    }
}
