
package cl.oursourmobile.view.recetas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.oursourmobile.data.repository.RecetasRepository
import cl.oursourmobile.model.Receta

@Composable
fun RecetasScreen(
    onVerDetalleReceta: (Long) -> Unit = {}
) {
    var busqueda by rememberSaveable {
        mutableStateOf("")
    }

    var categoriaSeleccionada by rememberSaveable {
        mutableStateOf("Todas")
    }

    val categorias = remember {
        listOf("Todas") + RecetasRepository.obtenerCategorias()
    }

    val recetasFiltradas = remember(busqueda, categoriaSeleccionada) {
        RecetasRepository.buscarRecetas(
            consulta = busqueda,
            categoria = categoriaSeleccionada
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(20.dp))

        Text(
            text = "OUR SOUR / RECETARIO",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Explora recetas",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Descubre preparaciones para cada ocasión.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(18.dp))

        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Buscar por receta o ingrediente") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(Modifier.height(12.dp))

        ScrollableTabRow(
            selectedTabIndex = categorias.indexOf(categoriaSeleccionada)
                .coerceAtLeast(0),
            edgePadding = 0.dp,
            containerColor = MaterialTheme.colorScheme.background,
            divider = {}
        ) {
            categorias.forEach { categoria ->
                Tab(
                    selected = categoriaSeleccionada == categoria,
                    onClick = {
                        categoriaSeleccionada = categoria
                    },
                    text = {
                        Text(categoria)
                    }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "${recetasFiltradas.size} recetas encontradas",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(12.dp))

        if (recetasFiltradas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No encontramos recetas con esos criterios.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(
                    items = recetasFiltradas,
                    key = { it.id }
                ) { receta ->
                    RecetaCard(
                        receta = receta,
                        onClick = {
                            onVerDetalleReceta(receta.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RecetaCard(
    receta: Receta,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = receta.categoria.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )

            Text(
                text = receta.nombre,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = receta.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            HorizontalDivider()

            Text(
                text = "Ingredientes",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = receta.ingredientes.joinToString(" · "),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            TextButton(
                onClick = onClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Ver preparación")
            }
        }
    }
}
