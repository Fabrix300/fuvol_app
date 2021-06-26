package pe.edu.ulima.pm.futbol.models.managers

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConnectionManager {
    //decalaramos la cariable de retrofit
    private var retrofit: Retrofit? = null

    companion object{
        private var instance: ConnectionManager? = null

        fun getInstance(): ConnectionManager{
            if(instance == null){
                instance = ConnectionManager()
            }
            return instance!!
        }
    }

    init {
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.football-data.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getRetrofit(): Retrofit{
        return retrofit!!
    }

}