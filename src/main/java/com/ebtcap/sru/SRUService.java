package com.ebtcap.sru;

import com.ebtcap.sru.K4.K4Rad;
import com.ebtcap.sru.K4.K4Summa;
import com.ebtcap.sru.K4.K4blankett;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SRUService {
    protected static final Logger logger = LogManager.getLogger();

    public static SRUFiles createSRU(SRUInfo sruInfo, List<K4blankett> k4blankettList) {

        SRUFiles sruFiles = new SRUFiles();
        List<String> sruBlankettStrings = generateBlanketterSRU(k4blankettList);
        List<String> sruInfoStrings = genereateInfoSRU(sruInfo);
        sruFiles.setSruBlankett(sruBlankettStrings);
        sruFiles.setSruInfo(sruInfoStrings);

        return sruFiles;
    }

    public static List<String> genereateInfoSRU(SRUInfo sruInfo) {
        List<String> sb = new ArrayList<>();
        sb.add("#DATABESKRIVNING_START");
        sb.add("#PRODUKT SRU");
        sb.add("#FILNAMN BLANKETTER.SRU");
        sb.add("#DATABESKRIVNING_SLUT");
        sb.add("#MEDIELEV_START");
        sb.add("#ORGNR " + sruInfo.getOrgNummer());
        sb.add("#NAMN " + sruInfo.getFullName());
        sb.add("#ADRESS " + sruInfo.getAdress());
        sb.add("#POSTNR " + sruInfo.getPostNr());
        sb.add("#POSTORT " + sruInfo.getPostOrt());
        sb.add("#EMAIL " + sruInfo.getEpost());
        sb.add("#MEDIELEV_SLUT");
        return sb;
    }

    public static List<String> generateBlanketterSRU(List<K4blankett> k4blankettList) {
        List<String> sb = new ArrayList<>();
        String pattern = "yyyyMMdd HHmmss";

        for (K4blankett blankett : k4blankettList) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(blankett.getIdentitetDatum());

            sb.add("#BLANKETT " + blankett.getBlankettId());
            sb.add("#IDENTITET "+ blankett.getIdentitetPersonnummer() + " " + date + "");
            sb.add("#NAMN " + blankett.getNamn());
            addAktieRows(sb, blankett.getAktieRader(), blankett.getSummaRadAktier());
            addValutaRows(sb, blankett.getValutaRader(), blankett.getSummaRadValuta());
            sb.add("#UPPGIFT 7014 " + blankett.getK4Nummer());
            sb.add("#BLANKETTSLUT");
        }
        sb.add("#FIL_SLUT");
        return sb;
    }

    private static void addValutaRows(List<String> sb, List<K4Rad> valutaRader, K4Summa summaRadValuta) {
        if (valutaRader == null || valutaRader.size() == 0) {
            return;
        }
        int counter = 31;

        if (valutaRader.size() > 7) {
            throw new RuntimeException("För många rader!");
        }
        for (K4Rad rad : valutaRader) {
            sb.add("#UPPGIFT 3" + counter + "0 " + rad.getAntal() + "");
            sb.add("#UPPGIFT 3" + counter + "1 " + rad.getBeteckning() + "");
            sb.add("#UPPGIFT 3" + counter + "2 " + rad.getForsaljningspris() + "");
            sb.add("#UPPGIFT 3" + counter + "3 " + rad.getOmkostnadsbelopp() + "");
            sb.add("#UPPGIFT 3" + counter + "4 " + rad.getVinst() + "");
            sb.add("#UPPGIFT 3" + counter + "5 " + rad.getForlust() + "");
            counter++;
        }
        if (summaRadValuta != null) {
            sb.add("#UPPGIFT 3400 " + summaRadValuta.getForsaljningspris() + "");
            sb.add("#UPPGIFT 3401 " + summaRadValuta.getOmkostnadsbelopp() + "");
            sb.add("#UPPGIFT 3403 " + summaRadValuta.getVinst() + "");
            sb.add("#UPPGIFT 3404 " + summaRadValuta.getForlust() + "");
        }
    }

    private static void addAktieRows(List<String> sb, List<K4Rad> aktieRader, K4Summa summaRadAktier) {
        int counter = 10;
        if (aktieRader == null || aktieRader.size() == 0) {
            return;
        }
        if (aktieRader.size() > 9) {
            throw new RuntimeException("För många rader!");
        }
        for (K4Rad rad : aktieRader) {
            sb.add("#UPPGIFT 3" + counter + "0 " + rad.getAntal() + "");
            sb.add("#UPPGIFT 3" + counter + "1 " + rad.getBeteckning() + "");
            sb.add("#UPPGIFT 3" + counter + "2 " + rad.getForsaljningspris() + "");
            sb.add("#UPPGIFT 3" + counter + "3 " + rad.getOmkostnadsbelopp() + "");
            sb.add("#UPPGIFT 3" + counter + "4 " + rad.getVinst() + "");
            sb.add("#UPPGIFT 3" + counter + "5 " + rad.getForlust() + "");
            counter++;
        }
        if (summaRadAktier != null) {
            sb.add("#UPPGIFT 3300 " + summaRadAktier.getForsaljningspris() + "");
            sb.add("#UPPGIFT 3301 " + summaRadAktier.getOmkostnadsbelopp() + "");
            sb.add("#UPPGIFT 3304 " + summaRadAktier.getVinst() + "");
            sb.add("#UPPGIFT 3305 " + summaRadAktier.getForlust() + "");
        }
    }

    public static void writeToDisk(SRUFiles sruFiles) {
        writeFile("BLANKETTER.SRU", sruFiles.getSruBlankett());
        writeFile("INFO.SRU", sruFiles.getSruInfo());
    }

    public static void writeFile(String fileName, List<String> lines) {

        try (FileWriter fw = new FileWriter(fileName, StandardCharsets.ISO_8859_1);
             BufferedWriter writer = new BufferedWriter(fw)) {

            for (String line : lines) {
                writer.append(line);
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SRUInfo setupSruInfo() {

        SRUInfo sruInfo = new SRUInfo();
        final String fileName = "indata.properties";
        logger.info("Reading properties from " + fileName);
        try (InputStream input = new FileInputStream(fileName)) {
            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            sruInfo.setOrgNummer(prop.getProperty("info.personnummer"));
            sruInfo.setAdress(prop.getProperty("info.adress"));
            sruInfo.setPostNr(prop.getProperty("info.postnummer"));
            sruInfo.setPostOrt(prop.getProperty("info.postort"));
            sruInfo.setFullName(prop.getProperty("info.namn"));
            sruInfo.setEpost(prop.getProperty("info.epost"));

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        logger.info(sruInfo);

        if (sruInfo.getOrgNummer() == null || sruInfo.getOrgNummer().length() != 12) {
            throw new RuntimeException("Invalid personnummer length. Should be 12 digits");
        }

        if (sruInfo.getFullName() == null || sruInfo.getFullName().length() < 2) {
            throw new RuntimeException("Name is missing");
        }

        if (sruInfo.getPostNr() == null || sruInfo.getPostNr().length() != 5) {
            throw new RuntimeException("Invalid postnummer length. Should be 5 digits");
        }

        return sruInfo;
    }
}
