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
    
            // Extract required fields
            String instrumentName = trade.getInstrumentDescription();
            double quantity = trade.getQuantityClose();
            Money pnl = Money.of(trade.getPnLClientCurrency(), trade.getClientCurrency());
    
            Money totalSalePrice = Money.of(0, trade.getClientCurrency());
            Money totalCost = Money.of(0, trade.getClientCurrency());
    
            // Apply logic based on PnLClientCurrency
            if (pnl.isPositive()) {
                totalSalePrice = pnl; // Positive PnL goes to Försäljningspris
            } else if (pnl.isNegative()) {
                totalCost = pnl.abs(); // Negative PnL goes to Omkostnadsbelopp
            }
    
            // Add trade to FinancialYear
            if (quantity > 0) {
                FinancialYearService.addBuyEquity(year, instrumentName, quantity, totalCost);
            } else {
                FinancialYearService.addSellEquity(year, instrumentName, Math.abs(quantity), totalSalePrice);
            }
        }
    }
}

