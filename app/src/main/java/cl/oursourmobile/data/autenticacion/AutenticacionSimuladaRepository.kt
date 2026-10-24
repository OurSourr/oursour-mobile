
package cl.oursourmobile.data.autenticacion

import java.util.UUID

class AutenticacionSimuladaRepository : AutenticacionRepository {

    private data class CredencialSimulada(
        val usuario: UsuarioSimulado,
        val contrasena: String
    )

    private val cuentas = mutableMapOf<String, CredencialSimulada>()

    private var usuarioActual: UsuarioSimulado? = null

    init {
        val administrador = UsuarioSimulado(
            id = "admin-demo",
            nombre = "Administrador Demo",
            correo = "admin@oursour.demo",
            rol = RolUsuario.ADMIN
        )

        cuentas[administrador.correo] = CredencialSimulada(
            usuario = administrador,
            contrasena = "AdminDemo123!"
        )
    }

    override fun registrar(
        nombre: String,
        correo: String,
        contrasena: String
    ): ResultadoRegistro {

        val nombreNormalizado = nombre.trim()
        val correoNormalizado = correo.trim().lowercase()

        if (
            nombreNormalizado.isBlank() ||
            !android.util.Patterns.EMAIL_ADDRESS
                .matcher(correoNormalizado).matches() ||
            contrasena.length < 8
        ) {
            return ResultadoRegistro.DatosInvalidos
        }

        if (cuentas.containsKey(correoNormalizado)) {
            return ResultadoRegistro.CorreoDuplicado
        }

        val usuario = UsuarioSimulado(
            id = UUID.randomUUID().toString(),
            nombre = nombreNormalizado,
            correo = correoNormalizado,
            rol = RolUsuario.USUARIO
        )

        cuentas[correoNormalizado] = CredencialSimulada(
            usuario = usuario,
            contrasena = contrasena
        )

        return ResultadoRegistro.Exito(usuario)
    }

    override fun iniciarSesion(
        correo: String,
        contrasena: String
    ): ResultadoLogin {

        val correoNormalizado = correo.trim().lowercase()
        val credencial = cuentas[correoNormalizado]

        if (
            credencial == null ||
            credencial.contrasena != contrasena
        ) {
            return ResultadoLogin.CredencialesInvalidas
        }

        usuarioActual = credencial.usuario

        return ResultadoLogin.Exito(credencial.usuario)
    }

    override fun obtenerUsuarioActual(): UsuarioSimulado? {
        return usuarioActual
    }

    override fun cerrarSesion() {
        usuarioActual = null
    }
}
