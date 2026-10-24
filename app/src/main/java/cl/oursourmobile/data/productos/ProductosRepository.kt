
package cl.oursourmobile.data.productos

import cl.oursourmobile.model.Producto
import kotlinx.coroutines.flow.StateFlow

interface ProductosRepository {

    val productos: StateFlow<List<Producto>>

    fun obtenerPorId(id: Int): Producto?

    fun agregar(producto: Producto): Boolean

    fun actualizar(producto: Producto): Boolean

    fun eliminar(id: Int): Boolean
}
