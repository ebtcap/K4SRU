package com.ebtcap.sru.brokerimports;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class IbkrCsvService {

    public static List<IbkrTrade> parseTradesFromCSV(InputStream tradesCSVInputStream) throws IOException {
        List<IbkrTrade> ibkrTrades = new ArrayList<>();
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(tradesCSVInputStream, "UTF-8"));

        String[] HEADERS = { "Symbol", "Buy/Sell", "Quantity", "TradePrice",
                "IBCommission", "CurrencyPrimary"};

        Iterable<CSVRecord> csvRecords = CSVFormat.DEFAULT
                .withHeader(HEADERS)
                .withFirstRecordAsHeader()
                .withDelimiter(',')
                .withRecordSeparator("\n")
                .withQuote('"')
                .parse(fileReader);


        for (CSVRecord csvRecord : csvRecords) {
            IbkrTrade trade = new IbkrTrade();
            trade.setSymbol(csvRecord.get(0));
            trade.setAction(csvRecord.get(1));
            trade.setAmount(attemptFloatParse(csvRecord.get(2)));
            trade.setUnitPrice(attemptFloatParse(csvRecord.get(3)));
            trade.setCommission(attemptFloatParse(csvRecord.get(4)));
            trade.setCurrency(csvRecord.get(5));
            ibkrTrades.add(trade);
        }
        return ibkrTrades;
    }

    private static float attemptFloatParse(String s) {
        try{
            if (s != null) {
                s = s.replaceAll(",",".");
            } else {
                return 0;
            }
            return Float.parseFloat(s);

        }catch (NumberFormatException ex) {
            //nada
        }
        return 0;
    }
}
