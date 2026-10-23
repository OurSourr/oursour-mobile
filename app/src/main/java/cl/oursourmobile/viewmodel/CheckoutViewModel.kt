package cl.oursourmobile.viewmodel

import androidx.lifecycle.ViewModel
import cl.oursourmobile.model.ItemCarrito
import cl.oursourmobile.model.Pedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CheckoutViewModel : ViewModel() {

    // Campos del formulario de despacho
    val nombre = MutableStateFlow("")
    val direccion = MutableStateFlow("")
    val telefono = MutableStateFlow("")

    // Tipo de entrega: "envio" o "retiro"

    fun cambiarNombre(nuevoNombre: String) {
        nombre.value = nuevoNombre
    }

    fun cambiarDireccion(nuevaDireccion: String) {
        direccion.value = nuevaDireccion
    }

    fun cambiarTelefono(nuevoTelefono: String) {
        telefono.value = nuevoTelefono
    }
    //


    private val _tipoEntrega = MutableStateFlow("envio")
    val tipoEntrega: StateFlow<String> = _tipoEntrega.asStateFlow()

    // Costo de envío según la opción ($3.990 despacho, $0 retiro)
    val costoEnvio: Int
        get() = if (_tipoEntrega.value == "envio") 3990 else 0

    fun seleccionarTipoEntrega(tipo: String) {
        _tipoEntrega.value = tipo
    }

    fun esFormularioValido(): Boolean {
        return nombre.value.isNotBlank() &&
                direccion.value.isNotBlank() &&
                telefono.value.isNotBlank()
    }

    // Genera el objeto Pedido completo para la simulación de pago o historial
    fun crearPedido(itemsCarrito: List<ItemCarrito>, subtotal: Int): Pedido {
        return Pedido(
            id = (1000..9999).random(),
            items = itemsCarrito,
            subtotal = subtotal,
            costoEnvio = costoEnvio,
            total = subtotal + costoEnvio,
            tipoEntrega = _tipoEntrega.value,
            nombreCliente = nombre.value,
            direccion = direccion.value,
            telefono = telefono.value
        )
    }
}