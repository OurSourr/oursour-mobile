package cl.oursourmobile.viewmodel
//ve la loogica
import androidx.lifecycle.ViewModel
import cl.oursourmobile.model.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import cl.oursourmobile.R

class CarritoViewModel: ViewModel() {
    // Lista de ítems en el carrito (iniciamos con los productos de muestra del mockup)
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

    // Cálculo dinámico del subtotal
    val subtotal: Int
        get() = _items.value.sumOf { it.precio * it.cantidad }

    fun incrementarCantidad(itemId: Int) {
        _items.value = _items.value.map { item ->
            if (item.id == itemId) item.copy(cantidad = item.cantidad + 1) else item
        }
    }

    fun decrementarCantidad(itemId: Int) {
        _items.value = _items.value.map { item ->
            if (item.id == itemId && item.cantidad > 1) {
                item.copy(cantidad = item.cantidad - 1)
            } else item
        }
    }

    fun eliminarItem(itemId: Int) {
        _items.value = _items.value.filter { it.id != itemId }
    }
}
