package com.ebtcap.sru.transactions;

import org.javamoney.moneta.Money;

public class SecuritySale {

    private final String name;
    private double amount;
    private  Money salePriceSEK;
    private  Money costPriceSEK;

    public SecuritySale(String name, double amount, Money salePriceSEK, Money costPriceSEK) {
        this.name = name;
        this.amount = amount;
        this.salePriceSEK = salePriceSEK;
        this.costPriceSEK = costPriceSEK;
    }

    public Money getProfit() {
        return salePriceSEK.subtract(costPriceSEK);
    }

    public void setSalePriceSEK(Money salePriceSEK) {
        this.salePriceSEK = salePriceSEK;
    }

    public void setCostPriceSEK(Money costPriceSEK) {
        this.costPriceSEK = costPriceSEK;
    }

    public void setAmount(double amount) {

        if (amount == 0) {
            throw new RuntimeException("WTF");
        }
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public Money getSalePriceSEK() {
        return salePriceSEK;
    }

    public Money getCostPriceSEK() {
        return costPriceSEK;
    }

    @Override
    public String toString() {
        return "SecuritySale{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                ", salePriceSEK=" + salePriceSEK +
                ", costPriceSEK=" + costPriceSEK +
                '}';
    }

    public String prettyPrint() {
        return "Namn :" + name + ". Antal omsatta: " + amount +
                ". Inköpspris: " + costPriceSEK + ". Försäljningspris: " + salePriceSEK + ". Resultat: " + getProfit();
    }
}
