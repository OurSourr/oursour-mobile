package cl.oursourmobile.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
// Importaciones ajustadas a los paquetes exactos de tu proyecto

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import cl.oursourmobile.data.database.AppDatabase
import cl.oursourmobile.data.repository.UsuarioRepository


data class LoginUiState(
    val correo: String = "",
    val contrasena: String = "",
    val mostrarContrasena: Boolean = false,
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val mensajeError: String? = null,
    val usuarioAutenticado: Any? = null
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UsuarioRepository

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        // Estándar del profesor: Instanciación de la BD y DAO dentro de init
        val usuarioDao = AppDatabase.getDatabase(context = application).usuarioDao()
        repository = UsuarioRepository(usuarioDao)
    }

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
            correo.isBlank() -> "Ingresa tu correo electrónico."
            !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> "Ingresa un correo electrónico válido."
            else -> null
        }

        val errorContrasena = when {
            estado.contrasena.isBlank() -> "Ingresa tu contraseña."
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

        // Estándar del profesor: Corrutina en viewModelScope consultando a Room
        viewModelScope.launch {
            try {
                // Llama al método login(mail, pass) de tu UsuarioRepository
                val usuario = repository.login(estado.correo, estado.contrasena)

                if (usuario != null) {
                    _uiState.value = _uiState.value.copy(
                        contrasena = "",
                        mensajeError = null,
                        usuarioAutenticado = usuario
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        contrasena = "",
                        errorContrasena = null,
                        mensajeError = "Correo o contraseña incorrectos.",
                        usuarioAutenticado = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    mensajeError = "Error al conectar con la base de datos.",
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