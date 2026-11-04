package de.mobilitylab.departures.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

private val Context.boardStore by preferencesDataStore("departure_cache")
class DepartureCache(context: Context, private val cpu: CoroutineDispatcher = Dispatchers.Default) : BoardCache {
    private val store = context.applicationContext.boardStore
    private val key = stringPreferencesKey("board")
    override val boards = store.data.catch { error ->
        if (error is java.io.IOException) emit(emptyPreferences()) else throw error
    }.map { preferences ->
        withContext(cpu) { preferences[key]?.let(::decode) ?: Board() }
    }
    override suspend fun save(board: Board) {
        // TODO-BEGIN U2 | JSON außerhalb Main berechnen; dann mit DataStore dauerhaft speichern.
        // Eigene Implementierung hier. Noch keine Speicherung.
        // TODO-END U2
    }
    private fun encode(board: Board): String = JSONObject().apply {
        put("updatedAt", board.updatedAt); put("advisory", board.advisory)
        put("departures", JSONArray().apply {
            board.departures.forEach { d -> put(JSONObject().apply {
                put("line", d.line); put("destination", d.destination); put("time", d.epochMillis)
                put("platform", d.platform); put("delay", d.delayMinutes)
            }) }
        })
    }.toString()
    private fun decode(json: String): Board = try {
        val root = JSONObject(json); val rows = root.getJSONArray("departures")
        Board((0 until rows.length()).map { i -> rows.getJSONObject(i).let {
            Departure(it.getString("line"), it.getString("destination"), it.getLong("time"), it.getString("platform"), it.getInt("delay"))
        } }, root.getString("advisory"), root.getLong("updatedAt"))
    } catch (_: org.json.JSONException) { Board(advisory = "Cache unlesbar. Bitte neu laden.") }
}
