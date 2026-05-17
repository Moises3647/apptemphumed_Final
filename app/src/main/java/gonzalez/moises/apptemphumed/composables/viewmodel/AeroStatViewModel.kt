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

// Representa los estados del proceso de Login
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AeroStatViewModel : ViewModel() {

    // Cambiado de constante fija a variable dinámica que iniciará vacía
    private var tokenStorage: String? = null

    private val api = Retrofit.Builder()
        .baseUrl("https://apiproyectofinal-vbmk.onrender.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AeroStatApiService::class.java)

    // Estados de autenticación para la UI
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _sensorState = MutableStateFlow<SensorResponse?>(null)
    val sensorState: StateFlow<SensorResponse?> = _sensorState

    private val _historyState = MutableStateFlow<List<HistoryPoint>>(emptyList())
    val historyState: StateFlow<List<HistoryPoint>> = _historyState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    // Función para autenticarse y adquirir el token dinámico
    fun loginUser(username: String, password: String, onSuccessNavigate: () -> Unit) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = api.login(username, password)

                // Formateamos el token agregando el espacio del estándar Bearer
                tokenStorage = "Bearer ${response.accessToken}"

                _authState.value = AuthState.Success

                // Disparamos la carga inicial de datos usando el nuevo token
                refreshData()

                // Ejecutamos la navegación hacia el Dashboard
                onSuccessNavigate()
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.localizedMessage ?: "Credenciales incorrectas")
            }
        }
    }

    fun refreshData() {
        // Obtenemos el token guardado. Si no se ha iniciado sesión, no realiza la petición.
        val currentToken = tokenStorage ?: return

        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                _sensorState.value = api.getLatestData(currentToken)
                _historyState.value = api.getHistory(20, currentToken)
            } catch (e: Exception) {
                // Manejo de errores de red o sesión expirada
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}