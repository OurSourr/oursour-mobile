package cl.oursourmobile.dao
//por cada clase que se convertira en tabla la buena practica dice que deberian tener su Dao


import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import cl.oursourmobile.model.ItemCarrito
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemCarritoDao {

    @Insert
    suspend fun itemCarritoAlmacenar(itemCarrito: ItemCarrito)

    @Update
    suspend fun itemCarritoActualizar(itemCarrito: ItemCarrito)

    @Delete
    suspend fun itemCarritoEliminar(itemCarrito: ItemCarrito)

    @Query("SELECT * FROM item_carrito ORDER BY id DESC")
    fun itemCarritoListar(): Flow<List<ItemCarrito>>

    @Query("DELETE FROM item_carrito")
    suspend fun itemCarritoVaciar()
}