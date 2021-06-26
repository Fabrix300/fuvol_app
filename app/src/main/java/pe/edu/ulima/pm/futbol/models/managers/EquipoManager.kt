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

    //Creamos el singleton de EquipoManager y su función para que devuelva su instancia
    companion object {
        private var instance: EquipoManager? = null

        //funcion para que devuelva la instancia
        fun getInstance(): EquipoManager {
            if (instance == null) {
                instance = EquipoManager()
            }
            return instance!!
        }
    }

    //Funcion donde traemos el Json del API parseado gracias al GSON en un Response que usa un objeto de tipo
    // EquipGeneral, del cual, extraemos su lista "teams" la cual es la lista de los equipos relacionados a
    // una competencia y la guardamos en ListaEquipos para manipularlo.
    //Por medio del callback de tipo onGetTeamsDone definido en los parámetros de la funcion, podemos llamar
    // en el main activity para posteriormente guardar estos equipos en SQLite
    fun getEquipos(context : Context, idComp : Int, callback: onGetTeamsDone){
        val equiList: ArrayList<Equipos> = ArrayList<Equipos>()
        val retrofit = ConnectionManager.getInstance().getRetrofit()
        val equipService = retrofit.create<EquipService>()
        equipService.getEquipos(idComp).enqueue(object : Callback<EquipGeneral> {
            override fun onResponse(call: Call<EquipGeneral>, response: Response<EquipGeneral>) {
                if(response.code() == 200 && response.body() != null){
                    //acá hacemos que ListaEquipos valga la lista "teams" del objeto EquipGeneral, el cual está
                    // en el body del response
                    val ListaEquipos = response.body()!!.teams
                    for (equi in ListaEquipos){
                        //hacemos que cada equipo en  ListaEquipos se añada a equiList que es una lista generada en
                        //esta misma funcion. Parece innecesario viendolo ahora pero como la app funciona no queremos
                        //tocarla, puede que algo deje de funcionar
                        equiList.add(equi)
                    }
                    contador += 1
                    //por medio de este callback podemos llamarla desde el mainActivity para guardar los equipos
                    //en Room una vez termine esta extracción desde la API de internet.
                    callback.onSuccess(equiList, idComp, contador)
                }else{
                    Toast.makeText( context, "Error", Toast.LENGTH_SHORT).show()
                }
            }
            //Funcion de falla
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
