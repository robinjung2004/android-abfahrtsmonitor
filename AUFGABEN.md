# Abfahrtsmonitor – Aufgabenblatt

## Arbeitsumgebung und Ziel

Alle Änderungen erfolgen in **Kotlin in Android Studio**. Die UI ist mit Jetpack Compose vorbereitet. Ein Projekt wird durch die Präsenzübung und Hausaufgabe weiterentwickelt. Die Webseite bedient ausschließlich das Lehrteam; sie ist eine Simulation, keine Android-Laufzeit.

Klont das von der Kursleitung genannte **Studierendenrepository** und öffnet dessen Projektordner mit `settings.gradle.kts` in Android Studio. Die Schritte stehen in `README.md`. Verwendet **JDK 17** als Gradle JDK, installiert **Android SDK 36** und startet auf einem Emulator/Gerät mit Android 8 (API 26) oder neuer. Für die Service-Regeln empfiehlt sich API 34 oder neuer. Gradle und Bibliotheken benötigen beim ersten Einrichten Internet. Danach benötigt der Datenmodus keine externe API.

Die Starterversion kompiliert und startet bereits. Eine anfangs leere Liste, der U3-Hinweis beim Aktualisieren und ein sofort wieder beendeter Timer sind erwartete Platzhalter; `STARTERSTATUS.md` erklärt sie. Android Studios Suche nach **TODO-BEGIN** findet alle neun Lücken. Ausschließlich den Inhalt zwischen BEGIN und END ersetzen; Kommentare und IDs erhalten. U = Unterricht, H1–H3 = Kernhausaufgabe, H4/H5 = freiwillige Vertiefung. Android Studio, Gradle Sync und Emulator sollen vor der Lehrveranstaltung eingerichtet sein. Für die benötigte Kotlin-Syntax gibt es `KOTLIN-EINSTIEG.md`; allgemeine Programmiererfahrung wird vorausgesetzt.

## Präsenzübung: Abfahrten laden und abbrechen

| ID | Datei unter `app/src/main/java/de/mobilitylab/departures/` | Auftrag | Sichtbares Ergebnis |
|---|---|---|---|
| U1 | `data/DepartureRepository.kt` | In `coroutineScope` beide `async`-Aufrufe vor dem ersten `await` starten. Den optionalen `IOException`-Hinweisfehler innerhalb seines Kindes mit Ersatztext behandeln. | Vier Abfahrten bleiben beim Hinweisfehler sichtbar; Dauer etwa 1,2 statt 2 Sekunden. |
| U2 · geführt | `data/DepartureCache.kt` | Gemeinsam mit dem Lehrteam JSON mit `withContext(cpu)` berechnen und anschließend über `store.edit` speichern. Den Kontextwechsel erklären; DataStore ist vorbereitet. | Cache-Zeitstempel und Abfahrten erscheinen und überstehen einen App-Neustart. |
| U3 | `ui/DepartureViewModel.kt` | Alten Job abbrechen; im `viewModelScope` laden; Ladezustand, Erfolg und `IOException` setzen; `CancellationException` weiterwerfen. | Wiederholtes Laden ersetzt den alten Auftrag; UI bleibt bedienbar. |
| U4 | `ui/DepartureViewModel.kt` | Nur `refreshJob` abbrechen; Rückmeldung und loading=false setzen. | Abbruch ist sichtbar; ein neuer Auftrag funktioniert. |

Beginnt mit U1, ergänzt U2 gemeinsam mit dem Lehrteam und bearbeitet anschließend U3/U4. U1, U3 und U4 bilden den selbstständigen Kern. Das Pflichtziel ist ein bedienbarer Reload mit gemeinsam geladenen Ergebnissen, verständlicher Fehlerrückmeldung und gezieltem Abbruch. Prüft zunächst Laden, frühen Abbruch und erneutes Laden. Vertieft danach je nach Arbeitsstand Hinweisfehler, Rotation und gespeicherte Daten. Keine Layoutänderung nötig.

### Schrittweise vorgehen

1. **U1 · Ergebnis vorhersagen:** Notiert vor dem Implementieren die erwartete Gesamtdauer und das Ergebnis bei einem optionalen Hinweisfehler. Startet nach eurer Ergänzung `UnterrichtRepositoryTest`; dafür müssen U2–U4 noch nicht fertig sein. Die UI zeigt das Ergebnis erst nach den folgenden Schritten.
2. **U2 · gemeinsam verfolgen:** Zeigt im vorbereiteten `save`-Ablauf auf den Teil, der JSON berechnet, und auf den Teil, der es speichert. Ergänzt mit dem Lehrteam den Kontextwechsel um die Berechnung und anschließend das Speichern über DataStore. Erklärt, in welchem Kontext es nach `withContext` weitergeht. Die komplette Cache-Prüfung folgt mit funktionierendem Reload.
3. **U3 · einen Auftrag verfolgen:** Bestimmt zuerst das Feld für den aktuellen Job und den Besitzer des neuen Auftrags. Ergänzt dann der Reihe nach das Ersetzen des alten Jobs, den Ladezustand, den Repository-Aufruf und die Erfolgsrückmeldung. Ergänzt danach die getrennten Pfade für `IOException` und `CancellationException`. Überlegt bei jedem Zustandswechsel, ob er von einem alten, inzwischen ersetzten Job stammen könnte. Die Unterpunkte im TODO helfen beim Lesen; sie sind keine zusätzlichen Lücken.
4. **U4 · gezielt stoppen:** Legt fest, welcher einzelne Job endet und welche Zustandsmeldung sichtbar wird. Begründet, warum der ViewModel-Scope für den nächsten Reload erhalten bleibt. Führt danach `Unterricht*` sowie U-A bis U-D aus.

Unter Windows im Projektordner:

```powershell
.\gradlew.bat testDebugUnitTest --tests "*UnterrichtRepositoryTest"
.\gradlew.bat testDebugUnitTest --tests "*Unterricht*"
```

Unter macOS/Linux `./gradlew` verwenden. `UnterrichtRepositoryTest` und `UnterrichtViewModelTest` gehören zur Präsenzphase; H1–H5 dürfen dafür offen bleiben. U2 zusätzlich über den Cache-Zeitstempel und einen App-Neustart prüfen. Sichert euren eigenen Stand anschließend als Commit. Offene U-Aufgaben könnt ihr zuhause abschließen. Bei Freigabe des Lehrteams enthält `Abfahrtsmonitor-Praesenzstand.zip` genau die drei bearbeiteten U-Dateien und eine `UEBERNAHME.md`: erst den eigenen Stand sichern, dann vergleichen und die benötigten Ergänzungen im bestehenden Projekt übernehmen. Alle H-Blöcke bleiben offen.

### Tests für die Übung

- **U-A:** Aktualisieren: Fortschritt sichtbar, anschließend vier Beispielabfahrten und Cache-Zeitstempel.
- **U-B:** Hinweisfehler einschalten, neu laden: Abfahrten bleiben; verständlicher Ersatztext erscheint.
- **U-C:** Laden starten und vor 500 ms abbrechen; zwei Sekunden warten: kein verspätetes Ladeergebnis. Danach erneut laden: funktioniert. Bereits abgeschlossene Speicheroperationen sind kein rücknehmbarer Abbruch.
- **U-D:** Während eines Reloads den Bildschirm drehen: kein zweiter unabhängiger Auftrag; Zustand bleibt nachvollziehbar. Nach Prozessneustart werden gespeicherte Daten wieder angezeigt.

## Partneraufgabe im zweiten Block: den Timer verbessern

Analysiert zu zweit diesen absichtlich fehlerhaften Entwurf. Eine Activity startet nach einem Button-Klick einen Timer, der bei Home weiterlaufen und außerhalb der App stoppbar sein soll:

```kotlin
lifecycleScope.launch {
    var remaining = 60
    while (true) {
        delay(1_000)
        remaining--
        showRemaining(remaining)
    }
}
```

1. Sagt voraus, was bei Home, Rotation und verspäteten Ticks mit genau diesem Auftrag passiert. Home zerstört die Activity nicht automatisch; bei Rotation wird ihre alte Instanz normalerweise zerstört.
2. Skizziert die Änderung: Service besitzt den Timerjob; Restzeit aus einer monotonen Deadline berechnen; Abbruch, Ablauf und Stop einschließlich Cleanup vorsehen.
3. Formuliert eure Begründung und eine konkrete Prüfung für Stop und erneuten Start. Bezieht Home/Rückkehr ein. Danach vergleichen wir die Entwürfe gemeinsam.

Als Pflichtziel reichen eine Skizze oder Pseudocode mit zwei begründeten Änderungen und einer Testidee. Wer weiter ist, vergleicht zusätzlich Prozessende und regulären Stop. Es kommt keine weitere Kotlin-Pflichtlücke hinzu. `delay` reagiert auf Cancellation; `while (true)` bedeutet deshalb in diesem Beispiel nicht, dass Abbruch unmöglich wäre. Dem Entwurf fehlen jedoch ein fachliches Ende bei null und die passende Zuständigkeit.

## Hausaufgabe – Kern ungefähr drei Stunden

Ausgangspunkt ist der fertige U1–U4-Stand **in eurem bisherigen Projekt**. Schließt offene U-Aufgaben zuerst ab oder übernehmt nach Sicherung eures eigenen Commits die vom Lehrteam freigegebenen U-Ergänzungen. Die H1–H5-Blöcke bleiben zunächst offen; ihr benötigt keinen neuen Clone und keine vollständige Musterlösung.

Ziel: Einen ausdrücklich gestarteten Abfahrtstimer außerhalb der Activity weiterführen, kontrolliert stoppen und Entscheidungen zu Coroutines und Scopes begründen. Das ist ein Lehrtimer: **Home und Rückkehr werden unterstützt, eine sekundengenaue Benachrichtigung im Geräteschlaf sowie automatisches Fortsetzen nach Force-stop oder Neustart sind nicht Bestandteil.** WorkManager ist eine freiwillige Vertiefung und wird nicht bewertet.

| ID | Datei | Zu ergänzende Logik | Abnahmekriterium |
|---|---|---|---|
| H1 | `timer/Countdown.kt` | Positive Differenz zwischen monotoner Deadline und Jetzt auf Sekunden aufrunden, mindestens null. | Tests für Restbruchteile, abgelaufene Zeit und verspätete Ticks bestehen. |
| H2 | `timer/DepartureTimerService.kt` | `scope.launch`, abbrechbare Schleife, Restzeit, `TimerStatus`, Notification-Update; bei null Abschlussmeldung und `finishTimer`. | Start, Home, Rückkehr, erneuter Start und Ablauf sind konsistent. |
| H3 | `timer/DepartureTimerService.kt` | In `onDestroy` den Service-Scope abbrechen und Timerzustand inaktiv setzen. | Regulärer Stop räumt auf; es bleibt keine laufende Schleife. |

Vorbereitet und zu erhalten: Compose-UI, Datenmodell, DataStore, Manifest, Service-Typ, Benachrichtigungskanal, Permission-Dialog, Start- und Stop-Aktionen, Worker-Anbindung. `START_NOT_STICKY` bleibt bewusst bestehen. Ein FGS ist kein zusätzlicher Thread. Der `specialUse`-Typ ist für den sichtbaren Lehr-Countdown begründet; eine spätere Play-Veröffentlichung bedarf einer eigenen Prüfung des konkreten Anwendungsfalls.

### Drei Transferfälle zu Coroutines und Scopes

Beantwortet alle drei Fälle in eigenen Worten, mit einer kurzen Codeskizze oder einer Tabelle, wo sie hilft. Eine zusätzliche Android-Oberfläche ist nicht erforderlich.

**T1 · Ergebnisse und Ausführungskontext:** Zwei unabhängige suspendierende Abfragen warten 1.200 ms und 800 ms. Vergleicht `async { a() }.await()` gefolgt von `async { b() }.await()` mit zwei zuerst gestarteten `async`-Aufträgen und anschließendem `await` in `coroutineScope`. Nennt die ungefähre Gesamtdauer und erklärt `Job`, `Deferred` und warum `await` den Thread nicht blockieren muss. Die Ergebnisse werden danach innerhalb von `withContext(Dispatchers.Default)` aufwendig ausgewertet. Wo setzt die aufrufende Coroutine anschließend fort? Erklärt außerdem, was `suspend` allein nicht auslöst.

**T2 · Lebensdauer:** Vergleicht einen Reload im `viewModelScope` eines an die Activity gebundenen ViewModels, einen Auftrag im `lifecycleScope` dieser Activity und einen inneren Sammelauftrag in `lifecycleScope.launch { repeatOnLifecycle(STARTED) { … } }`. Betrachtet Home, Rotation und das endgültige Schließen dieser Activity. Haltet fest, welcher Auftrag weiterlaufen kann, welcher abgebrochen wird und welcher bei Rückkehr neu startet. Geht von einem weiterlaufenden Prozess aus. Ordnet `collectAsStateWithLifecycle()` als vorbereitete Compose-Beobachtung ein und trennt die Beobachtung vom separat gestarteten Reload.

**T3 · Gemeinsamer Auftrag und Abbruch:** Zeichnet den Job-Baum eines Reloads, der in einem normalen `coroutineScope` zwei `async`-Kinder für Abfahrten und Hinweise startet. Beschreibt die Auswirkungen von Nutzerabbruch und einer ungefangenen `IOException` im Pflichtdaten-Kind auf die anderen Aufgaben. Erklärt, weshalb der optionale Hinweisfehler innerhalb dieses Kindes behandelt wird und `CancellationException` weitergegeben werden muss. Ergänzt, wie lange CPU-Arbeit kooperativ auf Cancellation reagieren kann und warum ein Abbrechen-Button nur den aktuellen Ladejob statt des gesamten `viewModelScope` beendet.

### Arbeitsplan (180 Minuten)

10 min Orientierung · 25 min H1 · 55 min H2/H3 · 35 min T1–T3 · 35 min Tests · 20 min Begründung und Abgabe zusammenstellen. Die Zeiten sind Orientierung; H4/H5 kommen nur bei freiwilliger Vertiefung hinzu.

### Pflichtprüfungen

| Test | Durchführung | Erwartung |
|---|---|---|
| H-A | 30-s-Test starten, Notification erlauben, Home, nach einigen Sekunden zurück | Timer bleibt aktiv; Restzeit ist weitergelaufen. |
| H-B | Während eines Timers einen neuen starten | Genau ein aktiver Countdown mit neuer Deadline. |
| H-C | Einmal per UI, einmal per Notification stoppen | Laufende Notification verschwindet; UI ist inaktiv. Danach erneuter Start möglich. |
| H-D | Timer vollständig ablaufen lassen | Inaktiver Zustand und Abschlussnotification; keine weiterlaufende Schleife. |
| H-E | Notification-Erlaubnis bei erster Nachfrage ablehnen | Lehrtimer startet nicht; verständliche Rückmeldung. Dies ist die UX-Entscheidung dieser App. |
| H-I | App über Einstellungen erzwungen beenden, erneut öffnen | Kein alter aktiver Timer behauptet; gespeicherter Cache bleibt. |

Für die Kernabgabe gelten U-A bis U-D sowie H-A bis H-E und H-I. Die IDs bleiben mit den vorhandenen Projektunterlagen kompatibel. H-F bis H-H gehören ausschließlich zur optionalen WorkManager-Vertiefung. Ein Lehrtimer darf keine falsche Live-Abfahrt versprechen.

Die Tests sind nach Lernphase getrennt. Nach H1 muss `HausaufgabeCountdownTest` bestehen; nach H2/H3 zusätzlich `HausaufgabeTimerTest` auf einem Gerät oder Emulator. Die bereits fertigen Unterrichtstests bleiben grün. Unter Windows:

```powershell
.\gradlew.bat testDebugUnitTest --tests "*Unterricht*" --tests "*HausaufgabeCountdownTest"
.\gradlew.bat connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=de.mobilitylab.departures.HausaufgabeTimerTest"
```

Die automatischen Tests ersetzen die manuellen Kernprüfungen nicht. `VertiefungWorkManagerTest` betrifft ausschließlich H4/H5. Ohne Klassenfilter enthält `connectedDebugAndroidTest` auch diese freiwilligen Tests; solange H4/H5 offen bleiben, sind ihre Fehler kein Mangel der Kernabgabe. Ein erfolgreicher Build allein belegt keine gelöste Aufgabe.

## Abgabe und Bewertung (100 Punkte)

Abgeben: euren fertigen Projektstand, `TESTPROTOKOLL.md` und eine eigene Erklärung zu T1–T3 und den Timergrenzen, insgesamt ungefähr ein bis zwei Seiten. Kopiert dafür `TESTPROTOKOLL-VORLAGE.md` nach `TESTPROTOKOLL.md` und tragt echte Beobachtungen ein. Sichert Code, Testprotokoll und Erklärung zusammen in einem abschließenden Commit. Die Kursleitung nennt Termin und Abgabekanal: eigener freigegebener Repository-Link mit Commit-ID oder Projekt-ZIP. Ein ZIP enthält keine `.git`, `.gradle`, `.idea`, `build`, `local.properties` oder Zugangsdaten. Nicht in die zentrale Kursvorlage pushen. Screenshots können Beobachtungen belegen, ersetzen aber keine Begründung.

- **50 Punkte Timer/Service:** H1 Deadline und Grenzen 15; H2 Start/Verlauf/Ablauf 25; H3 Stop/Cleanup 10.
- **30 Punkte Coroutines und Scopes:** T1 Ergebnisse und Kontext 10; T2 Lebensdauer 10; T3 Structured Concurrency und Cancellation 10. Pro Fall zählen fachlich richtige Vorhersagen und ihre nachvollziehbare Begründung gleichermaßen.
- **20 Punkte Tests und Nachvollziehbarkeit:** konkrete Beobachtungen zu den Kernprüfungen einschließlich offener Probleme 15; begründete Wahl des Timer-Besitzers und Grenzen bei Geräteschlaf, Force-stop und Neustart 5.

Die vorherigen U-Blöcke sind Arbeitsgrundlage. Kein UI-Neubau, keine echte DB-Anbindung und keine neuen Android-Komponenten als Pflichtzusatz. In der Erklärung auch beantworten: Warum kann ein Service Main blockieren? Warum `elapsedRealtime` statt `remaining--`? Was passiert bei Force-stop? Wann wäre eine Erinnerung über AlarmManager passender? H4/H5 sind keine Voraussetzung für die volle Punktzahl und bringen keine Zusatzpunkte.

## Freiwillige geführte Vertiefung: WorkManager

Zeitbedarf zusätzlich ungefähr 60–90 Minuten. Verwendet die vorbereiteten Dateien und die vom Lehrteam freigegebenen Hinweise als Hilfe. Ziel ist, einen aufschiebbaren Cache-Refresh zu verstehen und auszuprobieren. Diese Vertiefung gehört nicht zur Kernabgabe.

| ID | Datei | Zu ergänzende Logik | Beobachtung |
|---|---|---|---|
| H4 · optional | `work/RefreshWorker.kt` | Repository aktualisieren; `success`, begrenztes `retry` bei IOException und danach `failure`; Cancellation weiterwerfen. | Bei Dauerfehler maximal drei Versuche; gültiger Cache bleibt erhalten. |
| H5 · optional | `work/RefreshScheduler.kt` | CONNECTED-Constraint, exponentielles Backoff, eindeutige Namen; einmalig und periodisch (mindestens 15 min), KEEP-Policy. | Mehrfaches Einplanen erzeugt keinen doppelten aktiven Auftrag desselben Namens. |

Die Demo lädt lokal, wendet den Netzwerk-Constraint aber absichtlich trotzdem an. Damit lässt sich das Verhalten eines späteren Netzwerkzugriffs reproduzierbar beobachten. Android garantiert keinen festen Ausführungszeitpunkt.

| Optionaler Test | Durchführung | Erwartung |
|---|---|---|
| H-F | Netzwerk im Emulator abschalten, einmaligen Worker planen, danach Netz einschalten | Auftrag wartet; später `SUCCEEDED`; neuer Cache-Zeitstempel. |
| H-G | Mehrfach periodisch einplanen | Ein eindeutiger aktiver periodischer Auftrag. Einmalige und periodische Arbeit haben getrennte Namen. |
| H-H | Arbeit stoppen, Worker-Netzfehler einschalten, einmalig neu planen | Bis zu drei Versuche mit Backoff, danach `FAILED`. KEEP übernimmt keine geänderten Eingaben in bereits aktive Aufträge. |

Die zugehörigen Gerätetests separat starten: `.\gradlew.bat connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=de.mobilitylab.departures.VertiefungWorkManagerTest"`. Vergleicht zum Abschluss den gespeicherten WorkManager-Auftrag mit einer Schleife im `viewModelScope`. Begründet, warum beide keinen sekundengenauen Countdown oder Weckzeitpunkt garantieren.

## Optionaler Ausbau: Deutsche Bahn Timetables

Die ausgelieferten Projekte verwenden bewusst `DemoSource`, keine echten Bahnzeiten. Eine produktive DB-Anbindung ist **nicht aktiviert**. `DepartureSource` ist die Austauschgrenze. Die offizielle API bietet Stationssuche, stündliche Plandaten und Änderungsdaten; aktuelle Abfahrten erfordern deren korrekte Zusammenführung, nicht nur eine einzelne Planabfrage.

Für einen Ausbau: API-Zugang im DB-Portal beschaffen; Zugangsdaten ausschließlich in einem geschützten Backend halten; dort Plan- und Änderungsdaten zusammenführen und einen bereinigten Abfahrtsendpunkt anbieten. Der Kotlin-Client ruft diesen Endpunkt suspendierend auf. Mitternacht, Zeitzone Europe/Berlin, Zugausfälle, Gleiswechsel, Rate Limits und veraltete Daten testen. Keine API-Secrets in APK, Git, Browser oder Abgabe speichern. Datenlizenz und Attribution beachten.

Offizielle Quellen: [DB Timetables](https://developers.deutschebahn.com/db-api-marketplace/apis/product/timetables), [Coroutine Best Practices](https://developer.android.com/kotlin/coroutines/coroutines-best-practices), [FGS-Typen](https://developer.android.com/develop/background-work/services/fgs/service-types), [WorkRequests](https://developer.android.com/develop/background-work/background-tasks/persistent/getting-started/define-work), [AlarmManager](https://developer.android.com/develop/background-work/services/alarms).
