
package cl.oursourmobile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * Conserva los ingredientes seleccionados para el flujo
 * de descubrimiento y recomendaciones.
 *
 * El cálculo de compatibilidad corresponde al
 * MotorRecomendaciones, no a este ViewModel.
 */
class RecomendacionesViewModel : ViewModel() {

    var ingredientesSeleccionadosIds: List<Long> by mutableStateOf(emptyList())
        private set

    /**
     * Actualiza la selección, eliminando identificadores
     * inválidos y duplicados.
     */
    fun actualizarIngredientesSeleccionados(ids: Collection<Long>) {
        ingredientesSeleccionadosIds = ids
            .filter { it > 0L }
            .distinct()
    }

    /**
     * Limpia la selección cuando sea necesario.
     */
    fun limpiarSeleccion() {
        ingredientesSeleccionadosIds = emptyList()
    }
}
