
package cl.oursourmobile.navigation

/**
 * Rutas centralizadas de navegación de Our Sour Mobile.
 *
 * Cada pantalla tiene una ruta única para evitar
 * cadenas repetidas y facilitar el mantenimiento.
 */
sealed class Routes(val route: String) {

    // Autenticación y pantalla principal
    object Login : Routes("login")
    object Home : Routes("home")

    // Descubrimiento de cócteles
    object Ingredientes : Routes("ingredientes")
    object Recomendaciones : Routes("recomendaciones")
    object Recetas : Routes("recetas")

    object DetalleReceta : Routes("detalle_receta/{idReceta}") {
        fun crearRuta(idReceta: Long): String {
            return "detalle_receta/$idReceta"
        }
    }

    // Catálogo comercial
    object Productos : Routes("productos")

    object DetalleProducto : Routes("detalle_producto/{idProducto}") {
        fun crearRuta(idProducto: Int): String {
            return "detalle_producto/$idProducto"
        }
    }

    // Administración del catálogo
    object AdministrarProductos : Routes("administrar_productos")

    object AgregarProducto : Routes("agregar_producto")

    object EditarProducto : Routes("editar_producto/{idProducto}") {
        fun crearRuta(idProducto: Int): String {
            return "editar_producto/$idProducto"
        }
    }

    // Carrito y pedidos
    object Carrito : Routes("carrito")
    object Checkout : Routes("checkout")
    object PagoSimulado : Routes("pago_simulado")

    object ConfirmacionPedido : Routes("confirmacion_pedido/{idPedido}") {
        fun crearRuta(idPedido: Int): String {
            return "confirmacion_pedido/$idPedido"
        }
    }

    object HistorialPedidos : Routes("historial_pedidos")

    // Usuario
    object Perfil : Routes("perfil")
}
