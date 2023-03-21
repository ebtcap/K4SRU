package com.ebtcap.sru.excel;

public class ExcelEquityHolding {
    private String name;
    private String account;
    private double amount;
    private double costBaseSEK;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCostBaseSEK() {
        return costBaseSEK;
    }

    public void setCostBaseSEK(double costBaseSEK) {
        this.costBaseSEK = costBaseSEK;
    }
}
