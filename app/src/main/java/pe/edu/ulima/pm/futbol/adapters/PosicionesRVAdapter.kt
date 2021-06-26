package pe.edu.ulima.pm.futbol.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pe.edu.ulima.pm.futbol.R
import pe.edu.ulima.pm.futbol.models.beans.Equipos
import pe.edu.ulima.pm.futbol.models.beans.Posiciones
import java.util.ArrayList

class PosicionesRVAdapter: RecyclerView.Adapter<PosicionesRVAdapter.MyViewHolder> {
    /*
    *
    * INTEGRANTES:
    *  - DIEGO ANTONIO ESQUIVEL PATIÑO    20170532
    *  - FABRICIO SOTELO PARRA            20171497
    *
    * */

    //Definimos las variables que utilizará el EquiposRVAdapter, como la lista de posiciones
    // y el contexto de la actividad donde reside
    private var context: Context? = null
    private var posiciones: ArrayList<Posiciones>? =null

    //Constructor del adapter
    constructor(posiciones: ArrayList<Posiciones>, context: Context): super(){
        this.posiciones = posiciones
        this.context = context
    }

    //Definimos la clase MyViewHolder, que es de tipo ViewHolder, la cual se encargará de tener los elementos
    //que utilizarán los views del recyclerView
    class MyViewHolder: RecyclerView.ViewHolder{
        var tvTitulo: TextView? = null
        var tvPuntos: TextView? = null

        //Constructor del ViewHolder
        constructor(view: View): super(view){
            this.tvTitulo = view.findViewById(R.id.tv_pc_titulo)
            this.tvPuntos = view.findViewById(R.id.tv_pc_puntos)
        }
    }

    //Con esta función, seteamos el "posicion_card" como el layout que utilizará el viewHolder para mostrar en
    //el recyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.posicion_card, parent, false)
        return MyViewHolder(view)
    }

    //Con esta función, modificamos los elementos de las views con la información de nuestra lista de posiciones.
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var posicion = posiciones!!.get(position)
        holder.tvTitulo!!.text = "${posiciones!!.get(position).position}. ${posiciones!!.get(position).team.name}"
        holder.tvPuntos!!.text = "Puntos: ${posiciones!!.get(position).points}"
    }

    //funcion con la que se obtiene el numero de elementos que están en la lista Posiciones.
    override fun getItemCount(): Int {
        return posiciones!!.size
    }

}