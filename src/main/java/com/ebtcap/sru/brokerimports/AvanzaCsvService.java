package com.ebtcap.sru.brokerimports;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AvanzaCsvService {

    public static List<AvanzaTrade> parseTradesFromCSV(InputStream tradesCSVInputStream) throws IOException {
        List<AvanzaTrade> avanzaTradeList = new ArrayList<>();
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(tradesCSVInputStream, "UTF-8"));

        String[] HEADERS = { "Datum", "Konto", "Typ av transaktion", "Värdepapper/beskrivning",
                "Antal", "Kurs", "Belopp", "Courtage", "Valuta", "ISIN"};

        Iterable<CSVRecord> csvRecords = CSVFormat.DEFAULT
                .withHeader(HEADERS)
                .withFirstRecordAsHeader()
                .withDelimiter(';')
                .withRecordSeparator("\n")
                .withQuote(null)
                .parse(fileReader);


        for (CSVRecord csvRecord : csvRecords) {
            AvanzaTrade trade = new AvanzaTrade();
            trade.setAccountName(csvRecord.get(1));
            trade.setTypeOfTransaction(csvRecord.get(2));
            trade.setIssueName(csvRecord.get(3));
            trade.setAmount(attemptLongParse(csvRecord.get(4)));
            trade.setPrice(attemptFloatParse(csvRecord.get(5)));
            trade.setSum(attemptFloatParse(csvRecord.get(6)));
            trade.setFee(attemptFloatParse(csvRecord.get(7)));
            trade.setCurrency(csvRecord.get(8));
            trade.setIsin(csvRecord.get(9));

            if (trade.getAmount() != 0 && trade.getTypeOfTransaction() != null &&
                    (trade.getTypeOfTransaction().equals("Köp") ||
                            trade.getTypeOfTransaction().equals("Sälj")) && !trade.getIssueName().contains("Aktielån")) {
                avanzaTradeList.add(trade);
            }
        }
        return avanzaTradeList;
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

    private static long attemptLongParse(String s) {
        try{
            return Long.parseLong(s);

        }catch (NumberFormatException ex) {
            //nada
        }
        return 0;
    }

}
