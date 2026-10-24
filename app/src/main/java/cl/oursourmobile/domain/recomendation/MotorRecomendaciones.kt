
package cl.oursourmobile.domain.recommendation

import cl.oursourmobile.model.Receta

/**
 * Resultado del análisis de compatibilidad de una receta.
 *
 * Los identificadores permiten recuperar los datos de los
 * ingredientes desde el repositorio correspondiente.
 */
data class RecomendacionReceta(
    val receta: Receta,
    val porcentajeCompatibilidad: Int,
    val ingredientesDisponiblesIds: Set<Long>,
    val ingredientesFaltantesIds: Set<Long>
) {
    val sePuedePreparar: Boolean
        get() = ingredientesFaltantesIds.isEmpty()
}

/**
 * Motor de recomendaciones independiente de Android,
 * Jetpack Compose y las fuentes de datos.
 *
 * Solo utiliza las recetas y los identificadores
 * de ingredientes recibidos como parámetros.
 */
class MotorRecomendaciones {

    /**
     * Calcula las recomendaciones a partir de los ingredientes
     * seleccionados por el usuario.
     *
     * Reglas:
     * - Solo considera recetas publicadas.
     * - Excluye recetas sin ingredientes obligatorios.
     * - No duplica ingredientes seleccionados.
     * - Calcula coincidencias usando identificadores.
     * - Identifica los ingredientes obligatorios faltantes.
     * - Ordena por compatibilidad descendente.
     * - Desempata por cantidad de ingredientes faltantes
     *   y por nombre de receta.
     *
     * @param recetas Recetas disponibles para analizar.
     * @param ingredientesSeleccionadosIds Identificadores
     * de ingredientes que tiene el usuario.
     *
     * @return Recomendaciones ordenadas por compatibilidad.
     */
    fun calcularRecomendaciones(
        recetas: List<Receta>,
        ingredientesSeleccionadosIds: Collection<Long>
    ): List<RecomendacionReceta> {

        val seleccionados = ingredientesSeleccionadosIds
            .filter { it > 0L }
            .toSet()

        if (seleccionados.isEmpty()) {
            return emptyList()
        }

        return recetas
            .asSequence()
            .filter { it.esRecomendable() }
            .map { receta ->

                val obligatorios = receta.ingredientesObligatoriosIds

                val disponibles = obligatorios.intersect(seleccionados)
                val faltantes = obligatorios - seleccionados

                val porcentaje = (
                        disponibles.size.toDouble() /
                                obligatorios.size.toDouble() *
                                100.0
                        ).toInt()

                RecomendacionReceta(
                    receta = receta,
                    porcentajeCompatibilidad = porcentaje,
                    ingredientesDisponiblesIds = disponibles,
                    ingredientesFaltantesIds = faltantes
                )
            }
            .filter { it.porcentajeCompatibilidad > 0 }
            .sortedWith(
                compareByDescending<RecomendacionReceta> {
                    it.porcentajeCompatibilidad
                }
                    .thenBy {
                        it.ingredientesFaltantesIds.size
                    }
                    .thenBy {
                        it.receta.nombre
                    }
            )
            .toList()
    }
}
