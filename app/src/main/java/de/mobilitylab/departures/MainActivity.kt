package de.mobilitylab.departures

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import de.mobilitylab.departures.ui.DepartureViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
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
                            }
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
