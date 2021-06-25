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

    private var context: Context? = null
    private var posiciones: ArrayList<Posiciones>? =null

    constructor(posiciones: ArrayList<Posiciones>, context: Context): super(){
        this.posiciones = posiciones
        this.context = context
    }

    class MyViewHolder: RecyclerView.ViewHolder{
        var tvTitulo: TextView? = null
        var tvPuntos: TextView? = null

        constructor(view: View): super(view){
            this.tvTitulo = view.findViewById(R.id.tv_pc_titulo)
            this.tvPuntos = view.findViewById(R.id.tv_pc_puntos)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.posicion_card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var posicion = posiciones!!.get(position)
        holder.tvTitulo!!.text = "${posiciones!!.get(position).position}. ${posiciones!!.get(position).team.name}"
        holder.tvPuntos!!.text = "Puntos: ${posiciones!!.get(position).points}"
    }

    override fun getItemCount(): Int {
        return posiciones!!.size
    }

}