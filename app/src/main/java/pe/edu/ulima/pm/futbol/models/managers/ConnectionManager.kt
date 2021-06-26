package pe.edu.ulima.pm.futbol.models.managers

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConnectionManager {
    //decalaramos la variable de retrofit de tipo Retrofit
    private var retrofit: Retrofit? = null

    //Creamos el singleton de retrofit y su función para que devuelva su instancia
    companion object{
        private var instance: ConnectionManager? = null

        //funcion para que devuelva la instancia
        fun getInstance(): ConnectionManager{
            if(instance == null){
                instance = ConnectionManager()
            }
            return instance!!
        }
    }

    //En esta parte, al inicializar por primera vez el singleton, se crea el builder de retrofit, el cual
    //construye la conección con la urlBase.
    init {
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.football-data.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Funcion que retorna la variable retrofit de tipo Retrofit
    fun getRetrofit(): Retrofit{
        return retrofit!!
    }

}