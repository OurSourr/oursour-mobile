package cl.oursourmobile.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import cl.oursourmobile.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Insert
    suspend fun productoAlmacenar(producto: Producto)

    @Update
    suspend fun productoActualizar(producto: Producto)

    @Delete
    suspend fun productoEliminar(producto: Producto)

    @Query("SELECT * FROM producto ORDER BY id DESC")
    fun productoListar(): Flow<List<Producto>>

    @Query("""
        SELECT * FROM producto 
        WHERE id = :id LIMIT 1
    """)
    suspend fun productoObtenerPorId(id: Int): Producto?
}