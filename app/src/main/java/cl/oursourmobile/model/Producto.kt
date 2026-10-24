
package cl.oursourmobile.model

/**
 * Representa un producto disponible en el catálogo.
 *
 * La cantidad no pertenece a este modelo:
 * se administra mediante ItemCarrito.
 */
data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val imagenKey: String,
    val disponible: Boolean = true
) {
    init {
        require(id > 0) {
            "El identificador debe ser positivo"
        }

        require(nombre.isNotBlank()) {
            "El nombre del producto es obligatorio"
        }

        require(precio >= 0) {
            "El precio no puede ser negativo"
        }

        require(imagenKey.isNotBlank()) {
            "La clave de imagen es obligatoria"
        }
    }

    /**
     * Convierte el producto en un artículo del carrito.
     */
    fun convertirAItemCarrito(): ItemCarrito {
        return ItemCarrito(
            id = id,
            nombre = nombre,
            precio = precio,
            cantidad = 1,
            imagenKey = imagenKey
        )
    }
}
