package pe.edu.ulima.pm.futbol.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import pe.edu.ulima.pm.futbol.FuvolActivity
import pe.edu.ulima.pm.futbol.R
import pe.edu.ulima.pm.futbol.interfaces.OnCompetenciaItemClickListener
import pe.edu.ulima.pm.futbol.models.beans.Competencias
import java.util.ArrayList

class CompeticionesRVAdapter: RecyclerView.Adapter<CompeticionesRVAdapter.MyViewHolder> {
    /*
    *
    * INTEGRANTES:
    *  - DIEGO ANTONIO ESQUIVEL PATIÑO    20170532
    *  - FABRICIO SOTELO PARRA            20171497
    *
    * */

    private var context: Context? = null
    private var listener : OnCompetenciaItemClickListener? = null
    private var competencias: ArrayList<Competencias>? =null

    constructor(competencias: ArrayList<Competencias>, context: Context, lsnr : OnCompetenciaItemClickListener): super(){
        this.competencias = competencias
        this.context = context
        this.listener = lsnr
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.competicion_card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var competi = competencias!!.get(position)
        holder.tvTitulo!!.text = competencias!!.get(position).name
        holder.tvNroEquipos!!.text = "Temporadas disponibles: ${competencias!!.get(position).numberOfAvailableSeasons}"

        Log.i("posiciondelviewholder",position.toString()+"---------------------------------")
        //Aqui iria el listener de click
        holder.tvId!!.text = competencias!!.get(position).id.toString()

        holder.itemView.setOnClickListener { v : View ->
            Log.i("Dentro del listener","${competi.id}, $position")

            listener!!.onClick(holder.tvId!!.text.toString())


        }
    }

    override fun getItemCount(): Int {
        return competencias!!.size
    }

    class MyViewHolder: RecyclerView.ViewHolder{
        var tvTitulo: TextView? = null
        var tvNroEquipos: TextView? = null
        var tvId : TextView? = null

        constructor(view: View): super(view){
            this.tvTitulo = view.findViewById(R.id.tv_cc_titulo)
            this.tvNroEquipos = view.findViewById(R.id.tv_cc_nroEquipos)
            this.tvId = view.findViewById(R.id.tv_cc_id)
        }

    }

}