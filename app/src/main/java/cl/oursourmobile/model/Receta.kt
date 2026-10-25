
package cl.oursourmobile.model
import androidx.room3.PrimaryKey
import androidx.room3.Entity
import androidx.room3.Ignore

@Entity(tableName = "receta")

data class Receta(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val publicada: Boolean = true,
    val ingredientesTexto: String = "",
    val preparacionTexto: String = ""
) {
    @Ignore
    val ingredientesObligatoriosIds: Set<Long> = emptySet()
    @Ignore
    val ingredientes: List<String> = ingredientesTexto.split(";").filter { it.isNotBlank() }
    @Ignore
    val preparacion: List<String> = preparacionTexto.split(";").filter { it.isNotBlank() }
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

        if (ingredientesObligatoriosIds.isNotEmpty()) {
            require(ingredientesObligatoriosIds.all { it > 0L }) {
                "Los identificadores de ingredientes deben ser positivos."
            }
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
        return publicada && ingredientesTexto.isNotEmpty()
    }
}
