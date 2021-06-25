package pe.edu.ulima.pm.futbol.models.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pe.edu.ulima.pm.futbol.models.persistence.entities.Posicion

@Dao
interface PosicionDAO {
    @Query("SELECT * FROM Posicion")
    fun findAll() : List<Posicion>
    @Query("SELECT * FROM Posicion WHERE compId=:compeId")
    fun findByComp(compeId: Int) : List<Posicion>
    @Insert
    fun insert(posicion : Posicion)
    @Query("DELETE FROM Posicion")
    fun delete()
}