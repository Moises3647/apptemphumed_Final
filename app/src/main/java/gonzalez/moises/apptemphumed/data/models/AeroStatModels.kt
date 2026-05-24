package gonzalez.moises.apptemphumed.data.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String
)

data class SensorResponse(
    val id: Int,
    val temperatura: Double,
    val humedad: Double, // Cambiado a Double para alinearse con el Float de la DB
    val foto_path: String,
    val timestamp: String
)

data class HistoryPoint(
    val id: Int,
    val temperatura: Double,
    val humedad: Double, // Cambiado a Double para alinearse con el Float de la DB
    val foto_path: String,
    val timestamp: String
)

data class RegisterResponse(
    val mensaje: String
)