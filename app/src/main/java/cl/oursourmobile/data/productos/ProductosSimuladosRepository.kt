
package cl.oursourmobile.data.productos

import cl.oursourmobile.data.autenticacion.AutorizacionDemo
import cl.oursourmobile.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductosSimuladosRepository : ProductosRepository {

    private val _productos = MutableStateFlow(
        listOf(
            Producto(
                id = 1,
                nombre = "Agua Tónica Our Sour",
                descripcion = "El complemento ideal para tus combinaciones.",
                precio = 2990,
                imagenKey = "agua_tonica"
            ),
            Producto(
                id = 2,
                nombre = "Limón Our Sour",
                descripcion = "Un toque cítrico y fresco para tus recetas.",
                precio = 3990,
                imagenKey = "limon_our"
            ),
            Producto(
                id = 3,
                nombre = "Gin Our Sour",
                descripcion = "Un ingrediente protagonista para tus cócteles.",
                precio = 12990,
                imagenKey = "gin_our"
            ),
            Producto(
                id = 4,
                nombre = "Mix Our Sour",
                descripcion = "Una mezcla pensada para explorar nuevos sabores.",
                precio = 18990,
                imagenKey = "mix_sour"
            )
        )
    )

    override val productos: StateFlow<List<Producto>> =
        _productos.asStateFlow()

    private var ultimoIdAsignado: Int =
        _productos.value.maxOfOrNull { it.id } ?: 0

    /**
     * Genera el siguiente identificador disponible.
     *
     * No modifica el contador hasta que se agrega
     * correctamente el producto.
     */
    private fun generarSiguienteId(): Int? {
        if (ultimoIdAsignado == Int.MAX_VALUE) {
            return null
        }

        return ultimoIdAsignado + 1
    }

    override fun obtenerPorId(id: Int): Producto? {
        return _productos.value.find { it.id == id }
    }

    override fun agregar(producto: Producto): Boolean {
        if (!AutorizacionDemo.puedeAdministrarProductos()) {
            return false
        }

        val nuevoId = generarSiguienteId() ?: return false

        val nuevoProducto = producto.copy(id = nuevoId)

        _productos.value = _productos.value + nuevoProducto

        ultimoIdAsignado = nuevoId

        return true
    }

    override fun actualizar(producto: Producto): Boolean {
        if (!AutorizacionDemo.puedeAdministrarProductos()) {
            return false
        }

        if (_productos.value.none { it.id == producto.id }) {
            return false
        }

        _productos.value = _productos.value.map { existente ->
            if (existente.id == producto.id) {
                producto
            } else {
                existente
            }
        }

        return true
    }

    override fun eliminar(id: Int): Boolean {
        if (!AutorizacionDemo.puedeAdministrarProductos()) {
            return false
        }

        if (_productos.value.none { it.id == id }) {
            return false
        }

        _productos.value = _productos.value.filterNot {
            it.id == id
        }

        return true
    }
}
