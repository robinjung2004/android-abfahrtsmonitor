# Kotlin-Einstieg für dieses Projekt

Diese Übersicht setzt Programmiererfahrung voraus und hilft euch, den vorbereiteten Code zu lesen. Sie enthält keine vollständige Lösung der U- oder H-Aufgaben. Die Oberfläche und Android-Infrastruktur sind schon vorhanden; beginnt bei den in `AUFGABEN.md` genannten Dateien.

## Werte, Typen und Funktionen

```kotlin
val station: String = "Heilbronn" // diese Variable kann nicht neu zugewiesen werden
var attempts = 0                 // Typ Int wird erkannt; Wert darf sich ändern
attempts += 1

fun label(name: String, platform: Int): String {
    return "$name · Gleis $platform"
}

fun double(value: Int): Int = value * 2 // Funktion mit einem Ausdruck
```

Typen stehen hinter dem Namen. `val` macht ein referenziertes Objekt nicht automatisch unveränderlich. `Long` ist eine 64-Bit-Ganzzahl; `1_200L` ist ein gut lesbares Long-Literal. `Unit` bedeutet, dass kein fachlicher Rückgabewert geliefert wird. Eine Funktion ohne Rückgabewert muss kein `return` enthalten.

Parameter können Standardwerte haben. Bei `fun show(message: String, highlighted: Boolean = false)` sind sowohl `show("Bereit")` als auch `show(message = "Bereit", highlighted = true)` möglich. Benannte Argumente helfen beim Lesen.

## Lambdas und Funktionswerte

```kotlin
val names = listOf("Heilbronn", "Stuttgart")
val shortNames = names.filter { name -> name.length < 10 }
val lengths = names.map { it.length }
```

`{ name -> ... }` ist eine Funktion als Wert. Bei genau einem Parameter darf er `it` heißen, ohne ihn ausdrücklich zu deklarieren. Ist das letzte Argument einer Funktion eine Lambda, darf sie hinter den runden Klammern stehen; bei sonst keinen Argumenten können diese entfallen. Deshalb seht ihr Aufrufe wie `launch { ... }`, `withContext(cpu) { ... }` oder `Button(onClick = { ... })`.

Die geschweiften Klammern allein starten weder einen Thread noch eine Coroutine. Entscheidend ist die aufgerufene Funktion. `vm::cancelLoad` übergibt eine Referenz auf eine vorhandene Funktion, statt sie sofort auszuführen.

## Null und sichere Zugriffe

```kotlin
var selectedName: String? = null
val length: Int? = selectedName?.length
val displayName = selectedName ?: "Keine Auswahl"
```

`String?` erlaubt zusätzlich `null`. `?.` führt den Zugriff nur bei einem vorhandenen Wert aus; `?:` liefert einen Ersatzwert, wenn links `null` steht. Im Projekt bedeutet `refreshJob: Job?`, dass noch kein Ladejob vorhanden sein kann. `refreshJob?.cancel()` fordert den Abbruch nur an, wenn ein Job vorhanden ist. Ein `!!` erzwingt einen Zugriff und kann bei `null` abstürzen; ihr benötigt es für diese Aufgaben nicht.

## Zustände lesen und ersetzen

```kotlin
data class CounterState(val count: Int = 0, val message: String = "Bereit")
val before = CounterState()
val after = before.copy(count = 1)
```

Eine `data class` hält Werte. `copy` erstellt ein neues Objekt und übernimmt nicht angegebene Werte. In `DepartureViewModel` gibt es vorbereitete Zustandsobjekte; die Zuweisung an `mutableLoad.value` veröffentlicht einen neuen Ladezustand. Die UI beobachtet diesen Zustand bereits. Ihr müsst dafür keine Compose-Oberfläche schreiben.

## Suspendierende Funktionen und Jobs lesen

| Schreibweise | Bedeutung beim Lesen dieses Projekts |
|---|---|
| `suspend fun ...` | Die Funktion darf suspendierende Funktionen aufrufen. Das allein startet keine Coroutine und wechselt keinen Thread. |
| `launch { ... }` | Startet einen Auftrag in einem Scope; liefert einen `Job`. |
| `async { ... }` | Startet einen Auftrag mit Ergebnis; liefert ein `Deferred<T>`. |
| `await()` | Wartet bei Bedarf suspendierend auf dieses Ergebnis. |
| `withContext(...) { ... }` | Führt einen Block im angegebenen Kontext aus und liefert dessen Ergebnis an den Aufrufer zurück. |
| `job.cancel()` | Fordert kooperativen Abbruch dieses Jobs an; wartet selbst nicht auf seinen Abschluss. |

Lest bei einem Aufruf stets mit: **Welcher Scope besitzt den Auftrag? Was ist sein Ergebnis? Was geschieht bei Abbruch?** Die Reihenfolge der Aufrufe und die Fehlerbehandlung ergänzt ihr selbst in den Aufgaben.

## Fehler und Cancellation auseinanderhalten

Kotlin verwendet `try`, `catch` und `finally`. `catch (e: IOException)` verarbeitet diesen Fehlertyp; ein pauschales `catch (e: Exception)` fängt wesentlich mehr ab. Cancellation ist ein Kontrollsignal und darf in den Aufgaben nicht als gewöhnlicher Ladefehler verschluckt werden. Wird eine `CancellationException` ausdrücklich gefangen, gibt `throw e` sie weiter.

Ein `finally`-Block läuft auch bei Abbruch. Prüft deshalb bei Zustandsänderungen, ob sie noch zum aktuellen Auftrag gehören. `delay(...)` ist suspendierend und abbrechbar; eine CPU-Schleife braucht eigene kooperative Abbruchstellen. Die konkrete Lösung und Begründung gehören zu U3/U4 und T3.

## In Android Studio orientieren

- Im Projektfenster die Ansicht **Project** wählen, um Dateien und die Markdown-Unterlagen direkt zu finden.
- Mit der Projektsuche nach `TODO-BEGIN` oder der konkreten ID suchen.
- Mit „Go to Declaration“ zu einer Klasse oder Funktion springen und ihre Signatur lesen, bevor ihr sie verwendet.
- Ein rot unterstrichener Name kann einen fehlenden Import anzeigen. Die IDE bietet „Import“ als Korrektur an; prüft dabei den Paketnamen.
- Tests über das Symbol neben der jeweiligen Testklasse starten oder die gefilterten Befehle aus `README.md` verwenden. Ein Test aus einer späteren Phase darf noch fehlschlagen.
