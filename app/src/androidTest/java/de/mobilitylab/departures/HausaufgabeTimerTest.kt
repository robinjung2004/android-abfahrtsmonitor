package de.mobilitylab.departures

import android.app.NotificationManager
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import de.mobilitylab.departures.timer.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/** H1–H3: Läuft unabhängig von der freiwilligen WorkManager-Vertiefung. */
@RunWith(AndroidJUnit4::class)
class HausaufgabeTimerTest {
    private val app: MonitorApp get() = ApplicationProvider.getApplicationContext()
    private val notifications get() = app.getSystemService(NotificationManager::class.java)

    @Before fun prepareTimerAndPermission() {
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            val fd = InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand(
                "pm grant ${app.packageName} android.permission.POST_NOTIFICATIONS")
            android.os.ParcelFileDescriptor.AutoCloseInputStream(fd).bufferedReader().use { it.readText() }
        }
        stopTimerAndClearNotifications()
    }

    @After fun stopTimerAndClearNotifications() {
        app.stopService(Intent(app, DepartureTimerService::class.java))
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        notifications.cancelAll()
    }

    private fun startTimer(seconds: Long) {
        ContextCompat.startForegroundService(app, Intent(app, DepartureTimerService::class.java)
            .setAction(DepartureTimerService.START).putExtra("seconds", seconds).putExtra("label", "Integrationstest"))
    }

    @Test fun timerSurvivesStoppedAndRecreatedActivityAndNotificationCanStopIt() = runBlocking {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { startTimer(20) }
            withTimeout(5000) { TimerStatus.state.first { it.active } }
            val first = TimerStatus.state.value.remaining
            scenario.moveToState(Lifecycle.State.CREATED)
            delay(2200)
            assertTrue(TimerStatus.state.value.active)
            assertTrue(TimerStatus.state.value.remaining < first)
            scenario.moveToState(Lifecycle.State.RESUMED)
            scenario.recreate()
            assertTrue("Ein Activity-Neustart darf den Service-Timer nicht beenden.", TimerStatus.state.value.active)
            assertTrue(TimerStatus.state.value.remaining < first)

            scenario.onActivity { startTimer(8) }
            withTimeout(5000) { TimerStatus.state.first { it.active && it.remaining <= 8 } }
            val ongoing = notifications.activeNotifications.first { it.id == 10 }
            ongoing.notification.actions.first().actionIntent.send()
            withTimeout(5000) { TimerStatus.state.first { !it.active } }
            withTimeout(3000) {
                while (notifications.activeNotifications.any { it.id == 10 }) delay(50)
            }
            assertFalse(notifications.activeNotifications.any { it.id == 10 })
        }
    }

    @Test fun stoppingTheServiceCleansUpAndAllowsANewTimer() = runBlocking {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { startTimer(20) }
            withTimeout(5000) { TimerStatus.state.first { it.active } }
            scenario.onActivity { it.stopService(Intent(it, DepartureTimerService::class.java)) }
            withTimeout(5000) { TimerStatus.state.first { !it.active } }
            // Nach mehr als einem Tick darf der alte Job den Zustand nicht reaktivieren.
            delay(1200)
            assertFalse(TimerStatus.state.value.active)
            scenario.onActivity { startTimer(8) }
            withTimeout(5000) { TimerStatus.state.first { it.active && it.remaining <= 8 } }
            assertTrue(TimerStatus.state.value.active)
        }
    }

    @Test fun timerCompletesAndPostsCompletion() = runBlocking {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { startTimer(2) }
            withTimeout(5000) { TimerStatus.state.first { it.active } }
            withTimeout(6000) { TimerStatus.state.first { !it.active } }
            withTimeout(3000) {
                while (notifications.activeNotifications.none { it.id == 11 }) delay(50)
            }
            assertTrue(notifications.activeNotifications.any { it.id == 11 })
        }
    }
}
