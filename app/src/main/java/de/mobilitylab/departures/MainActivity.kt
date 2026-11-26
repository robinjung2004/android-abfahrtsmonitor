package de.mobilitylab.departures

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import de.mobilitylab.departures.timer.*
import de.mobilitylab.departures.ui.DepartureViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    private var pendingTimer: Pair<Long, String>? = null
    private val notificationPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) pendingTimer?.let { startTimer(it.first, it.second) }
        else android.widget.Toast.makeText(this, "Für den Lehrtimer bitte Benachrichtigungen erlauben.", android.widget.Toast.LENGTH_LONG).show()
        pendingTimer = null
    }
    private fun requestTimer(seconds: Long, label: String) {
        if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            pendingTimer = seconds to label
            notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else startTimer(seconds, label)
    }
    private fun startTimer(seconds: Long, label: String) {
        ContextCompat.startForegroundService(this, Intent(this, DepartureTimerService::class.java)
            .setAction(DepartureTimerService.START).putExtra("seconds", seconds).putExtra("label", label))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(colorScheme = lightColorScheme(primary = Color(0xFF002896), secondary = Color(0xFF007E88))) {
                val vm: DepartureViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T = DepartureViewModel((application as MonitorApp).repository) as T
                })
                val board by vm.board.collectAsStateWithLifecycle()
                val load by vm.load.collectAsStateWithLifecycle()
                val timer by TimerStatus.state.collectAsStateWithLifecycle()
                var failAdvisory by rememberSaveable { mutableStateOf(false) }
                val time = remember { SimpleDateFormat("HH:mm:ss", Locale.GERMANY) }
                Surface(Modifier.fillMaxSize(), color = Color(0xFFF2F5FB)) {
                    Column(Modifier.safeDrawingPadding().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("MOBILITY LAB · KOTLIN", color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
                        Text("Abfahrtsmonitor", style = MaterialTheme.typography.headlineLarge)
                        Text("Heilbronn Hbf", style = MaterialTheme.typography.titleLarge)
                        Text("BEISPIELDATEN · keine echten Bahnzeiten", color = Color(0xFF874600))
                        LabCard("Abfahrten laden · Unterricht U1–U4") {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(onClick = { vm.reload(failAdvisory) }) { Text("Aktualisieren") }
                                OutlinedButton(onClick = vm::cancelLoad, enabled = load.loading) { Text("Abbrechen") }
                            }
                            if (load.loading) LinearProgressIndicator(Modifier.fillMaxWidth())
                            Text(load.message)
                            Row { Checkbox(failAdvisory, { failAdvisory = it }); Text("Hinweisfehler simulieren", Modifier.padding(top = 12.dp)) }
                            Text(board.advisory, style = MaterialTheme.typography.bodyMedium)
                            Text(if (board.updatedAt == 0L) "Cache: noch leer" else "Cache gespeichert: ${time.format(Date(board.updatedAt))}", style = MaterialTheme.typography.labelMedium)
                        }
                        board.departures.forEach { departure ->
                            LabCard("${departure.line}  →  ${departure.destination}") {
                                Text("Plan ${time.format(Date(departure.epochMillis)).take(5)} · +${departure.delayMinutes} min · Gleis ${departure.platform}")
                                Button(onClick = {
                                    val seconds = ((departure.epochMillis + departure.delayMinutes * 60_000 - System.currentTimeMillis()) / 1000).coerceIn(1, 3600)
                                    requestTimer(seconds, departure.line + " nach " + departure.destination)
                                }) { Text("Abfahrtstimer · H1–H3 starten") }
                            }
                        }
                        LabCard("Abfahrtstimer · Hausaufgabe H1–H3") {
                            Text(if (timer.active) "${timer.remaining / 60}:${(timer.remaining % 60).toString().padStart(2, '0')}" else "—:—", style = MaterialTheme.typography.displaySmall)
                            Text(timer.label)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(onClick = { requestTimer(30, "30-Sekunden-Test") }) { Text("30 s testen") }
                                OutlinedButton(onClick = { stopService(Intent(this@MainActivity, DepartureTimerService::class.java)) }, enabled = timer.active) { Text("Stoppen") }
                            }
                            Text("Der Timer verwendet die monotone Uhr des Geräts. Force-stop beendet den Service.", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
@Composable
private fun LabCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            content()
        }
    }
}
