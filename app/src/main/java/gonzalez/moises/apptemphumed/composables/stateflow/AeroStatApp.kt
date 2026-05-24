package gonzalez.moises.apptemphumed.composables.stateflow

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import gonzalez.moises.apptemphumed.ui.viewmodels.AeroStatViewModel
import androidx.compose.runtime.getValue

object AeroStatColors {
    val PrimaryBlue  = Color(0xFF1A6EDB)
    val OnPrimary    = Color(0xFFFFFFFF)
    val Background   = Color(0xFFE8EFFE)
    val Surface      = Color(0xFFFFFFFF)
    val OnBackground = Color(0xFF0D1B3E)
    val OnSurface    = Color(0xFF0D1B3E)
    val Secondary    = Color(0xFF8A9BB8)
}

private val AeroStatColorScheme = lightColorScheme(
    primary       = AeroStatColors.PrimaryBlue,
    onPrimary     = AeroStatColors.OnPrimary,
    background    = AeroStatColors.Background,
    surface       = AeroStatColors.Surface,
    onBackground  = AeroStatColors.OnBackground,
    onSurface     = AeroStatColors.OnSurface,
    secondary     = AeroStatColors.Secondary,
)

@Composable
fun AeroStatTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = AeroStatColorScheme, content = content)
}

object Routes {
    const val LOGIN       = "login"
    const val REGISTER    = "register" // 1. Nueva ruta para el Registro
    const val DASHBOARD   = "dashboard"
    const val TEMPERATURE = "temperature"
    const val HUMIDITY    = "humidity"
}

@Composable
fun AeroStatApp(viewModel: AeroStatViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val navController = rememberNavController()
    val realSensorData by viewModel.sensorState.collectAsState()
    val realHistoryData by viewModel.historyState.collectAsState()

    AeroStatTheme {
        NavHost(navController = navController, startDestination = Routes.LOGIN) {
            composable(Routes.LOGIN) {
                val authState by viewModel.authState.collectAsState()

                LoginScreen(
                    authState = authState,
                    onLoginClick = { usuario, contrasena ->
                        viewModel.loginUser(usuario, contrasena) {
                            navController.navigate(Routes.DASHBOARD) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                        }
                    },
                    // 2. Aquí le pasas la acción para ir a registrarse (ajusta el nombre del parámetro si en tu Login es diferente)
                    onNavigateToRegister = {
                        navController.navigate(Routes.REGISTER)
                    }
                )
            }

            // 3. Agregamos el composable para la pantalla de Registro
            composable(Routes.REGISTER) {
                RegisterScreen(
                    viewModel = viewModel,
                    onSuccessNavigate = {
                        // Al registrarse con éxito, lo mandamos al Login de vuelta.
                        // Usamos popUpTo para limpiar la pantalla de registro de la pila.
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.REGISTER) { inclusive = true }
                        }
                    },
                    onBackToLogin = {
                        // Si pulsa "¿Ya tienes cuenta? Iniciar sesión", simplemente volvemos atrás
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.DASHBOARD) {
                DashboardScreen(
                    sensorData = realSensorData,
                    onTemperatureClick = { navController.navigate(Routes.TEMPERATURE) },
                    onHumidityClick    = { navController.navigate(Routes.HUMIDITY) }
                )
            }

            composable(Routes.TEMPERATURE) {
                TemperatureScreen(
                    sensorData = realSensorData,
                    historyList = realHistoryData,
                    onBack = { navController.popBackStack() },
                    onHumidityClick    = { navController.navigate(Routes.HUMIDITY) },
                    onDashboardClick = { navController.navigate(Routes.DASHBOARD) }
                )
            }

            composable(Routes.HUMIDITY) {
                HumidityScreen(
                    sensorData = realSensorData,
                    historyList = realHistoryData,
                    onBack = { navController.popBackStack() },
                    onTemperatureClick = { navController.navigate(Routes.TEMPERATURE) },
                    onDashboardClick = { navController.navigate(Routes.DASHBOARD) }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
fun AeroStatApp_prew() {
    AeroStatApp()
}