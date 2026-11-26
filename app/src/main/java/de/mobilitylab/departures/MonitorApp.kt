package de.mobilitylab.departures

import android.app.Application
import de.mobilitylab.departures.data.*

/** Kleine explizite Dependency Injection: Die Activity erhält das Repository aus dem Application-Objekt. */
class MonitorApp : Application() {
    val repository by lazy { DepartureRepository(DemoSource(), DepartureCache(this)) }
}
