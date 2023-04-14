package com.example.medicinemart.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.medicinemart.R
import com.example.medicinemart.databinding.ChitietsanphamBinding
import com.example.medicinemart.models.BannerAds
import com.example.medicinemart.models.Sanpham
import com.example.medicinemart.retrofit.API
import com.example.medicinemart.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat

private lateinit var binding_chi_tiet_san_pham: ChitietsanphamBinding

val products_in_cart = ArrayList<Sanpham>()
val quantity_product_in_cart = ArrayList<Int>()


class ChiTietSanPhamActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_chi_tiet_san_pham = ChitietsanphamBinding.inflate(layoutInflater)
        setContentView(binding_chi_tiet_san_pham.root)

        val item = intent.getSerializableExtra("item") as Sanpham
        binding_chi_tiet_san_pham.name.text = item.name
        val formatter: NumberFormat = DecimalFormat("#,###")
        binding_chi_tiet_san_pham.price.text = formatter.format(item.price) + "đ"
        Glide
            .with(this.applicationContext)
            .load(item.image)
            .into(binding_chi_tiet_san_pham.imageView)


        binding_chi_tiet_san_pham.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding_chi_tiet_san_pham.btnCart.setOnClickListener() {
            val intent = Intent(this, CartActivity::class.java)


            val viewPagerApi = RetrofitClient.getInstance().create(API::class.java)
            GlobalScope.launch(Dispatchers.Main) {
                val getCart = async { viewPagerApi.getCart() }
                val res_getCart = getCart.await().body()
                for (i in res_getCart!!) {
                    val id = i.getAsJsonPrimitive("id_product").asInt
                    val name = i.getAsJsonPrimitive("name").toString()
                    val type = i.getAsJsonPrimitive("type").toString()
                    val price = i.getAsJsonPrimitive("price").asInt
                    val describe = i.getAsJsonPrimitive("describe").toString()
                    val image = i.getAsJsonPrimitive("image").toString()
                    val tmp = Sanpham(id, name, type, price, describe, image)
                    products_in_cart.add(tmp)
                    quantity_product_in_cart.add(i.getAsJsonPrimitive("quantity").asInt)
                }

                println(products_in_cart)  // lần 1
            }
            println(products_in_cart)  // lần 2

            startActivity(intent)
            overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
        }

    }
}