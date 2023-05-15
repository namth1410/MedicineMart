package com.example.medicinemart.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val baseUrl = "https://03ff-2402-800-62d0-bf1c-39e4-57d6-7975-df6b.ngrok-free.app"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }

    var viewPagerApi = RetrofitClient.getInstance().create(API::class.java)

}