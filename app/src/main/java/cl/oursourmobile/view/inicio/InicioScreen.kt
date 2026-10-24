
package cl.oursourmobile.view.inicio

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import cl.oursourmobile.R
import cl.oursourmobile.ui.theme.OurSourMobileTheme
import cl.oursourmobile.ui.theme.SourGold
import cl.oursourmobile.ui.theme.SourGreenDark
import cl.oursourmobile.ui.theme.SourIvory

@Composable
fun InicioScreen(
    onDescubrirREcetasClick: () -> Unit = {},
    onRecetasClick: () -> Unit = {},
    onProductosClick: () -> Unit = {},
    onEscanearQrClick: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {

        // Identidad y bienvenida
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = SourGold,
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = "OUR SOUR",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp,
                    color = colors.primary
                )
            }

            Text(
                text = stringResource(R.string.inicio_bienvenida),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )

            Text(
                text = stringResource(R.string.inicio_descripcion),
                style = MaterialTheme.typography.bodyLarge,
                color = colors.onSurfaceVariant
            )
        }

        // Fotografía principal y presentación
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(24.dp))
        ) {
            Image(
                painter = painterResource(R.drawable.gin_our),
                contentDescription = "Cóctel destacado de Our Sour",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                SourGreenDark.copy(alpha = 0.45f),
                                SourGreenDark.copy(alpha = 0.95f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "DESCUBRE TU PRÓXIMO FAVORITO",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = SourGold
                )

                Text(
                    text = "El cóctel perfecto\ncomienza aquí.",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Explora nuevas combinaciones y encuentra recetas según tus ingredientes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        // Acción principal
        Button(
            onClick = onDescubrirREcetasClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.onPrimary
            )
        ) {
            Text(
                text = stringResource(R.string.inicio_boton_descubrir),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(Modifier.width(12.dp))

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }

        // Accesos a funcionalidades existentes
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(
                text = stringResource(R.string.inicio_explorar),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )

            Text(
                text = "Todo lo que necesitas para disfrutar la experiencia Our Sour.",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant
            )

            AccesoRapidoPremium(
                titulo = stringResource(R.string.inicio_acceso_recetas),
                descripcion = "Selecciona ingredientes y descubre nuevas combinaciones.",
                icono = Icons.Default.LocalBar,
                onClick = onRecetasClick
            )

            AccesoRapidoPremium(
                titulo = stringResource(R.string.inicio_acceso_productos),
                descripcion = "Explora los productos disponibles en nuestro catálogo.",
                icono = Icons.Default.ShoppingBag,
                onClick = onProductosClick
            )

            AccesoRapidoPremium(
                titulo = stringResource(R.string.inicio_acceso_escaner_qr),
                descripcion = "Accede a la experiencia de escaneo de productos.",
                icono = Icons.Default.QrCodeScanner,
                onClick = onEscanearQrClick
            )
        }

        HorizontalDivider(
            color = colors.outlineVariant
        )

        Text(
            text = "OUR SOUR  •  UNA EXPERIENCIA PARA DESCUBRIR",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.labelSmall,
            color = colors.onSurfaceVariant,
            textAlign = TextAlign.Center,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun AccesoRapidoPremium(
    titulo: String,
    descripcion: String,
    icono: ImageVector,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colors.onPrimaryContainer,
                    modifier = Modifier.size(26.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface
                )

                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Abrir $titulo",
                tint = colors.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InicioScreenPreview() {
    OurSourMobileTheme(darkTheme = false) {
        InicioScreen()
    }
}
