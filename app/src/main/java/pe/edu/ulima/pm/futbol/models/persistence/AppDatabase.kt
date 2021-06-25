package pe.edu.ulima.pm.futbol.models.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import pe.edu.ulima.pm.futbol.models.persistence.dao.CompeticionDAO
import pe.edu.ulima.pm.futbol.models.persistence.dao.EquipoDAO
import pe.edu.ulima.pm.futbol.models.persistence.dao.PosicionDAO
import pe.edu.ulima.pm.futbol.models.persistence.entities.Competencia
import pe.edu.ulima.pm.futbol.models.persistence.entities.Equipo
import pe.edu.ulima.pm.futbol.models.persistence.entities.Posicion


@Database(entities = arrayOf(Competencia :: class, Equipo :: class, Posicion :: class), version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun competicionDAO() : CompeticionDAO
    abstract fun equipoDAO() : EquipoDAO
    abstract fun posicionDAO() : PosicionDAO
}