package gonzalez.moises.apptemphumed.data.models

import com.google.gson.annotations.SerializedName

/**
 * Modelo para recibir la respuesta del endpoint /users/login.
 * Mapea el token JWT generado por tu API de FastAPI.
 */
data class LoginResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String
)

/**
 * Modelo para el endpoint /sensors/s1/latest.
 * Mapea el último registro de temperatura y humedad medido.
 */
data class SensorResponse(
    val id: Int,
    val temperatura: Double,
    val humedad: Double, // Cambiado a Double para alinearse con el Float de la DB
    val foto_path: String,
    val timestamp: String
)

/**
 * Modelo para el endpoint /sensors/s1/history.
 * Mapea cada punto del listado histórico de mediciones.
 */
data class HistoryPoint(
    val id: Int,
    val temperatura: Double,
    val humedad: Double, // Cambiado a Double para alinearse con el Float de la DB
    val foto_path: String,
    val timestamp: String
)