package pe.edu.ulima.pm.futbol.models.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Competencia(
    @PrimaryKey() val id: Int,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "numberOfAvailableSeasons") val numberOfAvailableSeasons : Int
)
