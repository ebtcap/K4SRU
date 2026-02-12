package com.ebtcap.sru.brokerimports;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SaxoExcelService {

    protected static final Logger logger = LogManager.getLogger();

    public static List<SaxoTrade> parseTradesFromExcel(InputStream inputStream) {
        List<SaxoTrade> trades = new ArrayList<>();
        int rowNumber = 0;
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming the data is in the first sheet
            logger.info("Reading Saxo Excel file, total rows: {}", sheet.getLastRowNum() + 1);
            for (Row row : sheet) {
                rowNumber++;
                if (rowNumber == 1) {
                    // Skip header row
                    continue;
                }
                
                // Skip empty rows
                if (row.getCell(0) == null || getCellValueAsString(row.getCell(0)).isEmpty()) {
                    continue;
                }
                
                SaxoTrade trade = new SaxoTrade();
                trade.setTradeDateClose(getCellValueAsString(row.getCell(0))); // Trade close date
                trade.setTradeDateOpen(getCellValueAsString(row.getCell(1))); // Trade open date
                trade.setAccountId(getCellValueAsString(row.getCell(2)));
                //trade.setAccountCurrency(getCellValueAsString(row.getCell(3)));
                // Changed to test to avoid currency conversion
                trade.setAccountCurrency(getCellValueAsString(row.getCell(17)));
                trade.setAssetType(getCellValueAsString(row.getCell(4)));
                trade.setInstrumentDescription(getCellValueAsString(row.getCell(5)));
                trade.setInstrumentSymbol(getCellValueAsString(row.getCell(6)));
                //trade.setInstrumentCurrency(getCellValueAsString(row.getCell(7)));
                // Changed to test to avoid currency conversion
                trade.setInstrumentCurrency(getCellValueAsString(row.getCell(17)));
                trade.setOpenPositionId(getCellValueAsString(row.getCell(8)));
                trade.setClosePositionId(getCellValueAsString(row.getCell(9)));
                trade.setQuantityClose(getCellValueAsDouble(row.getCell(10)));
                trade.setQuantityOpen(getCellValueAsDouble(row.getCell(11)));
                trade.setOpenPrice(getCellValueAsDouble(row.getCell(12)));
                trade.setClosePrice(getCellValueAsDouble(row.getCell(13)));
                trade.setTotalBookedOnOpeningLegAccountCurrency(getCellValueAsDouble(row.getCell(14)));
                trade.setTotalBookedOnOpeningLegClientCurrency(getCellValueAsDouble(row.getCell(15)));
                trade.setTotalBookedOnClosingLegAccountCurrency(getCellValueAsDouble(row.getCell(16)));
                trade.setClientCurrency(getCellValueAsString(row.getCell(17)));
                trade.setTotalBookedOnClosingLegClientCurrency(getCellValueAsDouble(row.getCell(18)));
                trade.setPnLAccountCurrency(getCellValueAsDouble(row.getCell(19)));
                trade.setPnLClientCurrency(getCellValueAsDouble(row.getCell(20)));
                
                // Debug logging for first few trades
                if (trades.size() < 3) {
                    logger.debug("Row {}: Instrument={}, Currency='{}', OpenQty={}, CloseQty={}", 
                        rowNumber, trade.getInstrumentDescription(), 
                        trade.getClientCurrency(), trade.getQuantityOpen(), trade.getQuantityClose());
                }
                
                trades.add(trade);
            }
            logger.info("Successfully parsed {} Saxo trades", trades.size());
        } catch (Exception e) {
            logger.error("Error parsing Saxo Excel file at row: {}", rowNumber, e);
            throw new RuntimeException("Error parsing Saxo Excel file at row " + rowNumber + ": " + e.getMessage(), e);
        }
        return trades;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                // Convert the date to a standard format (e.g., "yyyy-MM-dd")
                return cell.getLocalDateTimeCellValue().toLocalDate().toString();
            }
            return String.valueOf(cell.getNumericCellValue());
        } else {
            return "";
        }
    }

    private static double getCellValueAsDouble(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) {
            return 0.0;
        }
        return cell.getNumericCellValue();
    }
}