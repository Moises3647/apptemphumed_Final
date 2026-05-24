package gonzalez.moises.apptemphumed.data.network

import gonzalez.moises.apptemphumed.data.models.*
import retrofit2.Response
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
    @POST("users/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<RegisterResponse>
    @GET("sensors/s1/latest")
    suspend fun getLatestData(
        @Header("Authorization") token: String
    ): SensorResponse

    @GET("sensors/s1/history")
    suspend fun getHistory(
        @Query("limit") limite: Int = 20,
        @Header("Authorization") token: String
    ): List<HistoryPoint>
}