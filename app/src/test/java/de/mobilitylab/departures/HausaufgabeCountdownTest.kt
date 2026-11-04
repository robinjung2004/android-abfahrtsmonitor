package de.mobilitylab.departures

import de.mobilitylab.departures.timer.Countdown
import org.junit.Assert.assertEquals
import org.junit.Test

/** H1: Die Restzeit folgt der Deadline, nicht der Anzahl ausgeführter Ticks. */
class HausaufgabeCountdownTest {
    @Test fun positiveFractionsRoundUpIncludingOneMillisecond() {
        assertEquals(1L, Countdown.remainingSeconds(1001, 1000))
        assertEquals(2L, Countdown.remainingSeconds(2001, 1000))
    }

    @Test fun exactWholeSecondsStayUnchanged() {
        assertEquals(1L, Countdown.remainingSeconds(2000, 1000))
        assertEquals(61L, Countdown.remainingSeconds(62000, 1000))
    }

    @Test fun reachedAndExpiredDeadlinesReturnZero() {
        assertEquals(0L, Countdown.remainingSeconds(1000, 1000))
        assertEquals(0L, Countdown.remainingSeconds(1000, 2000))
    }

    @Test fun delayedTicksUseTheCurrentTime() {
        val deadline = 31_000L
        assertEquals(30L, Countdown.remainingSeconds(deadline, 1000))
        assertEquals(26L, Countdown.remainingSeconds(deadline, 5500))
        assertEquals(1L, Countdown.remainingSeconds(deadline, 30_999))
        assertEquals(0L, Countdown.remainingSeconds(deadline, 35_000))
    }
}
