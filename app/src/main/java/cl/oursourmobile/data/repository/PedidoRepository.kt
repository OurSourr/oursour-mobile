package cl.oursourmobile.repository

import cl.oursourmobile.dao.PedidoDao
import cl.oursourmobile.model.Pedido
import kotlinx.coroutines.flow.Flow

class PedidoRepository(private val pedidoDao: PedidoDao) {

    // Lista de pedidos observada en tiempo real desde Room
    val pedidos: Flow<List<Pedido>> = pedidoDao.pedidoListar()

    suspend fun registrarPedido(pedido: Pedido) {
        // Validaciones de negocio importadas de la versión de tu compañero
        require(pedido.nombreCliente.isNotBlank()) {
            "El nombre del cliente es obligatorio."
        }
        require(pedido.telefono.isNotBlank()) {
            "El teléfono del cliente es obligatorio."
        }
        require(pedido.tipoEntrega == "envio" || pedido.tipoEntrega == "retiro") {
            "El tipo de entrega no es válido."
        }
        if (pedido.tipoEntrega == "envio") {
            require(pedido.direccion.isNotBlank()) {
                "La dirección es obligatoria para despacho."
            }
        }

        // Guardar en la base de datos Room
        pedidoDao.pedidoAlmacenar(pedido)
    }

    suspend fun actualizar(pedido: Pedido) {
        pedidoDao.pedidoActualizar(pedido)
    }

    suspend fun eliminar(pedido: Pedido) {
        pedidoDao.pedidoEliminar(pedido)
    }

    suspend fun obtenerPedidoPorId(id: Int): Pedido? {
        if (id <= 0) return null
        return pedidoDao.pedidoObtenerPorId(id)
    }
}