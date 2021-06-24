package pe.edu.ulima.pm.futbol.models.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import pe.edu.ulima.pm.futbol.models.persistence.dao.CompeticionDAO
import pe.edu.ulima.pm.futbol.models.persistence.dao.EquipoDAO
import pe.edu.ulima.pm.futbol.models.persistence.entities.Competencia
import pe.edu.ulima.pm.futbol.models.persistence.entities.Equipo


@Database(entities = arrayOf(Competencia :: class, Equipo :: class), version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun competicionDAO() : CompeticionDAO
    abstract fun equipoDAO() : EquipoDAO
}