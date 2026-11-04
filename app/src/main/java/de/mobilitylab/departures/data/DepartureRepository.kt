package de.mobilitylab.departures.data

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.IOException

class DepartureRepository(private val source: DepartureSource, private val cache: BoardCache,
    private val now: () -> Long = System::currentTimeMillis) {
    val boards = cache.boards

    suspend fun loadBoard(failAdvisory: Boolean = false): Board {
        // TODO-BEGIN U1 | Beide Abfragen gleichzeitig starten; optionalen IOException-Hinweis lokal behandeln.
        // Gemeinsamer Besitzer: coroutineScope. Welche beiden source-Funktionen sind unabhängig?
        // Erst beide async-Kinder starten, danach ihre Ergebnisse für Board abwarten.
        // Nur der optionale Hinweis erhält bei IOException einen Ersatztext mit "nicht verfügbar".
        // Board benötigt auch den Zeitpunkt now(). Prüfen: UnterrichtRepositoryTest.
        return Board(advisory = "TODO U1: Daten gemeinsam laden")
        // TODO-END U1
    }
    suspend fun refresh(failAdvisory: Boolean = false) {
        cache.save(loadBoard(failAdvisory))
    }
}
