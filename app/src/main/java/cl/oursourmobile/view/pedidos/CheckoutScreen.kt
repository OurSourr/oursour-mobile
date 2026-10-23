package cl.oursourmobile.view.pedidos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    val subtotal = carritoViewModel.subtotal
    val tipoEntrega by checkoutViewModel.tipoEntrega.collectAsState()
    val costoEnvio = checkoutViewModel.costoEnvio
    val total = subtotal + costoEnvio

    val nombre by checkoutViewModel.nombre.collectAsState()
    val direccion by checkoutViewModel.direccion.collectAsState()
    val telefono by checkoutViewModel.telefono.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finalizar Compra", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolverAlCarrito) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
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
                    Text("Total a pagar", fontSize = 18.sp, fontWeight = FontWeight.Normal)
                    Text(
                        text = formatPrecioCLP(total),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E8842)
                    )
                }

                Button(
                    onClick = {
                        val nuevoPedido = checkoutViewModel.crearPedido(itemsCarrito, subtotal)
                        onConfirmarPago(nuevoPedido)
                    },
                    enabled = checkoutViewModel.esFormularioValido(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1E8842),
                        disabledContainerColor = Color(0xFFA5D6A7)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Ir al Pago Simulado", fontSize = 16.sp, color = Color.White)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Método de Entrega
            Text("Método de Entrega", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = tipoEntrega == "envio",
                    onClick = { checkoutViewModel.seleccionarTipoEntrega("envio") },
                    label = { Text("Despacho a Domicilio") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = tipoEntrega == "retiro",
                    onClick = { checkoutViewModel.seleccionarTipoEntrega("retiro") },
                    label = { Text("Retiro en Tienda") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Datos de Despacho
            Text("Datos de Despacho", fontWeight = FontWeight.Bold, fontSize = 16.sp)

            OutlinedTextField(
                value = nombre,
                onValueChange = { checkoutViewModel.cambiarNombre(it) },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = { checkoutViewModel.cambiarDireccion(it) },
                label = { Text("Dirección de Entrega") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { checkoutViewModel.cambiarTelefono(it) },
                label = { Text("Teléfono de Contacto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            // Resumen Financiero del Pedido
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Resumen del Pedido", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal productos", color = Color.Gray)
                        Text(formatPrecioCLP(subtotal))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Costo de envío", color = Color.Gray)
                        Text(if (costoEnvio == 0) "Gratis" else formatPrecioCLP(costoEnvio))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}