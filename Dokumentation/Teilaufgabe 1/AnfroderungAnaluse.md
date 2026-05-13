# Software Engineering I - Teilaufgabe 1 (Anforderungsanalyse und Planungsphase)

## Abgabedokument - Teilaufgabe 1 (Anforderungsanalyse und Planungsphase)

### Persönliche Daten, bitte vollständig ausfüllen:

- Nachname, Vorname: Pukhaev, Vikentiy
- Matrikelnummer: xxxxxxxx
- E-Mail-Adresse: axxxxxxxx@unet.univie.ac.at
- Datum: 03.04.2025

## Aufgabe 1: Anforderungsanalyse

### Typ der Anforderung: funktional

**Anforderung 1**

- **Anforderung**: User fordert ein neues Spiel am Server an.
- **Bezugsquelle**: [Spielidee, “Initial gilt es am Server ein neues Spiel anzufordern. Dieser erste Schritt wird noch von einem Menschen durchgeführt…”]

**Anforderung 2**

- **Anforderung**: Der Client muss die KI am Server registrieren.
- **Bezugsquelle**: [Spielidee, “Nach Start des Clients registrieren sich die KIs für das Spiel am Server…”]

**Anforderung 3**

- **Anforderung**: Der Client muss die Kartenhälfte an den Server schicken.
- **Bezugsquelle**: [Spielidee, “…erstellen/tauschen danach mit dem Server Kartenhälften aus.”]

**Anforderung 4**

- **Anforderung**: Die Spielfigur muss zwischen zwei benachbarten Feldern bewegen.
- **Bezugsquelle**: [Spielidee, “Die Karten sind in Felder aufgeteilt, zwischen denen sich die Spielfiguren, auf Anweisung der KIs, schrittweise waagrecht und senkrecht bewegen”]

### Typ der Anforderung: nicht funktional

**Anforderung 5**

- **Anforderung**: Das Spiel darf maximal 320 Spielaktionen dauern.
- **Bezugsquelle**: [Spielidee, “Um die Spiele für die Zuschauer spannend zu gestalten, wurde festgelegt, dass ein Spiel insgesamt nicht länger als 320 (und damit 320 Runden) dauern darf.”]

**Anforderung 6**

- **Anforderung**: Die KI muss maximal 5 Sekunden pro Spielaktion benötigen.
- **Bezugsquelle**: [Spielidee, “Für jede dieser rundenbasierten Spielaktion hat die KI maximal 5 Sekunden Bedenkzeit.”]

**Anforderung 7**

- **Anforderung**: Das Spiel muss automatisch beendet werden mit LOST-Status, wenn ein Client länger als 5 Sekunden für eine Spielaktion benötigt.
- **Bezugsquelle**: [Spielidee, “Für jede dieser rundenbasierten Spielaktion hat die KI maximal 5 Sekunden Bedenkzeit. Insgesamt dauert ein Spiel maximal 10 Minuten. Sollten diese Bedingungen nicht erfüllt werden, verliert die KI, welche gerade an der Reihe ist, das Spiel automatisch und der zugehörige menschliche Spieler bekommt dies vom Client mitgeteilt.”]

### Typ der Anforderung: Designbedingung

**Anforderung 8**

- **Anforderung**: Die Architektur des Spiels muss als klassische Client-Server-Architektur realisiert werden.
- **Bezugsquelle**: [Spielidee, “Die *Grobarchitektur* ist damit als klassische Client/Server Architektur vorgegeben.”]

Puhavik, hier ist dein Text streng gemäß dem vorgegebenen Template formatiert:

## Aufgabe 2: Anforderungsdokumentation

- **Name:** Bewegung der Spielfigur zwischen zwei benachbarten Feldern

- **Beschreibung und Priorität:**  
  Die Spielfigur muss sich schrittweise waagrecht und senkrecht zwischen zwei direkt benachbarten Feldern bewegen können, wobei die Anzahl der erforderlichen Bewegungsaktionen von der Terrainart (Wiese, Berg) abhängig ist. Diese Bewegungen erfolgen schrittweise und werden durch den Server validiert und ausgeführt.  
  **Priorität:** Hoch

- **Relevante Anforderungen:**  
  - **Funktional:** Der Client muss die Kartenhälfte an den Server schicken (Anforderung 3)  
  - **Nichtfunktional:** Die KI muss maximal 5 Sekunden pro Spielaktion benötigen (Anforderung 6)  
  - **Nichtfunktional:** Das Spiel darf maximal 320 Spielaktionen dauern (Anforderung 5)

- **Relevante Business Rules:**  
  - Bewegung muss nur zwischen direkt horizontal oder vertikal angrenzenden Feldern erfolgen.  
  - Wasserfelder dürfen niemals betreten werden.  
  - Wiesenfelder benötigen eine Bewegungsaktion zum Betreten.  
  - Wiesenfelder benötigen eine Bewegungsaktion zum Verlassen.  
  - Bergfelder benötigen zwei Bewegungsaktionen zum Betreten und zwei weitere zum Verlassen.  
  - Jede Bewegungsaktion muss einzeln vom Client an den Server geschickt und validiert werden.  
  - Der Spieler darf die Grenze der Karte nicht überschreiten.

### Impuls/Ergebnis – Typisches Szenario

**Vorbedingungen:**
- Der Client hat seine Kartenhälfte bereits an den Server gesendet.
- Der Client hat die vollständige Karte vom Server erhalten.
- Die Spielfigur befindet sich auf einem Wiesenfeld.
- Die Spielfigur möchte auf ein benachbartes Wiesenfeld wechseln.
- Der Client ist an der Reihe.

**Hauptsächlicher Ablauf:**
1. **Impuls:** Der Client fragt den Spielstatus beim Server an.  
   **Ergebnis:** Server antwortet mit Status „ACT“.
2. **Impuls:** Der Client sendet eine Bewegungsaktion, um das aktuelle Wiesenfeld zu verlassen.  
   **Ergebnis:** Server validiert die Aktion.
3. **Impuls:** Der Gegner fragt den Spielstatus beim Server an.  
   **Ergebnis:** Server antwortet mit Status „ACT“.
4. **Impuls:** Der Gegner sendet eine Bewegungsaktion, um das aktuelle Feld zu verlassen.  
   **Ergebnis:** Server validiert die Aktion.
5. **Impuls:** Der Client fragt erneut den Spielstatus beim Server an.  
   **Ergebnis:** Server antwortet mit Status „ACT“.
6. **Impuls:** Der Client sendet eine zweite Bewegungsaktion, um das Ziel-Wiesenfeld zu betreten.  
   **Ergebnis:** Server validiert die Aktion und aktualisiert die Position der Spielfigur.

**Nachbedingungen:**
- Die Spielfigur befindet sich nun auf dem Ziel-Wiesenfeld.
- Die nächste Bewegung wird geplant.

### Impuls/Ergebnis – Alternativszenario

**Vorbedingungen:**
- Der Client hat seine Kartenhälfte bereits an den Server gesendet.
- Der Client hat die vollständige Karte vom Server erhalten.
- Die Spielfigur befindet sich auf einem Wiesenfeld.
- Die Spielfigur möchte auf ein benachbartes Bergfeld wechseln.
- Der Client ist an der Reihe.

**Hauptsächlicher Ablauf:**
1. **Impuls:** Der Client fragt den Spielstatus beim Server an.  
   **Ergebnis:** Server antwortet mit Status „ACT“.
2. **Impuls:** Der Client sendet eine Bewegungsaktion, um das Wiesenfeld zu verlassen.  
   **Ergebnis:** Server validiert die Aktion.
3. **Impuls:** Der Gegner fragt den Spielstatus beim Server an.  
   **Ergebnis:** Server antwortet mit Status „ACT“.
4. **Impuls:** Der Gegner sendet eine Bewegungsaktion, um das aktuelle Feld zu verlassen.  
   **Ergebnis:** Server validiert die Aktion.
5. **Impuls:** Der Client fragt erneut den Spielstatus beim Server an.  
   **Ergebnis:** Server antwortet mit Status „ACT“.
6. **Impuls:** Der Client sendet eine zweite Bewegungsaktion, um das Bergfeld (erster Schritt) zu betreten.  
   **Ergebnis:** Server validiert die Aktion; die Spielfigur bleibt visuell noch am Ausgangsfeld.
7. **Impuls:** Der Gegner fragt erneut den Spielstatus beim Server an.  
   **Ergebnis:** Server antwortet mit Status „ACT“.
8. **Impuls:** Der Gegner sendet eine zweite Bewegungsaktion, um das Ziel-Feld zu betreten.  
   **Ergebnis:** Server validiert die Aktion und aktualisiert die Position der gegnerischen Spielfigur.
9. **Impuls:** Der Client fragt erneut den Spielstatus beim Server an.  
   **Ergebnis:** Server antwortet mit Status „ACT“.
10. **Impuls:** Der Client sendet eine dritte Bewegungsaktion, um das Bergfeld (zweiter Schritt) endgültig zu betreten.  
    **Ergebnis:** Server validiert die Aktion und aktualisiert die Position der Spielfigur.

**Nachbedingungen:**
- Die Spielfigur befindet sich auf dem Ziel-Bergfeld.
- Der Server deckt versteckte Schätze und gegnerische Burgen rund um das Bergfeld auf.
- Die nächste Bewegung wird geplant.

### Impuls/Ergebnis – Fehlerfall

**Vorbedingungen:**
- Die Spielfigur befindet sich neben einem Wasserfeld.
- Der Client versucht, das Wasserfeld zu betreten.

**Hauptsächlicher Ablauf:**
1. **Impuls:** Der Client sendet eine Bewegungsaktion zum Betreten des Wasserfeldes.  
   **Ergebnis:** Server erkennt die unerlaubte Aktion (Betreten von Wasser).
2. **Impuls:** Der Server reagiert mit einer Fehlermeldung.  
   **Ergebnis:** Das Spiel wird automatisch mit LOST-Status für diesen Client beendet.

**Nachbedingungen:**
- Der Client erhält den LOST-Status.
- Das Spiel terminiert automatisch für diesen Client.

### Benutzergeschichten

- **Als KI:** Möchte ich meine Spielfigur zwischen benachbarten Feldern bewegen, um schnellstmöglich den Schatz zu finden.
- **Als Server:** Möchte ich jede Bewegungsaktion validieren, um sicherzustellen, dass alle Spielregeln eingehalten werden.
- **Als Anwender:** Möchte ich die Bewegungen meiner KI in der CLI sehen, um den Spielverlauf nachvollziehen zu können.

### Benutzerschnittstelle

**CLI Mockup-Beispiel:**
```
Spielfeld:
🌿🦸🏻‍♂️🌿
🌿⛰️🌊

Aktion: Bewegung nach RECHTS ausgeführt.
Neue Position:
🌿🌿🦸🏻‍♂️
🌿⛰️🌊
```

### Externe Schnittstellen

- **Client-Server Schnittstelle:**  
  Die Kommunikation erfolgt über HTTP mit XML-formatierten Nachrichten. Clients verwenden GET-Anfragen für Spielstatusabfragen und POST-Anfragen für Bewegungen. Jede Anfrage enthält eindeutige Spieler- und Spiel-IDs. Der Server antwortet stets mit HTTP-Statuscode 200, wobei Fehler im Nachrichteninhalt über XML-Elemente wie *exceptionName* und *exceptionMessage* kommuniziert werden.

## Aufgabe 3: Architektur entwerfen, modellieren und validieren

### Klassendiagramm
![UML](Diagrams/UML_Client.svg)

### Sequenzdiagramm 1
![UML](Diagrams/Sequence1.svg)

### Sequenzdiagramm 2
![UML](Diagrams/Sequence2.svg)

## Aufgabe 4: Quellen dokumentieren

### Aufgabe 1: Anforderungsanalyse

- **Kurzbeschreibung der Übernommenen Teile**: Zur Überprüfung und sprachlichen Korrektur der erarbeiteten Inhalte wurde eine KI verwendet, da Deutsch nicht meine Muttersprache ist.  
- **Quellen der Übernommenen Teile**: KI-Prompts zur Korrektur der deutschen Sprache "Überprüfe meinen Text und korrigiere die sprachlichen Fehler."

### Aufgabe 2: Anforderungsdokumentation

- **Kurzbeschreibung der übernommenen Teile**: Zur sprachlichen Verbesserung und Überprüfung der Formulierungen wurde eine KI eingesetzt, da Deutsch nicht meine Muttersprache ist.  
- **Quellen der Übernommenen Teile**: KI-Prompts zur Korrektur der deutschen Sprache "Überprüfe meinen Text und korrigiere die sprachlichen Fehler."

### Aufgabe 3: Architektur entwerfen, modellieren und validieren

- **Kurzbeschreibung der übernommenen Teile**: Alle Inhalte wurden ausschließlich auf Basis der Spielidee und des zugehörigen Netzwerkprotokolls erstellt.  
- **Quellen der übernommenen Teile**: Spielidee, Netzwerkprotokoll.
