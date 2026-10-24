
package cl.oursourmobile.data.repository

import cl.oursourmobile.model.Receta

/**
 * Repositorio demostrativo de recetas de Our Sour Mobile.
 *
 * Centraliza los datos utilizados por el recetario,
 * el detalle de recetas y las recomendaciones.
 *
 * Posteriormente se conectará con las fuentes de datos
 * definitivas sin duplicar información en las pantallas.
 */
object RecetasRepository {

    private val recetas = listOf(
        Receta(
            id = 1L,
            nombre = "Gin Tonic",
            descripcion = "Un clásico refrescante con notas cítricas y un equilibrio perfecto.",
            categoria = "Refrescante",
            ingredientes = listOf(
                "Gin",
                "Agua tónica",
                "Limón",
                "Hielo"
            ),
            preparacion = listOf(
                "Enfriar una copa amplia con abundante hielo.",
                "Agregar el gin sobre el hielo.",
                "Incorporar lentamente el agua tónica.",
                "Añadir una rodaja de limón.",
                "Servir inmediatamente."
            ),
            ingredientesObligatoriosIds = setOf(
                3L, 5L, 1L, 9L
            )
        ),
        Receta(
            id = 2L,
            nombre = "Sour Clásico",
            descripcion = "Una preparación tradicional que combina acidez, dulzor y carácter.",
            categoria = "Clásico",
            ingredientes = listOf(
                "Pisco",
                "Limón",
                "Jarabe de goma",
                "Hielo"
            ),
            preparacion = listOf(
                "Agregar el pisco, el limón y el jarabe de goma a una coctelera.",
                "Incorporar hielo.",
                "Agitar enérgicamente hasta enfriar.",
                "Colar y servir en una copa."
            ),
            ingredientesObligatoriosIds = setOf(
                8L, 1L, 10L, 9L
            )
        ),
        Receta(
            id = 3L,
            nombre = "Limonada Fresca",
            descripcion = "Una alternativa natural, cítrica y refrescante sin alcohol.",
            categoria = "Sin alcohol",
            ingredientes = listOf(
                "Limón",
                "Agua",
                "Azúcar",
                "Hielo"
            ),
            preparacion = listOf(
                "Lavar y exprimir los limones.",
                "Mezclar el jugo con agua fría.",
                "Agregar azúcar al gusto.",
                "Revolver hasta integrar.",
                "Servir con abundante hielo."
            ),
            ingredientesObligatoriosIds = setOf(
                1L, 11L, 12L, 9L
            )
        ),
        Receta(
            id = 4L,
            nombre = "Gin Sour",
            descripcion = "Una combinación elegante de gin, limón y dulzor.",
            categoria = "Especialidad",
            ingredientes = listOf(
                "Gin",
                "Limón",
                "Jarabe de goma",
                "Hielo"
            ),
            preparacion = listOf(
                "Agregar gin, jugo de limón y jarabe de goma a una coctelera.",
                "Incorporar hielo.",
                "Agitar hasta enfriar la mezcla.",
                "Colar en una copa fría.",
                "Servir inmediatamente."
            ),
            ingredientesObligatoriosIds = setOf(
                3L, 1L, 10L, 9L
            )
        )
    )

    init {
        require(recetas.map { it.id }.distinct().size == recetas.size) {
            "No pueden existir identificadores de recetas duplicados."
        }

        val idsIngredientes = IngredienteRepository()
            .obtenerIngredientes()
            .map { it.id }
            .toSet()

        require(
            recetas.all { receta ->
                receta.ingredientesObligatoriosIds.all {
                    it in idsIngredientes
                }
            }
        ) {
            "Una receta contiene identificadores de ingredientes inexistentes."
        }
    }

    fun obtenerRecetasPublicadas(): List<Receta> {
        return recetas.filter { it.publicada }
    }

    fun obtenerRecetasRecomendables(): List<Receta> {
        return recetas.filter { it.esRecomendable() }
    }

    fun obtenerRecetaPorId(id: Long): Receta? {
        if (id <= 0L) return null

        return recetas.firstOrNull {
            it.id == id && it.publicada
        }
    }

    fun obtenerCategorias(): List<String> {
        return obtenerRecetasPublicadas()
            .map { it.categoria }
            .distinct()
            .sorted()
    }

    fun buscarRecetas(
        consulta: String = "",
        categoria: String? = null
    ): List<Receta> {
        val texto = consulta.trim()

        return obtenerRecetasPublicadas().filter { receta ->

            val coincideTexto =
                texto.isBlank() ||
                        receta.nombre.contains(
                            texto,
                            ignoreCase = true
                        ) ||
                        receta.descripcion.contains(
                            texto,
                            ignoreCase = true
                        ) ||
                        receta.ingredientes.any { ingrediente ->
                            ingrediente.contains(
                                texto,
                                ignoreCase = true
                            )
                        }

            val coincideCategoria =
                categoria.isNullOrBlank() ||
                        categoria.equals(
                            "Todas",
                            ignoreCase = true
                        ) ||
                        receta.categoria.equals(
                            categoria,
                            ignoreCase = true
                        )

            coincideTexto && coincideCategoria
        }
    }

    /**
     * Búsqueda preliminar por nombres.
     *
     * Se conserva por compatibilidad.
     * El motor definitivo utilizará identificadores
     * y calculará el porcentaje de coincidencia.
     */
    fun buscarPorIngredientes(
        ingredientesDisponibles: Collection<String>
    ): List<Receta> {
        val normalizados = ingredientesDisponibles
            .map { it.trim().lowercase() }
            .filter { it.isNotBlank() }
            .toSet()

        if (normalizados.isEmpty()) {
            return emptyList()
        }

        return obtenerRecetasRecomendables().filter { receta ->
            receta.ingredientes.any {
                it.trim().lowercase() in normalizados
            }
        }
    }
}
