package br.senai.sp.jandira.softwarehousesymbian.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {
    private val baseurl = "http://10.107.144.14:8080"

    private val retrofitFactory = Retrofit
        .Builder()
        .baseUrl(baseurl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun cadastro(): ApiService {
        return retrofitFactory.create(ApiService::class.java)
    }
}

