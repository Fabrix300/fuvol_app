package pe.edu.ulima.pm.futbol.models.dao

import pe.edu.ulima.pm.futbol.models.beans.CompeGeneral
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface CompeService {

    @Headers("X-Auth-Token: 3e067ce4f4414a3e973c872a8676e630")
    @GET("v2/competitions?plan=TIER_ONE")
    fun getCompeticiones(): Call<CompeGeneral>

}