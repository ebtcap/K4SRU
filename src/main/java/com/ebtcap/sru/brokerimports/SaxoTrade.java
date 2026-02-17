package com.ebtcap.sru.brokerimports;

public class SaxoTrade {
    private String tradeDateClose;
    private String tradeDateOpen;
    private String accountId;
    private String accountCurrency;
    private String assetType;
    private String instrumentDescription;
    private String instrumentSymbol;
    private String instrumentCurrency;
    private String openPositionId;
    private String closePositionId;
    private double quantityClose;
    private double quantityOpen;
    private double openPrice;
    private double closePrice;
    private double totalBookedOnOpeningLegAccountCurrency;
    private double totalBookedOnOpeningLegClientCurrency;
    private double totalBookedOnClosingLegAccountCurrency;
    private String clientCurrency;
    private double totalBookedOnClosingLegClientCurrency;
    private double pnLAccountCurrency;
    private double pnLClientCurrency;

    // Getters and setters for all fields
    public String getTradeDateClose() {
        return tradeDateClose;
    }

    public void setTradeDateClose(String tradeDateClose) {
        this.tradeDateClose = tradeDateClose;
    }

    public String getTradeDateOpen() {
        return tradeDateOpen;
    }

    public void setTradeDateOpen(String tradeDateOpen) {
        this.tradeDateOpen = tradeDateOpen;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountCurrency() {
        return accountCurrency;
    }

    public void setAccountCurrency(String accountCurrency) {
        this.accountCurrency = accountCurrency;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getInstrumentDescription() {
        return instrumentDescription;
    }

    public void setInstrumentDescription(String instrumentDescription) {
        this.instrumentDescription = instrumentDescription;
    }

    public String getInstrumentSymbol() {
        return instrumentSymbol;
    }

    public void setInstrumentSymbol(String instrumentSymbol) {
        this.instrumentSymbol = instrumentSymbol;
    }

    public String getInstrumentCurrency() {
        return instrumentCurrency;
    }

    public void setInstrumentCurrency(String instrumentCurrency) {
        this.instrumentCurrency = instrumentCurrency;
    }

    public String getOpenPositionId() {
        return openPositionId;
    }

    public void setOpenPositionId(String openPositionId) {
        this.openPositionId = openPositionId;
    }

    public String getClosePositionId() {
        return closePositionId;
    }

    public void setClosePositionId(String closePositionId) {
        this.closePositionId = closePositionId;
    }

    public double getQuantityClose() {
        return quantityClose;
    }

    public void setQuantityClose(double quantityClose) {
        this.quantityClose = quantityClose;
    }

    public double getQuantityOpen() {
        return quantityOpen;
    }

    public void setQuantityOpen(double quantityOpen) {
        this.quantityOpen = quantityOpen;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getTotalBookedOnOpeningLegAccountCurrency() {
        return totalBookedOnOpeningLegAccountCurrency;
    }

    public void setTotalBookedOnOpeningLegAccountCurrency(double totalBookedOnOpeningLegAccountCurrency) {
        this.totalBookedOnOpeningLegAccountCurrency = totalBookedOnOpeningLegAccountCurrency;
    }

    public double getTotalBookedOnOpeningLegClientCurrency() {
        return totalBookedOnOpeningLegClientCurrency;
    }

    public void setTotalBookedOnOpeningLegClientCurrency(double totalBookedOnOpeningLegClientCurrency) {
        this.totalBookedOnOpeningLegClientCurrency = totalBookedOnOpeningLegClientCurrency;
    }

    public double getTotalBookedOnClosingLegAccountCurrency() {
        return totalBookedOnClosingLegAccountCurrency;
    }

    public void setTotalBookedOnClosingLegAccountCurrency(double totalBookedOnClosingLegAccountCurrency) {
        this.totalBookedOnClosingLegAccountCurrency = totalBookedOnClosingLegAccountCurrency;
    }

    public String getClientCurrency() {
        return clientCurrency;
    }

    public void setClientCurrency(String clientCurrency) {
        this.clientCurrency = clientCurrency;
    }

    public double getTotalBookedOnClosingLegClientCurrency() {
        return totalBookedOnClosingLegClientCurrency;
    }

    public void setTotalBookedOnClosingLegClientCurrency(double totalBookedOnClosingLegClientCurrency) {
        this.totalBookedOnClosingLegClientCurrency = totalBookedOnClosingLegClientCurrency;
    }

    public double getPnLAccountCurrency() {
        return pnLAccountCurrency;
    }

    public void setPnLAccountCurrency(double pnLAccountCurrency) {
        this.pnLAccountCurrency = pnLAccountCurrency;
    }

    public double getPnLClientCurrency() {
        return pnLClientCurrency;
    }

    public void setPnLClientCurrency(double pnLClientCurrency) {
        this.pnLClientCurrency = pnLClientCurrency;
    }
}