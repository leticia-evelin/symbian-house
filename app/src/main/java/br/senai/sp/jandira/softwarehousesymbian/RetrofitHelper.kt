package br.senai.sp.jandira.softwarehousesymbian

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val baseurl = "http://10.107.144.14:3000"

    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}