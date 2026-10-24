
package cl.oursourmobile.view.catalogo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import cl.oursourmobile.R
import cl.oursourmobile.model.Producto
import cl.oursourmobile.viewmodel.CarritoViewModel

import java.text.NumberFormat
import java.util.Locale

@Composable
fun DetalleProductoScreen(
    producto: Producto,
    carritoViewModel: CarritoViewModel,
    onVolver: () -> Unit,
    onVerCarrito: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    val imagen = when (producto.imagenKey) {
        "agua_tonica" -> R.drawable.agua_tonica
        "limon_our" -> R.drawable.limon_our
        "gin_our" -> R.drawable.gin_our
        "mix_sour" -> R.drawable.mix_sour
        else -> R.drawable.gin_our
    }

    Scaffold(
        topBar = {
            Surface(
                color = colors.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onVolver
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }

                    Text(
                        text = "Detalle del producto",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = onVerCarrito
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Ver carrito"
                        )
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = colors.surface,
                shadowElevation = 8.dp
            ) {
                Button(
                    onClick = {
                        carritoViewModel.agregarProducto(
                            producto.convertirAItemCarrito()
                        )
                        onVerCarrito()
                    },
                    enabled = producto.disponible,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = if (producto.disponible) {
                            "Agregar al carrito"
                        } else {
                            "Producto no disponible"
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(colors.surface),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(imagen),
                    contentDescription = producto.nombre,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(12.dp)
                )
            }

            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "OUR SOUR / SELECCIÓN",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.secondary
                )

                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.onBackground
                )

                Text(
                    text = NumberFormat.getCurrencyInstance(
                        Locale.forLanguageTag("es-CL")
                    ).format(producto.precio),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = colors.primary
                )

                HorizontalDivider()

                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = producto.descripcion,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.onSurfaceVariant
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = colors.secondaryContainer
                ) {
                    Text(
                        text = if (producto.disponible) {
                            "Disponible para agregar al carrito"
                        } else {
                            "Actualmente no disponible"
                        },
                        modifier = Modifier.padding(14.dp),
                        color = colors.onSecondaryContainer,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
