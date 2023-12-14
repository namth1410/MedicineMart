package com.example.medicinemart.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.medicinemart.R
import com.example.medicinemart.common.Info
import com.example.medicinemart.databinding.SplashScreenBinding
import com.example.medicinemart.models.Sanpham
import com.example.medicinemart.retrofit.RetrofitClient
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private lateinit var binding_splash_screen: SplashScreenBinding

fun standardized(s: String) : String {
    if (s.startsWith("\"") && s.endsWith("\"")) {
        return s.substring(1, s.length - 1)
    }
    return s
}
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_splash_screen = SplashScreenBinding.inflate(layoutInflater)
        setContentView(binding_splash_screen.root)

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            // Có kết nối mạng, thực hiện các thao tác yêu cầu mạng ở đây
            getAccounts()

            val call = RetrofitClient.viewPagerApi.getAllProduct()
            call.enqueue(object : Callback<ArrayList<JsonObject>> {
                override fun onResponse(
                    call: Call<ArrayList<JsonObject>>, response: Response<ArrayList<JsonObject>>
                ) {
                    if (response.isSuccessful) {
                        for (i in response.body()!!) {
                            val id = i.getAsJsonPrimitive("id").asInt
                            var name = i.getAsJsonPrimitive("name").toString()
                            name = standardized(name)
                            var type = i.getAsJsonPrimitive("type").toString()
                            type = standardized(type)
                            val price = i.getAsJsonPrimitive("price").asInt
                            var describe = i.getAsJsonPrimitive("describe").toString()
                            describe = standardized(describe)
                            var image = i.getAsJsonPrimitive("image").toString()
                            image = standardized(image)
                            var ingredient = i.getAsJsonPrimitive("ingredient").toString()
                            ingredient = standardized(ingredient)
                            var user_guide = i.getAsJsonPrimitive("user_guide").toString()
                            user_guide = standardized(user_guide)

                            var barcode = ""
                            if (!i.get("barcode").isJsonNull) {
                                barcode = i.getAsJsonPrimitive("barcode").toString()
                            } else {
                                barcode = "1"
                            }
                            val tmp = Sanpham(
                                id, name, type, price, describe, ingredient, user_guide, image, barcode
                            )

                            Info.all_product.add(tmp)
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<JsonObject>>, t: Throwable) {

                }

            })

            Handler().postDelayed({
                // Chuyển sang màn hình chính của ứng dụng sau khi hiển thị logo trong 3 giây
                startActivity(Intent(this, DangNhapActivity::class.java))
                Animatoo.animateZoom(this)
                finish()
            }, 1500)
        } else {
            // Không có kết nối mạng, hiển thị thông báo hoặc thông báo cho người dùng biết
            val builder = AlertDialog.Builder(this)
            builder.setCancelable(false)

            val view = layoutInflater.inflate(R.layout.dialog_no_network, null)
            val closeButton = view.findViewById<Button>(R.id.dialog_close_button)

            builder.setView(view)
            val dialog = builder.create()
            dialog.show()

            closeButton.setOnClickListener {
                dialog.dismiss()
                finish()
            }
        }

        /*
        getAccounts()

        val call = RetrofitClient.viewPagerApi.getAllProduct()
        call.enqueue(object : Callback<ArrayList<JsonObject>> {
            override fun onResponse(
                call: Call<ArrayList<JsonObject>>, response: Response<ArrayList<JsonObject>>
            ) {
                if (response.isSuccessful) {
                    for (i in response.body()!!) {
                        val id = i.getAsJsonPrimitive("id").asInt
                        var name = i.getAsJsonPrimitive("name").toString()
                        name = standardized(name)
                        var type = i.getAsJsonPrimitive("type").toString()
                        type = standardized(type)
                        val price = i.getAsJsonPrimitive("price").asInt
                        var describe = i.getAsJsonPrimitive("describe").toString()
                        describe = standardized(describe)
                        var image = i.getAsJsonPrimitive("image").toString()
                        image = standardized(image)
                        var ingredient = i.getAsJsonPrimitive("ingredient").toString()
                        ingredient = standardized(ingredient)
                        var user_guide = i.getAsJsonPrimitive("user_guide").toString()
                        user_guide = standardized(user_guide)

                        var barcode = ""
                        if (!i.get("barcode").isJsonNull) {
                            barcode = i.getAsJsonPrimitive("barcode").toString()
                        } else {
                            barcode = "1"
                        }
                        val tmp = Sanpham(
                            id, name, type, price, describe, ingredient, user_guide, image, barcode
                        )

                        Info.all_product.add(tmp)
                    }
                }

                println(Info.all_product.get(0))
            }

            override fun onFailure(call: Call<ArrayList<JsonObject>>, t: Throwable) {

            }

        })

        Handler().postDelayed({
            // Chuyển sang màn hình chính của ứng dụng sau khi hiển thị logo trong 3 giây
            startActivity(Intent(this, DangNhapActivity::class.java))
            Animatoo.animateZoom(this)
            finish()
        }, 1500)

         */
    }


}