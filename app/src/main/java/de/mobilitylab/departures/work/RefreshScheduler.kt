package de.mobilitylab.departures.work

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object RefreshScheduler {
    const val PERIODIC = "departure-periodic-refresh"
    const val ONCE = "departure-once-refresh"
    fun schedule(context: Context, periodic: Boolean, simulateFailure: Boolean = false) {
        // TODO-BEGIN H5 | Netzwerkbedingung, exponentielles Backoff und eindeutige Arbeit anlegen; periodisch mindestens 15 min.
        // Eigene Implementierung hier. Noch keine Arbeit eingeplant.
        // TODO-END H5
    }
    fun stop(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(PERIODIC)
        WorkManager.getInstance(context).cancelUniqueWork(ONCE)
    }
}
