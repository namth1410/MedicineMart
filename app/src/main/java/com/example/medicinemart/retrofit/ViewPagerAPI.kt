package com.example.medicinemart.retrofit

import com.example.medicinemart.models.Address
import com.example.medicinemart.models.BannerAds
import com.example.medicinemart.models.Customer
import com.example.medicinemart.models.Sanpham
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.math.BigDecimal
import java.util.*

interface API {
//    @GET("/products")
//    suspend fun getQuotes() : Response<QuoteList>

    @GET("api/getBannerAds.php")
    suspend fun getBannerAds() : Response<ArrayList<BannerAds>>

    @GET("api/getAllProduct.php")
    suspend fun getAllProduct() : Response<ArrayList<Sanpham>>

    @GET("api/getCart.php")
    suspend fun getCart(@Query("id_customer") id_customer: Int) : Response<ArrayList<JsonObject>>

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

    @GET("api/getAddress.php")
    suspend fun getAddress(@Query("username") username: String) : Response<ArrayList<Address>>

    @FormUrlEncoded
    @POST("api/updateAddress.php")
    fun updateAddress(
        @Field("id") id: Int,
        @Field("full_name") full_name: String,
        @Field("phone") phone: String,
        @Field("td_x") td_x: BigDecimal,
        @Field("td_y") td_y: BigDecimal,
        @Field("location") location: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/deleteAddress.php")
    fun deleteAddress(
        @Field("id") id: Int
    ): Call<Void>

    @FormUrlEncoded
    @POST("api/insertAddress.php")
    fun insertAddress(
        @Field("username") id: String,
        @Field("full_name") full_name: String,
        @Field("phone") phone: String,
        @Field("td_x") td_x: BigDecimal,
        @Field("td_y") td_y: BigDecimal,
        @Field("location") location: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/addOrder.php")
    fun addOrder(
        @Field("id_customer") id_customer: Int
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/addOrderDetail.php")
    fun addOrderDetail(
        @Field("id_product") id_product: Int,
        @Field("quantity") quantity: Int,
        @Field("priceEach") priceEach: Int
    ): Call<ResponseBody>

    @GET("api/getIdAddressMax.php")
    suspend fun getIdAddressMax() : Response<Int>
}