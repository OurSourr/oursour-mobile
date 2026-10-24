
package cl.oursourmobile.view.recetas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.oursourmobile.data.repository.IngredienteRepository
import cl.oursourmobile.data.repository.RecetasRepository
import cl.oursourmobile.domain.recommendation.MotorRecomendaciones
import cl.oursourmobile.domain.recommendation.RecomendacionReceta

@Composable
fun RecomendacionesScreen(
    ingredientesSeleccionadosIds: List<Long>,
    onVerDetalleReceta: (Long) -> Unit = {},
    onVolverAIngredientes: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

    val ingredientes = remember {
        IngredienteRepository().obtenerIngredientes()
    }

    val nombresPorId = remember(ingredientes) {
        ingredientes.associate { it.id to it.nombre }
    }

    val recomendaciones = remember(ingredientesSeleccionadosIds) {
        MotorRecomendaciones().calcularRecomendaciones(
            recetas = RecetasRepository.obtenerRecetasRecomendables(),
            ingredientesSeleccionadosIds = ingredientesSeleccionadosIds
        )
    }

    val cantidadPreparables = recomendaciones.count {
        it.sePuedePreparar
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "OUR SOUR / RECOMENDACIONES",
                style = MaterialTheme.typography.labelMedium,
                color = colors.secondary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Cócteles para ti",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )

            Text(
                text = "Encontramos ${recomendaciones.size} recetas compatibles con tus ingredientes.",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant
            )

            if (cantidadPreparables > 0) {
                Text(
                    text = "Puedes preparar $cantidadPreparables receta(s) con todos sus ingredientes obligatorios.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            OutlinedButton(
                onClick = onVolverAIngredientes,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Modificar ingredientes")
            }
        }

        HorizontalDivider(color = colors.outlineVariant)

        if (recomendaciones.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Aún no encontramos coincidencias",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.onBackground
                    )

                    Text(
                        text = "Prueba seleccionando otros ingredientes para descubrir nuevas recetas.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurfaceVariant
                    )

                    Button(onClick = onVolverAIngredientes) {
                        Text("Elegir ingredientes")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(
                    items = recomendaciones,
                    key = { it.receta.id }
                ) { recomendacion ->
                    TarjetaRecomendacion(
                        recomendacion = recomendacion,
                        nombresPorId = nombresPorId,
                        onVerDetalle = {
                            onVerDetalleReceta(recomendacion.receta.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaRecomendacion(
    recomendacion: RecomendacionReceta,
    nombresPorId: Map<Long, String>,
    onVerDetalle: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    val disponibles = recomendacion.ingredientesDisponiblesIds
        .mapNotNull(nombresPorId::get)
        .sorted()

    val faltantes = recomendacion.ingredientesFaltantesIds
        .mapNotNull(nombresPorId::get)
        .sorted()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onVerDetalle),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recomendacion.receta.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "${recomendacion.porcentajeCompatibilidad}%",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = colors.primary
                )
            }

            LinearProgressIndicator(
                progress = {
                    recomendacion.porcentajeCompatibilidad / 100f
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = colors.primary,
                trackColor = colors.surfaceVariant
            )

            Text(
                text = recomendacion.receta.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant
            )

            if (recomendacion.sePuedePreparar) {
                Text(
                    text = "Lista para preparar",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = colors.primary
                )
            }

            if (disponibles.isNotEmpty()) {
                Text(
                    text = "Ya tienes: ${disponibles.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurface
                )
            }

            if (faltantes.isNotEmpty()) {
                Text(
                    text = "Te faltan: ${faltantes.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant
                )
            }

            TextButton(
                onClick = onVerDetalle,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Ver receta")
            }
        }
    }
}
