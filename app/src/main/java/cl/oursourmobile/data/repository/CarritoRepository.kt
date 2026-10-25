package cl.oursourmobile.repository

import cl.oursourmobile.dao.ItemCarritoDao
import cl.oursourmobile.model.ItemCarrito
import kotlinx.coroutines.flow.Flow

class ItemCarritoRepository(private val itemCarritoDao: ItemCarritoDao) {

    // Lista en tiempo real de los ítems en el carrito
    val itemsCarrito: Flow<List<ItemCarrito>> = itemCarritoDao.itemCarritoListar()

    suspend fun agregarOActualizar(item: ItemCarrito) {
        itemCarritoDao.itemCarritoAlmacenar(item)
    }

    suspend fun actualizarCantidad(item: ItemCarrito) {
        itemCarritoDao.itemCarritoActualizar(item)
    }

    suspend fun eliminarItem(item: ItemCarrito) {
        itemCarritoDao.itemCarritoEliminar(item)
    }

    suspend fun vaciarCarrito() {
        itemCarritoDao.itemCarritoVaciar()
    }
}