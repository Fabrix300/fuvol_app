package pe.edu.ulima.pm.futbol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.edu.ulima.pm.futbol.adapters.CompeticionesRVAdapter
import pe.edu.ulima.pm.futbol.models.beans.CompeGeneral
import pe.edu.ulima.pm.futbol.models.beans.Competencias
import pe.edu.ulima.pm.futbol.models.dao.CompeService
import pe.edu.ulima.pm.futbol.models.managers.CompeticionManager
import pe.edu.ulima.pm.futbol.models.managers.ConnectionManager
import retrofit2.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    var tvAmCompetencias: TextView? = null
    var rvCompetencias: RecyclerView? = null
    var compeList: ArrayList<Competencias>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        compeList = ArrayList<Competencias>()
        tvAmCompetencias = findViewById(R.id.tv_am_competencias)
        rvCompetencias = findViewById(R.id.rv_am_competencias)

        val retrofit = ConnectionManager.getInstance().getRetrofit()
        val compeService = retrofit.create<CompeService>()
        compeService.getCompeticiones().enqueue(object : Callback<CompeGeneral> {
            override fun onResponse(call: Call<CompeGeneral>, response: Response<CompeGeneral>) {
                if(response.code() == 200 && response.body() != null){
                    val ListaCompetencias = response.body()!!.competitions
                    for (compe in ListaCompetencias){
                        Log.i("waw", compe.name)
                        compeList!!.add(compe)
                    }
                    CompeticionManager.getInstance().setCompeticion(compeList!!)
                    putDataIntoRecyclerView(compeList!!)
                }else{
                    Toast.makeText( applicationContext, "Error", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<CompeGeneral>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun putDataIntoRecyclerView(competencias: ArrayList<Competencias>) {
        val rvCompetenciasAdapter = CompeticionesRVAdapter(competencias, applicationContext)
        rvCompetencias!!.layoutManager = LinearLayoutManager(applicationContext)
        rvCompetencias!!.adapter = rvCompetenciasAdapter
    }

    fun pasar(v: View){
        val intent = Intent()
        intent.setClass(this, FuvolActivity::class.java)
        startActivity(intent)
        finish()
    }

}