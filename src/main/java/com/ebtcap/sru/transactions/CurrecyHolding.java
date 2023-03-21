package com.ebtcap.sru.transactions;

import org.javamoney.moneta.Money;

public class CurrecyHolding {

    private Money money;
    private Money costInSEK;

    public CurrecyHolding(Money money, Money costInSEK) {
        this.money = money;
        this.costInSEK = costInSEK;
    }

    public void setMoney(Money money) {
        this.money = money;
    }

    public void setCostInSEK(Money costInSEK) {
        this.costInSEK = costInSEK;
    }

    public Money getMoney() {
        return money;
    }

    public Money getCostInSEK() {
        return costInSEK;
    }
}
