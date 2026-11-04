# Prüfstatus – 5. November 2026

## Geprüfte Umgebung

JDK 17 (lokal Microsoft 17.0.15), Gradle 8.13 mit geprüfter Distributions-SHA256,
Android Gradle Plugin 8.13.0, Kotlin 2.2.20, compile/target SDK 36 und min SDK 26.
Die Gradle-Version wird vom mitgelieferten Wrapper festgelegt.

## Unbearbeiteter Starter

Das separat exportierte Studierendenprojekt wurde ohne lokale SDK-Datei gebaut:
Debug-APK erfolgreich, Android-Test-APK erfolgreich. Alle Testquellen kompilieren.
Die zehn JVM-Akzeptanztests wurden ausgeführt: neun schlagen wegen der offenen
Aufgaben erwartbar fehl, einer besteht bereits (abgelaufene Deadline ergibt null).
Dies ist ein dokumentierter Ausgangszustand; kein Test wurde deaktiviert.

Die Unterrichtstests prüfen Repository und ViewModel. Nach U1–U4 dürfen die
Hausaufgabentests noch rot bleiben. Gefilterte Befehle und die erwarteten
Meilensteine stehen in [README.md](README.md).

## Referenzprüfung der Aufgaben

Das Lehrteam hat die vollständige Referenz mit denselben Testdateien geprüft:

- 10/10 JVM-Tests bestanden: 3 Repository-, 3 ViewModel- und 4 Countdown-Tests.
- 5/5 Android-Tests bestanden, keine übersprungen: 3 Timer- und 2 freiwillige
  WorkManager-Tests auf Pixel-10-Emulator, Android 16 / API 36.
- Geprüft sind unter anderem fortlaufender Timer bei gestoppter Activity,
  Activity-Neuerstellung, ersetzter Timer, Notification-Stop, regulärer Service-Stop,
  erneuter Start, Ablauf mit Abschlussnotification sowie Worker-Retry und eindeutige Planung.

Die vollständige Referenz ist nicht Bestandteil des Studierendenrepositorys.
Ein grüner GitHub-Build prüft zunächst die Kompilierbarkeit, nicht die gelösten Aufgaben.
GitHub Actions selbst wurde noch nicht auf einem Remote ausgeführt.

## Zusätzlich selbst prüfen

Die automatisierten Android-Tests setzen die Notification-Berechtigung vorab.
Ablehnung des Berechtigungsdialogs und Force-stop mit anschließendem Öffnen sind
weiterhin manuelle Fälle. Lifecycle-Statewechsel und Activity-Neuerstellung im Test
ersetzen außerdem nicht jede Geräte-/Herstellervariante. U2-Persistenz und die eigenen
Beobachtungen mit der App im [Testprotokoll](TESTPROTOKOLL-VORLAGE.md) festhalten.
Ein eigener Testlauf muss sich auf euren bearbeiteten Stand beziehen.
