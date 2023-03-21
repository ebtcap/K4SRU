package com.ebtcap.sru.brokerimports;

/**
 * ISIN,
 * Affärsdag,
 * Likviddag,
 * Ticker,
 * Transaktionstyp,
 * Antal,
 * Kurs,
 * Belopp,
 * Valuta,
 * Avräkningsnota,
 * Totalt,
 * Courtage,
 * exportToCsv_contractnotes_header_valRate,
 * exportToCsv_contractnotes_header_documentId,
 * exportToCsv_contractnotes_header_downloadHash
 */
public class ParetoTrade {
    private String ticker;
    private String transaktionstyp;
    private double antal;
    private float kurs;
    private double belopp;
    private String valuta;
    private double totalt;
    private double courtage;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getTransaktionstyp() {
        return transaktionstyp;
    }

    public void setTransaktionstyp(String transaktionstyp) {
        this.transaktionstyp = transaktionstyp;
    }

    public double getAntal() {
        return antal;
    }

    public void setAntal(double antal) {
        this.antal = antal;
    }

    public float getKurs() {
        return kurs;
    }

    public void setKurs(float kurs) {
        this.kurs = kurs;
    }

    public double getBelopp() {
        return belopp;
    }

    public void setBelopp(double belopp) {
        this.belopp = belopp;
    }

    public String getValuta() {
        return valuta;
    }

    public void setValuta(String valuta) {
        this.valuta = valuta;
    }

    public double getTotalt() {
        return totalt;
    }

    public void setTotalt(double totalt) {
        this.totalt = totalt;
    }

    public double getCourtage() {
        return courtage;
    }

    public void setCourtage(double courtage) {
        this.courtage = courtage;
    }

    @Override
    public String toString() {
        return "ParetoTrade{" +
                "ticker='" + ticker + '\'' +
                ", transaktionstyp='" + transaktionstyp + '\'' +
                ", antal=" + antal +
                ", kurs=" + kurs +
                ", belopp=" + belopp +
                ", valuta='" + valuta + '\'' +
                ", totalt=" + totalt +
                ", courtage=" + courtage +
                '}';
    }
}
