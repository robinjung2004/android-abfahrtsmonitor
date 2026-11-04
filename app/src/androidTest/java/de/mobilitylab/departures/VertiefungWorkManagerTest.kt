package de.mobilitylab.departures

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.*
import androidx.work.testing.TestListenableWorkerBuilder
import de.mobilitylab.departures.work.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

/** H4/H5: Freiwillig; diese Klasse gehört nicht zur Prüfung der Kernhausaufgabe. */
@RunWith(AndroidJUnit4::class)
class VertiefungWorkManagerTest {
    private val app: MonitorApp get() = ApplicationProvider.getApplicationContext()

    @Before @After fun cancelScheduledWork() {
        val manager = WorkManager.getInstance(app)
        manager.cancelUniqueWork(RefreshScheduler.PERIODIC).result.get(10, TimeUnit.SECONDS)
        manager.cancelUniqueWork(RefreshScheduler.ONCE).result.get(10, TimeUnit.SECONDS)
    }

    @Test fun workerPersistsAndHandlesRetryLimit() = runBlocking {
        val worker = TestListenableWorkerBuilder<RefreshWorker>(app).build()
        assertEquals(ListenableWorker.Result.success(workDataOf("result" to "Cache aktualisiert")), worker.doWork())
        val board = app.repository.boards.first()
        assertEquals(4, board.departures.size)
        assertTrue(board.updatedAt > 0)
        val data = workDataOf("simulateFailure" to true)
        assertEquals(ListenableWorker.Result.retry(), TestListenableWorkerBuilder<RefreshWorker>(app).setInputData(data).build().doWork())
        assertEquals(ListenableWorker.Result.failure(workDataOf("result" to "Netzfehler nach 3 Versuchen")),
            TestListenableWorkerBuilder<RefreshWorker>(app).setInputData(data).setRunAttemptCount(2).build().doWork())
    }

    @Test fun uniquePeriodicWorkKeepsOneActiveRequest() {
        val manager = WorkManager.getInstance(app)
        RefreshScheduler.schedule(app, true)
        RefreshScheduler.schedule(app, true)
        // Die Abfrage wird nach den beiden Enqueue-Aufträgen im WorkManager ausgeführt.
        val infos = manager.getWorkInfosForUniqueWork(RefreshScheduler.PERIODIC).get(10, TimeUnit.SECONDS)
        assertEquals(1, infos.count { !it.state.isFinished })
    }
}
