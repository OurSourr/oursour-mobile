package cl.oursourmobile.data.repository

import cl.oursourmobile.dao.ProductoDao
import cl.oursourmobile.model.Producto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class ProductoRepository(private val productoDao: ProductoDao) {

    val productos: Flow<List<Producto>> = productoDao.productoListar()

    suspend fun almacenar(producto: Producto) {
        productoDao.productoAlmacenar(producto)
    }

    suspend fun actualizar(producto: Producto) {
        productoDao.productoActualizar(producto)
    }

    suspend fun eliminar(producto: Producto) {
        productoDao.productoEliminar(producto)
    }

    suspend fun obtenerPorId(id: Int): Producto? {
        if (id <= 0) return null
        return productoDao.productoObtenerPorId(id)
    }

    // Búsqueda por texto (nombre o descripción) sobre el Flow de Room
    fun buscarProductos(consulta: String = ""): Flow<List<Producto>> {
        val texto = consulta.trim()

        return productos.map { lista ->
            lista.filter { producto ->
                texto.isBlank() ||
                        producto.nombre.contains(texto, ignoreCase = true) ||
                        producto.descripcion.contains(texto, ignoreCase = true)
            }
        }
    }

    // Filtra solo los productos disponibles
    fun obtenerDisponibles(): Flow<List<Producto>> {
        return productos.map { lista ->
            lista.filter { it.disponible }
        }
    }
}