package com.ebtcap.sru.K4;

import com.ebtcap.sru.SRUInfo;
import com.ebtcap.sru.transactions.CurrencySale;
import com.ebtcap.sru.transactions.FinancialYear;
import com.ebtcap.sru.transactions.SecuritySale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class K4blankettService {
    protected static final Logger logger = LogManager.getLogger();

    private static final int SALES_PER_PAGE = 9;
    private static final int VALUTA_PER_PAGE = 7;

    public static List<K4blankett> createFromFinancialYear(FinancialYear year, SRUInfo sruInfo) {
        final String personNrId = sruInfo.getOrgNummer();
        List<K4blankett> k4blankettList = new ArrayList<>();
        Date createDate = new Date();
        int page = 1;
        while (year.getSecuritySales().size() > (page -1) * SALES_PER_PAGE) {
            K4blankett kPage = createK4Page(year, page, personNrId, createDate);

            logger.info(kPage);

            k4blankettList.add(kPage);
            page++;
        }
        return k4blankettList;
    }


    /**
     * Första sidan innehåller valuta-handel
     * @param year stateObject
     * @param personNrId personnummer
     * @return blanketten
     */
    private static K4blankett createK4Page(final FinancialYear year, int page, final String personNrId, Date createDate) {
        K4blankett k4blankett = new K4blankett();
        k4blankett.setBlankettId("K4-" + year.getYear() +"P4");
        k4blankett.setIdentitetDatum(createDate);
        k4blankett.setK4Nummer(page);
        k4blankett.setIdentitetPersonnummer(personNrId);

        List<SecuritySale> equitySales = new ArrayList<>(year.getSecuritySales().values());
        List<CurrencySale> currencySales = new ArrayList<>(year.getCurrencySales().values());

        if ((page -1) * SALES_PER_PAGE < equitySales.size()) {
            int start = (page -1) * SALES_PER_PAGE;
            int end = Math.min(SALES_PER_PAGE + start, equitySales.size());
            List<SecuritySale> eqSecuritySalesSubSet = equitySales.subList(start, end);
            k4blankett.setAktieRader(createAktieRader(eqSecuritySalesSubSet));
            k4blankett.setSummaRadAktier(createSumAktier(eqSecuritySalesSubSet));
        }
        if (currencySales.size() > VALUTA_PER_PAGE) {
            throw new RuntimeException("Ej implementerat! För många valutor");
        }
        if (page ==  1 && currencySales.size() > 0) {
            k4blankett.setValutaRader(createValutaRader(currencySales));
            k4blankett.setSummaRadValuta(createSumValuta(currencySales));
        }

        return k4blankett;
    }

    private static List<K4Rad> createAktieRader(List<SecuritySale> eqSecuritySalesSubSet) {
        List<K4Rad> k4RadList = new ArrayList<>();
        for (SecuritySale securitySale : eqSecuritySalesSubSet) {
            k4RadList.add(makeK4Rad(securitySale));
        }
        return k4RadList;
    }

    private static K4Rad makeK4Rad(SecuritySale securitySale) {
        K4Rad k4Rad = new K4Rad();
        k4Rad.setAntal(Math.round(securitySale.getAmount()));
        k4Rad.setBeteckning(securitySale.getName());
        k4Rad.setForsaljningspris(Math.round(securitySale.getSalePriceSEK().getNumber().doubleValue()));
        k4Rad.setOmkostnadsbelopp(Math.round(securitySale.getCostPriceSEK().getNumber().doubleValue()));
        return k4Rad;
    }

    private static K4Summa createSumAktier(List<SecuritySale> eqSecuritySalesSubSet) {
        K4Summa summa = new K4Summa();
        for (SecuritySale securitySale : eqSecuritySalesSubSet) {
            K4Rad k4Rad = makeK4Rad(securitySale);
            summa.setOmkostnadsbelopp(summa.getOmkostnadsbelopp() + k4Rad.getOmkostnadsbelopp());
            summa.setForsaljningspris(summa.getForsaljningspris() + k4Rad.getForsaljningspris());
            summa.setVinst(summa.getVinst() + k4Rad.getVinst());
            summa.setForlust(summa.getForlust() + k4Rad.getForlust());
        }
        return summa;
    }

    private static List<K4Rad> createValutaRader(List<CurrencySale> currencySales) {
        List<K4Rad> k4RadList = new ArrayList<>();
        for (CurrencySale currencySale : currencySales) {
            k4RadList.add(makeK4RadFromCurrency(currencySale));
        }
        return k4RadList;
    }

    private static K4Rad makeK4RadFromCurrency(CurrencySale currencySale) {
        K4Rad k4Rad = new K4Rad();
        k4Rad.setAntal(Math.round(currencySale.getSoldCurrency().getNumber().doubleValue()));
        k4Rad.setBeteckning(currencySale.getSoldCurrency().getCurrency().getCurrencyCode());
        k4Rad.setForsaljningspris(Math.round(currencySale.getReceivedCurrencySEK().getNumber().doubleValue()));
        k4Rad.setOmkostnadsbelopp(Math.round(currencySale.getCostCurrencySEK().getNumber().doubleValue()));
        return k4Rad;
    }

    private static K4Summa createSumValuta(List<CurrencySale> currencySales) {
        K4Summa summa = new K4Summa();
        for (CurrencySale currencySale : currencySales) {
            K4Rad k4Rad = makeK4RadFromCurrency(currencySale);
            summa.setOmkostnadsbelopp(summa.getOmkostnadsbelopp() + k4Rad.getOmkostnadsbelopp());
            summa.setForsaljningspris(summa.getForsaljningspris() + k4Rad.getForsaljningspris());
            summa.setVinst(summa.getVinst() + k4Rad.getVinst());
            summa.setForlust(summa.getForlust() + k4Rad.getForlust());
        }
        return summa;
    }
}
