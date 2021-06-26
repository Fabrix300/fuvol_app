package pe.edu.ulima.pm.futbol.models.managers

import android.content.Context
import android.util.Log
import androidx.room.Room
import pe.edu.ulima.pm.futbol.models.beans.Competencias
import pe.edu.ulima.pm.futbol.models.beans.Equipos
import pe.edu.ulima.pm.futbol.models.persistence.AppDatabase
import pe.edu.ulima.pm.futbol.models.persistence.dao.CompeticionDAO
import pe.edu.ulima.pm.futbol.models.persistence.entities.Competencia
import pe.edu.ulima.pm.futbol.models.persistence.entities.Equipo
import java.util.ArrayList
import javax.security.auth.callback.Callback

class CompeticionManager {

    private var competiciones: ArrayList<Competencias>? = null

    //Creamos el singleton de CompeticionManager y su función para que devuelva su instancia
    companion object{
        private var instance: CompeticionManager? = null

        //funcion para que devuelva la instancia
        fun getInstance(): CompeticionManager {
            if(instance == null){
                instance = CompeticionManager()
            }
            return instance!!
        }
    }
    //funcion donde traemos la lista de competiciones desde el SQLite con ROom
    fun getCompeticionesRoom (context: Context, callback: (ArrayList<Competencias>) -> Unit){
        //instanciamos la db
        val db = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "Futbol").fallbackToDestructiveMigration().build()
        Thread{
            //instanciamos el DAO
            val competicionDAO = db.competicionDAO()
            //declaramos una lista de competencias
            val CompeticionList = ArrayList<Competencias>()
            //traemos el id, nombre y numero de seasons de cada competicion
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
            //retornamos en callback la lista de competiciones
            callback(CompeticionList)
        }.start()

    }
    //recibimos una lista de competencias y lo guardamos en el manager
    fun setCompeticion(competiciones: ArrayList<Competencias>){
        this.competiciones = competiciones
    }



}