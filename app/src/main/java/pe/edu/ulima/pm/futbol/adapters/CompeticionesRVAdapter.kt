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

    //Definimos las variables que utilizará el CompeticionesRVAdapter, como la lista de competencias, el listener
    //para el onclick y el contexto de la actividad donde reside
    private var context: Context? = null
    private var listener : OnCompetenciaItemClickListener? = null
    private var competencias: ArrayList<Competencias>? =null

    //Constructor del adapter
    constructor(competencias: ArrayList<Competencias>, context: Context, lsnr : OnCompetenciaItemClickListener): super(){
        this.competencias = competencias
        this.context = context
        this.listener = lsnr
    }

    //Con esta función, seteamos el "competicion_card" como el layout que utilizará el viewHolder para mostrar en
    //el recyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.competicion_card, parent, false)
        return MyViewHolder(view)
    }

    //Con esta función, modificamos los elementos de las views con la información de nuestra lista de competencias
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var competi = competencias!!.get(position)
        holder.tvTitulo!!.text = competencias!!.get(position).name
        holder.tvNroEquipos!!.text = "Temporadas disponibles: ${competencias!!.get(position).numberOfAvailableSeasons}"

        holder.tvId!!.text = competencias!!.get(position).id.toString()

        //Le seteamos el onclicklistener que pasará un elemento "listener" de tipo "OnCompetenciaItemClickListener",
        //El cual es una interfaz que pasará el id de la competición (la cual está en un elemento oculto de la view)
        holder.itemView.setOnClickListener { v : View ->
            Log.i("Dentro del listener","${competi.id}, $position")

            listener!!.onClick(holder.tvId!!.text.toString())


        }
    }

    //funcion con la que se obtiene el numero de elementos que están en la lista competencias
    override fun getItemCount(): Int {
        return competencias!!.size
    }

    //Definimos la clase MyViewHolder, que es de tipo ViewHolder, la cual se encargará de tener los elementos
    //que utilizarán los views del recycler
    class MyViewHolder: RecyclerView.ViewHolder{
        //Variables que utiliza el viewHolder
        var tvTitulo: TextView? = null
        var tvNroEquipos: TextView? = null
        var tvId : TextView? = null

        //Constructor del ViewHolder
        constructor(view: View): super(view){
            this.tvTitulo = view.findViewById(R.id.tv_cc_titulo)
            this.tvNroEquipos = view.findViewById(R.id.tv_cc_nroEquipos)
            this.tvId = view.findViewById(R.id.tv_cc_id)
        }

    }

}