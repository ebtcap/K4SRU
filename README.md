# SRU Maker

Det här programmet genererar K4 SRU-fil som kan laddas upp till skatteverket. Programmet skapar även  Excel-fil så att resultatet kan granskas.
Programmet hanterar valutakonton och räknar om enligt skatteverkets regler. 

I Skatteverkets delarationstjänst kan du importera filen på två ställen i e-tjänsten. Antingen direkt i menyvalet Bilagor, eller från den bilaga som har skapats åt dig. Välj BLANKETTER.SRU som du skapat med detta program. Klicka på Importera. Granska sedan resultatet innan du godkänner.

Programmet använder en genonsnittskurs för året för valutatransaktioner. Varje transaktion i en valuta som inte är SEK genererar en valutatransation också. T.ex. om du har sålt en aktie och fått betalt i dollar till ditt valutakonto. För blankningar bokas affären först när postionen är täckt.

Som "Beteckning" används lite blandat mellan ticker och aktienamn beroende vad vi fått ut i exporten från mäklaren. Det korrekta är egentligen aktienamn. Skatteverket bör dock acceptera ticker. Men se till att spara dina underlag ifall Skatteverket skulle höra av sig med frågor.

## Bygg
- Hämta ner och installera
  - Java 17 (t.ex. https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html)
  - Maven (https://maven.apache.org/download.cgi för installation se https://maven.apache.org/install.html)
- Ställ dig i samma katalog som pom.xml och kör  ```mvn package```
  - Funkar det skapas en target-mappp där SruMaker-1.0-SNAPSHOT.jar finns.


## Indata
Redigera alla filer som börjar på indata*
- Personliga uppgifter i indata.properties.
- CSV med transaktoner från Avanza, Pareto och Interactive Brokers. Använd den/de som är relavanta för dig. Ta bort den/de som är orelevanta.
- Ingående saldo för aktier och valutor med inköpsvärde.

OBS, granska csv manuellt! T.ex. ska inte fonder (inklusive ETF:er) vara med. Dvs. ska ej redovisas via K4.

## Kör programmet
Öppna ett terminalfönster och ställ dig i rot-katalogen (samma katalog där pom.xml finns)
Kör programmet genom att skriva  ``` java -classpath ./target/SruMaker-1.0-SNAPSHOT.jar com.ebtcap.sru.SruMaker 2022 true ```
- Det första argumentet är året
- Det andra är true/false om transaktionerna ska slås samman eller inte. Slå samman transaktionerna minskar antalet rader att deklarera.

Programmet läser in CSV-filerna och skapar följande utdata-filer
- BLANKETTER.SRU. Alla K4 blanketterna. Kan laddas upp på deklarationssidan import.
- utdata_K4_2022.xlsx. Innehåller alla K4-uppgifter.
- utadata_aktier_2022.xlsx och utdata_valuta_2022.xlsx. Innehåller utgående innehav i aktier och valuta. Dessa kan sedan användas som indata nästa år.


# Kontakt
https://twitter.com/ebtCap
