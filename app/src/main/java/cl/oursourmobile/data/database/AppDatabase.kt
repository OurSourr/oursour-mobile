package cl.oursourmobile.data.database
//permite crear una instancia de la base de datos
import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import cl.oursourmobile.dao.IngredienteDao
import cl.oursourmobile.dao.ItemCarritoDao
import cl.oursourmobile.dao.PedidoDao
import cl.oursourmobile.dao.ProductoDao
import cl.oursourmobile.dao.RecetaDao
import cl.oursourmobile.dao.UsuarioDao
import cl.oursourmobile.model.Ingrediente
import cl.oursourmobile.model.ItemCarrito
import cl.oursourmobile.model.Pedido
import cl.oursourmobile.model.Producto
import cl.oursourmobile.model.Receta
import cl.oursourmobile.model.Usuario

@Database(
    entities = [
        Usuario::class,
        Producto::class,
        Receta::class,
        Ingrediente::class,
        ItemCarrito::class,
        Pedido::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao
    abstract fun recetaDao(): RecetaDao
    abstract fun ingredienteDao(): IngredienteDao
    abstract fun itemCarritoDao(): ItemCarritoDao
    abstract fun pedidoDao(): PedidoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
        return INSTANCE?:synchronized(this){
            val instance = Room.databaseBuilder(
            context.applicationContext,
                AppDatabase::class.java,
                "db_proyecto"
            ).build()
            INSTANCE = instance
            instance
        }
        }
    }
}
