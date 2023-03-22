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
- Personliga uppgifter i indata.properties. Egentligen är det bara personnummer som är viktigt här.
- CSV med transaktoner från Avanza, Pareto och Interactive Brokers
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

OBS! Använder du Excel för att fylla i omkostnadsbelopp manuellt i skatteverkets tjänst så kontrollera även försäljningsvärde. Det händer att mäklare beräknar beloppet i SEK på annan växelkurs än i uppgiften som de skickar till skatteverket. Skriv i så fall över värdet som är förifyllt.

# Status 2023-03-21
Det här funkade förra året. Men i år får jag det tyvärr inte att funka. Skatteverkets tjänst kraschar när BLANKETTER.SRU läses in vid deklarationen.... Oklart var felet ligger. Tillfälligt fel? Jag tror dock att den genererade SRU-filen är korrekt. Laddar man upp en (annan) felaktig fil så ger deras tjänst ett tydligt felmeddelande. 


# Kontakt
https://twitter.com/ebtCap
