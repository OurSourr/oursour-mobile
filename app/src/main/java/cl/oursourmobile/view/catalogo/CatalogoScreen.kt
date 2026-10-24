
package cl.oursourmobile.view.catalogo

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

import cl.oursourmobile.R
import cl.oursourmobile.data.productos.ProductosDemo
import cl.oursourmobile.model.Producto
import cl.oursourmobile.viewmodel.CarritoViewModel
import cl.oursourmobile.viewmodel.ProductosViewModel

import java.text.NumberFormat
import java.util.Locale

/**
 * Consulta un producto desde el repositorio compartido.
 *
 * Se conserva esta función para mantener la compatibilidad
 * con las rutas actuales de navegación.
 */
fun obtenerProductoPorId(id: Int): Producto? {
    return ProductosDemo.repository.obtenerPorId(id)
}

@Composable
fun CatalogoScreen(
    carritoViewModel: CarritoViewModel,
    productosViewModel: ProductosViewModel,
    onVerCarrito: () -> Unit,
    onVerDetalleProducto: (Int) -> Unit = {}
) {
    val productos by productosViewModel.productos.collectAsState()

    var busqueda by rememberSaveable {
        mutableStateOf("")
    }

    var mensaje by remember {
        mutableStateOf<String?>(null)
    }

    val colors = MaterialTheme.colorScheme
    val textoBusqueda = busqueda.trim()

    val productosFiltrados = productos.filter { producto ->
        producto.nombre.contains(
            textoBusqueda,
            ignoreCase = true
        ) || producto.descripcion.contains(
            textoBusqueda,
            ignoreCase = true
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(20.dp))

        Text(
            text = "OUR SOUR / BOUTIQUE",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = colors.secondary
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Nuestra selección",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = colors.onBackground
        )

        Text(
            text = "Ingredientes y productos para tus mejores preparaciones.",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onSurfaceVariant
        )

        Spacer(Modifier.height(18.dp))

        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            label = { Text("Buscar productos") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "${productosFiltrados.size} productos disponibles",
            style = MaterialTheme.typography.labelLarge,
            color = colors.onSurfaceVariant
        )

        Spacer(Modifier.height(12.dp))

        if (productosFiltrados.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (productos.isEmpty()) {
                        "No hay productos disponibles."
                    } else {
                        "No encontramos productos con ese nombre."
                    }
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(
                    bottom = 20.dp
                )
            ) {
                items(
                    items = productosFiltrados,
                    key = { it.id }
                ) { producto ->
                    ProductoCard(
                        producto = producto,
                        onVerDetalle = {
                            onVerDetalleProducto(producto.id)
                        },
                        onAgregar = {
                            carritoViewModel.agregarProducto(
                                producto.convertirAItemCarrito()
                            )

                            mensaje =
                                "${producto.nombre} agregado al carrito"
                        }
                    )
                }
            }
        }

        // Separación permanente entre la lista y el botón fijo.
        Spacer(Modifier.height(20.dp))

        HorizontalDivider(
            color = colors.outlineVariant
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onVerCarrito,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Ver carrito",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(12.dp))
    }

    mensaje?.let { texto ->
        AlertDialog(
            onDismissRequest = {
                mensaje = null
            },
            title = {
                Text("Producto agregado")
            },
            text = {
                Text(texto)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mensaje = null
                    }
                ) {
                    Text("Continuar comprando")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mensaje = null
                        onVerCarrito()
                    }
                ) {
                    Text("Ir al carrito")
                }
            }
        )
    }
}

@Composable
private fun ProductoCard(
    producto: Producto,
    onVerDetalle: () -> Unit,
    onAgregar: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    val imagen = when (producto.imagenKey) {
        "agua_tonica" -> R.drawable.agua_tonica
        "limon_our" -> R.drawable.limon_our
        "gin_our" -> R.drawable.gin_our
        "mix_sour" -> R.drawable.mix_sour
        else -> R.drawable.gin_our
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column {
            Image(
                painter = painterResource(imagen),
                contentDescription =
                    "Ver detalle de ${producto.nombre}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clickable(onClick = onVerDetalle)
            )

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable(
                        onClick = onVerDetalle
                    )
                )

                Text(
                    text = producto.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant
                )

                Text(
                    text = NumberFormat.getCurrencyInstance(
                        Locale.forLanguageTag("es-CL")
                    ).format(producto.precio),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = colors.primary
                )

                OutlinedButton(
                    onClick = onVerDetalle,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Ver detalles")
                }

                Button(
                    onClick = onAgregar,
                    enabled = producto.disponible,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        if (producto.disponible) {
                            "Agregar al carrito"
                        } else {
                            "No disponible"
                        }
                    )
                }
            }
        }
    }
}
