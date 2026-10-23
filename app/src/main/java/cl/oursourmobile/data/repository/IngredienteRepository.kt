package cl.oursourmobile.data.repository

import cl.oursourmobile.model.Ingrediente
import cl.oursourmobile.data.mock.IngredienteMockData

class IngredienteRepository {

    fun obtenerIngredientes(): List<Ingrediente> {
        return IngredienteMockData.ingredientes
    }
}