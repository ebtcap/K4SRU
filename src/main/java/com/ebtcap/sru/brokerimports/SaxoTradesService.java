package com.ebtcap.sru.brokerimports;

import com.ebtcap.sru.transactions.FinancialYear;
import com.ebtcap.sru.transactions.FinancialYearService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javamoney.moneta.Money;

import java.util.List;


public class SaxoTradesService {

    protected static final Logger logger = LogManager.getLogger();

    public static void registerTrades(List<SaxoTrade> trades, FinancialYear year) {
        for (SaxoTrade trade : trades) {
            logger.debug(trade);
            // Add logic to register each trade with the FinancialYear object
            addTrade(year, trade);
        }
    }
    private static void addTrade(FinancialYear year, SaxoTrade trade) {
        double quantityPositive = Math.abs(trade.getQuantityClose());
        double totalPositive = Math.abs(trade.getTotalBookedOnClosingLegClientCurrency());

        if ("Köp".equals(trade.getAssetType())) {
            Money total = Money.of(totalPositive, trade.getClientCurrency());
            FinancialYearService.addBuyEquity(year, trade.getInstrumentDescription(), quantityPositive, total);
        } else if ("Sälj".equals(trade.getAssetType())) {
            Money total = Money.of(totalPositive, trade.getClientCurrency());
            FinancialYearService.addSellEquity(year, trade.getInstrumentDescription(), quantityPositive, total);
        } else {
            logger.warn("Unknown action" + trade);
        }
    }
}

