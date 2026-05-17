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
import gonzalez.moises.apptemphumed.ui.viewmodels.AuthState // Importante importar tu sealed class

// ─── Colors ──────────────────────────────────────────────────────────────────
private val PrimaryBlue   = Color(0xFF1A6EDB)
private val LightBlue     = Color(0xFFD6E8FF)
private val BackgroundTop  = Color(0xFFDEEDFF)
private val BackgroundBot  = Color(0xFFF0F6FF)
private val CardBg        = Color(0xFFFFFFFF)
private val TextPrimary   = Color(0xFF0D1B3E)
private val TextSecondary = Color(0xFF8A9BB8)
private val DividerColor  = Color(0xFFE0E8F5)

// ─── LoginScreen ACTUALIZADO ─────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authState: AuthState, // 💎 Parámetro para escuchar el estado del ViewModel
    onLoginClick: (username: String, password: String) -> Unit = { _, _ -> },
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Estados locales para controlar el cuadro de diálogo de error
    var showDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // LaunchedEffect reacciona cada vez que el authState cambie a Error
    LaunchedEffect(authState) {
        if (authState is AuthState.Error) {
            errorMessage = authState.message
            showDialog = true
        }
    }

    // ── 📦 CUADRO DE DIÁLOGO (Message Box) ───────────────────────────────────
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = "Acceso Denegado",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE53935) // Color rojo de alerta
                )
            },
            text = {
                Text(text = errorMessage, color = TextPrimary)
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Aceptar", color = PrimaryBlue, fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = CardBg
        )
    }

    // ── Cuerpo de la Interfaz ────────────────────────────────────────────────
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

            Spacer(Modifier.height(60.dp))

            // ── Logo bubble ──────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_upload_you_tube),
                    contentDescription = "Logo",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "AeroStat",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Atmospheric Precision & Intelligence",
                fontSize = 13.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(36.dp))

            // ── Card ─────────────────────────────────────────────────────────
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
                    // Username
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Username", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            placeholder = { Text("Enter your username", color = TextSecondary, fontSize = 14.sp) },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = TextSecondary)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = DividerColor,
                                focusedBorderColor = PrimaryBlue,
                                unfocusedContainerColor = Color(0xFFF5F9FF),
                                focusedContainerColor = Color(0xFFF5F9FF)
                            ),
                            singleLine = true
                        )
                    }

                    // Password
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Password", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = TextSecondary)
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = TextSecondary
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None
                            else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = DividerColor,
                                focusedBorderColor = PrimaryBlue,
                                unfocusedContainerColor = Color(0xFFF5F9FF),
                                focusedContainerColor = Color(0xFFF5F9FF)
                            ),
                            singleLine = true
                        )
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                            Text(
                                text = "Forgot password?",
                                color = PrimaryBlue,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.clickable { }
                            )
                        }
                    }

                    // Login button con indicador de carga opcional
                    Button(
                        onClick = { onLoginClick(username, password) },
                        enabled = authState !is AuthState.Loading, // Deshabilita el botón mientras carga
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        if (authState is AuthState.Loading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Login", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                            Spacer(Modifier.width(8.dp))
                            Text("→", fontSize = 18.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

// ─── Preview Actualizado ─────────────────────────────────────────────────────
@Preview(showBackground = true, widthDp = 375, heightDp = 780)
@Composable
fun LoginScreenPreview() {
    LoginScreen(authState = AuthState.Idle)
}