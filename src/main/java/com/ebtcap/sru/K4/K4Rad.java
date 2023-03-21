package com.ebtcap.sru.K4;

public class K4Rad {
    private long antal;
    private String beteckning;
    private long forsaljningspris;
    private long omkostnadsbelopp;

    public K4Rad() {
        antal = 0;
        forsaljningspris = 0;
        omkostnadsbelopp = 0;
    }

    public long getVinst() {
        if (forsaljningspris > omkostnadsbelopp) {
            return forsaljningspris - omkostnadsbelopp;
        }
        return 0;
    }

    public long getForlust() {
        if (forsaljningspris < omkostnadsbelopp) {
            return  omkostnadsbelopp - forsaljningspris;
        }
        return 0;
    }

    public long getForsaljningspris() {
        return forsaljningspris;
    }

    public void setForsaljningspris(long forsaljningspris) {
        this.forsaljningspris = forsaljningspris;
    }

    public long getAntal() {
        return antal;
    }

    public void setAntal(long antal) {
        this.antal = antal;
    }

    public String getBeteckning() {
        return beteckning;
    }

    public void setBeteckning(String beteckning) {
        this.beteckning = beteckning;
    }

    public long getOmkostnadsbelopp() {
        return omkostnadsbelopp;
    }

    public void setOmkostnadsbelopp(long omkostnadsbelopp) {
        this.omkostnadsbelopp = omkostnadsbelopp;
    }

    @Override
    public String toString() {
        return "K4Rad{" +
                "antal=" + antal +
                ", beteckning='" + beteckning + '\'' +
                ", forsaljningspris=" + forsaljningspris +
                ", omkostnadsbelopp=" + omkostnadsbelopp +
                ", vinst=" + getVinst() +
                ", förlust=" + getForlust() +
                '}';
    }
}
