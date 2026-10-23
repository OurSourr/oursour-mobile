package cl.oursourmobile.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// Imports de tus vistas
import cl.oursourmobile.view.carrito.CarritoScreen
//import cl.oursourmobile.view.catalogo.DetalleProductoScreen
//import cl.oursourmobile.view.pedidos.CheckoutScreen
//import cl.oursourmobile.view.pedidos.ConfirmacionPedidoScreen
//import cl.oursourmobile.view.pedidos.HistorialPedidosScreen
//import cl.oursourmobile.view.pedidos.PagoSimuladoScreen
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
//navController: elemento que permite recordar navegacion saber de donde viene
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = currentRoute == Routes.Home.route,
                    onClick = { navController.navigate(Routes.Home.route) },
                    icon = { Icon(Icons.Default.ShoppingBag, contentDescription = "Productos") },
                    label = { Text("Productos") }
                )
                NavigationBarItem(
                    selected = currentRoute == "descubrir",
                    onClick = { /* navController.navigate("descubrir") */ },
                    icon = { Icon(Icons.Default.Explore, contentDescription = "Descubrir") },
                    label = { Text("Descubrir") }
                )
                NavigationBarItem(
                    selected = currentRoute == Routes.Carrito.route,
                    onClick = { navController.navigate(Routes.Carrito.route) },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito") },
                    label = { Text("Carrito") }
                )
                NavigationBarItem(
                    selected = currentRoute == "perfil",
                    onClick = { /* navController.navigate("perfil") */ },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Carrito.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. Carrito de Compras (Pestaña principal actual)
            composable(Routes.Carrito.route) {
                CarritoScreen(
                    onProcederCheckout = {
                        // navController.navigate(Routes.Checkout.route)
                    }
                )
            }

            /*

        //1. Detalle de Producto
        composable(
            route = Routes.DetalleProducto.route,
            arguments = listOf(navArgument("idProducto") { type = NavType.IntType })
        ) {
            DetalleProductoScreen()
        }

        // 2. Carrito de Compras
        composable(Routes.Carrito.route) {
            CarritoScreen(
                onProcederCheckout = {
                    navController.navigate(Routes.Checkout.route)
                }
            )
        }

        // 3. Checkout y Despacho
        composable(Routes.Checkout.route) {
            CheckoutScreen()
        }

        // 4. Pago Simulado
        composable(Routes.PagoSimulado.route) {
            PagoSimuladoScreen()
        }

        // 5. Confirmación de Pedido
        composable(
            route = Routes.ConfirmacionPedido.route,
            arguments = listOf(navArgument("idPedido") { type = NavType.IntType })
        ) {
            ConfirmacionPedidoScreen()
        }

        // 6. Historial de Pedidos
        composable(Routes.HistorialPedidos.route) {
            HistorialPedidosScreen()
        }*/
        }
    }
}

