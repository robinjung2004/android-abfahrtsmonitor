package de.mobilitylab.departures

import android.app.Application
import de.mobilitylab.departures.data.*

/** Kleine explizite Dependency Injection: dieselbe Repository-Instanz für UI und Worker. */
class MonitorApp : Application() {
    val repository by lazy { DepartureRepository(DemoSource(), DepartureCache(this)) }
}
