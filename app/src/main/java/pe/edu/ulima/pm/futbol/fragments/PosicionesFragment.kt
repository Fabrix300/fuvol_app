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
    * */

    var rvPosiciones : RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_posiciones, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val idCompeticion = requireArguments().getInt("idCompe")
        rvPosiciones = requireView().findViewById(R.id.rv_fp_posiciones)

        PosicionManager.getInstance().getPosicionesRoom(requireContext(), idCompeticion, {posiciones : ArrayList<Posiciones> ->
            requireActivity().runOnUiThread(java.lang.Runnable{
                val rvEquiposAdapter = PosicionesRVAdapter(posiciones, requireContext())
                rvPosiciones!!.layoutManager = LinearLayoutManager(requireContext())
                rvPosiciones!!.adapter = rvEquiposAdapter
            })
        })

    }

}