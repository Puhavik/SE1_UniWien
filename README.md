[[_TOC_]]

# SE1 – Client-Implementierung (Treasure Hunt Game)

**Lehrveranstaltung:** Software Engineering I (SE1)  
**Semester:** Sommersemester 2025  
**Universität Wien**

| Feld | Wert |
|------|------|
| Name | Pukhaev, Vikentiy |
| Matrikelnummer | xxxxxxxx |
| E-Mail | axxxxxxxx@unet.univie.ac.at |
| Branch | `master` |

---

## Projektbeschreibung

Dieses Repository enthält die Implementierung eines **KI-gesteuerten Clients** für ein rundenbasiertes Multiplayer-Spiel im Rahmen der LV Software Engineering I.

Zwei KI-Clients treten gegeneinander an: Jede KI navigiert ihre Spielfigur auf einer geteilten Karte mit dem Ziel, den gegnerischen Schatz zu finden – bevor der Gegner den eigenen findet.

### Spielregeln (Kurzfassung)

- Karte ist in Felder aufgeteilt: **Wiese**, **Berg**, **Wasser**
- Wasserfelder dürfen **nicht** betreten werden
- Wiesenfelder: je 1 Aktion zum Betreten/Verlassen
- Bergfelder: je **2** Aktionen zum Betreten/Verlassen
- Max. **320 Spielaktionen** pro Spiel
- KI hat max. **5 Sekunden** Bedenkzeit pro Aktion
- Kommunikation via **HTTP + XML** (Client ↔ Server)

---

## Technologie-Stack

| Bereich | Technologie |
|---------|-------------|
| Sprache | Java |
| Build | Gradle |
| Architektur | MVC (Model-View-Controller) |
| Netzwerk | HTTP / XML |
| Wegfindung | A\*-Algorithmus (Manhattan-Distanz) |
| Testing | JUnit 5, Mockito |
| Logging | SLF4J / Logback |

---

## Repository-Struktur

```
SE1_xxxxxxxx/
├── Dokumentation/
│   ├── Teilaufgabe 1/      # Anforderungsanalyse, UML-Diagramme
│   ├── Teilaufgabe 2/      # Quelldokumentation TA2
│   └── Teilaufgabe 3/      # Quelldokumentation TA3
├── Executables/
│   ├── Teilaufgabe 2/      # JAR-Datei TA2
│   └── Teilaufgabe 3/      # JAR-Datei TA3
├── Source/
│   └── Client/             # Java-Sourcecode (Gradle-Projekt)
└── README.md
```

---

## Übungsaufgaben – Übersicht

### ✅ Teilaufgabe 1 – Anforderungsanalyse & Architektur
- Analyse funktionaler und nicht-funktionaler Anforderungen
- Erstellung von **Klassen- und Sequenzdiagrammen** (SVG)
- Dokumentation als Markdown-Dokument

### ✅ Teilaufgabe 2 – Basis-Client-Implementierung
- Netzwerkkommunikation via HTTP/XML
- Kartengenerierung und -austausch mit dem Server
- Grundlegende Bewegungslogik

### ✅ Teilaufgabe 3 – Erweiterte Implementierung
- A\*-Wegfindung mit Manhattan-Distanz
- MVC-Architektur
- Logging (SLF4J), Fehlerbehandlung, Unit-Tests (JUnit 5 + Mockito)

---

## Build & Ausführung

```bash
# Projekt bauen
./gradlew build

# JAR erstellen
./gradlew jar

# JAR ausführen (Argumente laut Moodle-Angabe)
java -jar <NameDerJarDatei.jar> <argumente>
```

> Eclipse-Import: **File → Import → Gradle → Existing Gradle Project** → Root-Ordner `Source/Client/` auswählen.

---

---

# Kursanleitung (von LV-Leitung bereitgestellt)

> Die folgenden Abschnitte sind die offiziellen Anweisungen der Lehrveranstaltung.

## Wofür wird GIT verwendet?

In dieser Lehrveranstaltung werden Ihre Übungsausarbeitungen, sofern nicht explizit anders angeführt, über GitLab abgegeben bzw. eingereicht. Wichtig hierbei ist, dass wir den letzten gepushten Commit Ihres Masterbranches als maßgeblich für die Bewertung und die Bestimmung der von Ihnen gewählten Deadline ansehen. Die jeweiligen Punkte und Prüfungsergebnisse sind anschließend in Moodle ersichtlich.

**Ändern Sie nicht den Namen des Masterbranches**: Dieser muss den Namen `master` tragen. Nur Daten, die vor der Abgabedeadline im Masterbranch liegen (daher Commit samt Push *vor* der Deadline) werden während potentieller Abgabegespräche und der Bewertung berücksichtigt. Sie können eigene Branches anlegen, vergessen Sie dann aber nicht auf einen finalen Merge (unter Beibehaltung aller individuellen Commits & Commitmessages, daher *kein* squash) *vor* der Deadline in den `master` Branch.

## Wie erhalte ich lokalen Zugriff auf dieses Repository?

Um optimal mit diesem Repository zu arbeiten sollten Sie es auf Ihr lokales Arbeitsgerät spiegeln. Verwenden Sie hierzu den Befehl `git clone URLIhresRepositories`. Die URL Ihres Repositories finden Sie im Kopf dieser Webseite rechts vom Namen des Repositories. Um diese zu erhalten drücken Sie auf den blauen mit `Clone` beschrifteten Knopf. Wählen Sie die mittels `Clone with HTTPS` bereitgestellte URL. Diese sollte vergleichbar sein zu `https://git01lab.cs.univie.ac.at/.....`.

**Probleme mit den Zertifikaten**: Falls Sie beim clonen Ihres Git Repositories Probleme gemeldet bekommen, die mit der Prüfung der Zertifikate in Verbindung stehen ist es eine schnelle Lösung diese abzuschalten. Hierzu kann folgender Befehl verwendet werden: `git config --global http.sslVerify false`

## Wie nütze ich dieses Repository?

Clonen Sie hierzu dieses Repository wie oben angegeben. Danach können Sie mit `git add`, `commit`, `push`, etc. damit arbeiten. Optimalerweise legen Sie hierzu nach dem initialen clone Ihren Namen (echten Namen, kein Nickname) und Ihre E-Mail-Adresse (E-Mail-Adresse der Universität Wien) fest sodass alle Commits Ihnen direkt zugeordnet werden können. Verwenden Sie hierzu folgende Befehle:

> `git config --global user.name "Mein Name"`

> `git config --global user.email a123456@univie.ac.at`

**Hilfe und Unterstützung für Git/GitLab**: Weitere Hilfen samt einer schrittweisen Einführung in den Umgang mit Git finden sich im Git & GitLab Screencast auf Moodle. Dort ist auch direkt ein Skriptum eingebunden um Details nachzulesen. Für erfahrene Studierende, ist als Referenz, bei den Screencasts auch ein Git Cheat-Sheet verlinkt. An einem der Übungstermine findet auch ein Git Tutorial statt. Anschließend können Sie immer auch unseren Tutor mit Fragen zu Git/GitLab, z.B. [email](mailto:tutor.swe1@univie.ac.at) oder (empfohlen) GitLab Issue kontaktieren.

Für weiterführende Informationen lohnt sich ein Blick in das Pro Git Handbuch: https://git-scm.com/book/de/v2 Besonders für das Thema branching empfiehlt sich außerdem: https://learngitbranching.js.org/

## Welche Inhalte sind vorgegeben und wofür sind diese gedacht?

Es wurden mehrere **Ordner** sowie **.gitignore** Dateien vorgegeben. Letztere dienen dazu Ihr Repository nicht mit "unnötigen" Dateien zu befüllen. Ändern Sie diese Dateien daher nicht bzw. nur sehr behutsam.

Die vorgegebenen Ordner sind wie folgt zu verwenden:

- **Dokumentation** – Nutzen Sie die darin enthaltenen Unterordner, pro Teilaufgabe ist ein anderer Unterordner vorgesehen, um Ihre Dokumentation abzulegen bzw. abzugeben. Achten Sie darauf, dass die abgegebenen Inhalte **lesbar** sind und **korrekt** dargestellt werden.
  - Für SVG-Dateien dient **Google Chrome / Chromium** als Referenz-Renderer.
  - Für Markdown-Dateien dient der integrierte **GitLab-Parser** als Referenz.
  - **Dateinamen und Pfade aus der Angabe auf Moodle beachten!**

- **Executables** – Hinterlegen Sie hier die finalen kompilierten `.jar`-Dateien für Teilaufgabe 2 und 3. Diese müssen sich zumindest mit `java -jar <NameDerJarDatei.jar>` ausführen lassen. **Prüfen Sie ob dies der Fall ist!**

- **Source** – Nutzen Sie diesen Ordner für die Implementierung von Teilaufgabe 2 und 3 (Sourcecode, Konfigurationen etc.).

## Was gilt es während der Implementierung zu beachten?

- **Vor einer Deadline**: Prüfen Sie sicherheitshalber, ob das Repository neu geklont, in Eclipse importiert und das Projekt gebaut sowie als JAR exportiert werden kann. Prüfen Sie das JAR mehrfach mit den bereitgestellten Lösungen zur Selbstevaluation.

- **Während der Bearbeitung**: Erstellen Sie keine zusätzlichen Ordner im Wurzelverzeichnis. Stellen Sie sicher, dass nicht mehrere widersprüchliche Versionen Ihrer Abgaben im `master`-Branch enthalten sind.

## Wie kann ich während der Implementierung Unterstützung erhalten?

- **Allgemeine Fragen** → Moodle-Forum nutzen
- **Spezifische Implementierungsfragen** → GitLab Issue erstellen und Tutor-Handle (`@simone99`) im Text erwähnen:
  - `Simon Eckerstorfer (Git Handle @simone99)`
- **E-Mail**: Tutor: [tutor.swe1@univie.ac.at](mailto:tutor.swe1@univie.ac.at) | LV-Leitung: [swe1.wst@univie.ac.at](mailto:swe1.wst@univie.ac.at)

> **Wichtig**: Verwenden Sie niemals `@all` in Issues. Tutoren können keine Fragen zur Beurteilung beantworten.

## Welche Funktionen sollen nicht genutzt werden?

GitLab ist eine mächtige Software – verwenden Sie optimalerweise einfach die vorgegebenen Einstellungen. Unbedachte Aktionen (z.B. das Löschen des Masterbranches) können negative Auswirkungen haben (*Think before you click!*).
