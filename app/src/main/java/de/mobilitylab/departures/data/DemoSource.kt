package de.mobilitylab.departures.data

import kotlinx.coroutines.delay
import java.io.IOException

/** Synthetische Lehrdaten, keine echten Bahn-Abfahrten. Beide Wartezeiten sind suspensiv. */
class DemoSource(private val now: () -> Long = System::currentTimeMillis) : DepartureSource {
    override suspend fun departures(): List<Departure> {
        delay(1200)
        val t = now()
        return listOf(
            Departure("RE 8", "Stuttgart Hbf", t + 5 * 60_000, "2", 2),
            Departure("S 4", "Öhringen-Cappel", t + 12 * 60_000, "3"),
            Departure("RE 10", "Mannheim Hbf", t + 23 * 60_000, "4", 5),
            Departure("RB 18", "Osterburken", t + 38 * 60_000, "1")
        )
    }
    override suspend fun advisory(fail: Boolean): String {
        delay(800)
        if (fail) throw IOException("Simulierter Hinweisfehler")
        return "Demo: Bitte die Anzeigen am Bahnsteig beachten."
    }
}
