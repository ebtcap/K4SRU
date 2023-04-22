package com.ebtcap.sru.transactions;

import com.ebtcap.sru.K4.K4blankett;
import com.ebtcap.sru.K4.K4blankettService;
import com.ebtcap.sru.K4.SRUValidator;
import com.ebtcap.sru.SRUFiles;
import com.ebtcap.sru.SRUInfo;
import com.ebtcap.sru.SRUService;
import org.javamoney.moneta.Money;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class FinancialYearServiceTest {

    @org.junit.jupiter.api.Test
    void addInitialEquityHolding() {
        FinancialYear financialYear = setupNewFinancialYear();


        assertEquals(2, financialYear.getEquities().size());
        assertEquals(150d, financialYear.getEquities().get("Volvo").getAmount());
        assertEquals("SEK 175.00", financialYear.getEquities().get("Volvo").getCostInSEK().toString());
    }

    @org.junit.jupiter.api.Test
    void addBuyEquity() {
        FinancialYear financialYear = setupNewFinancialYear();
        assertEquals("USD 1000.00", financialYear.getCurrencies().get("USD").getMoney().toString());

        FinancialYearService.addBuyEquity(financialYear,"Microsoft",10, Money.of(400,"USD"));
        assertEquals("USD 600.00", financialYear.getCurrencies().get("USD").getMoney().toString());
        assertEquals(10, financialYear.getEquities().get("Microsoft").getAmount());
        assertEquals("SEK 3616.00", financialYear.getEquities().get("Microsoft").getCostInSEK().toString());

        assertEquals("USD 400.00", financialYear.getCurrencySales().get("USD").getSoldCurrency().toString());
        assertEquals("SEK 3600.00", financialYear.getCurrencySales().get("USD").getCostCurrencySEK().toString());
        assertEquals("SEK 3616.00", financialYear.getCurrencySales().get("USD").getReceivedCurrencySEK().toString());

        FinancialYearService.addSellEquity(financialYear,"Microsoft",3, Money.of(100,"USD"));
        assertEquals(1, financialYear.getSecuritySales().size());
        assertEquals(7, financialYear.getEquities().get("Microsoft").getAmount());
        assertEquals("SEK 1084.80", financialYear.getSecuritySales().get("Microsoft").getCostPriceSEK().toString());
        assertEquals("USD 700.00", financialYear.getCurrencies().get("USD").getMoney().toString());

        FinancialYearService.addSellEquity(financialYear,"Microsoft",3, Money.of(100,"USD"));
        assertEquals(1, financialYear.getSecuritySales().size());
        assertEquals(4, financialYear.getEquities().get("Microsoft").getAmount());
        assertEquals("USD 800.00", financialYear.getCurrencies().get("USD").getMoney().toString());

        FinancialYearService.addSellEquity(financialYear,"Microsoft",4, Money.of(205,"USD"));
        assertEquals(10, financialYear.getSecuritySales().get("Microsoft").getAmount());
        assertEquals("SEK 3661.20", financialYear.getSecuritySales().get("Microsoft").getSalePriceSEK().toString());
        assertEquals("SEK 3616.00", financialYear.getSecuritySales().get("Microsoft").getCostPriceSEK().toString());
        assertEquals("SEK 45.20", financialYear.getSecuritySales().get("Microsoft").getProfit().toString());
        assertEquals("SEK 16.00", financialYear.getCurrencySales().get("USD").getProfit().toString());

    }

    /**
     * Super simple basic test
     */
    @org.junit.jupiter.api.Test
    void addBuyEquity2() {
        FinancialYear financialYear = setupEmptyFinanscialYear();

        FinancialYearService.addBuyEquity(financialYear,"Nordea",120, Money.of(6000,"SEK"));
        FinancialYearService.addSellEquity(financialYear,"Nordea",50, Money.of(3750,"SEK"));
        assertEquals(1, financialYear.getSecuritySales().size());
        assertEquals(70, financialYear.getEquities().get("Nordea").getAmount());
        assertEquals("SEK 2500.00", financialYear.getSecuritySales().get("Nordea").getCostPriceSEK().toString());
        assertEquals("SEK 3750.00", financialYear.getSecuritySales().get("Nordea").getSalePriceSEK().toString());
        assertEquals("SEK 1250.00", financialYear.getSecuritySales().get("Nordea").getProfit().toString());

        SRUInfo sruInfo = setupSruInfoForTest();

        //Generate the actual files
        List<K4blankett> k4blankettList = K4blankettService.createFromFinancialYear(financialYear, sruInfo);

        SRUValidator.validate(k4blankettList);

        SRUFiles sruFiles = SRUService.createSRU(sruInfo, k4blankettList);
        assertEquals("#UPPGIFT 3304 1250", sruFiles.getSruBlankett().get(11));
    }

    private SRUInfo setupSruInfoForTest() {
        SRUInfo sruInfo = new SRUInfo();
        sruInfo.setOrgNummer("121212-1212");
        return sruInfo;
    }

    private FinancialYear setupNewFinancialYear() {
        Map<String, Double> currecySEKConversionRates = new TreeMap<>();

        currecySEKConversionRates.put("USD", 9.04);

        FinancialYear financialYear = new FinancialYear(2022, currecySEKConversionRates);
        financialYear.setMergeTransactions(true);
        FinancialYearService.addInitialEquityHolding(financialYear,"Volvo", 100d, Money.of(100,"SEK"));
        FinancialYearService.addInitialEquityHolding(financialYear,"Volvo", 50d, Money.of(75,"SEK"));
        FinancialYearService.addInitialEquityHolding(financialYear,"Nordea", 50d, Money.of(75,"SEK"));

        FinancialYearService.addInitialCurrencyHolding(financialYear, Money.of(1000,"USD"), Money.of(9000,"SEK"));

        return financialYear;
    }

    private FinancialYear setupEmptyFinanscialYear() {
        Map<String, Double> currecySEKConversionRates = new TreeMap<>();

        currecySEKConversionRates.put("USD", 10.0);

        FinancialYear financialYear = new FinancialYear(2022, currecySEKConversionRates);
        financialYear.setMergeTransactions(true);


        return financialYear;
    }

    private FinancialYear setupNewFinancialYear2() {
        Map<String, Double> currecySEKConversionRates = new TreeMap<>();

        currecySEKConversionRates.put("USD",9.04);

        FinancialYear financialYear = new FinancialYear(2022, currecySEKConversionRates);
        financialYear.setMergeTransactions(true);
        FinancialYearService.addInitialCurrencyHolding(financialYear, Money.of(5,"USD"), Money.of(50,"SEK"));

        return financialYear;
    }

    @org.junit.jupiter.api.Test
    void sellShort() {
        FinancialYear financialYear = setupNewFinancialYear();
        assertEquals("USD 1000.00", financialYear.getCurrencies().get("USD").getMoney().toString());


        FinancialYearService.addSellEquity(financialYear,"Microsoft",3, Money.of(100,"USD"));
        assertEquals(0, financialYear.getSecuritySales().size());

        FinancialYearService.addBuyEquity(financialYear,"Microsoft",3, Money.of(90,"USD"));
        assertEquals("SEK 813.60", financialYear.getSecuritySales().get("Microsoft").getCostPriceSEK().toString());
        assertEquals("USD 1010.00", financialYear.getCurrencies().get("USD").getMoney().toString());

        assertEquals(1, financialYear.getSecuritySales().size());
        assertNull(financialYear.getEquities().get("Microsoft"));
        assertEquals("USD 1010.00", financialYear.getCurrencies().get("USD").getMoney().toString());
        assertEquals("SEK 90.40", financialYear.getSecuritySales().get("Microsoft").getProfit().toString());
        assertEquals("SEK 3.27", financialYear.getCurrencySales().get("USD").getProfit().toString());

    }

    @org.junit.jupiter.api.Test
    void sellShortBorrow() {
        FinancialYear financialYear = setupNewFinancialYear2();
        assertEquals("USD 5.00", financialYear.getCurrencies().get("USD").getMoney().toString());


        FinancialYearService.addSellEquity(financialYear,"Microsoft",3, Money.of(100,"USD"));
        assertEquals(0, financialYear.getSecuritySales().size());
        assertEquals(-3, financialYear.getEquities().get("Microsoft").getAmount());
        assertEquals("USD 105.00", financialYear.getCurrencies().get("USD").getMoney().toString());



        FinancialYearService.addBuyEquity(financialYear,"Microsoft",3, Money.of(90,"USD"));
        assertEquals(1, financialYear.getSecuritySales().size());
        assertNull(financialYear.getEquities().get("Microsoft"));
        assertEquals("USD 15.00", financialYear.getCurrencies().get("USD").getMoney().toString());
        assertEquals("SEK 90.40", financialYear.getSecuritySales().get("Microsoft").getProfit().toString());
        assertEquals("SEK -4.11", financialYear.getCurrencySales().get("USD").getProfit().toString());

        FinancialYearService.addBuyEquity(financialYear,"Microsoft",3, Money.of(85,"USD"));
        FinancialYearService.addSellEquity(financialYear,"Microsoft",3, Money.of(90,"USD"));
        assertEquals("USD 20.00", financialYear.getCurrencies().get("USD").getMoney().toString());
        assertEquals("SEK 135.60", financialYear.getSecuritySales().get("Microsoft").getProfit().toString());
        assertEquals("SEK -4.80", financialYear.getCurrencySales().get("USD").getProfit().toString());
        assertNull(financialYear.getEquities().get("Microsoft"));

        FinancialYearService.addSellEquity(financialYear,"Microsoft",3, Money.of(90,"USD"));
        assertEquals(-3, financialYear.getEquities().get("Microsoft").getAmount());
        FinancialYearService.addBuyEquity(financialYear,"Microsoft",13, Money.of(390,"USD"));
        assertEquals(10, financialYear.getEquities().get("Microsoft").getAmount());
        FinancialYearService.addSellEquity(financialYear,"Microsoft",5, Money.of(160,"USD"));
        assertEquals(5, financialYear.getEquities().get("Microsoft").getAmount());
        assertEquals("USD -120.00", financialYear.getCurrencies().get("USD").getMoney().toString());
        assertEquals("SEK 226.00", financialYear.getSecuritySales().get("Microsoft").getProfit().toString());

    }
}