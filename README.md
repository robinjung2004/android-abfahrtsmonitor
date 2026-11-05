# Abfahrtsmonitor – euer Workshop-Projekt

Ihr entwickelt **dieselbe Android-App im Unterricht und zuhause weiter**. Programmiererfahrung wird vorausgesetzt; Kotlin und Android dürfen neu sein. Oberfläche, Datenmodell und Android-Infrastruktur sind vorbereitet. Ihr bearbeitet neun markierte Kotlin-Blöcke: U1–U4 im Unterricht, H1–H3 als Hausaufgabe und H4/H5 nur freiwillig.

## 1. Projekt holen und starten

Die Kursleitung gibt euch die GitHub-Adresse des **Studierendenrepositorys**. In Android Studio **Get from VCS / Clone Repository** wählen und diese Adresse einfügen. Alternativ im Terminal klonen; `<KURS-REPOSITORY-URL>` vollständig durch die mitgeteilte Adresse ersetzen:

```text
git clone https://github.com/robinjung2004/android-abfahrtsmonitor.git
```

In Android Studio den geklonten Ordner **Abfahrtsmonitor** öffnen. Im Studierendenrepository liegen `settings.gradle.kts`, `gradlew.bat` und `app/` direkt darin. Falls ihr stattdessen das Lehrpaket oder Starter-ZIP verwendet, öffnet den enthaltenen Starterordner mit diesen Dateien.

Vor der Veranstaltung vorbereiten:

1. Gradle JDK auf **JDK 17** stellen (Settings → Build Tools → Gradle).
2. **Android SDK Platform 36**, Build Tools 36 und einen Emulator installieren. Der erste Gradle Sync braucht Internet; `local.properties` erzeugt Android Studio selbst.
3. App-Konfiguration auf einem Gerät/Emulator mit **API 26 oder höher** starten; für die Service- und Benachrichtigungsprüfungen API 34+ verwenden.
4. Prüfen, dass die Oberfläche „Abfahrtsmonitor“ erscheint. **Eine leere Abfahrtsliste ist anfangs richtig:** „Aktualisieren“ zeigt den Hinweis zu U3. Ein Timerstart endet vor H1–H3 sofort, WorkManager plant vor H4/H5 noch nichts ein. Der Build muss funktionieren; die noch offenen Aufgaben sollen ihre Akzeptanztests zunächst nicht bestehen.

Build im Terminal des Projektordners unter Windows:

```powershell
.\gradlew.bat assembleDebug
```

Unter macOS/Linux einmal `chmod +x gradlew` ausführen und in allen Befehlen `.\gradlew.bat` durch `./gradlew` ersetzen. Falls Einrichtung oder Start scheitern, Fehlermeldung und Android-Version vor der Veranstaltung an die Kursleitung weitergeben.

## 2. Im Unterricht bearbeiten

Lest bei Bedarf zuerst [KOTLIN-EINSTIEG.md](KOTLIN-EINSTIEG.md). [AUFGABEN.md](AUFGABEN.md) beschreibt Aufträge und Abnahme; [STARTERSTATUS.md](STARTERSTATUS.md) erklärt die anfänglichen Platzhalter.

In Android Studio nach **TODO-BEGIN** suchen. Nur die Inhalte zwischen den Markierungen ersetzen und IDs erhalten. Reihenfolge: **U1 → U2 gemeinsam → U3/U4**. Nach U1 könnt ihr das Repository bereits ohne fertige Oberfläche prüfen:

```powershell
.\gradlew.bat testDebugUnitTest --tests "*UnterrichtRepositoryTest"
```

Nach U1–U4 alle Unterrichtstests ausführen und in der App Laden, frühen Abbruch und erneutes Laden prüfen:

```powershell
.\gradlew.bat testDebugUnitTest --tests "*Unterricht*"
```

Die Unterrichtstests prüfen Repository und ViewModel. **U2 zusätzlich sichtbar prüfen:** Nach einem erfolgreichen Reload muss ein Cache-Zeitstempel erscheinen; nach App-Neustart bleiben die gespeicherten Abfahrten erhalten.

## 3. Im selben Projekt zuhause weiterarbeiten

Sichert euren eigenen Stand am Ende der Arbeitsphase. In Android Studio unter Commit die geänderten Quelldateien prüfen und committen. Im Terminal beispielsweise:

```text
git status
git add app/src/main
git commit -m "Eigenen Unterrichtsstand sichern"
```

Ein lokaler Commit genügt zum Sichern; für das Kursrepository braucht ihr dazu keine Schreibrechte. **Nicht in die zentrale Kursvorlage pushen.** Den eigenen Abgabeweg nennt die Kursleitung.

Sind U1–U4 noch offen, ergänzt sie zuhause vor H1–H3. Bei Freigabe des Lehrteams enthält **Abfahrtsmonitor-Praesenzstand.zip** genau die drei Dateien mit fertigen U1–U4 und eine `UEBERNAHME.md`. Sichert zuerst euren eigenen Commit, vergleicht die Dateien und übernehmt die benötigten U-Ergänzungen in **dieses Projekt**. H1–H5 bleiben dabei offen. Es ist kein Wechsel zur vollständigen Musterlösung und kein zweiter Clone nötig.

Für die Hausaufgabe H1–H3, die eigenen Transferantworten T1–T3 und [TESTPROTOKOLL-VORLAGE.md](TESTPROTOKOLL-VORLAGE.md) bearbeiten. Die Vorlage als `TESTPROTOKOLL.md` kopieren. Nach H1:

```powershell
.\gradlew.bat testDebugUnitTest --tests "*HausaufgabeCountdownTest"
```

Nach H2/H3 bei angeschlossenem Gerät oder laufendem Emulator:

```powershell
.\gradlew.bat connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=de.mobilitylab.departures.HausaufgabeTimerTest"
```

Für die Kernabgabe müssen zusätzlich die manuellen Fälle im Aufgabenblatt geprüft werden. Vor dem Abgeben erneut committen; der Commit muss auch Testprotokoll und eigene Antworten enthalten. Format und Umfang stehen unter „Abgabe und Bewertung“ in [AUFGABEN.md](AUFGABEN.md).

## Testphasen und erwarteter Stand

| Stand | Erwartung |
|---|---|
| Unbearbeiteter Starter | Build erfolgreich; fachliche Tests schlagen wegen Platzhaltern fehl. |
| U1 fertig | `UnterrichtRepositoryTest` grün. |
| U1–U4 fertig | `Unterricht*` grün und manuelle U-Prüfungen bestanden; H-Tests dürfen noch rot sein. |
| H1–H3 fertig | Unterrichtstests, `HausaufgabeCountdownTest`, `HausaufgabeTimerTest` und manuelle Kernprüfungen bestanden. |
| H4/H5 freiwillig fertig | Zusätzlich `VertiefungWorkManagerTest` grün. Keine Voraussetzung für die Kernabgabe. |

Alle JVM-Kerntests gemeinsam: `.\gradlew.bat testDebugUnitTest --tests "*Unterricht*" --tests "*Hausaufgabe*"`. Ohne Filter führt `testDebugUnitTest` auch die noch offene H1-Aufgabe aus. `connectedDebugAndroidTest` ohne Filter enthält zusätzlich die freiwillige WorkManager-Vertiefung. Ein roter Test aus einer späteren Phase ist kein Fehler der bereits abgeschlossenen Phase.

Auf GitHub prüft der automatische Workflow bei Push/Pull Request, ob App und Tests gebaut werden können. Er führt die offenen Aufgabentests dabei absichtlich nicht aus: **Ein grüner Build bedeutet nicht, dass die Aufgaben gelöst sind.** Unter Actions lässt sich die fachliche Prüfung mit `pruefung=unterricht` oder `pruefung=kernhausaufgabe` manuell starten. Die Kernhausaufgaben-Auswahl führt die JVM-Tests aus; Timer-Gerätetests und die manuellen Android-Fälle prüft ihr zusätzlich lokal.

Freiwillige WorkManager-Prüfung:

```powershell
.\gradlew.bat connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=de.mobilitylab.departures.VertiefungWorkManagerTest"
```

## Orientierung im Code

| Datei unter `app/src/main/java/de/mobilitylab/departures/` | Zuständigkeit |
|---|---|
| `MainActivity.kt` | Fertige Compose-Oberfläche; beobachtet Zustände lifecycleabhängig. |
| `ui/DepartureViewModel.kt` | U3/U4: Ladeauftrag und Rückmeldungen der Oberfläche. |
| `data/DepartureRepository.kt` | U1: unabhängige Abfragen gemeinsam ausführen. |
| `data/DepartureCache.kt` | U2: Umwandlung und vorbereitete DataStore-Speicherung. |
| `data/DemoSource.kt` | Synthetische Abfahrten nach 1.200 ms, Hinweis nach 800 ms. |
| `timer/Countdown.kt`, `timer/DepartureTimerService.kt` | H1–H3: Restzeit, Service-Ablauf und Cleanup. |
| `work/` | H4/H5: freiwillige WorkManager-Vertiefung. |

Für alle Pflichtaufgaben reichen die enthaltenen Beispieldaten; es gibt keine aktive Bahn-API und keine API-Schlüssel. Der fertig ergänzte Lehrtimer unterstützt Home und Rückkehr, garantiert aber keine sekundengenaue Abschlussmeldung im Geräteschlaf und keinen Neustart nach Force-stop oder Reboot. Die vorbereitete Android-Infrastruktur bleibt erhalten; ihre Grenzen erklärt das Aufgabenblatt.
