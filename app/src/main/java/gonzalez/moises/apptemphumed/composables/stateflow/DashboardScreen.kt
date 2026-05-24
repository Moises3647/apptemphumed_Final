package gonzalez.moises.apptemphumed.composables.stateflow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import gonzalez.moises.apptemphumed.data.models.SensorResponse

private val PrimaryBlue = Color(0xFF1A6EDB)
private val PageBg = Color(0xFFE8EFFE)
private val CardBg = Color(0xFFFFFFFF)
private val TextPrimary = Color(0xFF0D1B3E)
private val TextSecondary = Color(0xFF8A9BB8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    sensorData: SensorResponse?,
    onTemperatureClick: () -> Unit = {},
    onHumidityClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                MetricCard(
                    modifier = Modifier.weight(1f),
                    iconEmoji = "🌡",
                    label = "TEMPERATURA",
                    value = if (sensorData != null) "${sensorData.temperatura}°C" else "--°C",
                    onClick = onTemperatureClick
                )

                MetricCard(
                    modifier = Modifier.weight(1f),
                    iconEmoji = "💧",
                    label = "HUMEDAD",
                    value = if (sensorData != null) "${sensorData.humedad}%" else "--%",
                    onClick = onHumidityClick
                )
            }

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

            Spacer(Modifier.height(8.dp))
        }
    }
}

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
                    "TEMPERATURA",
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
                    "HUMEDAD",
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
    DashboardScreen(sensorData = null)
}