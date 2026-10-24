
package cl.oursourmobile.viewmodel

import androidx.lifecycle.ViewModel
import cl.oursourmobile.data.repository.PedidoRepository
import cl.oursourmobile.model.Pedido

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PedidoViewModel : ViewModel() {

    private val repository = PedidoRepository()

    private val _pedidoPendiente = MutableStateFlow<Pedido?>(null)
    val pedidoPendiente: StateFlow<Pedido?> =
        _pedidoPendiente.asStateFlow()

    private val _ultimoPedidoConfirmado = MutableStateFlow<Pedido?>(null)
    val ultimoPedidoConfirmado: StateFlow<Pedido?> =
        _ultimoPedidoConfirmado.asStateFlow()

    val historialPedidos: StateFlow<List<Pedido>> =
        repository.pedidos

    @Synchronized
    fun guardarPedidoPendiente(pedido: Pedido) {

        require(pedido.id == 0) {
            "El pedido pendiente debe ser nuevo."
        }

        require(pedido.items.isNotEmpty()) {
            "El pedido debe contener productos."
        }

        require(
            pedido.items.all {
                it.precio >= 0 && it.cantidad > 0
            }
        ) {
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
            "El subtotal del pedido es inconsistente."
        }

        require(
            pedido.costoEnvio >= 0 &&
                    pedido.total == totalCalculado
        ) {
            "El total del pedido es inconsistente."
        }

        // Guardamos una copia independiente de los productos.
        _pedidoPendiente.value = pedido.copy(
            items = pedido.items.map { it.copy() }
        )

        // Una nueva compra inicia un nuevo ciclo de confirmación.
        _ultimoPedidoConfirmado.value = null
    }

    @Synchronized
    fun confirmarPagoSimulado(): Pedido? {

        // Si no hay pedido pendiente, no registramos nada.
        // Una llamada repetida no genera una segunda compra.
        val pedido = _pedidoPendiente.value ?: return null

        // El repositorio registra el pedido una sola vez
        // dentro de esta operación sincronizada.
        val pedidoConfirmado = repository.registrarPedido(pedido)

        // Conservamos el comprobante del pedido confirmado.
        _ultimoPedidoConfirmado.value = pedidoConfirmado

        // Consumimos el pedido pendiente para impedir
        // que se vuelva a confirmar.
        _pedidoPendiente.value = null

        return pedidoConfirmado
    }

    @Synchronized
    fun limpiarPedidoPendiente() {
        _pedidoPendiente.value = null
    }

    fun obtenerPedidoPorId(id: Int): Pedido? {
        return repository.obtenerPedidoPorId(id)
    }
}
