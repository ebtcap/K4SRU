package com.ebtcap.sru.excel;

import com.ebtcap.sru.transactions.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class ExcelHoldingsWriter {
    public static void writeCurrencyHoldings(FinancialYear financialYear, File file) throws IOException {
        if (file.exists()) {
            boolean result = file.delete();
            if (!result) {
                throw new RuntimeException("Cannot delete " + file.getAbsolutePath());
            }
        }
        XSSFWorkbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("CurrencyHoldings");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 4000);

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
        headerCell.setCellValue("Valutakod");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Antal");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Omkostnadsbelopp");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("Snitt");
        headerCell.setCellStyle(headerStyle);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        int i = 1;
        for(CurrecyHolding currecyHolding : financialYear.getCurrencies().values()) {
            if (currecyHolding.getMoney().getNumber().longValue() == 0) {
                continue;
            }
            Row row = sheet.createRow(i);
            Cell cell = row.createCell(0);
            cell.setCellValue(currecyHolding.getMoney().getCurrency().getCurrencyCode());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(currecyHolding.getMoney().getNumber().doubleValue());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellValue(currecyHolding.getCostInSEK().getNumber().doubleValue());
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellValue(currecyHolding.getCostInSEK().getNumber().doubleValue() / currecyHolding.getMoney().getNumber().doubleValue());
            cell.setCellStyle(style);

            i++;
        }


        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        workbook.close();
    }

    public static void writeEquityHoldings(FinancialYear financialYear, File file) throws IOException {
        if (file.exists()) {
            boolean result = file.delete();
            if (!result) {
                throw new RuntimeException("Cannot delete " + file.getAbsolutePath());
            }
        }
        XSSFWorkbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("EquityHoldings");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);

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
        headerCell.setCellValue("Omkostnadsbelopp");
        headerCell.setCellStyle(headerStyle);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        int i = 1;
        for(SecurityHolding securityHolding : financialYear.getEquities().values()) {
            Row row = sheet.createRow(i);
            Cell cell = row.createCell(0);
            cell.setCellValue(securityHolding.getName());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(securityHolding.getAmount());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellValue(securityHolding.getCostInSEK().getNumber().longValue());
            cell.setCellStyle(style);

            i++;
        }



        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        workbook.close();
    }
}
