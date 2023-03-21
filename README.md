# SRU Maker

Det här programmet genererar K4 SRU-filer som kan laddas upp till skatteverket. Programmet skapar även  Excel-fil så att resultatet kan granskas.
Programmet hanterar valutakonton och räknar om enligt skatteverkets regler. Den hanterar även blankning.

## Bygg
- Hämta ner 
  - Java 17
  - Maven
- Bygg genom att köra mvn package


## Indata
Redigera alla filer som börjar på indata*
- Personliga uppgifter i indata.properties
- CSV med transaktoner från Avanza, Pareto och Interactive Brokers
- Ingående saldo för aktier och valutor med inköpsvärde.

## Kör programmet
Starta programmet genom att köra
java com.ebtcap.sru.SruMaker

Programmet läser in CSV-filerna och skapar INFO.SRU och BLANKETTER.SRU. Det skapas även en Excel med K4 informationen. Dessutom skrivs det ut 
två excel-filer som innehåller utgående innehav i aktier och valuta. Dessa kan sedan användas som indata nästa år.

# Status 2023-03-21
Får det tyvärr inte att funka. Skatteverkets tjänst kraschar när BLANKETTER.SRU läses in vid deklarationen.... Oklart var felet ligger.