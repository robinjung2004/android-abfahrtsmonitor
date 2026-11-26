package de.mobilitylab.departures.data

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.IOException

class DepartureRepository(private val source: DepartureSource, private val cache: BoardCache,
    private val now: () -> Long = System::currentTimeMillis) {
    val boards = cache.boards

    suspend fun loadBoard(failAdvisory: Boolean = false): Board {
        return coroutineScope {
            val departures = async { source.departures() }
            val advisory = async {
                try { source.advisory(failAdvisory) }
                catch (_: IOException) { "Hinweise nicht verfügbar; Abfahrten bleiben sichtbar." }
            }
            Board(departures.await(), advisory.await(), now())
        }
    }
    suspend fun refresh(failAdvisory: Boolean = false) {
        cache.save(loadBoard(failAdvisory))
    }
}
