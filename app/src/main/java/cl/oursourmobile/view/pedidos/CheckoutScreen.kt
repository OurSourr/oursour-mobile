
package cl.oursourmobile.view.pedidos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.oursourmobile.model.Pedido
import cl.oursourmobile.view.carrito.formatPrecioCLP
import cl.oursourmobile.viewmodel.CarritoViewModel
import cl.oursourmobile.viewmodel.CheckoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    carritoViewModel: CarritoViewModel = viewModel(),
    checkoutViewModel: CheckoutViewModel = viewModel(),
    onVolverAlCarrito: () -> Unit = {},
    onConfirmarPago: (Pedido) -> Unit = {}
) {
    val itemsCarrito by carritoViewModel.items.collectAsState()

    val nombre by checkoutViewModel.nombre.collectAsState()
    val direccion by checkoutViewModel.direccion.collectAsState()
    val telefono by checkoutViewModel.telefono.collectAsState()
    val tipoEntrega by checkoutViewModel.tipoEntrega.collectAsState()
    val mostrarErrores by checkoutViewModel.mostrarErrores.collectAsState()

    val errorNombre = if (mostrarErrores) checkoutViewModel.errorNombre() else null
    val errorTelefono = if (mostrarErrores) checkoutViewModel.errorTelefono() else null
    val errorDireccion = if (mostrarErrores) checkoutViewModel.errorDireccion() else null

    val carritoValido = itemsCarrito.isNotEmpty() &&
            itemsCarrito.all { it.precio >= 0 && it.cantidad > 0 }

    val subtotal = itemsCarrito.sumOf { item ->
        item.precio * item.cantidad
    }
    val costoEnvio = checkoutViewModel.costoEnvio
    val total = subtotal + costoEnvio

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Finalizar Compra",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolverAlCarrito) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al carrito"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total a pagar",
                            fontSize = 18.sp
                        )

                        Text(
                            text = formatPrecioCLP(total),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E8842)
                        )
                    }

                    if (!carritoValido) {
                        Text(
                            text = if (itemsCarrito.isEmpty()) {
                                "Agrega productos al carrito para continuar."
                            } else {
                                "El carrito contiene productos inválidos."
                            },
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Button(
                        onClick = {
                            if (checkoutViewModel.validarFormulario() && carritoValido) {
                                val pedido = checkoutViewModel.crearPedido(itemsCarrito)
                                onConfirmarPago(pedido)
                            }
                        },
                        enabled = carritoValido,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1E8842)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Ir a Pago Simulado",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Método de Entrega",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = tipoEntrega == CheckoutViewModel.TIPO_ENVIO,
                    onClick = {
                        checkoutViewModel.seleccionarTipoEntrega(
                            CheckoutViewModel.TIPO_ENVIO
                        )
                    },
                    label = { Text("Despacho a Domicilio") },
                    modifier = Modifier.weight(1f)
                )

                FilterChip(
                    selected = tipoEntrega == CheckoutViewModel.TIPO_RETIRO,
                    onClick = {
                        checkoutViewModel.seleccionarTipoEntrega(
                            CheckoutViewModel.TIPO_RETIRO
                        )
                    },
                    label = { Text("Retiro en Tienda") },
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = if (tipoEntrega == CheckoutViewModel.TIPO_ENVIO) {
                    "Datos de Despacho"
                } else {
                    "Datos de Contacto"
                },
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = checkoutViewModel::cambiarNombre,
                label = { Text("Nombre Completo") },
                placeholder = { Text("Ej.: María González") },
                supportingText = {
                    Text(
                        errorNombre
                            ?: "Ingresa nombre y apellido."
                    )
                },
                isError = errorNombre != null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )

            if (tipoEntrega == CheckoutViewModel.TIPO_ENVIO) {
                OutlinedTextField(
                    value = direccion,
                    onValueChange = checkoutViewModel::cambiarDireccion,
                    label = { Text("Dirección de Entrega") },
                    placeholder = { Text("Ej.: Av. Providencia 1234, Depto. 5") },
                    supportingText = {
                        Text(
                            errorDireccion
                                ?: "Incluye calle, número y departamento si corresponde."
                        )
                    },
                    isError = errorDireccion != null,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    )
                )
            }

            OutlinedTextField(
                value = telefono,
                onValueChange = checkoutViewModel::cambiarTelefono,
                label = { Text("Teléfono de Contacto") },
                placeholder = { Text("Ej.: +56 9 1234 5678") },
                supportingText = {
                    Text(
                        errorTelefono
                            ?: "Celular chileno: 9 dígitos, comenzando con 9."
                    )
                },
                isError = errorTelefono != null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                )
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Resumen del Pedido",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal productos")
                        Text(formatPrecioCLP(subtotal))
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Costo de envío")
                        Text(
                            if (costoEnvio == 0) {
                                "Gratis"
                            } else {
                                formatPrecioCLP(costoEnvio)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
