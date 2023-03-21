package com.ebtcap.sru.brokerimports;

import com.ebtcap.sru.transactions.FinancialYear;
import com.ebtcap.sru.transactions.FinancialYearService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javamoney.moneta.Money;

import java.util.List;

public class IbkrTradesService {
    protected static final Logger logger = LogManager.getLogger();

    public static void registerTrades(List<IbkrTrade> trades, FinancialYear year) {

        for (IbkrTrade trade : trades) {
            logger.debug(trade);
            String name = trade.getSymbol();
            boolean isCurrencyTrade = name.matches("[A-Z][A-Z][A-Z].[A-Z][A-Z][A-Z]");
            if (isCurrencyTrade) {
                registerCurrencyTrace(trade, year);
            } else {
                registerEquityTrade(trade, year);
            }
        }
    }

    private static void registerCurrencyTrace(IbkrTrade trade, FinancialYear year) {
        double quantityPositive = Math.abs(trade.getAmount());
        double commissionPositive = Math.abs(trade.getCommission());
        double pricePerCurrencyUnit = trade.getUnitPrice();
        String currency = trade.getSymbol().substring(0,3);

        if ("BUY".equals(trade.getAction())) {
            double totalAmountForeginCurrency = quantityPositive - commissionPositive;
            double totalAmountSEK = quantityPositive*pricePerCurrencyUnit;
            Money total = Money.of(totalAmountForeginCurrency, currency);
            Money totalCost = Money.of(totalAmountSEK, "SEK");
            FinancialYearService.addBuyCurrency(total, totalCost, year);
        } else
        if ("SELL".equals(trade.getAction())) {
            double totalReceivedSEK = quantityPositive * pricePerCurrencyUnit - commissionPositive * pricePerCurrencyUnit;
            Money total = Money.of(quantityPositive, currency);
            Money totalReceived = Money.of(totalReceivedSEK, "SEK");
            FinancialYearService.addSellCurrency(year, total, totalReceived);
        } else {
            logger.warn("Unknown action" + trade);
        }
    }

    private static void registerEquityTrade(IbkrTrade trade, FinancialYear year) {
        double quantityPositive = Math.abs(trade.getAmount());
        double commissionPositive = Math.abs(trade.getCommission());
        double pricePerShare = trade.getUnitPrice();

        if ("BUY".equals(trade.getAction())) {
            double totalAmount = quantityPositive*pricePerShare + commissionPositive;
            Money total = Money.of(totalAmount, trade.getCurrency());
            FinancialYearService.addBuyEquity(year, trade.getSymbol(), quantityPositive, total);
        } else
        if ("SELL".equals(trade.getAction())) {
            double totalAmount = quantityPositive*pricePerShare - commissionPositive;
            Money total = Money.of(totalAmount, trade.getCurrency());
            FinancialYearService.addSellEquity(year, trade.getSymbol(), quantityPositive, total);
        } else {
            logger.warn("Unknown action" + trade);
        }
    }
}
