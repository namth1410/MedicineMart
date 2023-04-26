package com.example.medicinemart.retrofit

import com.example.medicinemart.models.BannerAds
import com.example.medicinemart.models.Customer
import com.example.medicinemart.models.Sanpham
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface API {
//    @GET("/products")
//    suspend fun getQuotes() : Response<QuoteList>

    @GET("api/getBannerAds.php")
    suspend fun getBannerAds() : Response<ArrayList<BannerAds>>

    @GET("api/getAllProduct.php")
    suspend fun getAllProduct() : Response<ArrayList<Sanpham>>

    @GET("api/getCart.php")
    suspend fun getCart() : Response<ArrayList<JsonObject>>

    @FormUrlEncoded
    @POST("api/addCart.php")
    fun addToCart(
        @Field("id_customer") id_customer: Int,
        @Field("id_product") id_product: Int,
        @Field("quantity") quantity: Int
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/updateQuantity.php")
    fun updateQuantity(
        @Field("id_customer") idCustomer: Int,
        @Field("id_product") idProduct: Int,
        @Field("quantity") quantity: Int
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/deleteProductInCart.php")
    fun deleteProductInCart(
        @Field("id_customer") idCustomer: Int,
        @Field("id_product") idProduct: Int
    ): Call<Void>

    @GET("api/getOrder.php")
    suspend fun getOrder(
        @Query("id_customer") idCustomer: Int,
        @Query("status") status: String
    ) : Response<ArrayList<JsonObject>>

    @FormUrlEncoded
    @POST("api/sign_up.php")
    fun signUp(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("salt") salt: String
    ): Call<ResponseBody>

    @GET("api/getAccount.php")
    suspend fun getAccounts() : Response<ArrayList<JsonObject>>

    @GET("api/getInfoCustomer.php")
    fun getInfoCustomer(@Query("username") username: String) : Call<Customer>

    @FormUrlEncoded
    @POST("api/updateInfoCus.php")
    fun updateInfoCus(
        @Field("username") username: String,
        @Field("full_name") full_name: String,
        @Field("phone") phone: String
    ): Call<ResponseBody>
}