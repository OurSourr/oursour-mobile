package cl.oursourmobile.repository

import cl.oursourmobile.dao.RecetaDao
import cl.oursourmobile.model.Receta
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecetaRepository(private val recetaDao: RecetaDao) {

    // Lista en tiempo real conectada a Room
    val recetas: Flow<List<Receta>> = recetaDao.recetaListar()

    suspend fun almacenar(receta: Receta) {
        recetaDao.recetaAlmacenar(receta)
    }

    suspend fun actualizar(receta: Receta) {
        recetaDao.recetaActualizar(receta)
    }

    suspend fun eliminar(receta: Receta) {
        recetaDao.recetaEliminar(receta)
    }

    suspend fun obtenerPorId(id: Long): Receta? {
        if (id <= 0L) return null
        return recetaDao.recetaObtenerPorId(id)
    }

    // Búsqueda en tiempo real filtrando por texto o categoría sobre el Flow de Room
    fun buscarRecetas(
        consulta: String = "",
        categoria: String? = null
    ): Flow<List<Receta>> {
        val texto = consulta.trim()

        return recetas.map { lista ->
            lista.filter { receta ->
                val coincideTexto = texto.isBlank() ||
                        receta.nombre.contains(texto, ignoreCase = true) ||
                        receta.descripcion.contains(texto, ignoreCase = true)

                val coincideCategoria = categoria.isNullOrBlank() ||
                        categoria.equals("Todas", ignoreCase = true) ||
                        receta.categoria.equals(categoria, ignoreCase = true)

                coincideTexto && coincideCategoria
            }
        }
    }
}