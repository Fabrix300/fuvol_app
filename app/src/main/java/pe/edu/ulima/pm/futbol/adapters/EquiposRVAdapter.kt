package pe.edu.ulima.pm.futbol.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pe.edu.ulima.pm.futbol.R
import pe.edu.ulima.pm.futbol.models.beans.Equipos
import java.util.ArrayList

class EquiposRVAdapter : RecyclerView.Adapter<EquiposRVAdapter.MyViewHolder>{
    /*
    *
    * INTEGRANTES:
    *  - DIEGO ANTONIO ESQUIVEL PATIÑO    20170532
    *  - FABRICIO SOTELO PARRA            20171497
    *
    * */

    //Definimos las variables que utilizará el EquiposRVAdapter, como la lista de equipos
    // y el contexto de la actividad donde reside
    private var context: Context? = null
    private var equipos: ArrayList<Equipos>? =null

    //Constructor del adapter
    constructor(competencias: ArrayList<Equipos>, context: Context): super(){
        this.equipos = competencias
        this.context = context
    }

    //Definimos la clase MyViewHolder, que es de tipo ViewHolder, la cual se encargará de tener los elementos
    //que utilizarán los views del recyclerView
    class MyViewHolder: RecyclerView.ViewHolder{
        var tvTitulo: TextView? = null
        var tvNroEquipos: TextView? = null

        //Constructor del ViewHolder
        constructor(view: View): super(view){
            this.tvTitulo = view.findViewById(R.id.tv_cc_titulo)
            this.tvNroEquipos = view.findViewById(R.id.tv_cc_nroEquipos)
        }

    }

    //Con esta función, seteamos el "competicion_card" como el layout que utilizará el viewHolder para mostrar en
    //el recyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.competicion_card, parent, false)
        return EquiposRVAdapter.MyViewHolder(view)
    }

    //Con esta función, modificamos los elementos de las views con la información de nuestra lista de Equipos.
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var equip = equipos!!.get(position)
        holder.tvTitulo!!.text = equipos!!.get(position).name
        holder.tvNroEquipos!!.text = "Estadio: ${equipos!!.get(position).venue}"
    }

    //funcion con la que se obtiene el numero de elementos que están en la lista Equipos.
    override fun getItemCount(): Int {
        return equipos!!.size
    }


}