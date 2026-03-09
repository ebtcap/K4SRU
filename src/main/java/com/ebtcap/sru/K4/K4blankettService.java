package com.ebtcap.sru.K4;

import com.ebtcap.sru.SRUInfo;
import com.ebtcap.sru.transactions.CurrencySale;
import com.ebtcap.sru.transactions.FinancialYear;
import com.ebtcap.sru.transactions.SecuritySale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class K4blankettService {
    protected static final Logger logger = LogManager.getLogger();

    private static final int SALES_PER_PAGE = 9;
    private static final int VALUTA_PER_PAGE = 7;
    private static final int DERIVAT_PER_PAGE = 7; // Section D: max 7 rows per page

    public static List<K4blankett> createFromFinancialYear(FinancialYear year, SRUInfo sruInfo) {
        final String personNrId = sruInfo.getOrgNummer();
        List<K4blankett> k4blankettList = new ArrayList<>();
        Date createDate = new Date();
        
        // Separate securities by section
        List<SecuritySale> sectionASales = new ArrayList<>();
        List<SecuritySale> sectionCSales = new ArrayList<>();
        List<SecuritySale> sectionDSales = new ArrayList<>();
        
        for (SecuritySale sale : year.getSecuritySales().values()) {
            String section = sale.getSection();
            if ("C".equals(section)) {
                sectionCSales.add(sale);
            } else if ("D".equals(section)) {
                sectionDSales.add(sale);
            } else {
                // Default to Section A (equities)
                sectionASales.add(sale);
            }
        }
        
        logger.info("Securities by section - A: {}, C: {}, D: {}", 
            sectionASales.size(), sectionCSales.size(), sectionDSales.size());
        
        // Calculate total pages needed (based on the maximum items per page)
        int pagesForA = (int) Math.ceil((double) sectionASales.size() / SALES_PER_PAGE);
        int pagesForD = (int) Math.ceil((double) sectionDSales.size() / DERIVAT_PER_PAGE);
        int totalPages = Math.max(1, Math.max(pagesForA, pagesForD));
        
        for (int page = 1; page <= totalPages; page++) {
            K4blankett kPage = createK4Page(year, page, personNrId, sruInfo.getFullName(), createDate,
                    sectionASales, sectionCSales, sectionDSales);

            logger.info(kPage);

            k4blankettList.add(kPage);
        }
        return k4blankettList;
    }


    /**
     * Creates a K4 page with sections A, C, and D
     * @param year stateObject
     * @param page page number
     * @param personNrId personnummer
     * @param namn name
     * @param createDate creation date
     * @param sectionASales Section A sales (equities/indices)
     * @param sectionCSales Section C sales (bonds/currency - currently only from currency trades)
     * @param sectionDSales Section D sales (commodities/derivatives)
     * @return blanketten
     */
    private static K4blankett createK4Page(final FinancialYear year, int page, final String personNrId, final String namn,
                                           Date createDate, List<SecuritySale> sectionASales, 
                                           List<SecuritySale> sectionCSales, List<SecuritySale> sectionDSales) {
        K4blankett k4blankett = new K4blankett();
        k4blankett.setBlankettId("K4-" + year.getYear() +"P4");
        k4blankett.setIdentitetDatum(createDate);
        k4blankett.setK4Nummer(page);
        k4blankett.setIdentitetPersonnummer(personNrId);
        k4blankett.setNamn(namn);

        List<CurrencySale> currencySales = new ArrayList<>(year.getCurrencySales().values());

        // Section A: Equities and equity indices
        if ((page - 1) * SALES_PER_PAGE < sectionASales.size()) {
            int start = (page - 1) * SALES_PER_PAGE;
            int end = Math.min(SALES_PER_PAGE + start, sectionASales.size());
            List<SecuritySale> subset = sectionASales.subList(start, end);
            k4blankett.setAktieRader(createSecurityRader(subset));
            k4blankett.setSummaRadAktier(createSumSecurity(subset));
        } else {
            k4blankett.setAktieRader(Collections.emptyList());
        }
        
        // Section C: Currency sales (from currency trading) - only on first page
        // Section C futures (bonds) from sectionCSales would also go here
        if (page == 1) {
            List<K4Rad> sectionCRader = new ArrayList<>();
            K4Summa sectionCSumma = new K4Summa();
            
            // Add currency sales
            if (currencySales.size() > 0) {
                if (currencySales.size() > VALUTA_PER_PAGE) {
                    throw new RuntimeException("Ej implementerat! För många valutor");
                }
                sectionCRader.addAll(createValutaRader(currencySales));
                addToSumma(sectionCSumma, createSumValuta(currencySales));
            }
            
            // Add Section C securities (fixed income futures)
            if (sectionCSales.size() > 0) {
                int availableSlots = VALUTA_PER_PAGE - currencySales.size();
                if (sectionCSales.size() > availableSlots) {
                    logger.warn("Too many Section C items (currency + bonds/notes). Max {} per page. Have {} currency + {} bonds. Will create multiple pages.",
                        VALUTA_PER_PAGE, currencySales.size(), sectionCSales.size());
                    // Take only what fits on first page
                    List<SecuritySale> sectionCSalesPage1 = sectionCSales.subList(0, Math.min(availableSlots, sectionCSales.size()));
                    sectionCRader.addAll(createSecurityRader(sectionCSalesPage1));
                    addToSumma(sectionCSumma, createSumSecurity(sectionCSalesPage1));
                } else {
                    sectionCRader.addAll(createSecurityRader(sectionCSales));
                    addToSumma(sectionCSumma, createSumSecurity(sectionCSales));
                }
            }
            
            k4blankett.setValutaRader(sectionCRader);
            k4blankett.setSummaRadValuta(sectionCSumma);
        } else {
            k4blankett.setValutaRader(Collections.emptyList());
        }
        
        // Section D: Derivatives/Commodities - can span multiple pages
        if ((page - 1) * DERIVAT_PER_PAGE < sectionDSales.size()) {
            int start = (page - 1) * DERIVAT_PER_PAGE;
            int end = Math.min(DERIVAT_PER_PAGE + start, sectionDSales.size());
            List<SecuritySale> sectionDSalesPage = sectionDSales.subList(start, end);
            k4blankett.setDerivatRader(createSecurityRader(sectionDSalesPage));
            k4blankett.setSummaRadDerivat(createSumSecurity(sectionDSalesPage));
        } else {
            k4blankett.setDerivatRader(Collections.emptyList());
        }

        return k4blankett;
    }
    
    /**
     * Helper to add one K4Summa to another
     */
    private static void addToSumma(K4Summa target, K4Summa source) {
        if (source != null) {
            target.setForsaljningspris(target.getForsaljningspris() + source.getForsaljningspris());
            target.setOmkostnadsbelopp(target.getOmkostnadsbelopp() + source.getOmkostnadsbelopp());
            target.setVinst(target.getVinst() + source.getVinst());
            target.setForlust(target.getForlust() + source.getForlust());
        }
    }

    private static List<K4Rad> createSecurityRader(List<SecuritySale> securitySales) {
        List<K4Rad> k4RadList = new ArrayList<>();
        for (SecuritySale securitySale : securitySales) {
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

    private static K4Summa createSumSecurity(List<SecuritySale> securitySales) {
        K4Summa summa = new K4Summa();
        for (SecuritySale securitySale : securitySales) {
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
