package pe.edu.ulima.pm.futbol.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pe.edu.ulima.pm.futbol.R
import pe.edu.ulima.pm.futbol.models.beans.Competencias
import java.util.ArrayList

class CompeticionesRVAdapter: RecyclerView.Adapter<CompeticionesRVAdapter.MyViewHolder> {

    private var context: Context? = null
    private var competencias: ArrayList<Competencias>? =null

    constructor(competencias: ArrayList<Competencias>, context: Context): super(){
        this.competencias = competencias
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.competicion_card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvTitulo!!.text = competencias!!.get(position).name
        holder.tvNroEquipos!!.text = competencias!!.get(position).numberOfAvailableSeasons.toString()

        //Aqui iria el listener de click
    }

    override fun getItemCount(): Int {
        return competencias!!.size
    }

    class MyViewHolder: RecyclerView.ViewHolder{
        var tvTitulo: TextView? = null
        var tvNroEquipos: TextView? = null

        constructor(view: View): super(view){
            this.tvTitulo = view.findViewById(R.id.tv_cc_titulo)
            this.tvNroEquipos = view.findViewById(R.id.tv_cc_nroEquipos)
        }

    }

}