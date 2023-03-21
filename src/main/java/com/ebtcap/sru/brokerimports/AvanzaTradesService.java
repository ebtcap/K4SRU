package com.ebtcap.sru.brokerimports;

import com.ebtcap.sru.transactions.FinancialYear;
import com.ebtcap.sru.transactions.FinancialYearService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javamoney.moneta.Money;

import java.util.List;

public class AvanzaTradesService {

    protected static final Logger logger = LogManager.getLogger();

    public static void registerTrades(List<AvanzaTrade> avanzaTradeList, FinancialYear year) {
        for (AvanzaTrade trade : avanzaTradeList) {
            logger.debug(trade);
            registerEquityTrade(trade, year);
        }
    }

    private static void registerEquityTrade(AvanzaTrade trade, FinancialYear year) {
        double quantityPositive = Math.abs(trade.getAmount());
        double totaltPositive = Math.abs(trade.getSum());

        if ("Köp".equals(trade.getTypeOfTransaction())) {
            Money total = Money.of(totaltPositive, trade.getCurrency());
            FinancialYearService.addBuyEquity(year, trade.getIssueName(), quantityPositive, total);
        } else
        if ("Sälj".equals(trade.getTypeOfTransaction())) {
            Money total = Money.of(totaltPositive, trade.getCurrency());
            FinancialYearService.addSellEquity(year, trade.getIssueName(), quantityPositive, total);
        } else {
            logger.warn("Unknown action" + trade);
        }
    }
}
