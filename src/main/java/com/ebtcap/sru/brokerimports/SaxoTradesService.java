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
        logger.info("Processing {} Saxo trades from Excel", trades.size());
        int processedCount = 0;
        int skippedCount = 0;
        
        for (SaxoTrade trade : trades) {
            logger.debug(trade);
    
            // Saxo provides completed trades with both open and close legs already paired
            String instrumentName = trade.getInstrumentDescription();
            String currency = trade.getClientCurrency();
            
            // Validate currency code - skip if empty or null
            if (currency == null || currency.trim().isEmpty()) {
                skippedCount++;
                logger.warn("Skipped trade #{}: Empty currency. Instrument='{}', OpenDate={}, CloseDate={}", 
                    skippedCount, instrumentName, trade.getTradeDateOpen(), trade.getTradeDateClose());
                continue;
            }
            
            // Validate instrument name
            if (instrumentName == null || instrumentName.trim().isEmpty()) {
                skippedCount++;
                logger.warn("Skipped trade #{}: Empty instrument name. Currency={}, OpenDate={}, CloseDate={}", 
                    skippedCount, currency, trade.getTradeDateOpen(), trade.getTradeDateClose());
                continue;
            }
            
            // Opening leg (BUY)
            double quantityBuy = Math.abs(trade.getQuantityOpen());
            double costBuy = Math.abs(trade.getTotalBookedOnOpeningLegClientCurrency());
            Money totalCost = Money.of(costBuy, currency);
            
            // Closing leg (SELL)
            double quantitySell = Math.abs(trade.getQuantityClose());
            double proceedsSell = Math.abs(trade.getTotalBookedOnClosingLegClientCurrency());
            Money totalProceeds = Money.of(proceedsSell, currency);
            
            // Register both buy and sell sides of the completed trade
            // Values are already in SEK (ClientCurrency), so no FX conversion needed
            FinancialYearService.addBuyEquity(year, instrumentName, quantityBuy, totalCost);
            FinancialYearService.addSellEquity(year, instrumentName, quantitySell, totalProceeds);
            
            processedCount++;
            logger.debug("Registered Saxo trade #{}: {} Buy: {} @ {} SEK, Sell: {} @ {} SEK", 
                    processedCount, instrumentName, quantityBuy, costBuy, quantitySell, proceedsSell);
        }
        
        logger.info("Saxo trades summary: {} processed, {} skipped (from {} total)", 
            processedCount, skippedCount, trades.size());
    }
}

