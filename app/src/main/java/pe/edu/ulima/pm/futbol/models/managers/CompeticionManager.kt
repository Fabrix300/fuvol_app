package pe.edu.ulima.pm.futbol.models.managers

import pe.edu.ulima.pm.futbol.models.beans.Competencias
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

    fun getCompeticiones(): ArrayList<Competencias>{
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