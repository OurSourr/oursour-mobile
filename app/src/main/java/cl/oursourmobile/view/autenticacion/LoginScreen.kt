
package cl.oursourmobile.view.autenticacion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.oursourmobile.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onVolverComoInvitado: () -> Unit,
    onIrARegistro: () -> Unit,
    onLoginExitoso: () -> Unit = {},
    viewModel: LoginViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsState()

    LaunchedEffect(estado.usuarioAutenticado) {
        if (estado.usuarioAutenticado != null) {
            viewModel.limpiarResultadoAutenticacion()
            onLoginExitoso()
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Bienvenido a Our Sour",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Inicia sesión para gestionar tus compras y pedidos.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = estado.correo,
                onValueChange = viewModel::actualizarCorreo,
                label = { Text("Correo electrónico") },
                singleLine = true,
                isError = estado.errorCorreo != null,
                supportingText = {
                    estado.errorCorreo?.let { error ->
                        Text(error)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = estado.contrasena,
                onValueChange = viewModel::actualizarContrasena,
                label = { Text("Contraseña") },
                singleLine = true,
                isError = estado.errorContrasena != null,
                supportingText = {
                    estado.errorContrasena?.let { error ->
                        Text(error)
                    }
                },
                visualTransformation = if (estado.mostrarContrasena) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(
                        onClick = viewModel::alternarVisibilidadContrasena
                    ) {
                        Icon(
                            imageVector = if (estado.mostrarContrasena) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = if (estado.mostrarContrasena) {
                                "Ocultar contraseña"
                            } else {
                                "Mostrar contraseña"
                            }
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                modifier = Modifier.fillMaxWidth()
            )

            estado.mensajeError?.let { mensaje ->
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = mensaje,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.iniciarSesion()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Iniciar sesión")
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = onIrARegistro,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("¿No tienes cuenta? Regístrate")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onVolverComoInvitado,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continuar como invitado")
            }
        }
    }
}
