package pe.edu.ulima.pm.futbol.models.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Equipo(
    @PrimaryKey() val id: Int,
    @ColumnInfo(name= "name") val name : String,
    @ColumnInfo(name = "founded") val founded : Int
)