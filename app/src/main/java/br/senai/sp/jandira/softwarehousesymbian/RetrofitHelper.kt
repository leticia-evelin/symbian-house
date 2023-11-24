package br.senai.sp.jandira.softwarehousesymbian

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private val baseurl = "http://10.107.144.14:8080"

//    private val retrofitFactory =
//        .Builder()
//        .baseUrl(baseurl)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()

    fun getInstance(): Retrofit {
        return Retrofit.Builder()
        .baseUrl(baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

//    fun Register(): ApiService {
//        return retrofitFactory.create(ApiService::class.java)
//    }
}