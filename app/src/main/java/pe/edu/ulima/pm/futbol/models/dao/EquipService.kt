package pe.edu.ulima.pm.futbol.models.dao


import pe.edu.ulima.pm.futbol.models.beans.EquipGeneral
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface EquipService {

    @Headers("X-Auth-Token: 6c9a41e132c54031861c0f37af5320a1")
    @GET("v2/competitions/{id}/teams")
    fun getEquipos(@Path("id") id : Int): Call<EquipGeneral>
}