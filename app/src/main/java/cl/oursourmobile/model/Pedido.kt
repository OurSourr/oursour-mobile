package cl.oursourmobile.model

import androidx.room3.Entity
import androidx.room3.Ignore
import androidx.room3.PrimaryKey

@Entity(tableName = "pedido")
data class Pedido(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val subtotal: Int,
    val costoEnvio: Int,
    val total: Int,
    val tipoEntrega: String,
    val nombreCliente: String,
    val direccion: String,
    val telefono: String,
    // Campo de texto simple para la base de datos (ej: "2x Empanada, 1x Bebida")
    val itemsResumen: String = ""
) {
    // Room ignorará esta lista para no romper la tabla de SQLite
    @Ignore
    val items: List<ItemCarrito> = emptyList()

    init {
        require(id >= 0) {
            "El identificador del pedido debe ser mayor o igual a cero"
        }

        require(subtotal >= 0) {
            "El subtotal no puede ser negativo"
        }

        require(total >= 0) {
            "El total no puede ser negativo"
        }

        require(nombreCliente.isNotBlank()) {
            "El nombre del cliente es obligatorio"
        }
    }
}