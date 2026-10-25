package cl.oursourmobile.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import cl.oursourmobile.model.Ingrediente
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredienteDao {

    @Insert
    suspend fun ingredienteAlmacenar(ingrediente: Ingrediente)

    @Update
    suspend fun ingredienteActualizar(ingrediente: Ingrediente)

    @Delete
    suspend fun ingredienteEliminar(ingrediente: Ingrediente)

    @Query("SELECT * FROM ingrediente ORDER BY id DESC")
    fun ingredienteListar(): Flow<List<Ingrediente>>

    @Query("""
        SELECT * FROM ingrediente 
        WHERE id = :id LIMIT 1
    """)
    suspend fun ingredienteObtenerPorId(id: Long): Ingrediente?
}