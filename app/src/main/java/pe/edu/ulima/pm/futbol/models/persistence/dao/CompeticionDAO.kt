package pe.edu.ulima.pm.futbol.models.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pe.edu.ulima.pm.futbol.models.persistence.entities.Competencia


@Dao
interface CompeticionDAO {
    @Query("SELECT * FROM Competencia")
    fun findAll() : List<Competencia>
    @Insert
    fun insert(competencia : Competencia)
    @Query("DELETE FROM Competencia")
    fun delete()
}