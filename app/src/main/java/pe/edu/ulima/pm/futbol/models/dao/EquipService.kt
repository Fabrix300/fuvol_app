package pe.edu.ulima.pm.futbol.models.dao


import pe.edu.ulima.pm.futbol.models.beans.EquipGeneral
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface EquipService {

    @Headers("X-Auth-Token: 3e067ce4f4414a3e973c872a8676e630")
    @GET("v2/competitions/{id}/teams")
    fun getEquipos(@Path("id") id : Int): Call<EquipGeneral>
}