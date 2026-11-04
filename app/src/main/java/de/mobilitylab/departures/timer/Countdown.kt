package de.mobilitylab.departures.timer

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object Countdown {
    fun remainingSeconds(deadline: Long, now: Long): Long {
        // TODO-BEGIN H1 | Aus monotoner Deadline berechnen, positiv auf Sekunden aufrunden, mindestens null.
        return 0L // Platzhalter durch die Restzeitberechnung ersetzen.
        // TODO-END H1
    }
}
data class TimerState(val remaining: Long = 0, val label: String = "Kein Timer aktiv", val active: Boolean = false)
object TimerStatus {
    private val mutableState = MutableStateFlow(TimerState())
    val state = mutableState.asStateFlow()
    internal fun update(value: TimerState) { mutableState.value = value }
}
