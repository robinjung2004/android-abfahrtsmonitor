package de.mobilitylab.departures.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.mobilitylab.departures.data.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException

data class LoadState(val loading: Boolean = false, val message: String = "Bereit zum Laden")
class DepartureViewModel(private val repository: DepartureRepository) : ViewModel() {
    val board = repository.boards.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Board())
    private val mutableLoad = MutableStateFlow(LoadState())
    val load = mutableLoad.asStateFlow()
    private var refreshJob: Job? = null

    fun reload(failAdvisory: Boolean) {
        // TODO-BEGIN U3 | Alten Ladejob abbrechen; neuen im viewModelScope starten; Status und Fehler behandeln.
        // 1. Den bisher gemerkten refreshJob abbrechen (falls vorhanden).
        // 2. Einen neuen Job im viewModelScope starten und in refreshJob merken.
        // 3. Im Job: LoadState mit loading=true setzen, dann repository.refresh(failAdvisory) aufrufen.
        // 4. Erfolg und IOException mit loading=false melden; CancellationException weitergeben.
        // Dieses Gerüst zeigt absichtlich nur den Ausgangszustand. Schritte gemeinsam aufbauen.
        mutableLoad.value = LoadState(message = "TODO U3: Ladeauftrag ergänzen")
        // TODO-END U3
    }
    fun cancelLoad() {
        // TODO-BEGIN U4 | Nur refreshJob stoppen, nicht viewModelScope; erneutes Laden ermöglichen.
        // Eigene Implementierung hier. Noch kein Abbruch.
        // TODO-END U4
    }
}
