package com.ebtcap.sru.brokerimports;

import com.ebtcap.sru.transactions.FinancialYear;
import com.ebtcap.sru.transactions.FinancialYearService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javamoney.moneta.Money;

import java.util.List;

public class ParetoTradesService {
    protected static final Logger logger = LogManager.getLogger();

    public static void registerTrades(List<ParetoTrade> trades, FinancialYear year) {
        for (ParetoTrade trade : trades) {
            logger.debug(trade);
            String name = trade.getTicker();
            boolean isCurrencyTrade = "USD".equals(name) || "EUR".equals(name);
            if (isCurrencyTrade) {
                registerCurrencyTrace(trade, year);
            } else {
                registerEquityTrade(trade, year);
            }
        }
    }

    private static void registerCurrencyTrace(ParetoTrade trade, FinancialYear year) {
        double quantityPositive = Math.abs(trade.getAntal());
        double pricePerCurrencyUnit = trade.getKurs();
        double totalt = Math.abs(trade.getTotalt());

        if ("Buy".equals(trade.getTransaktionstyp())) {
            Money total = Money.of(quantityPositive, trade.getTicker());
            Money totalCost = Money.of(totalt*pricePerCurrencyUnit, "SEK");
            FinancialYearService.addBuyCurrency(total, totalCost, year);
        } else
        if ("Sell".equals(trade.getTransaktionstyp())) {
            Money total = Money.of(quantityPositive, trade.getTicker());
            Money totalReceived = Money.of(totalt * pricePerCurrencyUnit, "SEK");
            FinancialYearService.addSellCurrency(year, total, totalReceived);
        } else {
            logger.warn("Unknown action" + trade);
        }
    }

    private static void registerEquityTrade(ParetoTrade trade, FinancialYear year) {
        double quantityPositive = Math.abs(trade.getAntal());
        double totaltPositive = Math.abs(trade.getTotalt());

        if ("Buy".equals(trade.getTransaktionstyp())) {
            Money total = Money.of(totaltPositive, trade.getValuta());
            FinancialYearService.addBuyEquity(year, trade.getTicker(), quantityPositive, total);
        } else
        if ("Sell".equals(trade.getTransaktionstyp())) {
            Money total = Money.of(totaltPositive, trade.getValuta());
            FinancialYearService.addSellEquity(year, trade.getTicker(), quantityPositive, total);
        } else {
            logger.warn("Unknown action" + trade);
        }
    }
}
