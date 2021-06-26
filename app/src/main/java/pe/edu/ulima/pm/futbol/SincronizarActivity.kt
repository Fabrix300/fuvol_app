package pe.edu.ulima.pm.futbol

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class SincronizarActivity: AppCompatActivity() {
    /*
    *
    * INTEGRANTES:
    *  - DIEGO ANTONIO ESQUIVEL PATIÑO    20170532
    *  - FABRICIO SOTELO PARRA            20171497
    *
    * PARA PROBAR EL APP, USAR LAS COMPETICIONES: "Campeonato Brasileiro Serie A (id: 2013)", "Premier League (id: 2021)", "Championship (id: 2016)"
    *
    * */

    //creamos el boton de la actividad Sincronizar
    private var btAsSincronizar: AppCompatButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sincronizar)

        //En este bloque hacemos una verificación si es que es la primera vez que abre la app.
        if(IsFirstTime()){
            //Si es la primera vez, se obtiene el boton de la actividad, asignándolo a "btAsSincronizar" y
            // le agrergamos un onClickListener que simplemente pasará a la siguiente actividad "MainActivity"
            btAsSincronizar = findViewById(R.id.bt_as_sincronizar)

            btAsSincronizar!!.setOnClickListener {
                val intent = Intent()
                intent.setClass(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }else{
            //Si no es la primera vez que se abre la aplicación se procede a pasar de frente al "MainActivity"
            val intent = Intent()
            intent.setClass(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    //Función para obtener de los sharedPreferences el boolean "FIRST_TIME" el que indica si es que es la primera
    // vez que se abre la app
    private fun IsFirstTime() : Boolean{
        val valor = getSharedPreferences("USERS_DATA",
        Context.MODE_PRIVATE).getBoolean("FIRST_TIME",true)
        return valor
    }

}