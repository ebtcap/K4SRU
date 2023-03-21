package com.ebtcap.sru;

public class SRUInfo {
    private String orgNummer;
    private String fullName;
    private String adress;
    private String postNr;
    private String postOrt;
    private String epost;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOrgNummer() {
        return orgNummer;
    }

    public void setOrgNummer(String orgNummer) {
        this.orgNummer = orgNummer;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPostNr() {
        return postNr;
    }

    public void setPostNr(String postNr) {
        this.postNr = postNr;
    }

    public String getPostOrt() {
        return postOrt;
    }

    public void setPostOrt(String postOrt) {
        this.postOrt = postOrt;
    }

    public String getEpost() {
        return epost;
    }

    public void setEpost(String epost) {
        this.epost = epost;
    }

    @Override
    public String toString() {
        return "SRUInfo{" +
                "orgNummer='" + orgNummer + '\'' +
                ", fullName='" + fullName + '\'' +
                ", adress='" + adress + '\'' +
                ", postNr='" + postNr + '\'' +
                ", postOrt='" + postOrt + '\'' +
                ", epost='" + epost + '\'' +
                '}';
    }
}
