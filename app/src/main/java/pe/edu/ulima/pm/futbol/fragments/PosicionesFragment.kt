package pe.edu.ulima.pm.futbol.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.edu.ulima.pm.futbol.R
import pe.edu.ulima.pm.futbol.adapters.PosicionesRVAdapter
import pe.edu.ulima.pm.futbol.models.beans.Posiciones
import pe.edu.ulima.pm.futbol.models.managers.PosicionManager
import java.util.ArrayList

class PosicionesFragment: Fragment() {
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
    var rvPosiciones : RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //displayeamos el fragment
        return inflater.inflate(R.layout.fragment_posiciones, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //recogemos el id de la competencia pasada por el activity al crear el fragment
        val idCompeticion = requireArguments().getInt("idCompe")
        //encontramos el recyclerView en el XML
        rvPosiciones = requireView().findViewById(R.id.rv_fp_posiciones)
        //traemos la lista de posiciones desde el SQLite
        PosicionManager.getInstance().getPosicionesRoom(requireContext(), idCompeticion, {posiciones : ArrayList<Posiciones> ->
            //corremos en un UiThread el display del recycleView una vez traida la lista del SQLite
            requireActivity().runOnUiThread(java.lang.Runnable{
                //instanciamos el adapter de recylceView
                val rvEquiposAdapter = PosicionesRVAdapter(posiciones, requireContext())
                //asignamos el layoutManager y el adapter al recylceView
                rvPosiciones!!.layoutManager = LinearLayoutManager(requireContext())
                rvPosiciones!!.adapter = rvEquiposAdapter
            })
        })

    }

}