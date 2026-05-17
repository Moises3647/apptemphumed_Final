package gonzalez.moises.apptemphumed.composables.stateflow

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gonzalez.moises.apptemphumed.data.models.HistoryPoint
import gonzalez.moises.apptemphumed.data.models.SensorResponse

private val PrimaryBlue   = Color(0xFF1A6EDB)
private val PageBg        = Color(0xFFF0F5FF)
private val CardBg        = Color(0xFFFFFFFF)
private val TextPrimary   = Color(0xFF0D1B3E)
private val TextSecondary = Color(0xFF8A9BB8)
private val ChipBlue      = Color(0xFFE8F0FE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemperatureScreen(
    sensorData: SensorResponse?,          // 💎 Dato actual de la API
    historyList: List<HistoryPoint>,      // 💎 Historial real de la API
    onBack: () -> Unit = {},
    onDashboardClick: () -> Unit = {},
    onHumidityClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Temperature", color = PrimaryBlue, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = PrimaryBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PageBg)
            )
        },
        containerColor = PageBg,
        bottomBar = { TemperatureBottomBar(onDashboardClick, onHumidityClick) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(4.dp))

            Column {
                Text("HISTORICAL DATA", fontSize = 11.sp, color = TextSecondary, letterSpacing = 1.5.sp)
                Text("Temperature Analysis", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }

            // ── Chart ────────────────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("24h Temperature", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(PrimaryBlue))
                            Spacer(Modifier.width(4.dp))
                            Text("Celsius (°C)", fontSize = 11.sp, color = TextSecondary)
                        }
                    }
                    Spacer(Modifier.height(12.dp))

                    val points = historyList.map { it.temperatura.toFloat() }.reversed()
                    TemperatureLineChart(
                        modifier = Modifier.fillMaxWidth().height(140.dp),
                        dataPoints = points.ifEmpty { listOf(20f, 22f, 25f) }
                    )
                }
            }

            // ── Recent Readings ───────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Recent Readings", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                    Spacer(Modifier.height(12.dp))

                    if (historyList.isEmpty()) {
                        Text("No hay lecturas disponibles", color = TextSecondary, fontSize = 14.sp)
                    } else {
                        historyList.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(ChipBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("🌡", fontSize = 20.sp)
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("${item.temperatura}°C", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                                    Text(item.timestamp, fontSize = 12.sp, color = TextSecondary)
                                }
                            }
                            if (index < historyList.lastIndex) {
                                Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF0F4FA))
                            }
                        }
                    }
                }
            }

            // ── Current Reading ───────────────────────────────────────────────
            Box(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(PrimaryBlue).padding(20.dp)
            ) {
                Column {
                    Text("CURRENT READING", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f), letterSpacing = 1.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(if (sensorData != null) "${sensorData.temperatura}°C" else "--°C", fontSize = 56.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Current thermal tracking context acquired securely through Render OAuth2 JSON Web Tokens protocols.",
                        color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp, lineHeight = 18.sp
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun TemperatureLineChart(modifier: Modifier = Modifier, dataPoints: List<Float>) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val minVal = (dataPoints.minOrNull() ?: 0f) - 2f
        val maxVal = (dataPoints.maxOrNull() ?: 50f) + 2f
        val range = if (maxVal - minVal == 0f) 1f else maxVal - minVal

        fun xFor(i: Int) = i * (w / if (dataPoints.size > 1) (dataPoints.size - 1) else 1)
        fun yFor(v: Float) = h - ((v - minVal) / range) * (h * 0.85f) - h * 0.05f

        val path = Path().apply {
            dataPoints.forEachIndexed { i, v ->
                if (i == 0) moveTo(xFor(i), yFor(v)) else lineTo(xFor(i), yFor(v))
            }
        }
        drawPath(path, color = Color(0xFF1A6EDB), style = Stroke(width = 3f))
    }
}

@Composable
private fun TemperatureBottomBar(onDashboardClick: () -> Unit, onHumidityClick: () -> Unit) {
    NavigationBar(containerColor = CardBg, tonalElevation = 8.dp) {
        NavigationBarItem(selected = false, onClick = onDashboardClick, icon = { Text("⊞", fontSize = 22.sp) }, label = { Text("DASHBOARD", fontSize = 9.sp) })
        NavigationBarItem(selected = true, onClick = {}, icon = { Text("🌡", fontSize = 22.sp) }, label = { Text("TEMPERATURE", fontSize = 9.sp, color = PrimaryBlue, fontWeight = FontWeight.Bold) })
        NavigationBarItem(selected = false, onClick = onHumidityClick, icon = { Text("💧", fontSize = 22.sp) }, label = { Text("HUMIDITY", fontSize = 9.sp) })
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 780)
@Composable
fun temp_prew() {
    TemperatureScreen(sensorData = null, historyList = emptyList())
}