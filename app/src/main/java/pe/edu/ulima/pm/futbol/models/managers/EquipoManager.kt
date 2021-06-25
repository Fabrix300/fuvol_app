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
                        //Log.i("equ" , "Competicion: ${idComp}, Equipo: ${equi.name}, Venue : ${equi.venue}")
                        equiList.add(equi)
                    }
                    contador += 1
                    //equipos.addAll(equiList)
                    //saveEquipos(equiList!!, context)
                    callback.onSuccess(equiList, idComp, contador)
                }else{
                    Toast.makeText( context, "Error", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<EquipGeneral>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
        //callback(equiList)
        /*contador += 1
        if(contador == 3){
            saveEquipos(equipos, context)
        }*/
    }

    fun setEquipo(equip: ArrayList<Equipos>){
        this.equipos = equip
    }

    fun getEquiposRoom (context: Context, compId: Int){
        val db = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "Futbol").fallbackToDestructiveMigration().build()
        Thread{
            val equipoDAO = db.equipoDAO()

            val equipoList = ArrayList<Equipos>()
            equipoDAO.findByComp(compId).forEach{ e : Equipo ->
                equipoList.add(Equipos(
                    e.name,
                    e.venue
                )
                )
                Log.i("equipoRoom", "${e.name}, $compId")
            }

        }.start()
    }

    fun saveEquipos(equipos : ArrayList<Equipos>, context : Context, compId: Int) {
        val db = Room.databaseBuilder(context, AppDatabase::class.java, "Futbol")
            .fallbackToDestructiveMigration().build()
        Thread {
            val equipoDAO = db.equipoDAO()

            // para borrar la tabla solo cuando ya esta creada y es de pruebas--------------------
            //equipoDAO.delete()
            //--------------------------------------------------------------------------

            equipos.forEach { e: Equipos ->
                Log.i("Loque quieras", e.name)
                equipoDAO.insert(
                    Equipo(
                        0,
                        compId,
                        e.name,
                        e.venue
                    )
                )
            }

        }.start()
    }
}
