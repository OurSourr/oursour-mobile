package cl.oursourmobile.model
import androidx.room3.PrimaryKey
import androidx.room3.Entity
//data class porque solo almacenara datos
@Entity(tableName = "item_carrito")
data class ItemCarrito (
   @PrimaryKey(autoGenerate =true)
    val id: Int = 0,
    val nombre: String,
    val precio: Int,
    val cantidad: Int,
    val imagenKey: String =""
    ){
    init {
        require(id >= 0) {
            "El identificador debe ser mayor o igual a cero"
        }

        require(nombre.isNotBlank()) {
            "El nombre del item no puede estar vacío"
        }

        require(precio >= 0) {
            "El precio no puede ser negativo"
        }

        require(cantidad > 0) {
            "La cantidad debe ser mayor a cero"
        }
    }
}
