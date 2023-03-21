package com.ebtcap.sru.transactions;



import java.util.Map;
import java.util.TreeMap;

public class FinancialYear {
    private final Integer year;
    private boolean mergeTransactions;
    private final Map<String, SecurityHolding> equities;
    private final Map<String, CurrecyHolding> currencies;

    private final Map<String, SecuritySale> securitySales;
    private final Map<String, CurrencySale> currencySales;

    private final Map<String, Double> currencySEKConversionRates;


    public FinancialYear(Integer year, Map<String, Double> currencySEKConversionRates) {
        this.year = year;
        equities = new TreeMap<>();
        currencies = new TreeMap<>();
        securitySales = new TreeMap<>();
        currencySales = new TreeMap<>();
        this.currencySEKConversionRates = currencySEKConversionRates;
    }

    public Integer getYear() {
        return year;
    }

    public Map<String, Double> getCurrencySEKConversionRates() {
        return currencySEKConversionRates;
    }

    public Map<String, SecurityHolding> getEquities() {
        return equities;
    }

    public Map<String, CurrecyHolding> getCurrencies() {
        return currencies;
    }

    public Map<String, SecuritySale> getSecuritySales() {
        return securitySales;
    }

    public Map<String, CurrencySale> getCurrencySales() {
        return currencySales;
    }

    public boolean isMergeTransactions() {
        return mergeTransactions;
    }

    public void setMergeTransactions(boolean mergeTransactions) {
        this.mergeTransactions = mergeTransactions;
    }
}
