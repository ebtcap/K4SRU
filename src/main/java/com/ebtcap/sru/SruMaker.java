package com.ebtcap.sru;

import com.ebtcap.sru.K4.K4blankett;
import com.ebtcap.sru.K4.K4blankettService;
import com.ebtcap.sru.brokerimports.*;
import com.ebtcap.sru.excel.*;
import com.ebtcap.sru.transactions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javamoney.moneta.Money;


import java.io.*;
import java.util.List;
import java.util.Map;


public class SruMaker {
    protected static final Logger logger = LogManager.getLogger();
    public static void main(String [] args) throws IOException {
        int year = 2022;
        if (args.length >= 1) {
            year = Integer.parseInt(args[0]);
        }
        // if ww should collapse transactions i a singe security
        boolean mergeTransactions = false;
        if (args.length >= 2) {
            mergeTransactions = "true".equalsIgnoreCase(args[1]);
        }
        Map<String, Double> currencySEKConversionRates = getConversionRates();
        FinancialYear financialYear = new FinancialYear(year, currencySEKConversionRates);
        financialYear.setMergeTransactions(mergeTransactions);

        setupInitialCurrencyHoldings(financialYear);
        setupInitialEquityHoldings(financialYear);

        //Read the prepared excel-file of trades
        processTrades(financialYear);
        writeK4Excel(financialYear);

        SRUInfo sruInfo = SRUService.setupSruInfo();

        //Generate the actual files
        List<K4blankett> k4blankettList = K4blankettService.createFromFinancialYear(financialYear, sruInfo);

        SRUFiles sruFiles = SRUService.createSRU(sruInfo, k4blankettList);

        //Create the actual files
        writeSRUFilesToDisk(sruFiles);

        //Just for debug reasons
        writeHoldingsToDisk(financialYear);

        debugPrint(financialYear);
    }

    private static void writeK4Excel(FinancialYear financialYear) throws IOException {

        ExcelTradesWriter.writeTradesToFile(financialYear, new File(System.getProperty("user.dir") +
                File.separator  + "utdata_K4_" + financialYear.getYear() +".xlsx"));
    }

    private static void setupInitialEquityHoldings(FinancialYear year) throws IOException {
        logger.debug("Reading initial equity holdings");
        FileInputStream file = new FileInputStream(System.getProperty("user.dir") + File.separator  + "indata_aktier.xlsx");
        List<ExcelEquityHolding> excelEquityHoldings = ExcelEquityHoldingsReader.readFromFile(file);
        for (ExcelEquityHolding excelEquityHolding : excelEquityHoldings) {
            Money costBase = Money.of(excelEquityHolding.getCostBaseSEK(),"SEK");
            FinancialYearService.addInitialEquityHolding(year, excelEquityHolding.getName(), excelEquityHolding.getAmount(), costBase);
        }
    }

    private static void setupInitialCurrencyHoldings(FinancialYear year) throws IOException {
        logger.debug("Reading initial currency holdings");
        FileInputStream file = new FileInputStream(System.getProperty("user.dir") + File.separator  + "indata_valuta.xlsx");
        List<ExcelCurrencyHolding> excelCurrencyHoldings = ExcelCurrencyHoldingsReader.readFromFile(file);
        for (ExcelCurrencyHolding excelCurrencyHolding : excelCurrencyHoldings) {
            Money foreignCurrency = Money.of(excelCurrencyHolding.getAmount(), excelCurrencyHolding.getCurrencyCode());
            Money costBase = Money.of(excelCurrencyHolding.getCostBaseInSEK(),"SEK");
            FinancialYearService.addInitialCurrencyHolding(year,foreignCurrency, costBase);
        }
        logger.debug("Reading initial currency holdings done ");
    }

    private static void processTrades(FinancialYear year) throws IOException {
        logger.info("Processing trades " + year.getYear() + " start");

        parseParetoTrades(year);
        parseIBKRTrades(year);
        parseAvanzaTrades(year);
        FinancialYearService.clearInsignificantHoldings(year);

        logger.info("Processing trades done");
    }

    private static void parseAvanzaTrades(FinancialYear year) throws IOException {
        File file = new File(System.getProperty("user.dir") + File.separator  + "indata_avanza.csv");
        if (!file.exists()) {
            logger.info("Avanza file not found.");
            return;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        List<AvanzaTrade> avanzaTradeList = AvanzaCsvService.parseTradesFromCSV(fileInputStream);
        AvanzaTradesService.registerTrades(avanzaTradeList, year);
    }

    private static void parseIBKRTrades(FinancialYear year) throws IOException {
        File file = new File(System.getProperty("user.dir") + File.separator  + "indata_ibkr.csv");
        if (!file.exists()) {
            logger.info("IBKR file not found.");
            return;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        List<IbkrTrade> trades = IbkrCsvService.parseTradesFromCSV(fileInputStream);
        IbkrTradesService.registerTrades(trades, year);
    }

    private static void parseParetoTrades(FinancialYear year) throws IOException {
        File file = new File(System.getProperty("user.dir") + File.separator  + "indata_pareto.csv");
        if (!file.exists()) {
            logger.info("Pareto file not found.");
            return;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        List<ParetoTrade> trades = ParetoCsvService.parseTradesFromCSV(fileInputStream);
        ParetoTradesService.registerTrades(trades, year);
    }

    private static void writeHoldingsToDisk(FinancialYear year) throws IOException {

        File fileEquity = new File(System.getProperty("user.dir") + File.separator  + "utdata_aktier_" + year.getYear() + ".xlsx");
        File fileCurrency = new File(System.getProperty("user.dir") + File.separator  + "utdata_valuta_" + year.getYear() + ".xlsx");
        ExcelHoldingsWriter.writeEquityHoldings(year, fileEquity);
        ExcelHoldingsWriter.writeCurrencyHoldings(year, fileCurrency);
    }

    private static void writeSRUFilesToDisk(SRUFiles sruFiles) {
        logger.debug("Writing SRU files to disk");
        SRUService.writeToDisk(sruFiles);
    }

    private static Map<String, Double> getConversionRates() throws IOException {
        logger.debug("Reading conversion rates");
        FileInputStream file = new FileInputStream(System.getProperty("user.dir") + File.separator  + "indata_valutakurser.xlsx");
        return ExcelCurrencyReader.readXlxsFile(file);
    }

    private static void debugPrint(FinancialYear year) {
        double profit = 0d;
        logger.debug("----Trade summary start ---");
        for(SecuritySale securitySale : year.getSecuritySales().values()) {
            logger.debug(securitySale.prettyPrint());
            profit += securitySale.getProfit().getNumber().doubleValue();
        }
        logger.debug("----Trade summary end ---");
        logger.info("----Profit/loss from stocks: ---" + Money.of(profit,"SEK"));

        logger.debug("\n");
        logger.debug("----Currency Holdings "+ year.getYear() + "-12-31");
        for (CurrecyHolding currecyHolding : year.getCurrencies().values()) {
            logger.debug(currecyHolding.getMoney() + ". Costbase: " + currecyHolding.getCostInSEK() + ". Rate avg: " +
                    currecyHolding.getCostInSEK().divide(Math.round(currecyHolding.getMoney().getNumber().doubleValueExact())));
        }
        logger.debug("-----\n");
        logger.debug("----Equity Holdings "+ year.getYear() + "-12-31");
        for (SecurityHolding securityHolding : year.getEquities().values()) {
            logger.debug("" + securityHolding.getAmount() + " " + securityHolding.getName() + ". Costbase: " + securityHolding.getCostInSEK());
        }
        logger.debug("-----\n");
    }
}
