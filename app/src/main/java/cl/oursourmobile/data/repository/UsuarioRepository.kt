package cl.oursourmobile.data.repository

import cl.oursourmobile.dao.UsuarioDao
import cl.oursourmobile.model.Usuario
import kotlinx.coroutines.flow.Flow


class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    val usuarios: Flow<List<Usuario>> = usuarioDao.usuarioListar()

    suspend fun almacenar(usuario: Usuario) {
        usuarioDao.usuarioAlmacenar(usuario)
    }

    suspend fun actualizar(usuario: Usuario) {
        usuarioDao.usuarioActualizar(usuario)
    }

    suspend fun eliminar(usuario: Usuario) {
        usuarioDao.usuarioEliminar(usuario)
    }

    suspend fun login(mail: String, pass: String): Usuario? {
        return usuarioDao.usuarioLogin(mail, pass)
    }

    suspend fun obtenerPorId(id: Long): Usuario? {
        return usuarioDao.usuarioObtenerPorId(id)
    }
}