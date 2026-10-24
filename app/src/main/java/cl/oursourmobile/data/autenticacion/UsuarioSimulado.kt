
package cl.oursourmobile.data.autenticacion

enum class RolUsuario {
    USUARIO,
    ADMIN
}

data class UsuarioSimulado(
    val id: String,
    val nombre: String,
    val correo: String,
    val rol: RolUsuario
)
