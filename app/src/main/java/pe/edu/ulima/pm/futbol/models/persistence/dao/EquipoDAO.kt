package pe.edu.ulima.pm.futbol.models.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pe.edu.ulima.pm.futbol.models.persistence.entities.Equipo


@Dao
interface EquipoDAO {
    @Query("SELECT * FROM Equipo")
    fun findAll() : List<Equipo>
    @Insert
    fun insert(equipo : Equipo)
    @Query("DELETE FROM Equipo")
    fun nukeTable()
}