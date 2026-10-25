package cl.oursourmobile.model

import androidx.room3.PrimaryKey
import androidx.room3.Entity
//data class porque solo almacenara datos
@Entity(tableName = "usuario")

data class Usuario (
        @PrimaryKey(autoGenerate = true)
        val id:Int =0,
        val email: String,
        val pass:String

)