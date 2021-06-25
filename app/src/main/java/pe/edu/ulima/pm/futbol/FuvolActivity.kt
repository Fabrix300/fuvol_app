package pe.edu.ulima.pm.futbol

import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import pe.edu.ulima.pm.futbol.fragments.EquiposFragment
import pe.edu.ulima.pm.futbol.fragments.PosicionesFragment
import java.util.ArrayList

class FuvolActivity : AppCompatActivity()  {
    /*
    *
    * INTEGRANTES:
    *  - DIEGO ANTONIO ESQUIVEL PATIÑO    20170532
    *  - FABRICIO SOTELO PARRA            20171497
    *
    * */

    var fragments: ArrayList<Fragment> = ArrayList()
    var dlaFutbol: DrawerLayout? = null
    var idCompe: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_futbol)

        fragments.add(EquiposFragment())
        fragments.add(PosicionesFragment())

        idCompe = intent.getIntExtra("idCompe", 0)
        val bundle : Bundle = Bundle()
        bundle.putInt("idCompe", idCompe)

        fragments[0].arguments = bundle
        fragments[1].arguments = bundle

        val nviMain = findViewById<NavigationView>(R.id.nviFutbol)
        dlaFutbol = findViewById(R.id.dlaFutbol)

        nviMain.setNavigationItemSelectedListener { item : MenuItem ->
            item.setChecked(true)
            val ft = supportFragmentManager.beginTransaction()
            if (item.itemId == R.id.mnuEquipos) {
                // Abrimos fragment AccountFragment
                ft.replace(R.id.flaContent, fragments[0])
            }else if (item.itemId == R.id.mnuTablaPosiciones) {
                // Abrimos fragment ProductsFragment
                ft.replace(R.id.flaContent, fragments[1])
            }
            ft.addToBackStack(null)
            ft.commit()
            dlaFutbol!!.closeDrawers()
            true
        }

        // Agregamos fragment por defecto
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.flaContent, fragments[0])
        ft.addToBackStack(null)
        ft.commit()
    }

}