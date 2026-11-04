package de.mobilitylab.departures

import androidx.lifecycle.ViewModelStore
import de.mobilitylab.departures.data.*
import de.mobilitylab.departures.ui.DepartureViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

/** U3/U4: Echtes Repository, kontrollierte Quelle und virtueller Main-Dispatcher. */
@OptIn(ExperimentalCoroutinesApi::class)
class UnterrichtViewModelTest {
    private lateinit var mainDispatcher: TestDispatcher
    private lateinit var viewModels: ViewModelStore

    @Before fun prepareMainDispatcher() {
        mainDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(mainDispatcher)
        viewModels = ViewModelStore()
    }

    @After fun clearViewModelsAndMainDispatcher() {
        try {
            viewModels.clear()
            mainDispatcher.scheduler.runCurrent()
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test fun cancelLoadStopsTheRequestAndAllowsAnotherReload() = runTest(mainDispatcher) {
        val source = ControlledSource()
        val cache = RecordingCache()
        val vm = createViewModel(source, cache)

        vm.reload(false)
        runCurrent()
        assertTrue(vm.load.value.loading)
        val loadingMessage = vm.load.value.message
        advanceTimeBy(500)

        vm.cancelLoad()
        runCurrent()
        assertFalse(vm.load.value.loading)
        assertNotEquals(loadingMessage, vm.load.value.message)
        advanceUntilIdle()
        assertEquals(1, source.cancelledDepartures)
        assertTrue(cache.saved.isEmpty())

        vm.reload(false)
        runCurrent()
        assertTrue("Der ViewModel-Scope muss nach Abbrechen weiter nutzbar sein.", vm.load.value.loading)
        advanceUntilIdle()
        assertFalse(vm.load.value.loading)
        assertEquals(1, cache.saved.size)
        assertEquals("Test 2", cache.saved.single().departures.single().line)
    }

    @Test fun repeatedReloadReplacesTheOldRequest() = runTest(mainDispatcher) {
        val source = ControlledSource()
        val cache = RecordingCache()
        val vm = createViewModel(source, cache)

        vm.reload(false)
        runCurrent()
        advanceTimeBy(500)
        vm.reload(false)
        runCurrent()
        assertEquals(1, source.cancelledDepartures)

        // Der alte Auftrag wäre jetzt fertig; der neue benötigt noch 500 ms.
        advanceTimeBy(700)
        runCurrent()
        assertTrue(cache.saved.isEmpty())
        assertTrue(vm.load.value.loading)
        advanceUntilIdle()
        assertFalse(vm.load.value.loading)
        assertEquals(1, cache.saved.size)
        assertEquals("Test 2", cache.saved.single().departures.single().line)
    }

    @Test fun requiredIOExceptionEndsLoadingAndAllowsRetry() = runTest(mainDispatcher) {
        val source = ControlledSource().apply { failDepartures = true }
        val cache = RecordingCache()
        val vm = createViewModel(source, cache)

        vm.reload(false)
        runCurrent()
        assertTrue(vm.load.value.loading)
        val loadingMessage = vm.load.value.message
        advanceUntilIdle()
        assertFalse(vm.load.value.loading)
        assertTrue(cache.saved.isEmpty())
        assertTrue("Nach einem Fehler ist eine Rückmeldung erforderlich.", vm.load.value.message.isNotBlank())
        assertNotEquals(loadingMessage, vm.load.value.message)
        val failureMessage = vm.load.value.message

        source.failDepartures = false
        vm.reload(false)
        runCurrent()
        assertTrue(vm.load.value.loading)
        advanceUntilIdle()
        assertFalse(vm.load.value.loading)
        assertEquals(1, cache.saved.size)
        assertNotEquals(failureMessage, vm.load.value.message)
    }

    private fun createViewModel(source: DepartureSource, cache: BoardCache): DepartureViewModel {
        val vm = DepartureViewModel(DepartureRepository(source, cache) { 9999L })
        viewModels.put("test", vm)
        return vm
    }

    private class RecordingCache : BoardCache {
        override val boards = MutableStateFlow(Board())
        val saved = mutableListOf<Board>()
        override suspend fun save(board: Board) {
            saved += board
            boards.value = board
        }
    }

    private class ControlledSource : DepartureSource {
        var failDepartures = false
        var cancelledDepartures = 0
        private var requests = 0

        override suspend fun departures(): List<Departure> {
            val request = ++requests
            try {
                delay(1200)
                if (failDepartures) throw IOException("Simulierter Fehler der Pflichtdaten")
                return listOf(Departure("Test $request", "Testziel", 60_000L, "1"))
            } catch (error: CancellationException) {
                cancelledDepartures++
                throw error
            }
        }

        override suspend fun advisory(fail: Boolean): String {
            delay(800)
            if (fail) throw IOException("Simulierter Hinweisfehler")
            return "Testhinweis"
        }
    }
}
