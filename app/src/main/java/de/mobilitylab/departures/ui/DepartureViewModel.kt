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
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            mutableLoad.value = LoadState(true, "Abfahrten und Hinweise werden geladen …")
            try {
                repository.refresh(failAdvisory)
                mutableLoad.value = LoadState(message = "Daten geladen und gespeichert")
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: IOException) {
                mutableLoad.value = LoadState(message = "Laden fehlgeschlagen. Erneut versuchen.")
            }
        }
    }
    fun cancelLoad() {
        refreshJob?.cancel()
        mutableLoad.value = LoadState(message = "Laden abgebrochen")
    }
}
