
package cl.oursourmobile.view.perfil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import cl.oursourmobile.data.autenticacion.AutenticacionDemo
import cl.oursourmobile.data.autenticacion.RolUsuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    onVerMisPedidos: () -> Unit,
    onIniciarSesion: () -> Unit,
    onAdministrarProductos: () -> Unit = {}
) {
    val repositorio = AutenticacionDemo.repository

    var usuarioActual by remember {
        mutableStateOf(repositorio.obtenerUsuarioActual())
    }

    val esAdministrador = usuarioActual?.rol == RolUsuario.ADMIN

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi perfil",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Text(
                    text = if (usuarioActual == null) {
                        "Gestiona tu cuenta y tus compras"
                    } else {
                        "Bienvenido a tu cuenta de Our Sour"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (usuarioActual == null) {

                item {
                    Card(
                        onClick = onIniciarSesion,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = "Iniciar sesión",
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            supportingContent = {
                                Text("Accede a tu cuenta de Our Sour")
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.Login,
                                    contentDescription = null
                                )
                            },
                            trailingContent = {
                                Icon(
                                    imageVector =
                                        Icons.AutoMirrored.Filled.ArrowForwardIos,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }

            } else {

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = usuarioActual!!.nombre,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            supportingContent = {
                                Column(
                                    verticalArrangement =
                                        Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(usuarioActual!!.correo)

                                    Text(
                                        text = if (esAdministrador) {
                                            "Administrador"
                                        } else {
                                            "Usuario"
                                        },
                                        color =
                                            MaterialTheme.colorScheme.primary
                                    )
                                }
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.AccountCircle,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }

                if (esAdministrador) {
                    item {
                        Card(
                            onClick = onAdministrarProductos,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = "Administrar productos",
                                        fontWeight = FontWeight.SemiBold
                                    )
                                },
                                supportingContent = {
                                    Text(
                                        "Agregar, editar y eliminar productos del catálogo"
                                    )
                                },
                                leadingContent = {
                                    Icon(
                                        imageVector = Icons.Outlined.Inventory2,
                                        contentDescription = null
                                    )
                                },
                                trailingContent = {
                                    Icon(
                                        imageVector =
                                            Icons.AutoMirrored.Filled.ArrowForwardIos,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }
                }

                item {
                    Card(
                        onClick = {
                            repositorio.cerrarSesion()
                            usuarioActual =
                                repositorio.obtenerUsuarioActual()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = "Cerrar sesión",
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            supportingContent = {
                                Text("Salir de tu cuenta actual")
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.Logout,
                                    contentDescription = null
                                )
                            },
                            trailingContent = {
                                Icon(
                                    imageVector =
                                        Icons.AutoMirrored.Filled.ArrowForwardIos,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            }

            item {
                Card(
                    onClick = onVerMisPedidos,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ListItem(
                        headlineContent = {
                            Text(
                                text = "Mis pedidos",
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        supportingContent = {
                            Text("Consulta tu historial de compras")
                        },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Outlined.ReceiptLong,
                                contentDescription = null
                            )
                        },
                        trailingContent = {
                            Icon(
                                imageVector =
                                    Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null
                            )
                        }
                    )
                }
            }

            item {
                HorizontalDivider()
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Mi cuenta",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    ListItem(
                        headlineContent = {
                            Text("Mis datos")
                        },
                        supportingContent = {
                            Text("Disponible próximamente")
                        },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Outlined.AccountCircle,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
    }
}
