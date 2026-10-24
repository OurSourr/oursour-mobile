
package cl.oursourmobile.view.pedidos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.oursourmobile.model.Pedido
import cl.oursourmobile.view.carrito.formatPrecioCLP

@Composable
fun ConfirmacionPedidoScreen(
    pedido: Pedido,
    onVolverInicio: () -> Unit,
    onVerHistorial: () -> Unit
) {
    Scaffold { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Pedido confirmado",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(80.dp)
            )

            Text(
                text = "¡Pedido confirmado!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Tu pago simulado se ha confirmado correctamente. " +
                        "No se realizó ningún cobro real.",
                style = MaterialTheme.typography.bodyMedium
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Pedido N.º ${pedido.id}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    HorizontalDivider()

                    Text("Cliente: ${pedido.nombreCliente}")

                    Text(
                        text = if (pedido.tipoEntrega == "retiro") {
                            "Entrega: Retiro en tienda"
                        } else {
                            "Entrega: Despacho a domicilio"
                        }
                    )

                    if (pedido.tipoEntrega == "envio") {
                        Text("Dirección: ${pedido.direccion}")
                    }

                    HorizontalDivider()

                    pedido.items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${item.nombre} x${item.cantidad}",
                                modifier = Modifier.weight(1f)
                            )

                            Text(
                                text = formatPrecioCLP(
                                    item.precio * item.cantidad
                                )
                            )
                        }
                    }

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total confirmado",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = formatPrecioCLP(pedido.total),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Button(
                onClick = onVerHistorial,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver historial de pedidos")
            }

            OutlinedButton(
                onClick = onVolverInicio,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al inicio")
            }
        }
    }
}
