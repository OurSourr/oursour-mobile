package cl.oursourmobile.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import cl.oursourmobile.model.Receta
import kotlinx.coroutines.flow.Flow

@Dao
interface RecetaDao {

    @Insert
    suspend fun recetaAlmacenar(receta: Receta)

    @Update
    suspend fun recetaActualizar(receta: Receta)

    @Delete
    suspend fun recetaEliminar(receta: Receta)

    @Query("SELECT * FROM receta ORDER BY id DESC")
    fun recetaListar(): Flow<List<Receta>>

    @Query("""
        SELECT * FROM receta 
        WHERE id = :id LIMIT 1
    """)
    suspend fun recetaObtenerPorId(id: Long): Receta?
}