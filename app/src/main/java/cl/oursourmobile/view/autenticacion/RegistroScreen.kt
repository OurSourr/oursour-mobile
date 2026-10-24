
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.oursourmobile.viewmodel.RegistroViewModel

@Composable
fun RegistroScreen(
    onVolverAlLogin: () -> Unit,
    viewModel: RegistroViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsState()

    var mostrarContrasena by rememberSaveable {
        mutableStateOf(false)
    }

    var mostrarConfirmacion by rememberSaveable {
        mutableStateOf(false)
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
                text = "Crear cuenta",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Completa tus datos para registrarte en Our Sour.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = estado.nombre,
                onValueChange = {
                    viewModel.actualizarNombre(it)
                },
                label = { Text("Nombre") },
                singleLine = true,
                isError = estado.errorNombre != null,
                supportingText = {
                    estado.errorNombre?.let { Text(it) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = estado.correo,
                onValueChange = {
                    viewModel.actualizarCorreo(it)
                },
                label = { Text("Correo electrónico") },
                singleLine = true,
                isError = estado.errorCorreo != null,
                supportingText = {
                    estado.errorCorreo?.let { Text(it) }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = estado.contrasena,
                onValueChange = {
                    viewModel.actualizarContrasena(it)
                },
                label = { Text("Contraseña") },
                singleLine = true,
                isError = estado.errorContrasena != null,
                supportingText = {
                    estado.errorContrasena?.let { Text(it) }
                },
                visualTransformation = if (mostrarContrasena) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            mostrarContrasena = !mostrarContrasena
                        }
                    ) {
                        Icon(
                            imageVector = if (mostrarContrasena) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = if (mostrarContrasena) {
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

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = estado.confirmarContrasena,
                onValueChange = {
                    viewModel.actualizarConfirmacion(it)
                },
                label = { Text("Confirmar contraseña") },
                singleLine = true,
                isError = estado.errorConfirmacion != null,
                supportingText = {
                    estado.errorConfirmacion?.let { Text(it) }
                },
                visualTransformation = if (mostrarConfirmacion) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            mostrarConfirmacion = !mostrarConfirmacion
                        }
                    ) {
                        Icon(
                            imageVector = if (mostrarConfirmacion) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = if (mostrarConfirmacion) {
                                "Ocultar confirmación de contraseña"
                            } else {
                                "Mostrar confirmación de contraseña"
                            }
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.registrarUsuario()
                },
                enabled = !estado.registroExitoso,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Crear cuenta")
            }

            estado.mensajeRegistro?.let { mensaje ->
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = mensaje,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (estado.registroExitoso) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onVolverAlLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver a iniciar sesión")
            }
        }
    }
}
