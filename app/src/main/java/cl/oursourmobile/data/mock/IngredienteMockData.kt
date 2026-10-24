
package cl.oursourmobile.data.mock

import cl.oursourmobile.model.Ingrediente

/**
 * Catálogo demostrativo de ingredientes.
 *
 * Los identificadores existentes se conservan para mantener
 * la compatibilidad con las pantallas y selecciones actuales.
 *
 * Esta fuente será sustituida por los datos persistidos
 * o proporcionados por el backend cuando corresponda.
 */
object IngredienteMockData {

    val ingredientes: List<Ingrediente> = listOf(
        // Frutas
        Ingrediente(1L, "Limón", "Frutas"),
        Ingrediente(2L, "Naranja", "Frutas"),

        // Destilados y licores
        Ingrediente(3L, "Gin", "Destilados y licores"),
        Ingrediente(4L, "Ron", "Destilados y licores"),

        // Mezcladores
        Ingrediente(5L, "Agua tónica", "Mezcladores"),

        // Jugos
        Ingrediente(6L, "Jugo de piña", "Jugos"),

        // Productos Our Sour
        Ingrediente(7L, "Our Sour Limón", "Productos Our Sour"),

        // Ingredientes adicionales para las recetas
        Ingrediente(8L, "Pisco", "Destilados y licores"),
        Ingrediente(9L, "Hielo", "Complementos"),
        Ingrediente(10L, "Jarabe de goma", "Endulzantes"),
        Ingrediente(11L, "Agua", "Mezcladores"),
        Ingrediente(12L, "Azúcar", "Endulzantes")
    )

    init {
        require(ingredientes.all { it.id > 0L }) {
            "Los identificadores deben ser positivos."
        }

        require(ingredientes.map { it.id }.distinct().size == ingredientes.size) {
            "No pueden existir identificadores de ingredientes duplicados."
        }

        require(ingredientes.all {
            it.nombre.isNotBlank() && it.categoria.isNotBlank()
        }) {
            "Los ingredientes deben tener nombre y categoría."
        }
    }
}
