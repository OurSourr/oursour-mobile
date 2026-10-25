package cl.oursourmobile.repository
import cl.oursourmobile.dao.IngredienteDao
import cl.oursourmobile.model.Ingrediente
import kotlinx.coroutines.flow.Flow

class IngredienteRepository(private val ingredienteDao: IngredienteDao) {

    // Lista en tiempo real observada desde Room
    val ingredientes: Flow<List<Ingrediente>> = ingredienteDao.ingredienteListar()

    // Mantenemos este nombre por compatibilidad con el código que tenía tu compañero
    fun obtenerIngredientes(): Flow<List<Ingrediente>> {
        return ingredientes
    }

    suspend fun almacenar(ingrediente: Ingrediente) {
        ingredienteDao.ingredienteAlmacenar(ingrediente)
    }

    suspend fun actualizar(ingrediente: Ingrediente) {
        ingredienteDao.ingredienteActualizar(ingrediente)
    }

    suspend fun eliminar(ingrediente: Ingrediente) {
        ingredienteDao.ingredienteEliminar(ingrediente)
    }

    suspend fun obtenerPorId(id: Long): Ingrediente? {
        if (id <= 0L) return null
        return ingredienteDao.ingredienteObtenerPorId(id)
    }
}