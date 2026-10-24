
package cl.oursourmobile.data.autenticacion

sealed interface ResultadoRegistro {
    data class Exito(val usuario: UsuarioSimulado) : ResultadoRegistro
    data object CorreoDuplicado : ResultadoRegistro
    data object DatosInvalidos : ResultadoRegistro
}

sealed interface ResultadoLogin {
    data class Exito(val usuario: UsuarioSimulado) : ResultadoLogin
    data object CredencialesInvalidas : ResultadoLogin
}

interface AutenticacionRepository {

    fun registrar(
        nombre: String,
        correo: String,
        contrasena: String
    ): ResultadoRegistro

    fun iniciarSesion(
        correo: String,
        contrasena: String
    ): ResultadoLogin

    fun obtenerUsuarioActual(): UsuarioSimulado?

    fun cerrarSesion()
}

