package pe.edu.ulima.pm.futbol.models.dao

import pe.edu.ulima.pm.futbol.models.beans.CompeGeneral
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface CompeService {

    @Headers("X-Auth-Token: 6c9a41e132c54031861c0f37af5320a1")
    @GET("v2/competitions?plan=TIER_ONE")
    fun getCompeticiones(): Call<CompeGeneral>

}