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

class EquipoManager {

    private var equipos : ArrayList<Equipos>? = null
    var equiList: ArrayList<Equipos>? = null

    companion object {
        private var instance: EquipoManager? = null

        fun getInstance(): EquipoManager {
            if (instance == null) {
                instance = EquipoManager()
            }
            return instance!!
        }
    }

    fun getEquipos(context : Context): ArrayList<Equipos> {
        equiList = ArrayList<Equipos>()
        val retrofit = ConnectionManager.getInstance().getRetrofit()
        val equipService = retrofit.create<EquipService>()
        equipService.getEquipos().enqueue(object : Callback<EquipGeneral> {
            override fun onResponse(call: Call<EquipGeneral>, response: Response<EquipGeneral>) {
                if(response.code() == 200 && response.body() != null){
                    val ListaCompetencias = response.body()!!.teams
                    for (equi in ListaCompetencias){
                        Log.i("waw", equi.name)
                        equiList!!.add(equi)
                    }
                    saveEquipos(equiList!!, context)
                }else{
                    Toast.makeText( context, "Error", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<EquipGeneral>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

        if(equipos.isNullOrEmpty()){
            return ArrayList<Equipos>()
        }else{
            return equipos!!
        }
    }

    fun setEquipo(equip: ArrayList<Equipos>){
        this.equipos = equip
    }

    fun saveEquipos(equipos : ArrayList<Equipos>, context : Context) {
        val db = Room.databaseBuilder(context, AppDatabase::class.java, "Futbol")
            .fallbackToDestructiveMigration().build()
        Thread {
            val equipoDAO = db.equipoDAO()
            equipos.forEach { e: Equipos ->
                equipoDAO.insert(
                    Equipo(
                        e.id,
                        e.name,
                        e.founded
                    )
                )
            }
        }.start()
    }
}
