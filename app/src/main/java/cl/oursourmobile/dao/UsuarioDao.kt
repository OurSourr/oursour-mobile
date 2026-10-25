package cl.oursourmobile.dao
import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import cl.oursourmobile.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert
    suspend fun usuarioAlmacenar(usuario: Usuario)

    @Update
    suspend fun usuarioActualizar(usuario: Usuario)

    @Delete
    suspend fun usuarioEliminar(usuario: Usuario)

    @Query("SELECT * FROM usuario ORDER BY id DESC")
    fun usuarioListar(): Flow<List<Usuario>>

    @Query("""
        SELECT * FROM usuario 
        WHERE email = :mail AND pass = :pass 
        LIMIT 1
    """)
    suspend fun usuarioLogin(mail: String, pass: String): Usuario?

    @Query("""
        SELECT * FROM usuario 
        WHERE id = :id LIMIT 1
    """)
    suspend fun usuarioObtenerPorId(id: Long): Usuario?
}