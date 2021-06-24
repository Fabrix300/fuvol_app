package pe.edu.ulima.pm.futbol.models.managers

import android.content.Context
import android.util.Log
import androidx.room.Room
import pe.edu.ulima.pm.futbol.models.beans.Competencias
import pe.edu.ulima.pm.futbol.models.beans.Equipos
import pe.edu.ulima.pm.futbol.models.persistence.AppDatabase
import pe.edu.ulima.pm.futbol.models.persistence.entities.Competencia
import pe.edu.ulima.pm.futbol.models.persistence.entities.Equipo
import java.util.ArrayList

class CompeticionManager {

    private var competiciones: ArrayList<Competencias>? = null

    companion object{
        private var instance: CompeticionManager? = null

        fun getInstance(): CompeticionManager {
            if(instance == null){
                instance = CompeticionManager()
            }
            return instance!!
        }
    }

    fun getCompeticionesRoom (context: Context){
        val db = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "Futbol").fallbackToDestructiveMigration().build()
        Thread{
            val competicionDAO = db.competicionDAO()

            val CompeticionList = ArrayList<Competencias>()
            competicionDAO.findAll().forEach{ c : Competencia ->
                CompeticionList.add(
                    Competencias(
                    c.id,
                    c.name,
                    c.numberOfAvailableSeasons
                )
                )
                Log.i("room", c.name)
            }

        }.start()
    }

    fun getCompeticiones(context : Context): ArrayList<Competencias>{

        if(competiciones.isNullOrEmpty()){
            return ArrayList<Competencias>()
        }else{
            return competiciones!!
        }
    }

    fun setCompeticion(competiciones: ArrayList<Competencias>){
        this.competiciones = competiciones
    }



}