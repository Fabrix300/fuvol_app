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
                                /*EquipoManager.getInstance().getEquipos(this@MainActivity,compe.id, {listaEquipos : ArrayList<Equipos> ->
                                    if(counter == 3){
                                        for (equi in listaEquipos){
                                            Log.i("UwU", equi.name)
                                        }
                                        saveEquipos(listaEquipos!!)
                                    }
                                })*/

                                //Llamamos a los métodos de los managers de "Equipos" y "Posiciones",
                                //utilizando el id de la competicion para hacer las consultas a la api respectivas,
                                // puesto que estas consultas a la api necesitan de la id de la competición
                                eManager.getEquipos(this@MainActivity,compe.id, this@MainActivity)
                                pManager.getPosiciones(this@MainActivity, compe.id, this@MainActivity)
                                /*if(counter == 3){
                                    for (equi in equiList!!){
                                        Log.i("UwU", equi.name)
                                    }
                                    saveEquipos(equiList!!)
                                }*/

                                //Sumamos el counter para que solo podamos llegar a 3 en este if, y solo ejecutar las consultas
                                // de equipos y posiciones para las 3 primeras competiciones
                                counter += 1
                            }
                        }
                        //llamamos al competicion manager (que es un singleton) para guardar la lista "compeList"
                        // en su variable "competiciones"
                        CompeticionManager.getInstance().setCompeticion(compeList!!)
                        //Llamamos a la función saveCompeticiones...............................................
                        saveCompeticiones(compeList!!,{variable : Boolean ->
                            CompeticionManager.getInstance().getCompeticionesRoom(applicationContext, {competencias : ArrayList<Competencias> ->

                                this@MainActivity.runOnUiThread(java.lang.Runnable{
                                    putDataIntoRecyclerView(competencias!!)
                                })
                            })
                        })
                        // prueba de recollección de data desde sqlite
                        /*CompeticionManager.getInstance().getCompeticionesRoom(applicationContext, {competencias : ArrayList<Competencias> ->

                            this@MainActivity.runOnUiThread(java.lang.Runnable{
                                putDataIntoRecyclerView(competencias!!)
                            })
                        })*/
                        //putDataIntoRecyclerView(compeList!!)
                    }else{
                        Toast.makeText( applicationContext, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<CompeGeneral>, t: Throwable) {
                    Log.e("Error", t.message!!)
                }
            })

            val edit = getSharedPreferences(
                "USERS_DATA", Context.MODE_PRIVATE).edit()
            edit.putBoolean("FIRST_TIME", false)
            edit.commit()

            //------------------------------------------------
            PosicionManager.getInstance().getPosicionesRoom(this@MainActivity, 2016,
                { posiciones: ArrayList<Posiciones> ->
                    posiciones.forEach{ p : Posiciones ->
                        Log.i("recuperadoRoomPosicion", "nombre: ${p.team.name}, posicion : ${p.position}")
                    }
                })
            //------------------------------------------------

        }
        else{
            CompeticionManager.getInstance().getCompeticionesRoom(applicationContext, {competencias : ArrayList<Competencias> ->

                this@MainActivity.runOnUiThread(java.lang.Runnable{
                    putDataIntoRecyclerView(competencias!!)
                })
            })
            //EquipoManager.getInstance().getEquiposRoom(this)
        }

    }

    fun putDataIntoRecyclerView(competencias: ArrayList<Competencias>) {
        val rvCompetenciasAdapter = CompeticionesRVAdapter(competencias, applicationContext, this)
        rvCompetencias!!.layoutManager = LinearLayoutManager(applicationContext)
        rvCompetencias!!.adapter = rvCompetenciasAdapter
    }

    private fun saveCompeticiones(competiciones : ArrayList<Competencias>, callback: (Boolean) -> Unit){
        val db = Room.databaseBuilder(this,AppDatabase::class.java,"Futbol").fallbackToDestructiveMigration().build()
        Thread{
            val competicionDAO = db.competicionDAO()
            // para borrar la tabla solo cuando ya esta creada y es de pruebas--------------------
            competicionDAO.delete()
            //--------------------------------------------------------------------------
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
            callback(true)
        }.start()
    }

    fun saveEquipos(equipos : ArrayList<Equipos>, compId: Int, vez: Int) {
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "Futbol").fallbackToDestructiveMigration()
            .build()
        Thread {
            val equipoDAO = db.equipoDAO()

            // para borrar la tabla solo cuando ya esta creada y es de pruebas--------------------
            if(vez == 1){
                equipoDAO.delete()
            }

            //--------------------------------------------------------------------------

            Log.i("antes", "esto es antes del for each en guardado")
            equipos.forEach { e: Equipos ->
                equipoDAO.insert(
                    Equipo(
                        0,
                        compId,
                        e.name,
                        e.venue
                    )
                )
                Log.i("equipoGuardadoRoom", "${e.name}, $compId")
            }
            Log.i("despues", "esto es despues del for each en guardado")
            db.close()
        }.start()
    }

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

            //Log.i("antes", "esto es antes del for each en guardado")
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
                //Log.i("equipoGuardadoRoom", "${p.team.name}, $compId")
            }
            Log.i("despues", "esto es despues del for each en guardado")
            db.close()
        }.start()
    }

    fun pasar(v: View){
        val intent = Intent()
        intent.setClass(this, FuvolActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onSuccess(listaEquipos: ArrayList<Equipos>, compId: Int, vez: Int) {
        saveEquipos(listaEquipos, compId, vez)
    }

    override fun onError(msg: String) {

        TODO("Not yet implemented")
    }

    override fun onClick(id : String) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show()
        val id : Int = id.toInt()
        val intent = Intent()
        intent.setClass(this, FuvolActivity::class.java)
        intent.putExtra("idCompe", id)
        startActivity(intent)
        finish()

    }

    override fun onSuccessPos(listaPosiciones: ArrayList<Posiciones>, compId: Int, vez: Int) {
        savePosiciones(listaPosiciones, compId, vez)
        /*listaPosiciones.forEach{p : Posiciones ->
            Log.i("peticionPosiciones", "puntaje: ${p.points}, nombre: ${p.team.name}, competicion: $compId, standing : ${p.position}")
        }*/

    }

    override fun onErrorPos(msg: String) {
        TODO("Not yet implemented")
    }

}