package pe.edu.ulima.pm.futbol

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import pe.edu.ulima.pm.futbol.adapters.CompeticionesRVAdapter
import pe.edu.ulima.pm.futbol.interfaces.OnCompetenciaItemClickListener
import pe.edu.ulima.pm.futbol.models.beans.CompeGeneral
import pe.edu.ulima.pm.futbol.models.beans.Competencias
import pe.edu.ulima.pm.futbol.models.beans.Equipos
import pe.edu.ulima.pm.futbol.models.beans.Posiciones
import pe.edu.ulima.pm.futbol.models.dao.CompeService
import pe.edu.ulima.pm.futbol.models.managers.*
import pe.edu.ulima.pm.futbol.models.persistence.AppDatabase
import pe.edu.ulima.pm.futbol.models.persistence.dao.CompeticionDAO
import pe.edu.ulima.pm.futbol.models.persistence.entities.Competencia
import pe.edu.ulima.pm.futbol.models.persistence.entities.Equipo
import pe.edu.ulima.pm.futbol.models.persistence.entities.Posicion
import retrofit2.*
import java.util.ArrayList

class MainActivity : AppCompatActivity(), onGetTeamsDone, OnCompetenciaItemClickListener, onGetPosicionesDone {
    /*
    *
    * INTEGRANTES:
    *  - DIEGO ANTONIO ESQUIVEL PATIÑO    20170532
    *  - FABRICIO SOTELO PARRA            20171497
    *
    * PARA PROBAR EL APP, USAR LAS COMPETICIONES: "Campeonato Brasileiro Serie A (id: 2013)", "Premier League (id: 2021)", "Championship (id: 2016)"
    *
    * */

    //Acá creamos variables que contendrán textView, el recyclerView de la actividad y dos listas que se utilizarán
    // después.
    var tvAmCompetencias: TextView? = null
    var rvCompetencias: RecyclerView? = null
    var compeList: ArrayList<Competencias>? = null
    var equiList: ArrayList<Equipos>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Instanciamos algunas variables, compeList y equiList le asignamos unas listas vacías de tipo Competencias
        // y Equipos respectivamente y obtenemos el textView y el recyclerView de la actividad:
        compeList = ArrayList<Competencias>()
        equiList = ArrayList<Equipos>()
        tvAmCompetencias = findViewById(R.id.tv_am_competencias)
        rvCompetencias = findViewById(R.id.rv_am_competencias)

        //En esta comprobación gigante, se obtiene el valor del boolean del sharedPreference "FIRST_TIME",
        // valor con el cual procedemos a sincronizar con el api en caso sea true, o procedemos a obtener data
        // de Room en caso sea falso.
        if(getSharedPreferences("USERS_DATA",
            Context.MODE_PRIVATE).getBoolean("FIRST_TIME",true)){

            //toda esta sección, o gran parte, debería estar en el CompeticionManager, pero por cuestion de trabajo
            //se dejó dentro del activity
            val retrofit = ConnectionManager.getInstance().getRetrofit()
            val compeService = retrofit.create<CompeService>()
            //Con este servicio de Retrofit compService, hacemos un call al api para obtener las competiciones
            //que nos brinda, guardandolo en un objeto de tipo "CompeGeneral"
            compeService.getCompeticiones().enqueue(object : Callback<CompeGeneral> {
                override fun onResponse(call: Call<CompeGeneral>, response: Response<CompeGeneral>) {
                    //Comprobamos si la respuesta del servidor no es vacía y fue correcta
                    if(response.code() == 200 && response.body() != null){
                        //Si es correcta, procedemos a tomar la lista de competiciones del body del objeto CompeGeneral
                        //y lo guardamos en la lista "ListaCompetencias"
                        val ListaCompetencias = response.body()!!.competitions
                        //Contador a usar para solo obtener los equipos de las primeras 3 competiciones que lleguen
                        var counter =1
                        //Obtenemos las instancias de los singleton "EquipoManager" y "PosicionManager"
                        val eManager = EquipoManager.getInstance()
                        val pManager = PosicionManager.getInstance()
                        //En este for, añadimos cada competencia en "ListaCompetencias" a la lista ...>
                        for (compe in ListaCompetencias){
                            Log.i("waw", compe.id.toString())
                            // >... "compeList" aquí
                            compeList!!.add(compe)
                            // Usando el contador mencionado, llamamos las primeras 3 veces a las funciones "getEquipos" y
                            // "getPosiciones" de sus respectivos singleton (Managers) para que traigan los equipos y las
                            //listas de posiciones de la respectiva competencia tomada en este for en el que nos encontramos.
                            if(counter < 4){


                                //Llamamos a los métodos de los managers de "Equipos" y "Posiciones",
                                //utilizando el id de la competicion para hacer las consultas a la api respectivas,
                                // puesto que estas consultas a la api necesitan de la id de la competición
                                eManager.getEquipos(this@MainActivity,compe.id, this@MainActivity)
                                pManager.getPosiciones(this@MainActivity, compe.id, this@MainActivity)


                                //Sumamos el counter para que solo podamos llegar a 3 en este if, y solo ejecutar las consultas
                                // de equipos y posiciones para las 3 primeras competiciones
                                counter += 1
                            }
                        }
                        //llamamos al competicion manager (que es un singleton) para guardar la lista "compeList"
                        // en su variable "competiciones"
                        CompeticionManager.getInstance().setCompeticion(compeList!!)
                        //Llamamos a la función saveCompeticiones...............................................
                        saveCompeticiones(compeList!!,{variable : Boolean -> //esto se ejecutará como callback tras finalizar el Thread del save
                            CompeticionManager.getInstance().getCompeticionesRoom(applicationContext, {competencias : ArrayList<Competencias> ->
                                //esto se ejecutará como callback tras finalizar el Thread del get del room
                                //lo corremos como UiThread ya que no se puede acceder a componentes del view en un thread aparte del principal
                                this@MainActivity.runOnUiThread(java.lang.Runnable{
                                    //instanciamos el recyclerView con sus componentes de la lista
                                    putDataIntoRecyclerView(competencias!!)
                                })
                            })
                        })

                    }else{
                        //esto se llama en caso falle la respuesta
                        Toast.makeText( applicationContext, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
                //esto se ejecuta en caso falle el call
                override fun onFailure(call: Call<CompeGeneral>, t: Throwable) {
                    Log.e("Error", t.message!!)
                }
            })
            //editamos el sharedPreference para que ya no vuelva a ejecutar esto al crear la activityMain
            val edit = getSharedPreferences(
                "USERS_DATA", Context.MODE_PRIVATE).edit()
            edit.putBoolean("FIRST_TIME", false)
            edit.commit()
        }
        else{
            //esto lo haremos si ya hay data guardada en el room
            CompeticionManager.getInstance().getCompeticionesRoom(applicationContext, {competencias : ArrayList<Competencias> ->
                //aqui cargaremos la data del room en el recyclerView del activity
                this@MainActivity.runOnUiThread(java.lang.Runnable{
                    putDataIntoRecyclerView(competencias!!)
                })
            })
        }
    }
    //esta es la funcion donde mostraremos una lista en el recyclerView
    fun putDataIntoRecyclerView(competencias: ArrayList<Competencias>) {
        //instanciamos el RVAdapter
        val rvCompetenciasAdapter = CompeticionesRVAdapter(competencias, applicationContext, this)
        //Asignamos el layoutManager y su adapter al recyclerView
        rvCompetencias!!.layoutManager = LinearLayoutManager(applicationContext)
        rvCompetencias!!.adapter = rvCompetenciasAdapter
    }
    //esta funcion debería estar en el CompeticionManager
    //aquí guardaremos una lista de competiciones en el Room (SQLite) de la aplicacion
    private fun saveCompeticiones(competiciones : ArrayList<Competencias>, callback: (Boolean) -> Unit){
        //creamos el databaseBuilder con Room
        val db = Room.databaseBuilder(this,AppDatabase::class.java,"Futbol").fallbackToDestructiveMigration().build()
        Thread{
            //instanciamos el DAO desde el db
            val competicionDAO = db.competicionDAO()
            // para borrar la tabla solo cuando ya esta creada y es de pruebas--------------------
            competicionDAO.delete()
            //--------------------------------------------------------------------------
            //por cada competencia de la lista lo guardaremos como un entity Competencia
            competiciones.forEach{ c : Competencias ->
                competicionDAO.insert(
                    Competencia(
                        c.id,
                        c.name,
                        c.numberOfAvailableSeasons
                    )
                )
            }
            db.close()
            //hacemos el callback con un valor True solamente para avisar que ya se guardo
            callback(true)
        }.start()
    }

    //igualmente al saveCompetencias, esta funcion deberia estar en EquipoManager
    //por lo demas, es muy similar a la funcion saveCompeticiones, solo que esta funcion recibe un parametro extra
    //que nos sirve para hacer un delete solo la primera vez
    fun saveEquipos(equipos : ArrayList<Equipos>, compId: Int, vez: Int) {
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "Futbol").fallbackToDestructiveMigration()
            .build()
        Thread {
            val equipoDAO = db.equipoDAO()

            // para borrar la tabla solo cuando ya esta creada y es de pruebas--------------------
            //este if solo hara el delete de la tabla la primera vez que se quiere guardar en la tabla
            if(vez == 1){
                equipoDAO.delete()
            }
            //--------------------------------------------------------------------------

            equipos.forEach { e: Equipos ->
                equipoDAO.insert(
                    Equipo(
                        //asignamos ID 0 ya que se autogenera
                        0,
                        //segun el entity, añadiremos manualmente un compId al Equipo
                        compId,
                        e.name,
                        e.venue
                    )
                )
            }
            db.close()
        }.start()
    }
    //igualmente al saveEquipos, esta funcion deberia estar en el PosicionManager
    //esta funcion es casi igual al saveEquipos
    fun savePosiciones(posiciones : ArrayList<Posiciones>, compId : Int, vez : Int){
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "Futbol").fallbackToDestructiveMigration()
            .build()
        Thread {
            val posicionDAO = db.posicionDAO()
            // para borrar la tabla solo cuando ya esta creada y es de pruebas--------------------
            if(vez == 1){
                posicionDAO.delete()
            }
            //--------------------------------------------------------------------------
            posiciones.forEach { p: Posiciones ->
                posicionDAO.insert(
                    Posicion(
                        0,
                        compId,
                        p.team.name,
                        p.points,
                        p.position
                    )
                )
            }
            db.close()
        }.start()
    }

    fun pasar(v: View){
        val intent = Intent()
        intent.setClass(this, FuvolActivity::class.java)
        startActivity(intent)
        finish()
    }
    //este es el callback que se ejecutara una vez se traen de internet los equipos en una lista
    override fun onSuccess(listaEquipos: ArrayList<Equipos>, compId: Int, vez: Int) {
        //se procede a guardarla lista en el SQLite
        saveEquipos(listaEquipos, compId, vez)
    }
    //este es el error que devolvera si falla la descarga de internetlos equipos
    override fun onError(msg: String) {

        TODO("Not yet implemented")
    }
    //este es el onClick asignados a cada view del recyclerView para pasar a la vista de
    //equipos correspondiente a cada competencia
    override fun onClick(id : String) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show()
        val id : Int = id.toInt()
        val intent = Intent()
        intent.setClass(this, FuvolActivity::class.java)
        //pasamos el compId de la competencia correspondiente al siguiente activity
        intent.putExtra("idCompe", id)
        startActivity(intent)
        finish()

    }
    //esta funcion se llamara una vez se traen las posiciones de los equipos de internet
    override fun onSuccessPos(listaPosiciones: ArrayList<Posiciones>, compId: Int, vez: Int) {
        savePosiciones(listaPosiciones, compId, vez)
    }
    //esta funcion se llamara si el pedido de la lista de posiciones falla
    override fun onErrorPos(msg: String) {
        TODO("Not yet implemented")
    }
}