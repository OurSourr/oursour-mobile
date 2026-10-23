package cl.oursourmobile.data.mock

import cl.oursourmobile.model.Ingrediente

object IngredienteMockData {

    val ingredientes: List<Ingrediente> = listOf(
        Ingrediente(1L, "Limón", "Frutas"),
        Ingrediente(2L, "Naranja", "Frutas"),
        Ingrediente(3L, "Gin", "Destilados y licores"),
        Ingrediente(4L, "Ron", "Destilados y licores"),
        Ingrediente(5L, "Agua tónica", "Mezcladores"),
        Ingrediente(6L, "Jugo de piña", "Jugos"),
        Ingrediente(7L, "Our Sour Limón", "Productos Our Sour")
    )
}