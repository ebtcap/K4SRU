package com.ebtcap.sru.brokerimports;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SaxoExcelService {

    public static List<SaxoTrade> parseTradesFromExcel(InputStream inputStream) {
        List<SaxoTrade> trades = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming the data is in the first sheet
            int rowNumber = 0;
            for (Row row : sheet) {
                rowNumber++;
                if (rowNumber == 1) {
                    // Skip header row
                    continue;
                }
                SaxoTrade trade = new SaxoTrade();
                trade.setTradeDateClose(row.getCell(0).getStringCellValue());
                trade.setTradeDateOpen(row.getCell(1).getStringCellValue());
                trade.setAccountId(row.getCell(2).getStringCellValue());
                trade.setAccountCurrency(row.getCell(3).getStringCellValue());
                trade.setAssetType(row.getCell(4).getStringCellValue());
                trade.setInstrumentDescription(row.getCell(5).getStringCellValue());
                trade.setInstrumentSymbol(row.getCell(6).getStringCellValue());
                trade.setInstrumentCurrency(row.getCell(7).getStringCellValue());
                trade.setOpenPositionId(row.getCell(8).getStringCellValue());
                trade.setClosePositionId(row.getCell(9).getStringCellValue());
                trade.setQuantityClose(row.getCell(10).getNumericCellValue());
                trade.setQuantityOpen(row.getCell(11).getNumericCellValue());
                trade.setOpenPrice(row.getCell(12).getNumericCellValue());
                trade.setClosePrice(row.getCell(13).getNumericCellValue());
                trade.setTotalBookedOnOpeningLegAccountCurrency(row.getCell(14).getNumericCellValue());
                trade.setTotalBookedOnOpeningLegClientCurrency(row.getCell(15).getNumericCellValue());
                trade.setTotalBookedOnClosingLegAccountCurrency(row.getCell(16).getNumericCellValue());
                trade.setClientCurrency(row.getCell(17).getStringCellValue());
                trade.setTotalBookedOnClosingLegClientCurrency(row.getCell(18).getNumericCellValue());
                trade.setPnLAccountCurrency(row.getCell(19).getNumericCellValue());
                trade.setPnLClientCurrency(row.getCell(20).getNumericCellValue());
                trades.add(trade);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Saxo Excel file", e);
        }
        return trades;
    }
}