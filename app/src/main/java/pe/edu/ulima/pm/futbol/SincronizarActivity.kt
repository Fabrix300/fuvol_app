package pe.edu.ulima.pm.futbol

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class SincronizarActivity: AppCompatActivity() {

    private var tvAsSincronizar: TextView? = null
    private var btAsSincronizar: AppCompatButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sincronizar)

        tvAsSincronizar = findViewById(R.id.tv_as_competencias)
        btAsSincronizar = findViewById(R.id.bt_as_sincronizar)

        btAsSincronizar!!.setOnClickListener {
            //TODO: Poner aca las 2 sincronizaciones y logica de 1era vez (Descargar las competiciones y standings de las 3 primeras)
            //sincronizarCompeticiones()
            //sincronizarStandings()
            val intent = Intent()
            intent.setClass(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

}