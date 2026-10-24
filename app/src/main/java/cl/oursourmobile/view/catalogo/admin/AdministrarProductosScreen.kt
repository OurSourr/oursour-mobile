
package cl.oursourmobile.view.catalogo.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import cl.oursourmobile.data.autenticacion.AutorizacionDemo
import cl.oursourmobile.model.Producto
import cl.oursourmobile.viewmodel.ProductosViewModel

import java.text.NumberFormat
import java.util.Locale

@Composable
fun AdministrarProductosScreen(
    productosViewModel: ProductosViewModel,
    onVolver: () -> Unit,
    onAgregarProducto: () -> Unit = {},
    onEditarProducto: (Int) -> Unit = {}
) {
    val esAdministrador = AutorizacionDemo.puedeAdministrarProductos()

    if (!esAdministrador) {
        AccesoDenegadoProductos(onVolver = onVolver)
        return
    }

    val productos by productosViewModel.productos.collectAsState()

    var productoAEliminar by remember {
        mutableStateOf<Producto?>(null)
    }

    var mensajeError by remember {
        mutableStateOf<String?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                text = "Administrar productos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = "Gestiona los productos disponibles en el catálogo.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Button(
            onClick = onAgregarProducto,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Agregar producto")
        }

        Text(
            text = "${productos.size} productos registrados",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        if (productos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay productos registrados.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(
                    items = productos,
                    key = { it.id }
                ) { producto ->
                    ProductoAdministrableCard(
                        producto = producto,
                        onEditar = {
                            onEditarProducto(producto.id)
                        },
                        onEliminar = {
                            productoAEliminar = producto
                        }
                    )
                }
            }
        }
    }

    productoAEliminar?.let { producto ->
        AlertDialog(
            onDismissRequest = {
                productoAEliminar = null
            },
            title = {
                Text("Eliminar producto")
            },
            text = {
                Text(
                    "¿Deseas eliminar '${producto.nombre}' del catálogo?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val eliminado =
                            productosViewModel.eliminarProducto(producto.id)

                        productoAEliminar = null

                        if (!eliminado) {
                            mensajeError =
                                "No fue posible eliminar el producto."
                        }
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        productoAEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    mensajeError?.let { mensaje ->
        AlertDialog(
            onDismissRequest = {
                mensajeError = null
            },
            title = {
                Text("Operación no completada")
            },
            text = {
                Text(mensaje)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mensajeError = null
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}

@Composable
private fun ProductoAdministrableCard(
    producto: Producto,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = NumberFormat.getCurrencyInstance(
                    Locale.forLanguageTag("es-CL")
                ).format(producto.precio),
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = if (producto.disponible) {
                    "Disponible"
                } else {
                    "No disponible"
                },
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onEditar,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Editar")
                }

                OutlinedButton(
                    onClick = onEliminar,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}

@Composable
private fun AccesoDenegadoProductos(
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Acceso restringido",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Esta sección está disponible únicamente para administradores.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(20.dp))

        Button(onClick = onVolver) {
            Text("Volver")
        }
    }
}
