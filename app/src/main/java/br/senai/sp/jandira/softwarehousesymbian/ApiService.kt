package br.senai.sp.jandira.softwarehousesymbian


import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/usuario/cadastrarUsuario")
    suspend fun createUser(@Body body: JsonObject): Response<JsonObject>


}