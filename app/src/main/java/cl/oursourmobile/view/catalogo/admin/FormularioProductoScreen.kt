
package cl.oursourmobile.view.catalogo.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp

import cl.oursourmobile.data.autenticacion.AutorizacionDemo
import cl.oursourmobile.model.Producto

@Composable
fun FormularioProductoScreen(
    producto: Producto?,
    onGuardar: (
        nombre: String,
        descripcion: String,
        precio: Int,
        imagenKey: String,
        disponible: Boolean
    ) -> Boolean,
    onVolver: () -> Unit
) {
    if (!AutorizacionDemo.puedeAdministrarProductos()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Acceso restringido",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Esta sección está disponible únicamente para administradores."
            )

            Spacer(Modifier.height(20.dp))

            Button(onClick = onVolver) {
                Text("Volver")
            }
        }
        return
    }

    val esEdicion = producto != null

    var nombre by remember(producto?.id) {
        mutableStateOf(producto?.nombre.orEmpty())
    }

    var descripcion by remember(producto?.id) {
        mutableStateOf(producto?.descripcion.orEmpty())
    }

    var precioTexto by remember(producto?.id) {
        mutableStateOf(producto?.precio?.toString().orEmpty())
    }

    var imagenKey by remember(producto?.id) {
        mutableStateOf(producto?.imagenKey.orEmpty())
    }

    var disponible by remember(producto?.id) {
        mutableStateOf(producto?.disponible ?: true)
    }

    var mensajeError by remember(producto?.id) {
        mutableStateOf<String?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver"
                )
            }

            Text(
                text = if (esEdicion) {
                    "Editar producto"
                } else {
                    "Agregar producto"
                },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = "Completa los datos del producto.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                mensajeError = null
            },
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = {
                descripcion = it
                mensajeError = null
            },
            label = { Text("Descripción") },
            minLines = 3,
            maxLines = 5,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = precioTexto,
            onValueChange = {
                if (it.all(Char::isDigit)) {
                    precioTexto = it
                    mensajeError = null
                }
            },
            label = { Text("Precio (CLP)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = imagenKey,
            onValueChange = {
                imagenKey = it
                mensajeError = null
            },
            label = { Text("Clave de imagen") },
            supportingText = {
                Text(
                    "Debe corresponder a una imagen disponible en la aplicación."
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = disponible,
                onCheckedChange = {
                    disponible = it
                }
            )

            Text("Producto disponible")
        }

        if (mensajeError != null) {
            Text(
                text = mensajeError.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = {
                val nombreValidado = nombre.trim()
                val descripcionValidada = descripcion.trim()
                val imagenValidada = imagenKey.trim()
                val precioValidado = precioTexto.toIntOrNull()

                mensajeError = when {
                    nombreValidado.isBlank() ->
                        "El nombre del producto es obligatorio."

                    descripcionValidada.isBlank() ->
                        "La descripción es obligatoria."

                    precioValidado == null ->
                        "Ingresa un precio válido."

                    imagenValidada.isBlank() ->
                        "La clave de imagen es obligatoria."

                    else -> null
                }

                if (mensajeError == null) {
                    val guardado = onGuardar(
                        nombreValidado,
                        descripcionValidada,
                        requireNotNull(precioValidado),
                        imagenValidada,
                        disponible
                    )

                    if (guardado) {
                        onVolver()
                    } else {
                        mensajeError =
                            "No fue posible guardar el producto."
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (esEdicion) {
                    "Guardar cambios"
                } else {
                    "Agregar producto"
                }
            )
        }
    }
}
