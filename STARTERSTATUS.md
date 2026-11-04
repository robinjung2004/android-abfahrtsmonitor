# Starterstatus

Das Projekt soll vor der Bearbeitung bauen und die Oberfläche anzeigen. Eine leere Liste ist zunächst erwartet: „Aktualisieren“ meldet U3, der Timer beendet sich sofort wieder, und die freiwillige WorkManager-Planung tut noch nichts. Dies sind Aufgabenlücken, keine Einrichtungsfehler. Die folgenden Platzhalter sind absichtlich noch ohne Fachfunktion:

- **U1:** `return Board(advisory = "TODO U1: Daten gemeinsam laden")`
- **U2:** `// Eigene Implementierung hier. Noch keine Speicherung.`
- **U3:** `mutableLoad.value = LoadState(message = "TODO U3: Ladeauftrag ergänzen")`
- **U4:** `// Eigene Implementierung hier. Noch kein Abbruch.`
- **H1:** `return 0L // Platzhalter durch die Restzeitberechnung ersetzen.`
- **H2:** `finishTimer("TODO H2: Timerlogik ergänzen")`
- **H3:** `// Eigene Implementierung hier. Scope-Cleanup und Status ergänzen.`
- **H4:** `return Result.failure(workDataOf("result" to "TODO H4: Worker ergänzen"))`
- **H5:** `// Eigene Implementierung hier. Noch keine Arbeit eingeplant.`

Die Tests sind Akzeptanzkriterien und nach Lernphase getrennt:

- Nach U1: `UnterrichtRepositoryTest` soll bestehen; die Oberfläche braucht zusätzlich U2/U3.
- Nach U1–U4: `UnterrichtRepositoryTest` und `UnterrichtViewModelTest` sollen bestehen. Cache, Rotation und Neustart zusätzlich nach Aufgabenblatt prüfen. Die Hausaufgabentests dürfen weiterhin rot sein.
- Nach H1–H3: zusätzlich `HausaufgabeCountdownTest`, die Gerätetests in `HausaufgabeTimerTest` und die manuellen H-Kernprüfungen bestehen.
- Nur bei H4/H5: zusätzlich `VertiefungWorkManagerTest` prüfen. Diese Vertiefung wird nicht bewertet.

Die genauen Befehle stehen in `README.md`. Ein erfolgreicher Build oder ein grüner automatischer Build auf GitHub ist noch keine gelöste Aufgabe. Die Build-Prüfung führt die absichtlich offenen fachlichen Tests nicht automatisch aus.
