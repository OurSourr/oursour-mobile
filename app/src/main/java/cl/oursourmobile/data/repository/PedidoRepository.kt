
package cl.oursourmobile.data.repository

import cl.oursourmobile.model.Pedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PedidoRepository {

    private val _pedidos = MutableStateFlow<List<Pedido>>(emptyList())

    val pedidos: StateFlow<List<Pedido>> = _pedidos.asStateFlow()

    private var siguienteId = 1

    @Synchronized
    fun registrarPedido(pedido: Pedido): Pedido {
        require(pedido.id == 0) {
            "Solo se pueden registrar pedidos nuevos."
        }

        require(pedido.items.isNotEmpty()) {
            "El pedido no puede estar vacío."
        }

        require(pedido.items.all { it.precio >= 0 && it.cantidad > 0 }) {
            "El pedido contiene productos inválidos."
        }

        val subtotalCalculado = pedido.items.sumOf { item ->
            Math.multiplyExact(item.precio, item.cantidad)
        }

        val totalCalculado = Math.addExact(
            subtotalCalculado,
            pedido.costoEnvio
        )

        require(pedido.subtotal == subtotalCalculado) {
            "El subtotal del pedido es incorrecto."
        }

        require(pedido.costoEnvio >= 0 && pedido.total == totalCalculado) {
            "El total del pedido es incorrecto."
        }

        require(pedido.nombreCliente.isNotBlank()) {
            "El nombre del cliente es obligatorio."
        }

        require(pedido.telefono.isNotBlank()) {
            "El teléfono del cliente es obligatorio."
        }

        require(pedido.tipoEntrega == "envio" ||
                pedido.tipoEntrega == "retiro"
        ) {
            "El tipo de entrega no es válido."
        }

        if (pedido.tipoEntrega == "envio") {
            require(pedido.direccion.isNotBlank()) {
                "La dirección es obligatoria para despacho."
            }
        }

        check(siguienteId < Int.MAX_VALUE) {
            "Se agotaron los identificadores de pedidos."
        }

        val pedidoRegistrado = pedido.copy(
            id = siguienteId++,
            items = pedido.items.map { it.copy() }
        )

        _pedidos.value = _pedidos.value + pedidoRegistrado

        return pedidoRegistrado
    }

    fun obtenerPedidoPorId(id: Int): Pedido? {
        return _pedidos.value.firstOrNull { it.id == id }
    }
}
