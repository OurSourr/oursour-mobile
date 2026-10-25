package cl.oursourmobile.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "ingrediente")
data class Ingrediente(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nombre: String,
    val categoria: String = ""
)