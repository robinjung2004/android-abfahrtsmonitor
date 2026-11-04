package de.mobilitylab.departures

import de.mobilitylab.departures.data.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException

/** U1: Gemeinsames Laden, lokaler optionaler Fehler und strukturierter Abbruch. */
@OptIn(ExperimentalCoroutinesApi::class)
class UnterrichtRepositoryTest {
    private val cache = object : BoardCache {
        override val boards = flowOf(Board())
        override suspend fun save(board: Board) = Unit
    }

    @Test fun loadingIsConcurrentAndOptionalFailurePreservesDepartures() = runTest {
        val repository = DepartureRepository(DemoSource { 1000 }, cache) { 9999 }
        val board = repository.loadBoard(true)
        assertEquals(1200L, currentTime)
        assertEquals(4, board.departures.size)
        assertTrue(board.advisory.contains("nicht verfügbar"))
        assertEquals(9999L, board.updatedAt)
    }

    @Test fun cancellationDoesNotWriteCache() = runTest {
        var written = false
        val checkedCache = object : BoardCache {
            override val boards = flowOf(Board())
            override suspend fun save(board: Board) { written = true }
        }
        val job = launch { DepartureRepository(DemoSource(), checkedCache).refresh() }
        advanceTimeBy(500)
        job.cancelAndJoin()
        advanceUntilIdle()
        assertFalse(written)
    }

    @Test fun requiredFailureCancelsSibling() = runTest {
        var cancelled = false
        val source = object : DepartureSource {
            override suspend fun departures(): List<Departure> {
                delay(100)
                throw IOException("offline")
            }
            override suspend fun advisory(fail: Boolean): String {
                try {
                    delay(1000)
                    return "unreachable"
                } catch (error: CancellationException) {
                    cancelled = true
                    throw error
                }
            }
        }
        try {
            DepartureRepository(source, cache).loadBoard()
            fail("Ein Fehler der Pflichtdaten muss weitergegeben werden.")
        } catch (_: IOException) { }
        assertTrue(cancelled)
    }
}
