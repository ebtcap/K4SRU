package com.ebtcap.sru.transactions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javamoney.moneta.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class FinancialYearService {
    protected static final Logger logger = LogManager.getLogger();
    private static final double CUTOFF_LIMIT = 0.1;

    public static void addInitialEquityHolding(FinancialYear financialYear,
                                               String name, double amount, Money costInSEK) {
        SecurityHolding stock;
        if(!financialYear.getEquities().containsKey(name)) {
            stock = new SecurityHolding(name, amount, costInSEK);
            financialYear.getEquities().put(name, stock);
        } else {
            stock = financialYear.getEquities().get(name);
            stock.setAmount(stock.getAmount() + amount);
            stock.setCostInSEK(stock.getCostInSEK().add(costInSEK));
        }
    }

    public static void addInitialCurrencyHolding(FinancialYear financialYear,
                                                 Money foreignCurrency, Money costInSEK) {
        CurrecyHolding currencyHolding;
        final String currencyCode = foreignCurrency.getCurrency().getCurrencyCode();
        if(!financialYear.getCurrencies().containsKey(currencyCode)) {
            currencyHolding = new CurrecyHolding(foreignCurrency, costInSEK);
            financialYear.getCurrencies().put(currencyCode, currencyHolding);
        } else {
            currencyHolding = financialYear.getCurrencies().get(currencyCode);
            currencyHolding.getMoney().add(foreignCurrency);
            currencyHolding.getCostInSEK().add(costInSEK);
        }
    }

    public static void addBuyEquity(FinancialYear financialYear,
                                               String name, double amount, Money totalCostInclFees) {

        if (amount < 0) {
            throw new RuntimeException("Amount buy must be positive");
        }
        if (totalCostInclFees.getNumber().doubleValue() < 0) {
            throw new RuntimeException("Amount Money buy must be positive");
        }

        Money costInSEK;
        if (totalCostInclFees.getCurrency().getCurrencyCode().equals("SEK")) {
            costInSEK = totalCostInclFees;
        } else {
            costInSEK = convertCurrency(totalCostInclFees, financialYear);
            addSellCurrency(financialYear, totalCostInclFees, costInSEK);
        }

        recordBuyTransaction(financialYear,name, amount, costInSEK);
    }



    /**
     * Om det är en blankningsaffär vill vi inte boka affären innan (del)positionen är täckt.
     *
     */
    public static void addSellEquity(FinancialYear financialYear,
                                         String stockName, double shareAmount, Money totalReceivedInclFees) {
        if (stockName == null || totalReceivedInclFees == null || financialYear == null) {
            throw new RuntimeException("Invalid input");
        }
        if (shareAmount <= 0) {
            throw new RuntimeException("Amount must be a positive number");
        }

        // Handel currency
        Money receivedInSEK;
        if (totalReceivedInclFees.getCurrency().getCurrencyCode().equals("SEK")) {
            receivedInSEK = totalReceivedInclFees;
        } else {
            receivedInSEK = convertCurrency(totalReceivedInclFees, financialYear);
            addBuyCurrency(totalReceivedInclFees, receivedInSEK, financialYear);
        }

        recordSaleTransaction(financialYear, stockName, shareAmount, receivedInSEK);
    }

    /*
    If part of the stock is shorted, we want to record that part of the trade as a "sell"
     */
    private static void recordBuyTransaction(FinancialYear financialYear, String stockName,
                                             final double totalShareAmountToBuy, Money paidInSEK) {
        // Find holding
        SecurityHolding stockHolding = getStockHolding(financialYear, stockName);


        //Split into sell "existing" and cover shorted
        double amountToBuy = totalShareAmountToBuy;
        double amountToCoverFromShorted = 0;
        double factorToCoverFromShorted = 0;
        if (stockHolding.getAmount() < 0) {
            amountToCoverFromShorted = Math.min(Math.abs(stockHolding.getAmount()), totalShareAmountToBuy);
            amountToBuy = totalShareAmountToBuy - amountToCoverFromShorted;
            factorToCoverFromShorted = amountToCoverFromShorted / totalShareAmountToBuy;
        }
        logger.debug("Amount to cover " + amountToCoverFromShorted + ". Amount to buy to own: " + amountToBuy);

        if (amountToCoverFromShorted > 0) {
            Money costInSEK = getEquityCostBaseOf(stockHolding, amountToCoverFromShorted);

            //Save sell event
            SecuritySale securitySale = new SecuritySale(stockName, amountToCoverFromShorted, costInSEK, paidInSEK.multiply(factorToCoverFromShorted));
            addSecuritySale(financialYear, securitySale);

            //Adjust holding
            adjustHolding(stockHolding, amountToCoverFromShorted, costInSEK);
        }
        if (amountToBuy > 0) {
            //Adjust holding
            adjustHolding(stockHolding, amountToBuy, paidInSEK.multiply(1-factorToCoverFromShorted));
        }

        //Save updated holding
        financialYear.getEquities().put(stockName, stockHolding);
        if (stockHolding.getAmount() == 0) {
            financialYear.getEquities().remove(stockName);
        }
    }

    private static void recordSaleTransaction(FinancialYear financialYear, String stockName, double totalShareAmountToSell, Money receivedInSEK) {
        // Find holding
        SecurityHolding stockHolding = getStockHolding(financialYear, stockName);

        //Split into sell "existing" and cover shorted
        double amountToSellFromOwned = totalShareAmountToSell;
        double amountToSellShort = 0;
        double factorToSellFromOwned = 1;
        if (stockHolding.getAmount() < totalShareAmountToSell) {
            if (stockHolding.getAmount() <= 0) {
                amountToSellShort = totalShareAmountToSell;
                amountToSellFromOwned = 0;
            } else {
                amountToSellShort = totalShareAmountToSell - stockHolding.getAmount();
                amountToSellFromOwned = stockHolding.getAmount();
            }
            factorToSellFromOwned = amountToSellFromOwned / totalShareAmountToSell;
        }
        logger.debug("Amount to sell " + amountToSellFromOwned + ". Amount to sell short: " + amountToSellShort);

        if (amountToSellFromOwned > 0) {
            Money costInSEK = getEquityCostBaseOf(stockHolding, amountToSellFromOwned);

            //Save sell event
            SecuritySale securitySale = new SecuritySale(stockName, amountToSellFromOwned, receivedInSEK.multiply(factorToSellFromOwned), costInSEK);
            addSecuritySale(financialYear, securitySale);

            //Adjust holding
            adjustHolding(stockHolding, amountToSellFromOwned * -1, costInSEK.multiply(-1));
        }
        if (amountToSellShort > 0) {
            //Adjust holding
            adjustHolding(stockHolding, amountToSellShort * -1, receivedInSEK.multiply(-1 * (1 - factorToSellFromOwned)));
        }

        //Save updated holding
        financialYear.getEquities().put(stockName, stockHolding);
        if (stockHolding.getAmount() == 0) {
            financialYear.getEquities().remove(stockName);
        }
    }

    private static void adjustHolding(SecurityHolding stockHolding, double amount, Money money) {
        stockHolding.setAmount(stockHolding.getAmount() + amount);
        stockHolding.setCostInSEK(stockHolding.getCostInSEK().add(money));
    }


    private static SecurityHolding getStockHolding(FinancialYear financialYear, String stockName) {
        SecurityHolding securityHolding;
        if(!financialYear.getEquities().containsKey(stockName)) {
            securityHolding = new SecurityHolding(stockName, 0, Money.of(0,"SEK"));
        } else {
            securityHolding = financialYear.getEquities().get(stockName);
        }
        return securityHolding;
    }

    public static void addBuyCurrency(Money foreginCurrencyBought, Money sekPaid, FinancialYear financialYear) {
        CurrecyHolding currencyHolding = financialYear.getCurrencies().get(foreginCurrencyBought.getCurrency().getCurrencyCode());
        if (currencyHolding == null) {
            currencyHolding = new CurrecyHolding(Money.of(0, foreginCurrencyBought.getCurrency()), Money.of(0,"SEK"));
            financialYear.getCurrencies().put(currencyHolding.getMoney().getCurrency().getCurrencyCode(), currencyHolding);
        }
        currencyHolding.setMoney(currencyHolding.getMoney().add(foreginCurrencyBought));
        currencyHolding.setCostInSEK(currencyHolding.getCostInSEK().add(sekPaid));
    }

    public static void addSellCurrency(FinancialYear financialYear, Money moneyInForeignCurrency, Money moneyInSEK) {
        if (moneyInSEK == null || moneyInForeignCurrency == null || financialYear == null) {
            throw new RuntimeException("Invalid input");
        }

        CurrecyHolding currencyHolding = financialYear.getCurrencies().get(moneyInForeignCurrency.getCurrency().getCurrencyCode());
        if (currencyHolding == null) {
            currencyHolding = new CurrecyHolding(Money.of(0, moneyInForeignCurrency.getCurrency()), Money.of(0,"SEK"));
            financialYear.getCurrencies().put(currencyHolding.getMoney().getCurrency().getCurrencyCode(), currencyHolding);
        }

        //Get the cost of the currency
        Money costCurrencySEK = getCostOf(financialYear, moneyInForeignCurrency);

        //Save the currency Sale
        CurrencySale currencySale = new CurrencySale(moneyInForeignCurrency, moneyInSEK, costCurrencySEK);
        addCurrencySale(financialYear, currencySale);
    }

    private static void addSecuritySale(FinancialYear financialYear, SecuritySale securitySale) {
        logger.info("Adding SALE " + securitySale.getAmount() + ". Result: " + securitySale.getProfit());
        SecuritySale existingSale = financialYear.getSecuritySales().get(securitySale.getName());
        if (financialYear.isMergeTransactions()) {
            if (existingSale == null) {
                financialYear.getSecuritySales().put(securitySale.getName(), securitySale);
            } else {
                //merge
                existingSale.setAmount(existingSale.getAmount() + securitySale.getAmount());
                existingSale.setSalePriceSEK(existingSale.getSalePriceSEK().add(securitySale.getSalePriceSEK()));
                existingSale.setCostPriceSEK(existingSale.getCostPriceSEK().add(securitySale.getCostPriceSEK()));
            }
        } else {
            financialYear.getSecuritySales().put(UUID.randomUUID().toString(), securitySale);
        }

    }

    private static Money convertCurrency(Money moneyInForeignCurrency, FinancialYear financialYear) {
        if (moneyInForeignCurrency.getCurrency().getCurrencyCode().equals("SEK")) {
            return  moneyInForeignCurrency;
        }
        Double sekConversionRate = financialYear.getCurrencySEKConversionRates()
                .get(moneyInForeignCurrency.getCurrency().getCurrencyCode());
        if (sekConversionRate == null) {
            throw new RuntimeException("Could not find conversion rate for " + moneyInForeignCurrency.getCurrency().getCurrencyCode());
        }
        return Money.of(moneyInForeignCurrency.getNumber().doubleValue() * (sekConversionRate),"SEK");
    }

    private static void addCurrencySale(FinancialYear financialYear, CurrencySale currencySale) {
        CurrecyHolding currencyHolding = financialYear.getCurrencies().get(currencySale.getSoldCurrency().getCurrency().getCurrencyCode());
        //Save to currencyHoldings
        currencyHolding.setMoney(currencyHolding.getMoney().subtract(currencySale.getSoldCurrency()));
        currencyHolding.setCostInSEK(currencyHolding.getCostInSEK().subtract(currencySale.getCostCurrencySEK()));
        if (currencyHolding.getMoney().getNumber().doubleValue() == 0) {
            financialYear.getCurrencies().remove(currencySale.getSoldCurrency().getCurrency().getCurrencyCode());
        }

        //Merge
        CurrencySale existingSale = financialYear.getCurrencySales().get(currencySale.getSoldCurrency().getCurrency().getCurrencyCode());
        if (existingSale == null) {
            financialYear.getCurrencySales().put(currencySale.getSoldCurrency().getCurrency().getCurrencyCode(), currencySale);
        } else {
            //merge
            existingSale.setSoldCurrency(existingSale.getSoldCurrency().add(currencySale.getSoldCurrency()));
            existingSale.setCostCurrencySEK(existingSale.getCostCurrencySEK().add(currencySale.getCostCurrencySEK()));
            existingSale.setReceivedCurrencySEK(existingSale.getReceivedCurrencySEK().add(currencySale.getReceivedCurrencySEK()));
        }
    }

    private static Money getCostOf(FinancialYear financialYear, Money moneyInForeignCurrency) {
        if (moneyInForeignCurrency.getNumber().doubleValue() == 0) {
            return Money.of(0, "SEK");
        }
        CurrecyHolding currecyHolding = financialYear.getCurrencies().get(moneyInForeignCurrency.getCurrency().getCurrencyCode());
        if (currecyHolding == null) {
            return convertCurrency(moneyInForeignCurrency,financialYear);
        }
        if (moneyInForeignCurrency.getNumber().doubleValue() > currecyHolding.getMoney().getNumber().doubleValue()) {
            double overShot = moneyInForeignCurrency.getNumber().doubleValue() - currecyHolding.getMoney().getNumber().doubleValue();

            return currecyHolding.getCostInSEK().add(convertCurrency(Money.of(overShot, moneyInForeignCurrency.getCurrency()),financialYear));
        }
        double fraction = moneyInForeignCurrency.getNumber().doubleValue() / currecyHolding.getMoney().getNumber().doubleValue();
        return currecyHolding.getCostInSEK().multiply(fraction);
    }

    private static Money getEquityCostBaseOf(SecurityHolding stock, double shareAmount) {
        if (shareAmount == 0 || stock == null) {
            return Money.of(0, "SEK");
        }

        if (shareAmount > Math.abs(stock.getAmount())) {
            throw new RuntimeException("Cannot calculate cost base for that ratio");
        }

        double fraction = shareAmount / stock.getAmount();
        return stock.getCostInSEK().multiply(fraction);
    }

    public static void clearInsignificantHoldings(FinancialYear year) {
        List<String> toRemove = new ArrayList<>();
        for (SecurityHolding securityHolding : year.getEquities().values()) {
            if (Math.abs(securityHolding.getAmount()) < CUTOFF_LIMIT) {
                toRemove.add(securityHolding.getName());
            }
        }
        toRemove.forEach(r -> year.getEquities().remove(r));
    }
}
