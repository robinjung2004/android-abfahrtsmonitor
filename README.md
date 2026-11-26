# Abfahrtsmonitor · Präsenzübung

Du baust eine Abfahrtstafel für Heilbronn Hbf. Beim Aktualisieren lädt die App vier Beispielabfahrten und einen Verkehrshinweis, speichert das Ergebnis und erlaubt dir, einen laufenden Abruf abzubrechen. Bearbeite U1–U4 auf deinem eigenen Gerät.

## Projekt starten

1. Klone das Kursprojekt einmal mit `git clone https://github.com/robinjung2004/android-abfahrtsmonitor.git`. Öffne den Ordner `android-abfahrtsmonitor` mit `settings.gradle.kts` in Android Studio.
2. Verwende JDK 17 für Gradle und installiere Android SDK Platform 36 sowie Build Tools 36.0.0. Für den ersten Gradle Sync brauchst du Internet. Starte die App auf einem Gerät oder Emulator mit API 26 oder höher.
3. Im Starter ist die Liste leer. Beim Aktualisieren erscheint zunächst der Platzhalter für den Ladeauftrag. Suche in Android Studio nach `TODO-BEGIN`. Ersetze in dieser Reihenfolge den Inhalt der vier markierten Blöcke.

## Abfahrten laden, speichern und abbrechen

| Aufgabe | Datei | Was du programmierst |
|---|---|---|
| U1 | [DepartureRepository.kt](app/src/main/java/de/mobilitylab/departures/data/DepartureRepository.kt) | Starte in `coroutineScope` die beiden Abfragen für Abfahrten und Hinweis jeweils mit `async`, bevor du auf das erste Ergebnis wartest. Baue aus den Ergebnissen und `now()` ein `Board`. Fange die `IOException` des optionalen Hinweises in dessen Kind ab und setze einen Ersatztext mit „nicht verfügbar“. Fehler beim Laden der Abfahrten werden weitergegeben. |
| U2 | [DepartureCache.kt](app/src/main/java/de/mobilitylab/departures/data/DepartureCache.kt) | Wandle das Board mit `encode(board)` innerhalb von `withContext(cpu)` in JSON um. Speichere das Ergebnis anschließend mit `store.edit` unter dem vorbereiteten `key`. |
| U3 | [DepartureViewModel.kt](app/src/main/java/de/mobilitylab/departures/ui/DepartureViewModel.kt) | Beende den bisherigen `refreshJob`. Starte den neuen im `viewModelScope`, merke ihn dir und setze den Ladezustand. Rufe `repository.refresh(failAdvisory)` auf. Melde Erfolg oder `IOException` mit beendetem Ladezustand. Wirf eine `CancellationException` weiter. |
| U4 | [DepartureViewModel.kt](app/src/main/java/de/mobilitylab/departures/ui/DepartureViewModel.kt) | Brich in `cancelLoad()` den aktuellen `refreshJob` ab. Setze `loading` auf `false` und zeige eine Abbruchmeldung. Der nächste Reload muss wieder funktionieren. |

## Ergebnis ausprobieren

- **Laden:** Beim Aktualisieren erscheinen zuerst die Ladeanzeige, danach vier Abfahrten und ein Cache-Zeitstempel. Schalte den simulierten Hinweisfehler ein und lade erneut. Die Abfahrten bleiben nutzbar, der Hinweis zeigt den Ersatztext.
- **Abbrechen:** Starte einen Reload und brich ihn sofort ab. Nach zwei Sekunden sollte weiterhin die Abbruchmeldung sichtbar sein. Starte erneut und lass diesen Reload zu Ende laufen.
- **Lebensdauer:** Drehe während des Ladens das Gerät. Der bestehende Auftrag läuft weiter. Schließe die App nach erfolgreichem Laden und starte sie erneut. Die gespeicherten Abfahrten und ihr Zeitstempel sind wieder sichtbar.

Zum Prüfen von U1 kannst du im Projektterminal `./gradlew testDebugUnitTest --tests "*UnterrichtRepositoryTest"` ausführen, nach U3/U4 `./gradlew testDebugUnitTest --tests "*Unterricht*"`. Unter Windows verwendest du `.\gradlew.bat` anstelle von `./gradlew`.

## Kursstand später aktualisieren

Speichere deine Änderungen in Android Studio und bleibe mit deiner uncommitteten Präsenzlösung auf `main`. Sobald das Lehrteam das Update freigibt, führe im Terminal dieses Projekts beide Befehle nacheinander aus:

```sh
git stash push -u -m "Meine Präsenzübung"
git pull --ff-only
```

Der erste Befehl legt deine bearbeiteten und neu angelegten Projektdateien lokal zur Seite. Wenn Git „No local changes to save“ meldet, ist dein Arbeitsverzeichnis bereits sauber. Der zweite Befehl holt die Freigabe in denselben Projektordner. Lass die Sicherung im Stash. Mit `git stash list` findest du sie wieder, mit `git stash show -p` kannst du die Änderungen im jüngsten Eintrag ansehen.
