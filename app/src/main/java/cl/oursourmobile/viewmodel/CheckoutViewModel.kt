
package cl.oursourmobile.viewmodel

import androidx.lifecycle.ViewModel
import cl.oursourmobile.model.ItemCarrito
import cl.oursourmobile.model.Pedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CheckoutViewModel : ViewModel() {

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre.asStateFlow()

    private val _direccion = MutableStateFlow("")
    val direccion: StateFlow<String> = _direccion.asStateFlow()

    private val _telefono = MutableStateFlow("")
    val telefono: StateFlow<String> = _telefono.asStateFlow()

    private val _tipoEntrega = MutableStateFlow(TIPO_ENVIO)
    val tipoEntrega: StateFlow<String> = _tipoEntrega.asStateFlow()

    private val _mostrarErrores = MutableStateFlow(false)
    val mostrarErrores: StateFlow<Boolean> = _mostrarErrores.asStateFlow()

    val costoEnvio: Int
        get() = if (_tipoEntrega.value == TIPO_ENVIO) COSTO_ENVIO else 0

    fun cambiarNombre(valor: String) {
        _nombre.value = valor.take(100)
    }

    fun cambiarDireccion(valor: String) {
        _direccion.value = valor.take(200)
    }

    fun cambiarTelefono(valor: String) {
        _telefono.value = valor.take(20)
    }

    fun seleccionarTipoEntrega(tipo: String) {
        require(tipo == TIPO_ENVIO || tipo == TIPO_RETIRO) {
            "El tipo de entrega no es válido."
        }
        _tipoEntrega.value = tipo
    }

    fun errorNombre(): String? {
        val valor = _nombre.value.trim()

        if (valor.isBlank()) {
            return "Ingresa tu nombre completo."
        }

        if (!REGEX_NOMBRE.matches(valor)) {
            return "Ingresa nombre y apellido, usando solo letras."
        }

        return null
    }

    fun errorTelefono(): String? {
        val valor = _telefono.value.trim()

        if (valor.isBlank()) {
            return "Ingresa tu teléfono de contacto."
        }

        val normalizado = valor.replace(Regex("[\\s()-]"), "")

        if (!REGEX_TELEFONO.matches(normalizado)) {
            return "Usa un celular chileno: 9XXXXXXXX o +569XXXXXXXX."
        }

        return null
    }

    fun errorDireccion(): String? {
        if (_tipoEntrega.value == TIPO_RETIRO) {
            return null
        }

        val valor = _direccion.value.trim()

        if (valor.isBlank()) {
            return "Ingresa una dirección de entrega."
        }

        if (valor.length < 8) {
            return "Ingresa una dirección más completa (mínimo 8 caracteres)."
        }

        return null
    }

    fun esFormularioValido(): Boolean {
        return errorNombre() == null &&
                errorTelefono() == null &&
                errorDireccion() == null
    }

    fun validarFormulario(): Boolean {
        _mostrarErrores.value = true
        return esFormularioValido()
    }

    fun crearPedido(itemsCarrito: List<ItemCarrito>): Pedido {
        require(esFormularioValido()) {
            "Debes corregir los datos del formulario."
        }

        require(itemsCarrito.isNotEmpty()) {
            "No puedes generar un pedido con el carrito vacío."
        }

        require(itemsCarrito.all { it.precio >= 0 && it.cantidad > 0 }) {
            "El carrito contiene productos inválidos."
        }

        val subtotal = itemsCarrito.sumOf {
            Math.multiplyExact(it.precio, it.cantidad)
        }

        val total = Math.addExact(subtotal, costoEnvio)

        return Pedido(
            id = 0,
            items = itemsCarrito.map { it.copy() },
            subtotal = subtotal,
            costoEnvio = costoEnvio,
            total = total,
            tipoEntrega = _tipoEntrega.value,
            nombreCliente = _nombre.value.trim(),
            direccion = if (_tipoEntrega.value == TIPO_ENVIO) {
                _direccion.value.trim()
            } else {
                ""
            },
            telefono = _telefono.value.trim()
        )
    }

    companion object {
        const val TIPO_ENVIO = "envio"
        const val TIPO_RETIRO = "retiro"
        const val COSTO_ENVIO = 3990

        private val REGEX_NOMBRE =
            Regex("^[\\p{L}]+(?:[ '-][\\p{L}]+)+$")

        private val REGEX_TELEFONO =
            Regex("^(?:\\+?56)?9\\d{8}$")
    }
}
