package cl.oursourmobile.navigation
//responsable de la navegacion
sealed class Routes(val route: String) {
    // Rutas globales / autenticación
    object Login : Routes("login")
    object Home : Routes("home")

    // pantallas (Flujo Comercial y Pedidos)
    object DetalleProducto : Routes("detalle_producto/{idProducto}") {
        fun crearRuta(idProducto: Int) = "detalle_producto/$idProducto"
    }
    object Carrito : Routes("carrito")
    object Checkout : Routes("checkout")
    object PagoSimulado : Routes("pago_simulado")
    object ConfirmacionPedido : Routes("confirmacion_pedido/{idPedido}") {
        fun crearRuta(idPedido: Int) = "confirmacion_pedido/$idPedido"
    }
    object HistorialPedidos : Routes("historial_pedidos")
}