package de.mobilitylab.departures.timer

import android.app.*
import android.content.*
import android.content.pm.ServiceInfo
import android.os.*
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import de.mobilitylab.departures.MainActivity
import de.mobilitylab.departures.R
import kotlinx.coroutines.*

class DepartureTimerService : Service() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var timerJob: Job? = null
    private val notifications get() = getSystemService(NotificationManager::class.java)

    override fun onCreate() {
        super.onCreate()
        notifications.createNotificationChannel(NotificationChannel(CHANNEL, "Abfahrtstimer", NotificationManager.IMPORTANCE_DEFAULT))
    }
    override fun onBind(intent: Intent?): IBinder? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == STOP) { finishTimer("Timer gestoppt"); return START_NOT_STICKY }
        if (intent?.action != START) { stopSelf(); return START_NOT_STICKY }
        val seconds = intent.getLongExtra("seconds", 30).coerceIn(1, 3600)
        val label = intent.getStringExtra("label") ?: "Abfahrt"
        val deadline = SystemClock.elapsedRealtime() + seconds * 1000
        // Vorgegebene Plattform-Infrastruktur: sofortige sichtbare Notification ist zwingend.
        ServiceCompat.startForeground(this, ID, notification(label, seconds),
            if (Build.VERSION.SDK_INT >= 34) ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE else 0)
        timerJob?.cancel()
        // TODO-BEGIN H2 | Eigene Coroutine starten, Deadline auswerten, State/Notification aktualisieren, bei null beenden.
        finishTimer("TODO H2: Timerlogik ergänzen")
        // TODO-END H2
        return START_NOT_STICKY
    }
    private fun openApp() = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    private fun notification(label: String, seconds: Long): Notification {
        val stop = PendingIntent.getService(this, 1, Intent(this, DepartureTimerService::class.java).setAction(STOP), PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        return NotificationCompat.Builder(this, CHANNEL).setSmallIcon(R.drawable.ic_train)
            .setContentTitle("$label · ${seconds / 60}:${(seconds % 60).toString().padStart(2, '0')}")
            .setContentText("Abfahrtstimer läuft · zum Öffnen antippen").setContentIntent(openApp())
            .setOngoing(true).setOnlyAlertOnce(true).addAction(0, "Stoppen", stop).build()
    }
    private fun finishTimer(message: String) {
        timerJob?.cancel()
        TimerStatus.update(TimerState(label = message))
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        stopSelf()
    }
    override fun onDestroy() {
        // TODO-BEGIN H3 | Alle Service-Coroutines freigeben und UI-Zustand auf inaktiv setzen.
        // Eigene Implementierung hier. Scope-Cleanup und Status ergänzen.
        // TODO-END H3
        super.onDestroy()
    }
    companion object {
        const val START = "de.mobilitylab.START_TIMER"
        const val STOP = "de.mobilitylab.STOP_TIMER"
        private const val CHANNEL = "departure_timer"
        private const val ID = 10
        private const val DONE_ID = 11
    }
}
