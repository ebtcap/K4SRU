package com.ebtcap.sru.transactions;

import org.javamoney.moneta.Money;

public class CurrencySale {
    private Money soldCurrency;
    private Money receivedCurrencySEK;
    private Money costCurrencySEK;

    public CurrencySale(Money soldCurrency, Money receivedCurrencySEK, Money costCurrencySEK) {
        this.soldCurrency = soldCurrency;
        this.receivedCurrencySEK = receivedCurrencySEK;
        this.costCurrencySEK = costCurrencySEK;
    }

    public Money getProfit() {
        return receivedCurrencySEK.subtract(costCurrencySEK);
    }

    public Money getSoldCurrency() {
        return soldCurrency;
    }

    public void setSoldCurrency(Money soldCurrency) {
        this.soldCurrency = soldCurrency;
    }

    public Money getReceivedCurrencySEK() {
        return receivedCurrencySEK;
    }

    public void setReceivedCurrencySEK(Money receivedCurrencySEK) {
        this.receivedCurrencySEK = receivedCurrencySEK;
    }

    public Money getCostCurrencySEK() {
        return costCurrencySEK;
    }

    public void setCostCurrencySEK(Money costCurrencySEK) {
        this.costCurrencySEK = costCurrencySEK;
    }

    @Override
    public String toString() {
        return "CurrencySale{" +
                "soldCurrency=" + soldCurrency +
                ", receivedCurrencySEK=" + receivedCurrencySEK +
                ", costCurrencySEK=" + costCurrencySEK +
                '}';
    }
}
