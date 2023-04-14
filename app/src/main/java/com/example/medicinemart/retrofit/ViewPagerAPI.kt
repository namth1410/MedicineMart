package com.example.medicinemart.retrofit

import com.example.medicinemart.models.BannerAds
import com.example.medicinemart.models.Sanpham
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET

interface API {
//    @GET("/products")
//    suspend fun getQuotes() : Response<QuoteList>

    @GET("api/getBannerAds.php")
    suspend fun getBannerAds() : Response<ArrayList<BannerAds>>

    @GET("api/getAllProduct.php")
    suspend fun getAllProduct() : Response<ArrayList<Sanpham>>

    @GET("api/getCart.php")
    suspend fun getCart() : Response<ArrayList<JsonObject>>
}