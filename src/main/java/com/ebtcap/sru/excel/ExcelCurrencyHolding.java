package com.ebtcap.sru.excel;

public class ExcelCurrencyHolding {
    private String accountName;
    private String currencyCode;
    private double amount;
    private double costBaseInSEK;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCostBaseInSEK() {
        return costBaseInSEK;
    }

    public void setCostBaseInSEK(double costBaseInSEK) {
        this.costBaseInSEK = costBaseInSEK;
    }
}
