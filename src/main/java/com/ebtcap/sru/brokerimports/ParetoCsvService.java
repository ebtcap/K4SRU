package com.ebtcap.sru.brokerimports;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ParetoCsvService {

    public static List<ParetoTrade> parseTradesFromCSV(InputStream tradesCSVInputStream) throws IOException {
        List<ParetoTrade> paretoTrades = new ArrayList<>();
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(tradesCSVInputStream, "UTF-8"));

        String[] HEADERS = { "ISIN","Affärsdag","Likviddag","Ticker",
                "Transaktionstyp","Antal","Kurs","Belopp","Valuta","Avräkningsnota","Totalt","Courtage",
                "exportToCsv_contractnotes_header_valRate",
                "exportToCsv_contractnotes_header_documentId",
                "exportToCsv_contractnotes_header_downloadHash"
        };

        Iterable<CSVRecord> csvRecords = CSVFormat.DEFAULT
                .withHeader(HEADERS)
                .withFirstRecordAsHeader()
                .withDelimiter(',')
                .withRecordSeparator("\n")
                .withQuote('"')
                .parse(fileReader);


        for (CSVRecord csvRecord : csvRecords) {
            ParetoTrade trade = new ParetoTrade();
            trade.setBelopp(attemptDoubleParse(csvRecord.get(7)));
            trade.setAntal(attemptDoubleParse(csvRecord.get(5)));
            trade.setCourtage(attemptFloatParse(csvRecord.get(11)));
            trade.setKurs(attemptFloatParse(csvRecord.get(6)));
            trade.setTicker(csvRecord.get(3));
            trade.setTotalt(attemptDoubleParse(csvRecord.get(10)));
            trade.setTransaktionstyp(csvRecord.get(4));
            trade.setValuta(csvRecord.get(8));
            paretoTrades.add(trade);
        }
        return paretoTrades;
    }

    private static double attemptDoubleParse(String s) {
        try{
            if (s != null) {
                s = s.replaceAll(",",".");
            } else {
                return 0;
            }
            return Double.parseDouble(s);

        }catch (NumberFormatException ex) {
            //nada
        }
        return 0;
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
