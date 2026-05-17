package gonzalez.moises.apptemphumed.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gonzalez.moises.apptemphumed.data.models.*
import gonzalez.moises.apptemphumed.data.network.AeroStatApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AeroStatViewModel : ViewModel() {

    private val TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTgxMDUxMjIzOH0.YW7i9DszE45erlTEDDs-e8n-XpxRoT5gCqSRO0moA9M"

    // IMPORTANTE: Cambia 192.168.1.15 por la IP real de tu servidor FastAPI
    private val api = Retrofit.Builder()
        .baseUrl("https://apiproyectofinal-vbmk.onrender.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AeroStatApiService::class.java)

    private val _sensorState = MutableStateFlow<SensorResponse?>(null)
    val sensorState: StateFlow<SensorResponse?> = _sensorState

    private val _historyState = MutableStateFlow<List<HistoryPoint>>(emptyList())
    val historyState: StateFlow<List<HistoryPoint>> = _historyState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                _sensorState.value = api.getLatestData(TOKEN)
                //_historyState.value = api.getHistory(20, TOKEN)
            } catch (e: Exception) {
                // Aquí podrías manejar el error de conexión
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}