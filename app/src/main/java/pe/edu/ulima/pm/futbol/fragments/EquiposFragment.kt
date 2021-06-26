package pe.edu.ulima.pm.futbol.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.edu.ulima.pm.futbol.R
import pe.edu.ulima.pm.futbol.adapters.CompeticionesRVAdapter
import pe.edu.ulima.pm.futbol.adapters.EquiposRVAdapter
import pe.edu.ulima.pm.futbol.models.beans.Competencias
import pe.edu.ulima.pm.futbol.models.beans.Equipos
import pe.edu.ulima.pm.futbol.models.managers.EquipoManager
import java.util.ArrayList

class EquiposFragment: Fragment() {
    /*
    *
    * INTEGRANTES:
    *  - DIEGO ANTONIO ESQUIVEL PATIÑO    20170532
    *  - FABRICIO SOTELO PARRA            20171497
    *
    * PARA PROBAR EL APP, USAR LAS COMPETICIONES: "Campeonato Brasileiro Serie A (id: 2013)", "Premier League (id: 2021)", "Championship (id: 2016)"
    *
    * */
    //declaramos el recyclerView del fragment
    var rvEquipos : RecyclerView? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //displayeamos el fragment
        return inflater.inflate(R.layout.fragment_equipos, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //recogemos el id de la competencia pasada por el activity al crear el fragment
        val idCompeticion = requireArguments().getInt("idCompe")
        //encontramos el recyclerView en el XML
        rvEquipos = requireView().findViewById(R.id.rv_fe_equipos)
        //traemos la lista de equipos desde el SQLite
        EquipoManager.getInstance().getEquiposRoom(requireContext(), idCompeticion, {equipos : ArrayList<Equipos> ->
            //corremos en un UiThread el display del recycleView una vez traida la lista del SQLite
            requireActivity().runOnUiThread(java.lang.Runnable{
                //instanciamos el adapter de recylceView
                val rvEquiposAdapter = EquiposRVAdapter(equipos, requireContext())
                //asignamos el layoutManager y el adapter al recylceView
                rvEquipos!!.layoutManager = LinearLayoutManager(requireContext())
                rvEquipos!!.adapter = rvEquiposAdapter
            })
        })
    }

}