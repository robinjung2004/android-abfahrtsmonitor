# Abfahrtsmonitor · Hausaufgabe

Dein Projekt enthält jetzt die Musterlösung der Präsenzübung U1–U4. Ergänze einen sichtbaren Abfahrtstimer, der beim Wechsel zum Startbildschirm weiterläuft. Du programmierst H1–H3 selbst und beantwortest die drei Transferfragen in [ANTWORTEN.md](ANTWORTEN.md). Zeitrahmen für die Einzelarbeit: etwa drei Stunden.

Öffne denselben Projektordner in Android Studio und synchronisiere Gradle. JDK 17, Android SDK Platform 36 und Build Tools 36.0.0 werden verwendet. Für die Service- und Benachrichtigungsfunktionen empfiehlt sich ein Gerät oder Emulator mit API 34 oder höher. Die App verwendet lokale Beispieldaten.

## H1–H3 · Timer und Service (70 Punkte)

| Aufgabe | Punkte | Auftrag und erwartetes Verhalten |
|---|---:|---|
| H1 · Restzeit | 20 | Implementiere `remainingSeconds(deadline, now)` in [Countdown.kt](app/src/main/java/de/mobilitylab/departures/timer/Countdown.kt). Beide Werte sind monotone Zeitangaben in Millisekunden. Runde eine positive Restzeit auf die nächste volle Sekunde auf. An und nach der Deadline ist das Ergebnis null Sekunden. Das gilt auch bei einem verspäteten Tick. |
| H2 · Ablauf | 35 | Implementiere den markierten Startblock in [DepartureTimerService.kt](app/src/main/java/de/mobilitylab/departures/timer/DepartureTimerService.kt). Weise `timerJob` eine Coroutine im vorhandenen Service-Scope zu. Ermittle in einer abbrechbaren Schleife mit H1 und `SystemClock.elapsedRealtime()` die Restzeit zur vorbereiteten Deadline. Aktualisiere `TimerStatus` und die laufende Benachrichtigung. Warte zwischen Aktualisierungen mit `delay`. Bei null erscheint eine Abschlussbenachrichtigung und `finishTimer` beendet den Timer. Home und Rückkehr erhalten den Countdown. Ein erneuter Start ersetzt den bisherigen Timer. |
| H3 · Aufräumen | 15 | Ergänze `onDestroy()` im selben Service. Beende den Service-Scope und sorge für einen inaktiven Timerzustand. Stoppen in der App oder über die Benachrichtigung beendet den laufenden Timer. Beim nächsten Start gibt es genau einen neuen Countdown. |

Die Markierungen `TODO-BEGIN H1` bis `H3` zeigen dir die Stellen. Oberfläche, Freigabedialog für Benachrichtigungen, Foreground-Start, Stop-Aktion und Notification-Kanal sind vorbereitet. Ein Force-stop beendet den Timer. Ein automatischer Neustart nach Reboot und eine sekundengenaue Abschlussmeldung im Geräteschlaf gehören nicht zum Funktionsumfang.

Probier „30 s testen“, Home und Rückkehr, einen zweiten Start, Stoppen und den regulären Ablauf aus. Für die Selbstkontrolle stehen diese Befehle im Projektterminal zur Verfügung. Unter Windows ersetze `./gradlew` durch `.\gradlew.bat`.

```sh
./gradlew testDebugUnitTest
./gradlew connectedDebugAndroidTest
```

Für den zweiten Befehl muss ein Emulator laufen oder ein Android-Gerät verbunden sein.

## T1–T3 · Coroutines und Scopes (30 Punkte)

Beantworte die drei kurzen Fragen zu Ergebnissen und Kontext, Lebensdauer sowie Abbruch und Fehlern direkt in [ANTWORTEN.md](ANTWORTEN.md). Für jede Antwort sind zehn Punkte vorgesehen.

## Projekt-ZIP abgeben

Speichere deine Änderungen am Kotlin-Code und an `ANTWORTEN.md`. Führe im Projektordner unter Windows `.\gradlew.bat abgabeZip` oder unter macOS/Linux `./gradlew abgabeZip` aus. Der Befehl packt den aktuellen Arbeitsstand einschließlich uncommittierter Änderungen.

Lade **`build/abgabe/Abfahrtsmonitor-Abgabe.zip`** als Projekt-ZIP im vorgesehenen **ILIAS-Briefkasten** hoch.
