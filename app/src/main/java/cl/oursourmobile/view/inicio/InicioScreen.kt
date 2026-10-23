
package cl.oursourmobile.view.inicio

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.oursourmobile.R
import cl.oursourmobile.ui.theme.OurSourMobileTheme


@Composable
fun InicioScreen(
    onDescubrirREcetasClick: () -> Unit = {},
    onRecetasClick: () -> Unit ={},
    onProductosClick: () -> Unit = {},
    onEscanearQrClick: ()-> Unit = {}
) {
    Scaffold { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Título de bienvenida
            Text(
                text = stringResource(R.string.inicio_bienvenida),
                style = MaterialTheme.typography.headlineMedium
            )

            // Descripción
            Text(
                text = stringResource(R.string.inicio_descripcion),
                style = MaterialTheme.typography.bodyLarge
            )

            // Imagen provisional
            Image(
                painter = painterResource(R.drawable.gin_our),
                contentDescription = "Cóctel gin tonic",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )

            // Botón principal
            Button(
                onClick = onDescubrirREcetasClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = stringResource(R.string.inicio_boton_descubrir)
                )
            }

            // Título de accesos rápidos
            Text(
                text = stringResource(R.string.inicio_explorar),
                style = MaterialTheme.typography.titleMedium
            )

            // Accesos rápidos provisionales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AccesoRapidoCard(
                    titulo = stringResource(R.string.inicio_acceso_recetas),
                    modifier = Modifier.weight(1f),
                    onClick = onRecetasClick
                )

                AccesoRapidoCard(
                    titulo = stringResource(R.string.inicio_acceso_productos),
                    modifier = Modifier.weight(1f),
                    onClick = onProductosClick
                )

                AccesoRapidoCard(
                    titulo = stringResource(R.string.inicio_acceso_escaner_qr),
                    modifier = Modifier.weight(1f),
                    onClick = onEscanearQrClick
                )
            }
        }
    }
}

@Composable
private fun AccesoRapidoCard(
    titulo: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit ={}

) {
    Card(
        onClick,
        modifier = modifier.height(50.dp)
    ) {
        Text(
            text = titulo,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            textAlign = TextAlign.Center

        )
    }
}

@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    OurSourMobileTheme {
        InicioScreen()
    }
}
