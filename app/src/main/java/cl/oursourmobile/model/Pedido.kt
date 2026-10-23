package cl.oursourmobile.model

data class Pedido(
    val id: Int = 0,
    val items: List<ItemCarrito>,
    val subtotal: Int,
    val costoEnvio: Int,
    val total: Int,
    val tipoEntrega: String,
    val nombreCliente: String,
    val direccion: String,
    val telefono: String
)