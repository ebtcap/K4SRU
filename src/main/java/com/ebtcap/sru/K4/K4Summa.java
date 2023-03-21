package com.ebtcap.sru.K4;

public class K4Summa extends K4Rad {
    private long vinst;
    private long forlust;

    @Override
    public long getVinst() {
        return vinst;
    }

    public void setVinst(long vinst) {
        this.vinst = vinst;
    }

    @Override
    public long getForlust() {
        return forlust;
    }

    public void setForlust(long forlust) {
        this.forlust = forlust;
    }

    @Override
    public String toString() {
        return "K4Rad{" +
                ", forsaljningspris=" + getForsaljningspris() +
                ", omkostnadsbelopp=" + getOmkostnadsbelopp() +
                ", vinst=" + vinst +
                ", förlust=" + forlust +
                '}';
    }
}
