
package cl.oursourmobile.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import cl.oursourmobile.data.autenticacion.AutenticacionDemo
import cl.oursourmobile.data.autenticacion.AutenticacionRepository
import cl.oursourmobile.data.autenticacion.ResultadoRegistro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class RegistroUiState(
    val nombre: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val confirmarContrasena: String = "",
    val errorNombre: String? = null,
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val errorConfirmacion: String? = null,
    val mensajeRegistro: String? = null,
    val registroExitoso: Boolean = false
)

class RegistroViewModel(
    private val autenticacionRepository: AutenticacionRepository =
        AutenticacionDemo.repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())

    val uiState: StateFlow<RegistroUiState> =
        _uiState.asStateFlow()

    fun actualizarNombre(valor: String) {
        _uiState.value = _uiState.value.copy(
            nombre = valor,
            errorNombre = null,
            mensajeRegistro = null,
            registroExitoso = false
        )
    }

    fun actualizarCorreo(valor: String) {
        _uiState.value = _uiState.value.copy(
            correo = valor,
            errorCorreo = null,
            mensajeRegistro = null,
            registroExitoso = false
        )
    }

    fun actualizarContrasena(valor: String) {
        _uiState.value = _uiState.value.copy(
            contrasena = valor,
            errorContrasena = null,
            errorConfirmacion = null,
            mensajeRegistro = null,
            registroExitoso = false
        )
    }

    fun actualizarConfirmacion(valor: String) {
        _uiState.value = _uiState.value.copy(
            confirmarContrasena = valor,
            errorConfirmacion = null,
            mensajeRegistro = null,
            registroExitoso = false
        )
    }

    fun validarFormulario(): Boolean {
        val estado = _uiState.value

        val errorNombre = when {
            estado.nombre.isBlank() ->
                "Ingresa tu nombre."
            else -> null
        }

        val errorCorreo = when {
            estado.correo.isBlank() ->
                "Ingresa tu correo electrónico."

            !Patterns.EMAIL_ADDRESS.matcher(
                estado.correo.trim()
            ).matches() ->
                "Ingresa un correo electrónico válido."

            else -> null
        }

        val errorContrasena = when {
            estado.contrasena.isBlank() ->
                "Ingresa una contraseña."

            estado.contrasena.length < 8 ->
                "La contraseña debe tener al menos 8 caracteres."

            else -> null
        }

        val errorConfirmacion = when {
            estado.confirmarContrasena.isBlank() ->
                "Confirma tu contraseña."

            estado.confirmarContrasena != estado.contrasena ->
                "Las contraseñas no coinciden."

            else -> null
        }

        _uiState.value = estado.copy(
            errorNombre = errorNombre,
            errorCorreo = errorCorreo,
            errorContrasena = errorContrasena,
            errorConfirmacion = errorConfirmacion,
            mensajeRegistro = null,
            registroExitoso = false
        )

        return errorNombre == null &&
                errorCorreo == null &&
                errorContrasena == null &&
                errorConfirmacion == null
    }

    fun registrarUsuario() {
        if (!validarFormulario()) {
            return
        }

        val estado = _uiState.value

        val resultado = autenticacionRepository.registrar(
            nombre = estado.nombre,
            correo = estado.correo,
            contrasena = estado.contrasena
        )

        when (resultado) {
            is ResultadoRegistro.Exito -> {
                _uiState.value = _uiState.value.copy(
                    contrasena = "",
                    confirmarContrasena = "",
                    mensajeRegistro =
                        "Cuenta de prueba creada correctamente. Ya puedes iniciar sesión.",
                    registroExitoso = true
                )
            }

            ResultadoRegistro.CorreoDuplicado -> {
                _uiState.value = _uiState.value.copy(
                    errorCorreo = "Este correo ya está registrado.",
                    mensajeRegistro = null,
                    registroExitoso = false
                )
            }

            ResultadoRegistro.DatosInvalidos -> {
                _uiState.value = _uiState.value.copy(
                    mensajeRegistro =
                        "No fue posible registrar la cuenta. Revisa los datos.",
                    registroExitoso = false
                )
            }
        }
    }
}
