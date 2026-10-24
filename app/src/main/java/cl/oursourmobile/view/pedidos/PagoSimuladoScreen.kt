
package cl.oursourmobile.view.pedidos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.oursourmobile.model.Pedido
import cl.oursourmobile.view.carrito.formatPrecioCLP

@Composable
fun PagoSimuladoScreen(
    pedido: Pedido,
    onConfirmarPago: () -> Unit,
    onCancelar: () -> Unit
) {
    var mostrarConfirmacion by rememberSaveable {
        mutableStateOf(false)
    }

    // Bloquea inmediatamente las pulsaciones repetidas
    // mientras se completa la confirmación.
    var confirmacionEnCurso by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            Surface(
                tonalElevation = 2.dp
            ) {
                Text(
                    text = "Pago simulado",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Revisa tu pedido",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Esta es una simulación. No se realizará ningún cobro real.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Resumen de compra",
                        fontWeight = FontWeight.Bold
                    )

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

                    ResumenMonto(
                        etiqueta = "Subtotal",
                        monto = pedido.subtotal
                    )

                    ResumenMonto(
                        etiqueta = "Envío",
                        monto = pedido.costoEnvio
                    )

                    HorizontalDivider()

                    ResumenMonto(
                        etiqueta = "Total",
                        monto = pedido.total,
                        destacado = true
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Datos de entrega",
                        fontWeight = FontWeight.Bold
                    )

                    Text("Cliente: ${pedido.nombreCliente}")
                    Text("Teléfono: ${pedido.telefono}")

                    Text(
                        text = if (pedido.tipoEntrega == "retiro") {
                            "Modalidad: Retiro en tienda"
                        } else {
                            "Modalidad: Despacho a domicilio"
                        }
                    )

                    if (pedido.tipoEntrega == "envio") {
                        Text("Dirección: ${pedido.direccion}")
                    }
                }
            }

            Button(
                onClick = {
                    if (!confirmacionEnCurso) {
                        mostrarConfirmacion = true
                    }
                },
                enabled = !confirmacionEnCurso,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    if (confirmacionEnCurso) {
                        "Confirmando pedido..."
                    } else {
                        "Confirmar pago simulado"
                    }
                )
            }

            OutlinedButton(
                onClick = onCancelar,
                enabled = !confirmacionEnCurso,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al checkout")
            }
        }
    }

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = {
                if (!confirmacionEnCurso) {
                    mostrarConfirmacion = false
                }
            },
            title = {
                Text("Confirmar simulación")
            },
            text = {
                Text(
                    "¿Deseas confirmar este pedido por " +
                            "${formatPrecioCLP(pedido.total)}? " +
                            "No se realizará ningún cobro real."
                )
            },
            confirmButton = {
                TextButton(
                    enabled = !confirmacionEnCurso,
                    onClick = {
                        if (!confirmacionEnCurso) {
                            confirmacionEnCurso = true
                            mostrarConfirmacion = false

                            onConfirmarPago()
                        }
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !confirmacionEnCurso,
                    onClick = {
                        mostrarConfirmacion = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun ResumenMonto(
    etiqueta: String,
    monto: Int,
    destacado: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = etiqueta,
            fontWeight = if (destacado) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            }
        )

        Text(
            text = formatPrecioCLP(monto),
            fontWeight = if (destacado) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            }
        )
    }
}
