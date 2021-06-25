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
    * */

    
    var rvEquipos : RecyclerView? = null


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
        rvEquipos = requireView().findViewById(R.id.rv_fe_equipos)

        EquipoManager.getInstance().getEquiposRoom(requireContext(), idCompeticion, {equipos : ArrayList<Equipos> ->
            requireActivity().runOnUiThread(java.lang.Runnable{
                val rvEquiposAdapter = EquiposRVAdapter(equipos, requireContext())
                rvEquipos!!.layoutManager = LinearLayoutManager(requireContext())
                rvEquipos!!.adapter = rvEquiposAdapter
            })
        })
    }

}