package cl.oursourmobile.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import cl.oursourmobile.model.Pedido
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {

    @Insert
    suspend fun pedidoAlmacenar(pedido: Pedido)

    @Update
    suspend fun pedidoActualizar(pedido: Pedido)

    @Delete
    suspend fun pedidoEliminar(pedido: Pedido)

    @Query("SELECT * FROM pedido ORDER BY id DESC")
    fun pedidoListar(): Flow<List<Pedido>>

    @Query("""
        SELECT * FROM pedido 
        WHERE id = :id LIMIT 1
    """)
    suspend fun pedidoObtenerPorId(id: Int): Pedido?
}