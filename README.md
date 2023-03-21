# SRU Maker

Det här programmet genererar K4 SRU-filer som kan laddas upp till skatteverket. Programmet skapar även  Excel-fil så att resultatet kan granskas.
Programmet hanterar valutakonton och räknar om enligt skatteverkets regler. 

Skatteverket tillåter två sätt att deklarera
- Via deras webb där SRU-fil kan laddas upp. Dock finns en maxgräns på 300 rader. Alternativet att slå samman rader kan vara intressant för denna metod.
- Använda filuppladdningstjänsten. Det har jag inte provat. Det är vid detta alternativ du även behöver INFO.SRU. Jag är också lite osäker på om man kan lämna bara K4-bilagan och göra resten via deras webb. Eller om man då måste göra hela deklarationen via fil vilket inte det här programmet löser.

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

OBS, granska csv manuellt! T.ex. ska inte fonder (inklusive ETF:er) vara med. Dvs. ska ej redovisas via K4.

## Kör programmet
Starta programmet genom att köra
java com.ebtcap.sru.SruMaker 2022 false
- Det första argumentet är året
- Det andra är true/false om transaktionerna ska slås samman eller inte. Slå samman transaktionerna minskar antalet rader att deklarera.

Programmet läser in CSV-filerna och skapar följande utdata-filer
- INFO.SRU används bara vid filuppladdning
- BLANKETTER.SRU. Alla K4 blanketterna. Kan laddas upp på deklarationssidan import.
- utdata_K4_2022.xlsx. Innehåller alla K4-uppgifter.
- utadata_aktier_2022.xlsx och utdata_valuta_2022.xlsx. Innehåller utgående innehav i aktier och valuta. Dessa kan sedan användas som indata nästa år.

OBS! Använder du Excel för att fylla i omkostnadsbelopp manuellt i skatteverkets tjänst så kontrollera även försäljningsvärde. Det händer att mäklare beräknar beloppet i SEK på annan växelkurs än i uppgiften som de skickar till skatteverket. Skriv i så fall över värdet som är förifyllt.

# Status 2023-03-21
Får det tyvärr inte att funka. Skatteverkets tjänst kraschar när BLANKETTER.SRU läses in vid deklarationen.... Oklart var felet ligger. 
Men det går ju att deklarera för hand med hjälp av den genererade Excel-filen. Vill någon hjälpa till att
så SRU-uppladdningen att funka så skulle det uppskattas!

# Kontakt
https://twitter.com/ebtCap
