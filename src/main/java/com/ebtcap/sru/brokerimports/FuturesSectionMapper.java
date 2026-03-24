package com.ebtcap.sru.brokerimports;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Maps futures instruments to SRU K4 sections:
 * - Section A: Equity-related futures (equity indices)
 * - Section C: Fixed income and currency futures
 * - Section D: Commodities (energy, metals, agricultural, softs)
 */
public class FuturesSectionMapper {

    private static final Map<Pattern, String> INSTRUMENT_PATTERNS = new HashMap<>();

    static {
        // Section A: Equity indices
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*E-mini.*Dow.*"), "A");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*E-mini.*S&P.*"), "A");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*E-mini.*Nasdaq.*"), "A");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*E-mini.*Russell.*"), "A");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Micro E-mini.*"), "A");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*STOXX.*"), "A");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*DAX.*"), "A");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*FTSE.*"), "A");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Nikkei.*"), "A");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*MSCI.*Emerging.*"), "A");

        // Section C: Fixed income (bonds, notes, bills)
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Treasury.*Note.*"), "C");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Treasury.*Bond.*"), "C");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*T-Note.*"), "C");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*T-Bond.*"), "C");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Ultra.*Bond.*"), "C");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Bund.*"), "C");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Euribor.*"), "C");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*SOFR.*"), "C");

        // Section C: Currency futures (if needed in the future)
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Euro.*FX.*"), "C");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*British.*Pound.*"), "C");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Japanese.*Yen.*"), "C");

        // Section D: Energy
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Crude.*Oil.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*WTI.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Brent.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Natural.*Gas.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Heating.*Oil.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*ULSD.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Gasoline.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*RBOB.*"), "D");

        // Section D: Precious metals
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Gold.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Silver.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Platinum.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Palladium.*"), "D");

        // Section D: Base metals
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Copper.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Aluminum.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Zinc.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Nickel.*"), "D");

        // Section D: Agricultural - Grains
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Wheat.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Corn.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Soybean.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Soybean.*Oil.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Soybean.*Meal.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Oats.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Rice.*"), "D");

        // Section D: Agricultural - Softs
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Coffee.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Sugar.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Cocoa.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Cotton.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Orange.*Juice.*"), "D");

        // Section D: Livestock
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Live.*Cattle.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Feeder.*Cattle.*"), "D");
        INSTRUMENT_PATTERNS.put(Pattern.compile("(?i).*Lean.*Hog.*"), "D");
    }

    /**
     * Determines the K4 section for a futures instrument.
     * 
     * @param instrumentDescription The description of the futures instrument
     * @return "A" for equity indices, "C" for fixed income/currency, "D" for commodities, or "D" as default
     */
    public static String getSection(String instrumentDescription) {
        if (instrumentDescription == null || instrumentDescription.trim().isEmpty()) {
            return "D"; // Default to Section D for unknown instruments
        }

        // Check each pattern
        for (Map.Entry<Pattern, String> entry : INSTRUMENT_PATTERNS.entrySet()) {
            if (entry.getKey().matcher(instrumentDescription).matches()) {
                return entry.getValue();
            }
        }

        // Default to Section D (other derivatives)
        return "D";
    }
}
