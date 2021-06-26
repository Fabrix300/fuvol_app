package pe.edu.ulima.pm.futbol.models.managers

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.room.Room
import pe.edu.ulima.pm.futbol.models.beans.CompeGeneral
import pe.edu.ulima.pm.futbol.models.beans.Competencias
import pe.edu.ulima.pm.futbol.models.beans.EquipGeneral
import pe.edu.ulima.pm.futbol.models.beans.Equipos
import pe.edu.ulima.pm.futbol.models.dao.CompeService
import pe.edu.ulima.pm.futbol.models.dao.EquipService
import pe.edu.ulima.pm.futbol.models.persistence.AppDatabase
import pe.edu.ulima.pm.futbol.models.persistence.entities.Competencia
import pe.edu.ulima.pm.futbol.models.persistence.entities.Equipo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.ArrayList

interface onGetTeamsDone{
    fun onSuccess(listaEquipos: ArrayList<Equipos>, compId: Int, vez: Int)
    fun onError(msg: String)
}

class EquipoManager {

    private var contador = 0
    var equipos : ArrayList<Equipos> = ArrayList<Equipos>()

    companion object {
        private var instance: EquipoManager? = null

        fun getInstance(): EquipoManager {
            if (instance == null) {
                instance = EquipoManager()
            }
            return instance!!
        }
    }

    fun getEquipos(context : Context, idComp : Int, callback: onGetTeamsDone){
        val equiList: ArrayList<Equipos> = ArrayList<Equipos>()
        val retrofit = ConnectionManager.getInstance().getRetrofit()
        val equipService = retrofit.create<EquipService>()
        equipService.getEquipos(idComp).enqueue(object : Callback<EquipGeneral> {
            override fun onResponse(call: Call<EquipGeneral>, response: Response<EquipGeneral>) {
                if(response.code() == 200 && response.body() != null){
                    val ListaEquipos = response.body()!!.teams
                    for (equi in ListaEquipos){
                        equiList.add(equi)
                    }
                    contador += 1
                    callback.onSuccess(equiList, idComp, contador)
                }else{
                    Toast.makeText( context, "Error", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<EquipGeneral>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }
    //funcion donde recuperamos la lista de equipos con Room desde el SQLite
    fun getEquiposRoom (context: Context, compId: Int, callback: (ArrayList<Equipos>) -> Unit ){
        //hacemos la conexion por Room al SQLite
        val db = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "Futbol").fallbackToDestructiveMigration().build()
        Thread{
            //instanciamos el DAO
            val equipoDAO = db.equipoDAO()
            //instanciamos la lista de equipos
            val equipoList = ArrayList<Equipos>()
            //traemos el nombre y el estadio de cada equipo
            equipoDAO.findByComp(compId).forEach{ e : Equipo ->
                equipoList.add(Equipos(
                    e.name,
                    e.venue
                )
                )
                Log.i("equipoRoom", "${e.name}, $compId")
            }
            //retornamos la lista de equipos
            callback(equipoList)
        }.start()
    }
}
