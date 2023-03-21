package com.ebtcap.sru.brokerimports;

import java.util.Date;

public class AvanzaTrade {

    private String accountName;

    private String typeOfTransaction;

    private String issueName;

    private long amount;

    private float price;

    private float sum;

    private float fee;

    private String currency;

    private String isin;


    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getTypeOfTransaction() {
        return typeOfTransaction;
    }

    public void setTypeOfTransaction(String typeOfTransaction) {
        this.typeOfTransaction = typeOfTransaction;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    @Override
    public String toString() {
        return "AvanzaTrade{" +
                "accountName='" + accountName + '\'' +
                ", typeOfTransaction='" + typeOfTransaction + '\'' +
                ", issueName='" + issueName + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                ", sum=" + sum +
                ", fee=" + fee +
                ", currency='" + currency + '\'' +
                ", isin='" + isin + '\'' +
                '}';
    }
}
