package pe.edu.ulima.pm.futbol.models.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Posicion(
    @PrimaryKey(autoGenerate = true) val id : Int,
    @ColumnInfo(name = "compId") val compId: Int,
    @ColumnInfo(name= "name") val name : String,
    @ColumnInfo(name = "points") val points : Int,
    @ColumnInfo(name = "position") val position : Int,
)
