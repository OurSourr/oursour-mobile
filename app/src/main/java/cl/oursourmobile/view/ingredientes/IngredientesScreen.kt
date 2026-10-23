
package cl.oursourmobile.view.ingredientes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.oursourmobile.R
import cl.oursourmobile.viewmodel.IngredientesViewModel
import androidx.compose.material3.Checkbox
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment

// PANTALLA 2 - INGREDIENTES
@Composable
fun IngredientesScreen() {

    // ViewModel de la pantalla
    val ingredientesViewModel: IngredientesViewModel = viewModel()

    // Estado del buscador
    var busqueda by rememberSaveable {
        mutableStateOf("")
    }

    // Identificadores de ingredientes seleccionados
    var ingredientesSeleccionados by rememberSaveable {
        mutableStateOf(listOf<Long>())
    }

    // Obtener el catálogo desde el ViewModel
    val ingredientes = ingredientesViewModel.ingredientes

    // Filtrar ingredientes según el texto ingresado
    val ingredientesFiltrados = ingredientes.filter { ingrediente ->
        ingrediente.nombre.contains(
            other = busqueda,
            ignoreCase = true
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Título
        Text(
            text = stringResource(R.string.ingredientes_titulo)
        )

        // Buscador
        OutlinedTextField(
            value = busqueda,
            onValueChange = { nuevoTexto ->
                busqueda = nuevoTexto
            },
            label = {
                Text(
                    text = stringResource(R.string.ingredientes_buscar)
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Catálogo de ingredientes
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(
                items = ingredientesFiltrados,
                key = { ingrediente -> ingrediente.id }
            ) { ingrediente ->

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = ingrediente.id in ingredientesSeleccionados,
                        onCheckedChange = { seleccionado ->
                            ingredientesSeleccionados = if (seleccionado) {
                                ingredientesSeleccionados + ingrediente.id
                            } else {
                                ingredientesSeleccionados - ingrediente.id
                            }
                        }
                    )

                    Text(
                        text = ingrediente.nombre
                    )
                }
            }
        }
    }
}

// Vista previa para Android Studio
@Preview(showBackground = true)
@Composable
fun IngredientesScreenPreview() {
    IngredientesScreen()
}
