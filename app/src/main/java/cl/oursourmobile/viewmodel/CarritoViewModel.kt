
package cl.oursourmobile.viewmodel

import androidx.lifecycle.ViewModel
import cl.oursourmobile.model.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CarritoViewModel : ViewModel() {

    // Productos demostrativos existentes
    private val _items = MutableStateFlow(
        listOf(
            ItemCarrito(
                id = 1,
                nombre = "Agua Tónica Our Sour",
                precio = 2990,
                cantidad = 1,
                imagenKey = "agua_tonica"
            ),
            ItemCarrito(
                id = 2,
                nombre = "Limón Our Sour",
                precio = 3990,
                cantidad = 1,
                imagenKey = "limon_our"
            ),
            ItemCarrito(
                id = 3,
                nombre = "Gin Our Sour",
                precio = 12990,
                cantidad = 1,
                imagenKey = "gin_our"
            ),
            ItemCarrito(
                id = 4,
                nombre = "Mix Our Sour",
                precio = 18990,
                cantidad = 1,
                imagenKey = "mix_sour"
            )
        )
    )

    val items: StateFlow<List<ItemCarrito>> = _items.asStateFlow()

    // Subtotal dinámico
    val subtotal: Int
        get() = _items.value.sumOf { item ->
            Math.multiplyExact(item.precio, item.cantidad)
        }

    /**
     * Agrega una unidad del producto al carrito.
     * Si ya existe, incrementa su cantidad.
     */
    fun agregarProducto(producto: ItemCarrito) {
        require(producto.id > 0) {
            "El identificador del producto debe ser positivo."
        }
        require(producto.precio >= 0) {
            "El precio del producto no puede ser negativo."
        }

        _items.update { productosActuales ->
            val productoExistente = productosActuales.find {
                it.id == producto.id
            }

            if (productoExistente == null) {
                productosActuales + producto.copy(cantidad = 1)
            } else {
                productosActuales.map { item ->
                    if (item.id == producto.id) {
                        item.copy(
                            cantidad = Math.addExact(item.cantidad, 1)
                        )
                    } else {
                        item
                    }
                }
            }
        }
    }

    /**
     * Incrementa la cantidad de un producto existente.
     */
    fun incrementarCantidad(itemId: Int) {
        _items.update { productosActuales ->
            productosActuales.map { item ->
                if (item.id == itemId) {
                    item.copy(
                        cantidad = Math.addExact(item.cantidad, 1)
                    )
                } else {
                    item
                }
            }
        }
    }

    /**
     * Disminuye la cantidad sin permitir valores
     * inferiores a una unidad.
     */
    fun decrementarCantidad(itemId: Int) {
        _items.update { productosActuales ->
            productosActuales.map { item ->
                if (item.id == itemId && item.cantidad > 1) {
                    item.copy(cantidad = item.cantidad - 1)
                } else {
                    item
                }
            }
        }
    }

    /**
     * Elimina completamente un producto del carrito.
     */
    fun eliminarItem(itemId: Int) {
        _items.update { productosActuales ->
            productosActuales.filter { item ->
                item.id != itemId
            }
        }
    }

    /**
     * Vacía el carrito después de confirmar
     * correctamente una compra.
     */
    fun vaciarCarrito() {
        _items.value = emptyList()
    }
}
