package de.mobilitylab.departures.work

import android.content.Context
import androidx.work.*
import de.mobilitylab.departures.MonitorApp
import kotlinx.coroutines.CancellationException
import java.io.IOException

class RefreshWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        // TODO-BEGIN H4 | Repository aktualisieren; IOException begrenzt erneut versuchen; Cancellation weitergeben.
        return Result.failure(workDataOf("result" to "TODO H4: Worker ergänzen"))
        // TODO-END H4
    }
}
