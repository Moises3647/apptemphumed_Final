package gonzalez.moises.apptemphumed.composables.stateflow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gonzalez.moises.apptemphumed.ui.viewmodels.AeroStatViewModel
import gonzalez.moises.apptemphumed.ui.viewmodels.AuthState

private val PrimaryBlue = Color(0xFF1A6EDB)
private val BackgroundTop = Color(0xFFDEEDFF)
private val BackgroundBot = Color(0xFFF0F6FF)
private val CardBg = Color(0xFFFFFFFF)
private val TextPrimary = Color(0xFF0D1B3E)
private val TextSecondary = Color(0xFF8A9BB8)
private val DividerColor = Color(0xFFE0E8F5)
private val SuccessGreen = Color(0xFF2E7D32) // Verde para el éxito

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AeroStatViewModel,
    onSuccessNavigate: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Estados para controlar los Pop-Ups
    var showErrorDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val authState by viewModel.authState.collectAsState()

    // Escuchamos los cambios en el AuthState del ViewModel
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Error -> {
                errorMessage = (authState as AuthState.Error).message
                showErrorDialog = true
            }
            is AuthState.Success -> {
                showSuccessDialog = true
            }
            else -> {}
        }
    }

    // 1. POP-UP DE ERROR
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("Entendido", color = PrimaryBlue, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Text("Error de Registro", fontWeight = FontWeight.Bold, color = TextPrimary)
            },
            text = {
                Text(errorMessage, color = Color(0xFF5C6B8B))
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = CardBg
        )
    }

    // 2. POP-UP DE ÉXITO
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                onSuccessNavigate() // Navega si cierran el diálogo tocando fuera
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        onSuccessNavigate() // Navega al confirmar
                    }
                ) {
                    Text("Aceptar", color = SuccessGreen, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Text("¡Registro Exitoso!", fontWeight = FontWeight.Bold, color = SuccessGreen)
            },
            text = {
                Text("El usuario se ha creado correctamente en el servidor de AeroStat.", color = Color(0xFF5C6B8B))
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = CardBg
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BackgroundTop, BackgroundBot)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_input_add),
                    contentDescription = "Logo",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Crear Usuario",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = "Registra una nueva cuenta",
                fontSize = 13.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            "Nombre de Usuario",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )

                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            enabled = authState !is AuthState.Loading,
                            placeholder = {
                                Text(
                                    "Ingresa un usuario",
                                    color = TextSecondary,
                                    fontSize = 14.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = TextSecondary
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = DividerColor,
                                focusedBorderColor = PrimaryBlue,
                                unfocusedContainerColor = Color(0xFFF5F9FF),
                                focusedContainerColor = Color(0xFFF5F9FF)
                            )
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            "Contraseña",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            enabled = authState !is AuthState.Loading,
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = TextSecondary
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = {
                                    passwordVisible = !passwordVisible
                                }) {
                                    Icon(
                                        if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = TextSecondary
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = DividerColor,
                                focusedBorderColor = PrimaryBlue,
                                unfocusedContainerColor = Color(0xFFF5F9FF),
                                focusedContainerColor = Color(0xFFF5F9FF)
                            )
                        )
                    }

                    Button(
                        onClick = {
                            // Pasamos un bloque vacío corporativo porque ahora manejamos la navegación
                            // de éxito directamente tras presionar "Aceptar" en el pop-up de confirmación.
                            viewModel.registerUser(username, password, {})
                        },
                        enabled = authState !is AuthState.Loading && username.isNotBlank() && password.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBlue
                        )
                    ) {
                        if (authState is AuthState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                "Crear Usuario",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "¿Ya tienes cuenta?",
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "Iniciar sesión",
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable(enabled = authState !is AuthState.Loading) {
                                onBackToLogin()
                            }
                        )
                    }
                }
            }
        }
    }
}