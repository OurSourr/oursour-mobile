package cl.oursourmobile.viewmodel

import androidx.lifecycle.ViewModel
import cl.oursourmobile.data.repository.IngredienteRepository
import cl.oursourmobile.model.Ingrediente

class IngredientesViewModel : ViewModel() {

    private val repository = IngredienteRepository()

    val ingredientes: List<Ingrediente> =
        repository.obtenerIngredientes()
}