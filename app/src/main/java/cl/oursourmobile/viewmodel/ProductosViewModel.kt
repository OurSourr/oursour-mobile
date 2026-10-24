
package cl.oursourmobile.viewmodel

import androidx.lifecycle.ViewModel
import cl.oursourmobile.data.productos.ProductosDemo
import cl.oursourmobile.data.productos.ProductosRepository
import cl.oursourmobile.model.Producto
import kotlinx.coroutines.flow.StateFlow

class ProductosViewModel(
    private val repository: ProductosRepository = ProductosDemo.repository
) : ViewModel() {

    val productos: StateFlow<List<Producto>> = repository.productos

    fun obtenerProductoPorId(id: Int): Producto? {
        return repository.obtenerPorId(id)
    }

    fun agregarProducto(producto: Producto): Boolean {
        return repository.agregar(producto)
    }

    fun actualizarProducto(producto: Producto): Boolean {
        return repository.actualizar(producto)
    }

    fun eliminarProducto(id: Int): Boolean {
        return repository.eliminar(id)
    }

    /**
     * Crea un producto utilizando los datos del formulario.
     *
     * El identificador inicial es provisional.
     * El repositorio asigna el identificador definitivo.
     */
    fun crearProducto(
        nombre: String,
        descripcion: String,
        precio: Int,
        imagenKey: String,
        disponible: Boolean
    ): Boolean {
        if (!datosValidos(nombre, descripcion, precio, imagenKey)) {
            return false
        }

        val producto = Producto(
            id = 1,
            nombre = nombre.trim(),
            descripcion = descripcion.trim(),
            precio = precio,
            imagenKey = imagenKey.trim(),
            disponible = disponible
        )

        return repository.agregar(producto)
    }

    /**
     * Actualiza un producto existente conservando su ID.
     */
    fun editarProducto(
        id: Int,
        nombre: String,
        descripcion: String,
        precio: Int,
        imagenKey: String,
        disponible: Boolean
    ): Boolean {
        if (!datosValidos(nombre, descripcion, precio, imagenKey)) {
            return false
        }

        val productoExistente = repository.obtenerPorId(id)
            ?: return false

        val productoActualizado = productoExistente.copy(
            nombre = nombre.trim(),
            descripcion = descripcion.trim(),
            precio = precio,
            imagenKey = imagenKey.trim(),
            disponible = disponible
        )

        return repository.actualizar(productoActualizado)
    }

    /**
     * Validaciones compartidas por creación y edición.
     */
    private fun datosValidos(
        nombre: String,
        descripcion: String,
        precio: Int,
        imagenKey: String
    ): Boolean {
        return nombre.isNotBlank() &&
                descripcion.isNotBlank() &&
                precio >= 0 &&
                imagenKey.isNotBlank()
    }
}
