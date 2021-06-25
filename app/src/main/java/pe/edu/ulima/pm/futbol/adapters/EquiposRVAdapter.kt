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

    private var context: Context? = null
    private var equipos: ArrayList<Equipos>? =null

    constructor(competencias: ArrayList<Equipos>, context: Context): super(){
        this.equipos = competencias
        this.context = context
    }

    class MyViewHolder: RecyclerView.ViewHolder{
        var tvTitulo: TextView? = null
        var tvNroEquipos: TextView? = null

        constructor(view: View): super(view){
            this.tvTitulo = view.findViewById(R.id.tv_cc_titulo)
            this.tvNroEquipos = view.findViewById(R.id.tv_cc_nroEquipos)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.competicion_card, parent, false)
        return EquiposRVAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var equip = equipos!!.get(position)
        holder.tvTitulo!!.text = equipos!!.get(position).name
        holder.tvNroEquipos!!.text = "Estadio: ${equipos!!.get(position).venue}"

        Log.i("posiciondelviewholder",position.toString()+"---------------------------------")
        //Aqui iria el listener de click
    }

    override fun getItemCount(): Int {
        return equipos!!.size
    }


}