
package cl.oursourmobile.model

data class Receta(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val ingredientes: List<String>,
    val preparacion: List<String>,
    val publicada: Boolean = true,
    val ingredientesObligatoriosIds: Set<Long> = emptySet()
) {
    init {
        require(id > 0L) {
            "El identificador de la receta debe ser positivo."
        }

        require(nombre.isNotBlank()) {
            "El nombre de la receta es obligatorio."
        }

        require(descripcion.isNotBlank()) {
            "La descripción de la receta es obligatoria."
        }

        require(categoria.isNotBlank()) {
            "La categoría de la receta es obligatoria."
        }

        require(ingredientes.all { it.isNotBlank() }) {
            "Los nombres de ingredientes no pueden estar vacíos."
        }

        require(preparacion.all { it.isNotBlank() }) {
            "Las instrucciones no pueden estar vacías."
        }

        require(ingredientesObligatoriosIds.all { it > 0L }) {
            "Los identificadores de ingredientes deben ser positivos."
        }

        if (publicada) {
            require(ingredientes.isNotEmpty()) {
                "Una receta publicada debe tener ingredientes."
            }

            require(preparacion.isNotEmpty()) {
                "Una receta publicada debe tener instrucciones."
            }
        }
    }

    /**
     * Indica si la receta tiene la información mínima
     * necesaria para participar en recomendaciones.
     */
    fun esRecomendable(): Boolean {
        return publicada && ingredientesObligatoriosIds.isNotEmpty()
    }
}
