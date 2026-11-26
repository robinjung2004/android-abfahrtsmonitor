# Meine Antworten zu Coroutines und Scopes

Schreibe deine Antwort jeweils direkt unter die Frage in eigenen Worten.

## T1 · Ergebnisse und Kontext (10 Punkte)

Die App lädt Abfahrten und Verkehrshinweis aus zwei unabhängigen Quellen. Warum startest du beide `async`-Aufträge vor dem ersten `await`, und wie erhältst du ihre Ergebnisse? In welchem Kontext läuft die aufrufende Coroutine nach `withContext(Dispatchers.Default)` weiter?

**Meine Antwort**

…

## T2 · Lebensdauer (10 Punkte)

Der Reload läuft im `viewModelScope` eines Activity-gebundenen ViewModels, die Oberfläche beobachtet den Zustand mit `collectAsStateWithLifecycle()`. Was passiert mit dem laufenden Reload und dieser Beobachtung, wenn du Home drückst und zurückkehrst? Was passiert mit beiden bei einer Rotation, solange der Prozess weiterläuft?

**Meine Antwort**

…

## T3 · Abbruch und Fehler (10 Punkte)

Ein Reload startet in `coroutineScope` zwei `async`-Kinder für Abfahrten und Verkehrshinweis. Was passiert mit den Kindern, wenn der Benutzer den Ladejob abbricht? Weshalb fängst du eine `IOException` beim optionalen Hinweis im betreffenden Kind ab und gibst eine `CancellationException` weiter?

**Meine Antwort**

…
