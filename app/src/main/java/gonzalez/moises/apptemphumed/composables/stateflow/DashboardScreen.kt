package gonzalez.moises.apptemphumed.composables.stateflow

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import gonzalez.moises.apptemphumed.ui.viewmodels.AeroStatViewModel

// ─── Colors ──────────────────────────────────────────────────────────────────
private val PrimaryBlue = Color(0xFF1A6EDB)
private val LightBlue = Color(0xFFB8D0F5)
private val PageBg = Color(0xFFE8EFFE)
private val CardBg = Color(0xFFFFFFFF)
private val TextPrimary = Color(0xFF0D1B3E)
private val TextSecondary = Color(0xFF8A9BB8)
private val ChipBlue = Color(0xFFE8F0FE)
private val BarBgColor = Color(0xFFD0DCEF)

// ─── Bar chart data ───────────────────────────────────────────────────────────
data class TrendBar(
    val hour: String,
    val tempFraction: Float,
    val humFraction: Float,
    val isNow: Boolean = false
)

private val trendData = listOf(
    TrendBar("09:00", 0.55f, 0.80f),
    TrendBar("10:00", 0.60f, 0.78f),
    TrendBar("11:00", 0.65f, 0.75f),
    TrendBar("12:00", 0.70f, 0.72f),
    TrendBar("13:00", 0.75f, 0.68f),
    TrendBar("AHORA", 0.80f, 0.65f, isNow = true),
)

// ─── DashboardScreen ─────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onTemperatureClick: () -> Unit = {},
    onHumidityClick: () -> Unit = {},
) {

    val viewModel: AeroStatViewModel = viewModel()

    val sensorData by viewModel.sensorState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Logo",
                            tint = PrimaryBlue,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "AeroStat",
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PageBg)
            )
        },
        containerColor = PageBg,
        bottomBar = { DashboardBottomBar(onTemperatureClick, onHumidityClick) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Spacer(Modifier.height(8.dp))

            // ── Metric cards row ───────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                MetricCard(
                    modifier = Modifier.weight(1f),
                    iconEmoji = "🌡",
                    label = "TEMPERATURA",
                    value = "${sensorData?.temperatura ?: "--"}°C",
                    onClick = onTemperatureClick
                )

                MetricCard(
                    modifier = Modifier.weight(1f),
                    iconEmoji = "💧",
                    label = "HUMEDAD",
                    value = "${sensorData?.humedad ?: "--"}%",
                    onClick = onHumidityClick
                )
            }

            // ── Date/image card ────────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {

                Column(modifier = Modifier.fillMaxWidth()) {

                    AsyncImage(
                        model = sensorData?.foto_path,
                        contentDescription = "Imagen del sensor",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 20.dp,
                                    topEnd = 20.dp
                                )
                            ),
                        contentScale = ContentScale.Crop
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            sensorData?.timestamp ?: "Sin fecha",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            "Datos obtenidos desde la API",
                            fontSize = 15.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // ── Daily Trend chart ─────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {

                Column(modifier = Modifier.padding(16.dp)) {

                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column {

                            Text(
                                "Tendencia Diaria",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = TextPrimary
                            )

                            Text(
                                "ÚLTIMAS 6 HORAS",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                letterSpacing = 1.sp
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            LegendDot(color = PrimaryBlue, label = "Temp")

                            Spacer(Modifier.width(8.dp))

                            LegendDot(color = LightBlue, label = "Hum")
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Bar chart
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {

                        trendData.forEach { bar ->
                            TrendBarColumn(bar = bar)
                        }
                    }
                }
            }

            // ── Status bar ────────────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text("↻", fontSize = 18.sp, color = TextSecondary)

                    Spacer(Modifier.width(10.dp))

                    Text(
                        "Actualizando datos...",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

// ─── Metric Card ─────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MetricCard(
    modifier: Modifier = Modifier,
    iconEmoji: String,
    label: String,
    value: String,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Text(iconEmoji, fontSize = 28.sp)

            Text(
                label,
                fontSize = 10.sp,
                color = TextSecondary,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center
            )

            Text(
                value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ─── Legend dot ──────────────────────────────────────────────────────────────
@Composable
private fun LegendDot(color: Color, label: String) {

    Row(verticalAlignment = Alignment.CenterVertically) {

        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )

        Spacer(Modifier.width(4.dp))

        Text(label, fontSize = 12.sp, color = TextSecondary)
    }
}

// ─── Trend bar column ────────────────────────────────────────────────────────
@Composable
private fun TrendBarColumn(bar: TrendBar) {

    val maxBarHeight = 130.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.width(40.dp)
    ) {

        Box(
            modifier = Modifier
                .width(28.dp)
                .height(maxBarHeight),
            contentAlignment = Alignment.BottomCenter
        ) {

            // Background track
            Box(
                modifier = Modifier
                    .width(28.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(BarBgColor)
            )

            // Humidity bar
            Box(
                modifier = Modifier
                    .width(28.dp)
                    .fillMaxHeight(bar.humFraction)
                    .clip(RoundedCornerShape(8.dp))
                    .background(LightBlue)
            )

            // Temperature bar
            Box(
                modifier = Modifier
                    .width(28.dp)
                    .fillMaxHeight(bar.tempFraction)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PrimaryBlue)
            )
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = bar.hour,
            fontSize = if (bar.isNow) 11.sp else 10.sp,
            color = if (bar.isNow) PrimaryBlue else TextSecondary,
            fontWeight = if (bar.isNow) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}

// ─── Tree silhouette (Canvas) ─────────────────────────────────────────────────
private fun DrawScope.drawTreeSilhouette() {

    val cx = size.width / 2
    val cy = size.height / 2

    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFFD700).copy(alpha = 0.6f),
                Color.Transparent
            ),
            center = Offset(cx * 0.3f, cy * 0.4f),
            radius = size.width * 0.35f
        ),
        radius = size.width * 0.35f,
        center = Offset(cx * 0.3f, cy * 0.4f)
    )

    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF5A8A3A).copy(alpha = 0.7f),
                Color(0xFF3A6A1A).copy(alpha = 0.9f)
            ),
            startY = size.height * 0.75f,
            endY = size.height
        ),
        topLeft = Offset(0f, size.height * 0.78f),
        size = Size(size.width, size.height * 0.22f)
    )

    drawRoundRect(
        color = Color(0xFF5C3A1E),
        topLeft = Offset(cx - 12f, size.height * 0.6f),
        size = Size(24f, size.height * 0.25f),
        cornerRadius = CornerRadius(4f)
    )

    drawCircle(
        color = Color(0xFF2D6A2D).copy(alpha = 0.9f),
        radius = size.width * 0.22f,
        center = Offset(cx, size.height * 0.42f)
    )
}

// ─── Bottom Navigation Bar ────────────────────────────────────────────────────
@Composable
fun DashboardBottomBar(
    onTemperatureClick: () -> Unit,
    onHumidityClick: () -> Unit
) {

    NavigationBar(
        containerColor = CardBg,
        tonalElevation = 8.dp
    ) {

        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Text("⊞", fontSize = 22.sp) },
            label = {
                Text(
                    "DASHBOARD",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = onTemperatureClick,
            icon = { Text("🌡", fontSize = 22.sp) },
            label = {
                Text(
                    "TEMPERATURE",
                    fontSize = 9.sp,
                    color = TextSecondary
                )
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = onHumidityClick,
            icon = { Text("💧", fontSize = 22.sp) },
            label = {
                Text(
                    "HUMIDITY",
                    fontSize = 9.sp,
                    color = TextSecondary
                )
            }
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}