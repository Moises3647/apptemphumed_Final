package gonzalez.moises.apptemphumed.data.models

data class SensorResponse(
    val humedad: Int,
    val timestamp: String,
    val temperatura: Double,
    val foto_path: String,
    val id: Int
)

data class HistoryPoint(
    val humedad: Int,
    val timestamp: String,
    val temperatura: Double,
    val foto_path: String,
    val id: Int
)