
package cl.oursourmobile.view.ingredientes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.oursourmobile.viewmodel.IngredientesViewModel

@Composable
fun IngredientesScreen(
    onVerRecomendaciones: ((List<Long>) -> Unit)? = null,
    ingredientesViewModel: IngredientesViewModel = viewModel()
) {
    val colors = MaterialTheme.colorScheme

    var busqueda by rememberSaveable {
        mutableStateOf("")
    }

    var seleccionados by rememberSaveable {
        mutableStateOf(listOf<Long>())
    }

    var mostrarResumen by remember {
        mutableStateOf(false)
    }

    val ingredientes = ingredientesViewModel.ingredientes

    val filtrados = remember(ingredientes, busqueda) {
        ingredientes.filter {
            it.nombre.contains(busqueda.trim(), ignoreCase = true)
        }
    }

    val nombresSeleccionados = ingredientes
        .filter { it.id in seleccionados }
        .map { it.nombre }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {

        // Encabezado
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "OUR SOUR  /  DESCUBRIR",
                style = MaterialTheme.typography.labelMedium,
                color = colors.secondary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Text(
                text = "¿Qué ingredientes\ntienes disponibles?",
                style = MaterialTheme.typography.headlineMedium,
                color = colors.onBackground,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Selecciona tus ingredientes y descubre nuevas posibilidades para preparar cócteles.",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant
            )

            Spacer(Modifier.height(6.dp))

            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Buscar ingrediente") },
                placeholder = { Text("Ej.: limón, hielo, menta") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${seleccionados.size} seleccionados",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.primary
                )

                TextButton(
                    onClick = { seleccionados = emptyList() },
                    enabled = seleccionados.isNotEmpty()
                ) {
                    Text("Limpiar selección")
                }
            }
        }

        HorizontalDivider(color = colors.outlineVariant)

        // Listado de ingredientes
        if (filtrados.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No encontramos ingredientes con ese nombre.",
                    color = colors.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    horizontal = 20.dp,
                    vertical = 12.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = filtrados,
                    key = { it.id }
                ) { ingrediente ->

                    val seleccionado = ingrediente.id in seleccionados

                    val actualizarSeleccion: () -> Unit = {
                        seleccionados = if (seleccionado) {
                            seleccionados - ingrediente.id
                        } else {
                            seleccionados + ingrediente.id
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = actualizarSeleccion),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (seleccionado) {
                                colors.primaryContainer
                            } else {
                                colors.surface
                            }
                        ),
                        border = if (seleccionado) {
                            androidx.compose.foundation.BorderStroke(
                                1.dp,
                                colors.primary
                            )
                        } else {
                            null
                        },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 1.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 16.dp,
                                    end = 10.dp,
                                    top = 8.dp,
                                    bottom = 8.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = ingrediente.nombre,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = if (seleccionado) {
                                        colors.onPrimaryContainer
                                    } else {
                                        colors.onSurface
                                    },
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = if (seleccionado) {
                                        "Agregado a tu selección"
                                    } else {
                                        "Toca para seleccionar"
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (seleccionado) {
                                        colors.onPrimaryContainer
                                    } else {
                                        colors.onSurfaceVariant
                                    }
                                )
                            }

                            Checkbox(
                                checked = seleccionado,
                                onCheckedChange = {
                                    actualizarSeleccion()
                                }
                            )
                        }
                    }
                }
            }
        }

        // Acción inferior fija
        Surface(
            color = colors.surface,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (seleccionados.isEmpty()) {
                        "Selecciona al menos un ingrediente."
                    } else {
                        "Tienes ${seleccionados.size} ingrediente(s) listo(s)."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant
                )

                Button(
                    onClick = {
                        val callback = onVerRecomendaciones
                        if (callback != null) {
                            callback(seleccionados)
                        } else {
                            mostrarResumen = true
                        }
                    },
                    enabled = seleccionados.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Descubrir cócteles",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

    if (mostrarResumen) {
        AlertDialog(
            onDismissRequest = {
                mostrarResumen = false
            },
            title = {
                Text("Tus ingredientes")
            },
            text = {
                Text(
                    "Has seleccionado:\n\n" +
                            nombresSeleccionados.joinToString("\n") {
                                "• $it"
                            } +
                            "\n\nLa conexión con recomendaciones será el siguiente paso."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarResumen = false }
                ) {
                    Text("Entendido")
                }
            }
        )
    }
}
