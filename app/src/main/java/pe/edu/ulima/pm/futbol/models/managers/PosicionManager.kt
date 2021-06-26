package pe.edu.ulima.pm.futbol.models.managers

import android.content.Context
import android.widget.Toast
import androidx.room.Room
import pe.edu.ulima.pm.futbol.models.beans.Equipos
import pe.edu.ulima.pm.futbol.models.beans.PosiGeneral
import pe.edu.ulima.pm.futbol.models.beans.Posiciones
import pe.edu.ulima.pm.futbol.models.beans.Team
import pe.edu.ulima.pm.futbol.models.dao.PosicionesService
import pe.edu.ulima.pm.futbol.models.persistence.AppDatabase
import pe.edu.ulima.pm.futbol.models.persistence.entities.Posicion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

interface onGetPosicionesDone{
    fun onSuccessPos(listaPosiciones: java.util.ArrayList<Posiciones>, compId: Int, vez: Int)
    fun onErrorPos(msg: String)
}

class PosicionManager {

    var equipos : ArrayList<Posiciones> = ArrayList<Posiciones>()
    private var contador = 0

    //Creamos el singleton de PosicionManager y su función para que devuelva su instancia
    companion object{
        private var instance : PosicionManager? = null

        //funcion para que devuelva la instancia
        fun getInstance():PosicionManager{
            if(instance == null){
                instance = PosicionManager()
            }
            return instance!!
        }
    }

    //Funcion donde traemos el Json del API parseado gracias al GSON en un Response que usa un objeto de tipo
    // PosiGeneral, del cual extraemos su lista standings y la guardamos en ListaSeasons. Pero,
    //nos interesa la lista "table", por ello accedemos a ella y la guardamos en ListaPosiciones.
    //Por medio del callback de tipo onGetPosicionesDone definido en los parámetros de la funcion, podemos llamar
    // en el main activity para posteriormente guardar esta información de posiciones en SQLite
    fun getPosiciones(context : Context, idComp : Int, callback : onGetPosicionesDone){
        val posList : ArrayList<Posiciones> = ArrayList<Posiciones>()
        val retrofit = ConnectionManager.getInstance().getRetrofit()
        val posicionService = retrofit.create<PosicionesService>()
        posicionService.getPosiciones(idComp).enqueue(object : Callback<PosiGeneral>{
            override fun onResponse(call : Call<PosiGeneral>, response: Response<PosiGeneral>){
                if(response.code() == 200 && response.body() != null){
                    //Guardamos la lista de standings en ListaSeasons
                    val ListaSeasons = response.body()!!.standings
                    //Agarramos el primer standing de la lista de seasons
                    val stan = ListaSeasons.get(0)
                    //de ese primer standing, extraemos su table el cual contiene las posiciones de los equipos
                    //y guardamos en ListaPosiciones
                    val ListaPosisiones = stan.table
                    for (posi in ListaPosisiones){
                        //agregamos cada elemento de ListaPosiciones en la lista posList.
                        posList.add(posi)
                    }
                    contador +=1
                    //por medio de este callback podemos llamarla desde el mainActivity para guardar la información
                    //de las posiciones en Room una vez termine esta extracción desde la API de internet.
                    callback.onSuccessPos(posList, idComp, contador)
                }else{
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PosiGeneral>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }
    //esta funcion trae una lista de posiciones desde el SQLite
    fun getPosicionesRoom(context: Context, compId:Int, callback: (ArrayList<Posiciones>) -> Unit){
        //hacemos la conexion por Room al SQLite
        val db = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "Futbol").fallbackToDestructiveMigration().build()
        Thread{
            //instanciamos el DAO
            val posicionDAO = db.posicionDAO()
            //instanciamos la lista de posiciones
            val posicionList = ArrayList<Posiciones>()
            //traemos la posicion, el team interno y los puntos de cada posicion de forma ordenada declarada en el DAO
            posicionDAO.findByComp(compId).forEach{ p : Posicion ->
                val team = Team(p.name)
                posicionList.add(
                    Posiciones(
                        p.position,
                        team,
                        p.points
                )
                )
            }
            //retornamos en callback la lista de posiciones traida del SQLite
            callback(posicionList)
        }.start()
    }
}