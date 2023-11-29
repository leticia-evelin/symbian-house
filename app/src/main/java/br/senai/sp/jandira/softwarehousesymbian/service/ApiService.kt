package br.senai.sp.jandira.softwarehousesymbian.service


import br.senai.sp.jandira.softwarehousesymbian.model.ApiResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/usuario/cadastrarUsuario")
    suspend fun createUser(@Body body: JsonObject): Call<ApiResponse>


}