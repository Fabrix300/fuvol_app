package pe.edu.ulima.pm.futbol.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import pe.edu.ulima.pm.futbol.R

class EquiposFragment: Fragment() {

    var tvFeTitulo: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_equipos, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val idCompeticion = requireArguments().getInt("idCompe")
        tvFeTitulo = requireView().findViewById(R.id.tv_fe_titulo)
        tvFeTitulo!!.append(idCompeticion.toString())
    }

}