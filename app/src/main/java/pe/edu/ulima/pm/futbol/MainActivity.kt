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
import pe.edu.ulima.pm.futbol.models.beans.CompeGeneral
import pe.edu.ulima.pm.futbol.models.beans.Competencias
import pe.edu.ulima.pm.futbol.models.beans.Equipos
import pe.edu.ulima.pm.futbol.models.dao.CompeService
import pe.edu.ulima.pm.futbol.models.managers.CompeticionManager
import pe.edu.ulima.pm.futbol.models.managers.ConnectionManager
import pe.edu.ulima.pm.futbol.models.managers.EquipoManager
import pe.edu.ulima.pm.futbol.models.managers.onGetTeamsDone
import pe.edu.ulima.pm.futbol.models.persistence.AppDatabase
import pe.edu.ulima.pm.futbol.models.persistence.dao.CompeticionDAO
import pe.edu.ulima.pm.futbol.models.persistence.entities.Competencia
import pe.edu.ulima.pm.futbol.models.persistence.entities.Equipo
import retrofit2.*
import java.util.ArrayList

class MainActivity : AppCompatActivity(), onGetTeamsDone {

    var tvAmCompetencias: TextView? = null
    var rvCompetencias: RecyclerView? = null
    var compeList: ArrayList<Competencias>? = null
    var equiList: ArrayList<Equipos>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        compeList = ArrayList<Competencias>()
        equiList = ArrayList<Equipos>()
        tvAmCompetencias = findViewById(R.id.tv_am_competencias)
        rvCompetencias = findViewById(R.id.rv_am_competencias)

        if(/*getSharedPreferences("USERS_DATA",
            Context.MODE_PRIVATE).getBoolean("FIRST_TIME",true)*/true){
            val retrofit = ConnectionManager.getInstance().getRetrofit()
            val compeService = retrofit.create<CompeService>()
            compeService.getCompeticiones().enqueue(object : Callback<CompeGeneral> {
                override fun onResponse(call: Call<CompeGeneral>, response: Response<CompeGeneral>) {
                    if(response.code() == 200 && response.body() != null){
                        val ListaCompetencias = response.body()!!.competitions
                        var counter =1
                        val eManager = EquipoManager.getInstance()
                        for (compe in ListaCompetencias){
                            Log.i("waw", compe.id.toString())
                            compeList!!.add(compe)
                            if(counter < 4){
                                /*EquipoManager.getInstance().getEquipos(this@MainActivity,compe.id, {listaEquipos : ArrayList<Equipos> ->
                                    if(counter == 3){
                                        for (equi in listaEquipos){
                                            Log.i("UwU", equi.name)
                                        }
                                        saveEquipos(listaEquipos!!)
                                    }
                                })*/
                                eManager.getEquipos(this@MainActivity,compe.id, this@MainActivity)
                                /*if(counter == 3){
                                    for (equi in equiList!!){
                                        Log.i("UwU", equi.name)
                                    }
                                    saveEquipos(equiList!!)
                                }*/
                                counter += 1
                            }
                        }
                        CompeticionManager.getInstance().setCompeticion(compeList!!)
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

            EquipoManager.getInstance().getEquiposRoom(this, 2013)
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
        val rvCompetenciasAdapter = CompeticionesRVAdapter(competencias, applicationContext)
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

}