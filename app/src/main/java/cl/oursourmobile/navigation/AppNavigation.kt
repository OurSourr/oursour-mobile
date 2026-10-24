
package cl.oursourmobile.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import cl.oursourmobile.viewmodel.CarritoViewModel
import cl.oursourmobile.viewmodel.PedidoViewModel
import cl.oursourmobile.viewmodel.ProductosViewModel
import cl.oursourmobile.viewmodel.RecomendacionesViewModel

import cl.oursourmobile.view.autenticacion.LoginScreen
import cl.oursourmobile.view.autenticacion.RegistroScreen
import cl.oursourmobile.view.inicio.InicioScreen
import cl.oursourmobile.view.ingredientes.IngredientesScreen
import cl.oursourmobile.view.catalogo.CatalogoScreen
import cl.oursourmobile.view.catalogo.DetalleProductoScreen
import cl.oursourmobile.view.catalogo.obtenerProductoPorId
import cl.oursourmobile.view.catalogo.admin.AdministrarProductosScreen
import cl.oursourmobile.view.catalogo.admin.FormularioProductoScreen
import cl.oursourmobile.view.carrito.CarritoScreen
import cl.oursourmobile.view.pedidos.CheckoutScreen
import cl.oursourmobile.view.pedidos.PagoSimuladoScreen
import cl.oursourmobile.view.pedidos.ConfirmacionPedidoScreen
import cl.oursourmobile.view.pedidos.HistorialPedidosScreen
import cl.oursourmobile.view.recetas.RecetasScreen
import cl.oursourmobile.view.recetas.DetalleRecetaScreen
import cl.oursourmobile.view.recetas.RecomendacionesScreen
import cl.oursourmobile.view.perfil.PerfilScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val carritoViewModel: CarritoViewModel = viewModel()
    val recomendacionesViewModel: RecomendacionesViewModel = viewModel()
    val pedidoViewModel: PedidoViewModel = viewModel()
    val productosViewModel: ProductosViewModel = viewModel()

    val seccionesPrincipales = setOf(
        Routes.Home.route,
        Routes.Ingredientes.route,
        Routes.Recetas.route,
        Routes.Productos.route,
        Routes.Carrito.route,
        Routes.Perfil.route
    )

    fun navegarSeccion(ruta: String) {
        navController.navigate(ruta) {
            popUpTo(Routes.Home.route) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        bottomBar = {
            if (currentRoute in seccionesPrincipales) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface
                ) {

                    NavigationBarItem(
                        selected = currentRoute == Routes.Home.route,
                        onClick = {
                            navegarSeccion(Routes.Home.route)
                        },
                        icon = {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Inicio"
                            )
                        },
                        label = { Text("Inicio") }
                    )

                    NavigationBarItem(
                        selected = currentRoute == Routes.Ingredientes.route ||
                                currentRoute == Routes.Recetas.route,
                        onClick = {
                            navegarSeccion(Routes.Ingredientes.route)
                        },
                        icon = {
                            Icon(
                                Icons.Default.Explore,
                                contentDescription = "Descubrir"
                            )
                        },
                        label = { Text("Descubrir") }
                    )

                    NavigationBarItem(
                        selected = currentRoute == Routes.Productos.route,
                        onClick = {
                            navegarSeccion(Routes.Productos.route)
                        },
                        icon = {
                            Icon(
                                Icons.Default.ShoppingBag,
                                contentDescription = "Productos"
                            )
                        },
                        label = { Text("Productos") }
                    )

                    NavigationBarItem(
                        selected = currentRoute == Routes.Carrito.route,
                        onClick = {
                            navegarSeccion(Routes.Carrito.route)
                        },
                        icon = {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = "Carrito"
                            )
                        },
                        label = { Text("Carrito") }
                    )

                    NavigationBarItem(
                        selected = currentRoute == Routes.Perfil.route,
                        onClick = {
                            navegarSeccion(Routes.Perfil.route)
                        },
                        icon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Perfil"
                            )
                        },
                        label = { Text("Perfil") }
                    )
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Routes.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            // Pantalla principal
            composable(Routes.Home.route) {
                InicioScreen(
                    onDescubrirREcetasClick = {
                        navegarSeccion(Routes.Ingredientes.route)
                    },
                    onRecetasClick = {
                        navegarSeccion(Routes.Recetas.route)
                    },
                    onProductosClick = {
                        navegarSeccion(Routes.Productos.route)
                    },
                    onEscanearQrClick = {
                        navController.navigate("qr_temporal")
                    }
                )
            }

            // Inicio de sesión
            composable(Routes.Login.route) {
                LoginScreen(
                    onVolverComoInvitado = {
                        navController.popBackStack()
                    },
                    onIrARegistro = {
                        navController.navigate("registro_temporal") {
                            launchSingleTop = true
                        }
                    },
                    onLoginExitoso = {
                        navController.navigate(Routes.Perfil.route) {
                            popUpTo(Routes.Login.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            // Registro
            composable("registro_temporal") {
                RegistroScreen(
                    onVolverAlLogin = {
                        navController.popBackStack()
                    }
                )
            }

            // Selección de ingredientes
            composable(Routes.Ingredientes.route) {
                IngredientesScreen(
                    onVerRecomendaciones = { idsSeleccionados ->
                        recomendacionesViewModel
                            .actualizarIngredientesSeleccionados(
                                idsSeleccionados
                            )

                        navController.navigate(
                            Routes.Recomendaciones.route
                        ) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            // Recomendaciones
            composable(Routes.Recomendaciones.route) {
                RecomendacionesScreen(
                    ingredientesSeleccionadosIds =
                        recomendacionesViewModel.ingredientesSeleccionadosIds,
                    onVerDetalleReceta = { idReceta ->
                        navController.navigate(
                            Routes.DetalleReceta.crearRuta(idReceta)
                        )
                    },
                    onVolverAIngredientes = {
                        navController.popBackStack()
                    }
                )
            }

            // Recetas
            composable(Routes.Recetas.route) {
                RecetasScreen(
                    onVerDetalleReceta = { idReceta ->
                        navController.navigate(
                            Routes.DetalleReceta.crearRuta(idReceta)
                        )
                    }
                )
            }

            // Detalle de receta
            composable(
                route = Routes.DetalleReceta.route,
                arguments = listOf(
                    navArgument("idReceta") {
                        type = NavType.LongType
                    }
                )
            ) { entry ->

                val idReceta = entry.arguments?.getLong("idReceta")

                if (idReceta != null && idReceta > 0L) {
                    DetalleRecetaScreen(
                        idReceta = idReceta,
                        onVolver = {
                            navController.popBackStack()
                        },
                        onVerProductos = {
                            navegarSeccion(Routes.Productos.route)
                        }
                    )
                } else {
                    PantallaTemporal(
                        titulo = "Receta no encontrada",
                        descripcion =
                            "El identificador de la receta no es válido."
                    )
                }
            }

            // Catálogo de productos
            composable(Routes.Productos.route) {
                CatalogoScreen(
                    carritoViewModel = carritoViewModel,
                    productosViewModel = productosViewModel,
                    onVerCarrito = {
                        navegarSeccion(Routes.Carrito.route)
                    },
                    onVerDetalleProducto = { idProducto ->
                        navController.navigate(
                            Routes.DetalleProducto.crearRuta(idProducto)
                        )
                    }
                )
            }

            // Administración de productos
            composable(Routes.AdministrarProductos.route) {
                AdministrarProductosScreen(
                    productosViewModel = productosViewModel,
                    onVolver = {
                        navController.popBackStack()
                    },
                    onAgregarProducto = {
                        navController.navigate(
                            Routes.AgregarProducto.route
                        ) {
                            launchSingleTop = true
                        }
                    },
                    onEditarProducto = { idProducto ->
                        navController.navigate(
                            Routes.EditarProducto.crearRuta(idProducto)
                        )
                    }
                )
            }

            // Agregar producto
            composable(Routes.AgregarProducto.route) {
                FormularioProductoScreen(
                    producto = null,
                    onGuardar = {
                            nombre,
                            descripcion,
                            precio,
                            imagenKey,
                            disponible ->

                        productosViewModel.crearProducto(
                            nombre = nombre,
                            descripcion = descripcion,
                            precio = precio,
                            imagenKey = imagenKey,
                            disponible = disponible
                        )
                    },
                    onVolver = {
                        navController.popBackStack()
                    }
                )
            }

            // Editar producto
            composable(
                route = Routes.EditarProducto.route,
                arguments = listOf(
                    navArgument("idProducto") {
                        type = NavType.IntType
                    }
                )
            ) { entry ->

                val idProducto = entry.arguments?.getInt("idProducto")
                val producto = idProducto?.let {
                    productosViewModel.obtenerProductoPorId(it)
                }

                if (producto != null) {
                    FormularioProductoScreen(
                        producto = producto,
                        onGuardar = {
                                nombre,
                                descripcion,
                                precio,
                                imagenKey,
                                disponible ->

                            productosViewModel.editarProducto(
                                id = producto.id,
                                nombre = nombre,
                                descripcion = descripcion,
                                precio = precio,
                                imagenKey = imagenKey,
                                disponible = disponible
                            )
                        },
                        onVolver = {
                            navController.popBackStack()
                        }
                    )
                } else {
                    PantallaTemporal(
                        titulo = "Producto no encontrado",
                        descripcion =
                            "No fue posible encontrar el producto solicitado."
                    )
                }
            }

            // Detalle de producto
            composable(
                route = Routes.DetalleProducto.route,
                arguments = listOf(
                    navArgument("idProducto") {
                        type = NavType.IntType
                    }
                )
            ) { entry ->

                val idProducto = entry.arguments?.getInt("idProducto")
                val producto = idProducto?.let {
                    obtenerProductoPorId(it)
                }

                if (producto != null) {
                    DetalleProductoScreen(
                        producto = producto,
                        carritoViewModel = carritoViewModel,
                        onVolver = {
                            navController.popBackStack()
                        },
                        onVerCarrito = {
                            navegarSeccion(Routes.Carrito.route)
                        }
                    )
                } else {
                    PantallaTemporal(
                        titulo = "Producto no encontrado",
                        descripcion =
                            "El producto solicitado no está disponible."
                    )
                }
            }

            // Carrito
            composable(Routes.Carrito.route) {
                CarritoScreen(
                    viewModel = carritoViewModel,
                    onProcederCheckout = {
                        navController.navigate(Routes.Checkout.route)
                    }
                )
            }

            // Checkout
            composable(Routes.Checkout.route) {
                CheckoutScreen(
                    carritoViewModel = carritoViewModel,
                    onVolverAlCarrito = {
                        navController.popBackStack()
                    },
                    onConfirmarPago = { pedido ->
                        pedidoViewModel.guardarPedidoPendiente(pedido)

                        navController.navigate(
                            Routes.PagoSimulado.route
                        ) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            // Pago simulado
            composable(Routes.PagoSimulado.route) {

                val pedidoPendiente by
                pedidoViewModel.pedidoPendiente.collectAsState()

                if (pedidoPendiente != null) {
                    PagoSimuladoScreen(
                        pedido = requireNotNull(pedidoPendiente),
                        onConfirmarPago = {
                            val pedidoConfirmado =
                                pedidoViewModel.confirmarPagoSimulado()

                            if (pedidoConfirmado != null) {
                                carritoViewModel.vaciarCarrito()

                                navController.navigate(
                                    Routes.ConfirmacionPedido.crearRuta(
                                        pedidoConfirmado.id
                                    )
                                ) {
                                    popUpTo(Routes.Carrito.route) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            }
                        },
                        onCancelar = {
                            navController.popBackStack()
                        }
                    )
                } else {
                    PantallaTemporal(
                        titulo = "No hay un pedido pendiente",
                        descripcion =
                            "Regresa al carrito para iniciar una compra."
                    )
                }
            }

            // Confirmación de pedido
            composable(
                route = Routes.ConfirmacionPedido.route,
                arguments = listOf(
                    navArgument("idPedido") {
                        type = NavType.IntType
                    }
                )
            ) { entry ->

                val idPedido = entry.arguments?.getInt("idPedido")
                val pedido = idPedido?.let {
                    pedidoViewModel.obtenerPedidoPorId(it)
                }

                if (pedido != null) {
                    ConfirmacionPedidoScreen(
                        pedido = pedido,
                        onVolverInicio = {
                            navController.navigate(Routes.Home.route) {
                                popUpTo(Routes.Home.route) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        },
                        onVerHistorial = {
                            navController.navigate(
                                Routes.HistorialPedidos.route
                            )
                        }
                    )
                } else {
                    PantallaTemporal(
                        titulo = "Pedido no encontrado",
                        descripcion =
                            "No fue posible recuperar el pedido solicitado."
                    )
                }
            }

            // Historial de pedidos
            composable(Routes.HistorialPedidos.route) {

                val pedidos by
                pedidoViewModel.historialPedidos.collectAsState()

                HistorialPedidosScreen(
                    pedidos = pedidos,
                    onVolver = {
                        navController.popBackStack()
                    },
                    onVerPedido = { idPedido ->
                        navController.navigate(
                            Routes.ConfirmacionPedido.crearRuta(idPedido)
                        )
                    }
                )
            }

            // Perfil de usuario
            composable(Routes.Perfil.route) {
                PerfilScreen(
                    onVerMisPedidos = {
                        navController.navigate(
                            Routes.HistorialPedidos.route
                        ) {
                            launchSingleTop = true
                        }
                    },
                    onIniciarSesion = {
                        navController.navigate(Routes.Login.route) {
                            launchSingleTop = true
                        }
                    },
                    onAdministrarProductos = {
                        navController.navigate(
                            Routes.AdministrarProductos.route
                        ) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            // Pantalla temporal del escáner QR
            composable("qr_temporal") {
                PantallaTemporal(
                    titulo = "Escáner QR",
                    descripcion =
                        "Conectaremos aquí el escáner QR."
                )
            }
        }
    }
}

@Composable
private fun PantallaTemporal(
    titulo: String,
    descripcion: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = descripcion,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
