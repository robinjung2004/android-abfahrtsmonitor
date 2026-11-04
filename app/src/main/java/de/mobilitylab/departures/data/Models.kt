package de.mobilitylab.departures.data

data class Departure(val line: String, val destination: String, val epochMillis: Long, val platform: String, val delayMinutes: Int = 0)
data class Board(val departures: List<Departure> = emptyList(), val advisory: String = "Noch keine Daten geladen.", val updatedAt: Long = 0)
interface DepartureSource {
    suspend fun departures(): List<Departure>
    suspend fun advisory(fail: Boolean): String
}
interface BoardCache {
    val boards: kotlinx.coroutines.flow.Flow<Board>
    suspend fun save(board: Board)
}
