package com.example.medicinemart.activities

import com.example.medicinemart.R
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.medicinemart.databinding.ChitietsanphamBinding
import com.example.medicinemart.models.Sanpham
import com.example.medicinemart.retrofit.API
import com.example.medicinemart.retrofit.RetrofitClient
import com.example.medicinemart.retrofit.RetrofitClient.viewPagerApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

        binding_chi_tiet_san_pham.btnThemVaoGioHang.setOnClickListener {
            if (products_in_cart.isEmpty()) {
                val call = viewPagerApi.addToCart(1, item.id, 1)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            // Xử lý kết quả trả về nếu thêm hàng mới thành công
                            Toast.makeText(this@ChiTietSanPhamActivity, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
                            println("success")
                            reloadCartRecycleView()
                        } else {
                            // Xử lý lỗi nếu thêm hàng mới thất bại
                            println("fail")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // Xử lý lỗi nếu không thể kết nối tới server
                    }
                })
            }
            for (i in products_in_cart.indices) {
                if (products_in_cart[i].id == item.id) {
                    println(products_in_cart[i].name)
                    Toast.makeText(this, "Sản phẩm đã có trong giỏ hàng", Toast.LENGTH_SHORT).show()
                    break
                } else if (i == products_in_cart.size - 1 && products_in_cart[i].id != item.id) {
                    val call = viewPagerApi.addToCart(1, item.id, 1)
                    call.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            if (response.isSuccessful) {
                                // Xử lý kết quả trả về nếu thêm hàng mới thành công
                                Toast.makeText(this@ChiTietSanPhamActivity, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
                                println("success")
                            } else {
                                // Xử lý lỗi nếu thêm hàng mới thất bại
                                println("fail")
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            // Xử lý lỗi nếu không thể kết nối tới server
                        }
                    })
                    break
                }
            }
        }

        binding_chi_tiet_san_pham.btnCart.setOnClickListener() {
            val intent = Intent(this, CartActivity::class.java)
            loadDataCart()

            startActivity(intent)
            overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
        }

    }
}