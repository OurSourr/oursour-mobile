
package cl.oursourmobile.view.pedidos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.oursourmobile.model.Pedido
import cl.oursourmobile.view.carrito.formatPrecioCLP

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialPedidosScreen(
    pedidos: List<Pedido>,
    onVolver: () -> Unit,
    onVerPedido: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Historial de pedidos",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        if (pedidos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Todavía no tienes pedidos confirmados.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = pedidos.asReversed(),
                    key = { pedido -> pedido.id }
                ) { pedido ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Pedido N.º ${pedido.id}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "${pedido.items.sumOf { it.cantidad }} unidades"
                            )

                            Text(
                                text = if (pedido.tipoEntrega == "retiro") {
                                    "Retiro en tienda"
                                } else {
                                    "Despacho a domicilio"
                                }
                            )

                            Text(
                                text = "Total: ${formatPrecioCLP(pedido.total)}",
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Pago simulado confirmado",
                                color = MaterialTheme.colorScheme.primary
                            )

                            TextButton(
                                onClick = {
                                    onVerPedido(pedido.id)
                                }
                            ) {
                                Text("Ver detalle")
                            }
                        }
                    }
                }
            }
        }
    }
}
