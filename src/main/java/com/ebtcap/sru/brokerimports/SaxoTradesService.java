package com.ebtcap.sru.brokerimports;

import com.ebtcap.sru.transactions.FinancialYear;
import com.ebtcap.sru.transactions.SecuritySale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javamoney.moneta.Money;

import java.util.List;
import java.util.UUID;


public class SaxoTradesService {

    protected static final Logger logger = LogManager.getLogger();

    public static void registerTrades(List<SaxoTrade> trades, FinancialYear year) {
        logger.info("Processing {} Saxo trades from Excel", trades.size());
        int processedCount = 0;
        int skippedCount = 0;
        double totalPnL = 0.0;
        
        for (SaxoTrade trade : trades) {
            logger.debug(trade);
    
            // Saxo provides completed trades with P&L already calculated in SEK (ClientCurrency)
            String instrumentName = trade.getInstrumentDescription();
            double pnlSEK = trade.getPnLClientCurrency();
            
            // Validate instrument name
            if (instrumentName == null || instrumentName.trim().isEmpty()) {
                skippedCount++;
                logger.warn("Skipped trade #{}: Empty instrument name. PnL={}, CloseDate={}", 
                    skippedCount, pnlSEK, trade.getTradeDateClose());
                continue;
            }
            
            // Determine which K4 section this futures instrument belongs to
            String section = FuturesSectionMapper.getSection(instrumentName);
            
            // Use quantity for reporting purposes (number of contracts)
            double quantity = Math.abs(trade.getQuantityClose());
            
            // Apply the profit/loss logic:
            // For PROFIT (pnl > 0): Put amount in Försäljningspris, 0 in Omkostnadsbelopp → generates Vinst
            // For LOSS (pnl < 0): Put 0 in Försäljningspris, abs(amount) in Omkostnadsbelopp → generates Förlust
            
            Money salePriceSEK;
            Money costPriceSEK;
            
            if (pnlSEK >= 0) {
                // Profit: sale price = pnl, cost = 0
                salePriceSEK = Money.of(pnlSEK, "SEK");
                costPriceSEK = Money.of(0, "SEK");
            } else {
                // Loss: sale price = 0, cost = abs(pnl)
                salePriceSEK = Money.of(0, "SEK");
                costPriceSEK = Money.of(Math.abs(pnlSEK), "SEK");
            }
            
            // Create the SecuritySale directly with the section information
            String saleId = UUID.randomUUID().toString();
            SecuritySale sale = new SecuritySale(instrumentName, quantity, salePriceSEK, costPriceSEK, section);
            
            year.getSecuritySales().put(saleId, sale);
            
            totalPnL += pnlSEK;
            processedCount++;
            
            logger.debug("Registered Saxo trade #{}: {} (Section {}), Qty={}, PnL={} SEK, SalePrice={}, Cost={}", 
                    processedCount, instrumentName, section, quantity, pnlSEK, 
                    salePriceSEK.getNumber().doubleValue(), costPriceSEK.getNumber().doubleValue());
        }
        
        logger.info("Saxo trades summary: {} processed, {} skipped (from {} total). Total P&L: {} SEK", 
            processedCount, skippedCount, trades.size(), String.format("%.2f", totalPnL));
    }
}

