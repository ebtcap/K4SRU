package com.ebtcap.sru.K4;

import java.util.List;

public class SRUValidator {

    public static void validate(List<K4blankett> k4blankettList) {

        for (K4blankett k4blankett : k4blankettList) {
            for (K4Rad k4Rad : k4blankett.getAktieRader()) {
                validateK4Rad(k4Rad);
            }
            for (K4Rad k4Rad : k4blankett.getValutaRader()) {
                validateK4Rad(k4Rad);
            }
        }

    }

    private static void validateK4Rad(K4Rad k4Rad) {
        if (k4Rad.getBeteckning() == null || k4Rad.getBeteckning().length() == 0) {
            throw new RuntimeException("Betecking saknas på en rad." + k4Rad);
        }

        if (k4Rad.getBeteckning().length() > 80) {
            throw new RuntimeException("Betecking får inte vara över 80 tecken." + k4Rad);
        }

        if (k4Rad.getForsaljningspris() < 0) {
            throw new RuntimeException("Försäljningspris får inte vara negativt. " + k4Rad);
        }

        if (k4Rad.getOmkostnadsbelopp()< 0) {
            throw new RuntimeException("Omkostnadsbelopp får inte vara negativt. " + k4Rad);
        }
        if (k4Rad.getVinst() < 0) {
            throw new RuntimeException("Vinst-fältet får inte vara negativt. " + k4Rad);
        }
        if (k4Rad.getForlust() < 0) {
            throw new RuntimeException("Förlust-fältet får inte vara negativt. " + k4Rad);
        }

    }
}
