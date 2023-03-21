package com.ebtcap.sru.brokerimports;

public class IbkrTrade {
    private String symbol;
    private String action;
    private float amount;
    private float unitPrice;
    private float commission;
    private String currency;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public float getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "IbkrTrade{" +
                "symbol='" + symbol + '\'' +
                ", action='" + action + '\'' +
                ", amount=" + amount +
                ", unitPrice=" + unitPrice +
                ", commission=" + commission +
                ", currency='" + currency + '\'' +
                '}';
    }
}
