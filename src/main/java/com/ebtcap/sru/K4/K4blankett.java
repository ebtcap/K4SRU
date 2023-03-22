package com.ebtcap.sru.K4;

import java.util.Date;
import java.util.List;

/**

 */
public class K4blankett {
    private int K4Nummer;
    private String identitetPersonnummer;
    private String namn;
    private Date identitetDatum;
    private String blankettId;

    private List<K4Rad> aktieRader;
    private K4Summa summaRadAktier;

    private List<K4Rad> valutaRader;
    private K4Summa summaRadValuta;

    public int getK4Nummer() {
        return K4Nummer;
    }

    public void setK4Nummer(int k4Nummer) {
        K4Nummer = k4Nummer;
    }

    public String getIdentitetPersonnummer() {
        return identitetPersonnummer;
    }

    public void setIdentitetPersonnummer(String identitetPersonnummer) {
        this.identitetPersonnummer = identitetPersonnummer;
    }

    public Date getIdentitetDatum() {
        return identitetDatum;
    }

    public void setIdentitetDatum(Date identitetDatum) {
        this.identitetDatum = identitetDatum;
    }

    public String getBlankettId() {
        return blankettId;
    }

    public void setBlankettId(String blankettId) {
        this.blankettId = blankettId;
    }

    public List<K4Rad> getAktieRader() {
        return aktieRader;
    }

    public void setAktieRader(List<K4Rad> aktieRader) {
        this.aktieRader = aktieRader;
    }

    public K4Summa getSummaRadAktier() {
        return summaRadAktier;
    }

    public void setSummaRadAktier(K4Summa summaRadAktier) {
        this.summaRadAktier = summaRadAktier;
    }

    public List<K4Rad> getValutaRader() {
        return valutaRader;
    }

    public void setValutaRader(List<K4Rad> valutaRader) {
        this.valutaRader = valutaRader;
    }

    public K4Summa getSummaRadValuta() {
        return summaRadValuta;
    }

    public void setSummaRadValuta(K4Summa summaRadValuta) {
        this.summaRadValuta = summaRadValuta;
    }

    public String getNamn() {
        return namn;
    }

    public void setNamn(String namn) {
        this.namn = namn;
    }

    @Override
    public String toString() {
        return "K4blankett{" +
                "K4Nummer=" + K4Nummer +
                ", identitetPersonnummer='" + identitetPersonnummer + '\'' +
                ", namn='" + namn + '\'' +
                ", identitetDatum=" + identitetDatum +
                ", blankettId='" + blankettId + '\'' +
                ", aktieRader=" + print(aktieRader) +
                ", summaRadAktier=" + summaRadAktier +
                ", valutaRader=" + print(valutaRader) +
                ", summaRadValuta=" + summaRadValuta +
                '}';
    }

    private String print(List<K4Rad> aktieRader) {
        if (aktieRader == null) {
            return "-";
        }
        StringBuilder string = new StringBuilder();
        for (K4Rad rad : aktieRader) {
            string.append(rad.toString());
        }
        return string.toString();
    }
}
