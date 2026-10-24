
package cl.oursourmobile.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import cl.oursourmobile.data.autenticacion.AutenticacionDemo
import cl.oursourmobile.data.autenticacion.AutenticacionRepository
import cl.oursourmobile.data.autenticacion.ResultadoLogin
import cl.oursourmobile.data.autenticacion.UsuarioSimulado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LoginUiState(
    val correo: String = "",
    val contrasena: String = "",
    val mostrarContrasena: Boolean = false,
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val mensajeError: String? = null,
    val usuarioAutenticado: UsuarioSimulado? = null
)

class LoginViewModel(
    private val autenticacionRepository: AutenticacionRepository =
        AutenticacionDemo.repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())

    val uiState: StateFlow<LoginUiState> =
        _uiState.asStateFlow()

    fun actualizarCorreo(correo: String) {
        _uiState.value = _uiState.value.copy(
            correo = correo,
            errorCorreo = null,
            mensajeError = null
        )
    }

    fun actualizarContrasena(contrasena: String) {
        _uiState.value = _uiState.value.copy(
            contrasena = contrasena,
            errorContrasena = null,
            mensajeError = null
        )
    }

    fun alternarVisibilidadContrasena() {
        _uiState.value = _uiState.value.copy(
            mostrarContrasena = !_uiState.value.mostrarContrasena
        )
    }

    fun validarFormulario(): Boolean {
        val estado = _uiState.value
        val correo = estado.correo.trim()

        val errorCorreo = when {
            correo.isBlank() ->
                "Ingresa tu correo electrónico."

            !Patterns.EMAIL_ADDRESS.matcher(correo).matches() ->
                "Ingresa un correo electrónico válido."

            else -> null
        }

        val errorContrasena = when {
            estado.contrasena.isBlank() ->
                "Ingresa tu contraseña."

            else -> null
        }

        _uiState.value = estado.copy(
            errorCorreo = errorCorreo,
            errorContrasena = errorContrasena,
            mensajeError = null,
            usuarioAutenticado = null
        )

        return errorCorreo == null && errorContrasena == null
    }

    fun iniciarSesion() {
        if (!validarFormulario()) {
            return
        }

        val estado = _uiState.value

        when (
            val resultado = autenticacionRepository.iniciarSesion(
                correo = estado.correo,
                contrasena = estado.contrasena
            )
        ) {
            is ResultadoLogin.Exito -> {
                _uiState.value = _uiState.value.copy(
                    contrasena = "",
                    mensajeError = null,
                    usuarioAutenticado = resultado.usuario
                )
            }

            ResultadoLogin.CredencialesInvalidas -> {
                _uiState.value = _uiState.value.copy(
                    contrasena = "",
                    errorContrasena = null,
                    mensajeError = "Correo o contraseña incorrectos.",
                    usuarioAutenticado = null
                )
            }
        }
    }

    fun limpiarResultadoAutenticacion() {
        _uiState.value = _uiState.value.copy(
            usuarioAutenticado = null
        )
    }
}
