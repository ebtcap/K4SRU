# SRU Maker

Det här programmet genererar K4 SRU-fil som kan laddas upp till skatteverket. Programmet skapar även  Excel-fil så att resultatet kan granskas.
Programmet hanterar valutakonton och räknar om enligt skatteverkets regler. 

I Skatteverkets delarationstjänst kan du importera filen på två ställen i e-tjänsten. Antingen direkt i menyvalet Bilagor, eller från den bilaga som har skapats åt dig. Välj BLANKETTER.SRU som du skapat med detta program. Klicka på Importera. Granska sedan resultatet innan du godkänner

## Bygg
- Hämta ner och installera
  - Java 17 (eller senare)
  - Maven ( https://maven.apache.org/download.cgi)
- Bygg genom att köra mvn package


## Indata
Redigera alla filer som börjar på indata*
- Personliga uppgifter i indata.properties.
- CSV med transaktoner från Avanza, Pareto och Interactive Brokers. Använd den/de som är relavanta för dig. Ta bort den/de som är orelevanta.
- Ingående saldo för aktier och valutor med inköpsvärde.

OBS, granska csv manuellt! T.ex. ska inte fonder (inklusive ETF:er) vara med. Dvs. ska ej redovisas via K4.

## Kör programmet
Starta programmet genom att köra
java com.ebtcap.sru.SruMaker 2022 true
- Det första argumentet är året
- Det andra är true/false om transaktionerna ska slås samman eller inte. Slå samman transaktionerna minskar antalet rader att deklarera.

Programmet läser in CSV-filerna och skapar följande utdata-filer
- BLANKETTER.SRU. Alla K4 blanketterna. Kan laddas upp på deklarationssidan import.
- utdata_K4_2022.xlsx. Innehåller alla K4-uppgifter.
- utadata_aktier_2022.xlsx och utdata_valuta_2022.xlsx. Innehåller utgående innehav i aktier och valuta. Dessa kan sedan användas som indata nästa år.

Programmet använder en genonsnittskurs för året för valutatransaktioner. Varje transaktion i en valuta som inte är SEK genererar en valutatransation också. T.ex. om du har sålt en aktie och fått betalt i dollar till ditt valutakonto.

# Kontakt
https://twitter.com/ebtCap
