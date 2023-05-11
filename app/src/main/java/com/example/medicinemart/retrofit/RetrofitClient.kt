package com.example.medicinemart.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val baseUrl = "https://0e2d-2402-800-62d0-bf1c-6d83-e12-fd98-c729.ngrok-free.app"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }

    var viewPagerApi = RetrofitClient.getInstance().create(API::class.java)

}