# SRU Maker

Det här programmet genererar K4 SRU-fil som kan laddas upp till skatteverket. Programmet skapar även Excel-fil så att resultatet kan granskas. Programmet hanterar valutakonton och räknar om enligt skatteverkets regler. 

Indata från mäklare sker med CSV-filer eller Excel-filer. Följande format finns färdiga:
- **Pareto** (CSV)
- **Interactive Brokers** (CSV - ta ut en rapport med "Flex Queries" -> Trades)
- **Avanza** (CSV)
- **Saxo Bank** (Excel .xlsx - exportera "Closed Positions" rapport)

Om du har data från andra mäklare så kan du antingen anpassa formatet till någon av de färdiga ovan eller så bygger du en ny import i programmet. Uppgifterna du vill ha med är
- Namn (på aktie, valuta alt. ticker)
- Antal
- Köp/Sälj
- Totalt belopp (inklusive avgifter)
- Valuta (framförallt om inte mäklaren växlat till SEK automatiskt.)

Har de dragit avgifter mm. i annan valuta än SEK ska det redovisas som en valutatansaktion. Likaså blir mottagna utdelningar ett köp av valuta. Själva utdelningen i sig brukar mäklaren rapportera och ska inte vara med i K4, men valutadelen behöver du ha koll på.

Programmet använder en genomsnittskurs för året för valutatransaktioner. Varje transaktion i en valuta som inte är SEK genererar en valutatransaktion också. T.ex. om du har sålt en aktie och fått betalt i dollar till ditt valutakonto. För blankningar bokas affären först när positionen är täckt.

**OBS för Saxo Bank:** Saxo-filen innehåller redan avslutade positioner (paired trades) där både köp och sälj ingår i samma rad. Saxo konverterar även automatiskt alla belopp till kontovaluta (SEK), vilket innebär att ingen separat valutakonvertering behövs för dessa affärer.

Som "Beteckning" används lite blandat mellan ticker och aktienamn beroende vad vi fått ut i exporten från mäklaren. Det korrekta är egentligen aktienamn. Skatteverket bör dock acceptera ticker. Men se till att spara dina underlag ifall Skatteverket skulle höra av sig med frågor.

## Programmets indata
Redigera alla filer som börjar på `indata*`:
- **indata.properties** - Personliga uppgifter (personnummer, namn, adress)
- **indata_avanza.csv** - Transaktioner från Avanza (om relevant)
- **indata_ibkr.csv** - Transaktioner från Interactive Brokers (om relevant)
- **indata_pareto.csv** - Transaktioner från Pareto (om relevant)
- **indata_saxo.xlsx** - Avslutade positioner från Saxo Bank (om relevant)
- **indata_aktier.xlsx** - Ingående saldo för aktier med inköpsvärde
- **indata_valuta.xlsx** - Ingående saldo för valutor med inköpsvärde
- **indata_valutakurser.xlsx** - Årskurser för valutaomräkning

Programmet hoppar automatiskt över filer som saknas, så du behöver bara ha de filer som är relevanta för dig.

**OBS:** Granska CSV/Excel-filer manuellt! T.ex. ska inte fonder (inklusive ETF:er) vara med, dvs. ska ej redovisas via K4.


## Bygg programmet
- Hämta ner och installera
  - Java 17 (t.ex. https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html)
  - Maven (https://maven.apache.org/download.cgi för installation se https://maven.apache.org/install.html samt https://phoenixnap.com/kb/install-maven-windows)
- Ställ dig i samma katalog som pom.xml och kör  ```mvn package```
  - Funkar det skapas en target-mapp där SruMaker-1.0-SNAPSHOT.jar finns.

Alternativt, om du inte kan/vill bygga själv, ladda ner SruMaker-1.0-SNAPSHOT.jar från "Releases". Då måste du också ladda ner indata-filerna till samma mapp.


## Kör programmet
Öppna ett terminalfönster och ställ dig i rot-katalogen (samma katalog där pom.xml finns).

Kör programmet genom att skriva:
```bash
java -classpath ./target/SruMaker-1.1-SNAPSHOT-jar-with-dependencies.jar com.ebtcap.sru.SruMaker 2024 true
```

**Parametrar:**
- **Första argumentet** (2024) - Deklarationsåret
- **Andra argumentet** (true/false) - Om transaktioner ska slås samman. `true` minskar antalet rader i deklarationen genom att gruppera transaktioner per värdepapper.

Programmet läser in CSV-filerna och skapar följande utdata-filer
- BLANKETTER.SRU. Alla K4 blanketterna. Kan laddas upp på deklarationssidan import.
- utdata_K4_2023.xlsx. Innehåller alla K4-uppgifter.
- utdata_aktier_2023.xlsx och utdata_valuta_2023.xlsx. Innehåller utgående innehav i aktier och valuta. Dessa kan sedan användas som indata nästa år.

## Deklarera
I Skatteverkets deklarationstjänst kan du importera filen på två ställen i e-tjänsten. Antingen direkt i menyvalet Bilagor, eller från den bilaga som har skapats åt dig. Välj BLANKETTER.SRU som du skapat med detta program. Klicka på Importera. Granska sedan resultatet innan du godkänner.
Kom ihåg att spara alla underlag ifall du blir ombedd att komma in med dessa. Kan även bara bra att ha inför nästa års deklaration. 


# Kontakt
https://twitter.com/ebtCap
