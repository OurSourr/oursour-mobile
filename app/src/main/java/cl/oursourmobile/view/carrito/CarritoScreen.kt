package cl.oursourmobile.view.carrito

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.oursourmobile.R
import cl.oursourmobile.model.ItemCarrito
import cl.oursourmobile.viewmodel.CarritoViewModel
import java.text.NumberFormat
import java.util.Locale

// Función auxiliar para agregar los puntos de miles ($2.990)
fun formatPrecioCLP(monto: Int): String {
    val localeCL = Locale.Builder().setLanguage("es").setRegion("CL").build()
    val format = NumberFormat.getCurrencyInstance(localeCL)
    return format.format(monto)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    viewModel: CarritoViewModel = viewModel(),
    onProcederCheckout: () -> Unit = {}
) {
    val items by viewModel.items.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi Carrito",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }
            )
        }
    ) { paddingValues ->
        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío", color = Color.Gray)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Lista de productos ocupando el espacio disponible
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items, key = { it.id }) { item ->
                        ItemCarritoCard(
                            item = item,
                            onIncrementar = { viewModel.incrementarCantidad(item.id) },
                            onDecrementar = { viewModel.decrementarCantidad(item.id) },
                            onEliminar = { viewModel.eliminarItem(item.id) }
                        )
                    }
                }

                // Panel de Subtotal + Checkout pegado en la parte inferior sobre la barra principal
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Subtotal", fontSize = 18.sp, fontWeight = FontWeight.Normal)
                        Text(
                            text = formatPrecioCLP(viewModel.subtotal),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = onProcederCheckout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Proceder al checkout", fontSize = 16.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun ItemCarritoCard(
    item: ItemCarrito,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit,
    onEliminar: () -> Unit
) {
    val context = LocalContext.current

    // Buscamos el ID dinámico a partir de la clave en String (ej: "agua_tonica", "limon_our", "gin_our")
    val imageResId = remember(item.imagenKey) {
        val id = context.resources.getIdentifier(item.imagenKey, "drawable", context.packageName)
        if (id != 0) id else R.drawable.ic_launcher_foreground
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = item.nombre,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(4.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Información del Producto (Nombre y Precio)
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.nombre, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatPrecioCLP(item.precio),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            // Contenedor gris tipo píldora para (- 1 +)
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFEEEEEE)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    IconButton(onClick = onDecrementar, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Remove, contentDescription = "Restar", modifier = Modifier.size(16.dp))
                    }

                    Text(
                        text = "${item.cantidad}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )

                    IconButton(onClick = onIncrementar, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Add, contentDescription = "Sumar", modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Botón de eliminar fuera de la píldora
            IconButton(onClick = onEliminar, modifier = Modifier.size(28.dp)) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}