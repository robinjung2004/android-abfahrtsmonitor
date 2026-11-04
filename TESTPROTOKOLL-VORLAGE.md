# Eigenes Testprotokoll

Name/Gruppe: …  Datum: …  Android-Version/API: …  Gerät/Emulator: …

Diese Vorlage als `TESTPROTOKOLL.md` kopieren. Unterricht und Hausaufgabe werden im selben Projekt dokumentiert. Anfängliche Platzhalterfehler sind erwartet; für jede bearbeitete Phase die tatsächlichen Ergebnisse eintragen.

## Automatische Prüfungen nach Phase

| Testgruppe | Nach welcher Aufgabe? | Datum / Ergebnis / offener Fehler |
|---|---|---|
| `UnterrichtRepositoryTest` | U1 | … |
| `UnterrichtViewModelTest` | U3/U4 | … |
| `HausaufgabeCountdownTest` | H1 | … |
| `HausaufgabeTimerTest` auf Gerät/Emulator | H2/H3 | … |

Die gefilterten Befehle stehen in `README.md`. U2 wird zusätzlich mit den folgenden Cache-Prüfungen beobachtet. Ein grüner Build allein genügt nicht; die automatischen Prüfungen ersetzen keine manuelle Beobachtung.

## Manuelle Kernprüfungen

| Fall | Erwartung | Eigene Beobachtung | Bestanden / offen |
|---|---|---|---|
| U-A · Laden | Fortschritt, Abfahrten und Cache-Zeitstempel | … | … |
| U-B · Optionaler Fehler | Abfahrten und verständlicher Ersatztext bleiben sichtbar | … | … |
| U-C · Abbruch und Neustart | Kein verspätetes Ergebnis; erneutes Laden funktioniert | … | … |
| U-D · Rotation / Prozessneustart | Kein doppelter Reload; gespeicherte Daten bleiben erhalten | … | … |
| H-A · Home und Rückkehr | Timer bleibt aktiv; Restzeit ist weitergelaufen | … | … |
| H-B · Erneuter Start | Ein aktiver Timer mit neuer Deadline | … | … |
| H-C · Stop aus UI / Notification | Laufende Notification endet; neuer Start möglich | … | … |
| H-D · Ablauf | Inaktiver Zustand, Abschlussnotification, keine weitere Schleife | … | … |
| H-E · Notification abgelehnt | Timer startet nicht; verständliche Rückmeldung | … | … |
| H-I · Force-stop / neues Öffnen | Kein alter aktiver Timer behauptet; Cache bleibt erhalten | … | … |

Für jeden Fall Durchführung und tatsächliche Beobachtung festhalten. Ein offener Fehler darf als offen dokumentiert werden; konkrete Beobachtungen sind aussagekräftiger als ein pauschales „bestanden“.

## Transferfälle T1–T3

Eigene Erklärung beilegen oder hier ergänzen, insgesamt mit den Timergrenzen ungefähr ein bis zwei Seiten:

- **T1:** unabhängige Ergebnisse, `async`/`await`, `Job`/`Deferred`, `suspend` und Rückkehr nach `withContext`.
- **T2:** `viewModelScope`, `lifecycleScope` und `repeatOnLifecycle` bei Home, Rotation und endgültigem Activity-Ende; Compose-Beobachtung einordnen.
- **T3:** Job-Baum, Nutzerabbruch, Pflichtfehler, optionaler Fehler und kooperative Cancellation.

Die vollständigen Szenarien stehen im Aufgabenblatt.

## Entscheidungen

Besitzer des Timers: …
Deadline statt Herunterzählen: …
Warum ein Service trotzdem Main blockieren kann: …
Grenzen bei Geräteschlaf, Force-stop und Neustart: …
Einsatzfall für AlarmManager: …

## Nur bei freiwilliger WorkManager-Vertiefung

H4/H5 und H-F bis H-H werden nicht bewertet. Offen gebliebene WorkManager-Integrationstests gehören nicht zur Kernbewertung.

`VertiefungWorkManagerTest` ausgeführt am / Ergebnis: …

| Fall | Erwartung | Eigene Beobachtung | Bestanden / offen |
|---|---|---|---|
| H-F · Netzwerkbedingung | Auftrag wartet und aktualisiert später den Cache | … | … |
| H-G · Eindeutige Planung | Ein aktiver periodischer Auftrag | … | … |
| H-H · Retry | Höchstens drei Versuche, danach Fehlerstatus | … | … |

Unterschied zwischen WorkManager-Auftrag und ViewModel-Schleife: …

## Eigenen Stand sichern

Unterrichtsstand als Commit gesichert am: …

Optional: U-Ergänzungen des Lehrteams übernommen; welche Dateien/Änderungen und welche eigenen Fehler dadurch verstanden wurden: …

Vor Abgabe Code, dieses Protokoll und eigene Transferantworten gemeinsam committen. Den abschließenden Commit-Hash über `git rev-parse HEAD` ermitteln und im Abgabekanal nennen; er muss nicht in diesem bereits committeten Dokument nachgetragen werden.
