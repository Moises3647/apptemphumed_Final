package gonzalez.moises.apptemphumed.data.network

import gonzalez.moises.apptemphumed.data.models.*
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Field

interface AeroStatApiService {

    @FormUrlEncoded
    @POST("users/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("sensors/s1/latest")
    suspend fun getLatestData(
        @Header("Authorization") token: String
    ): SensorResponse

    @GET("sensors/s1/history")
    suspend fun getHistory(
        @Query("limit") limite: Int = 20, // Cambiado 'limite' a 'limit' para coincidir con tu backend de FastAPI
        @Header("Authorization") token: String
    ): List<HistoryPoint>
}