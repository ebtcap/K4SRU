package com.ebtcap.sru.transactions;

import org.javamoney.moneta.Money;

public class SecurityHolding {
    private String name;
    private double amount;
    private Money costInSEK;

    public SecurityHolding(String name, double amount, Money costInSEK) {
        this.name = name;
        this.amount = amount;
        this.costInSEK = costInSEK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Money getCostInSEK() {
        return costInSEK;
    }

    public void setCostInSEK(Money costInSEK) {
        this.costInSEK = costInSEK;
    }
}
