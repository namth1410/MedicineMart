package com.example.medicinemart.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.example.medicinemart.R
import com.example.medicinemart.common.Info.customer
import com.example.medicinemart.common.Info.products_in_cart
import com.example.medicinemart.databinding.ChitietsanphamBinding
import com.example.medicinemart.models.Sanpham
import com.example.medicinemart.retrofit.RetrofitClient.viewPagerApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat


private lateinit var binding_chi_tiet_san_pham: ChitietsanphamBinding

class ChiTietSanPhamActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_chi_tiet_san_pham = ChitietsanphamBinding.inflate(layoutInflater)
        setContentView(binding_chi_tiet_san_pham.root)

        val item = intent.getSerializableExtra("item") as Sanpham
        val goto = intent.getSerializableExtra("goto") as String

        val transitionName = intent.getStringExtra("transition_name")
        ViewCompat.setTransitionName(binding_chi_tiet_san_pham.imageView, transitionName)

        binding_chi_tiet_san_pham.name.text = item.name
        val formatter: NumberFormat = DecimalFormat("#,###")
        binding_chi_tiet_san_pham.price.text = formatter.format(item.price) + "đ"
        if (item.image.startsWith("\"") && item.image.endsWith("\"")) {
            item.image = item.image.substring(1, item.image.length - 1)
        }
        Glide
            .with(this.applicationContext)
            .load(item.image)
            .into(binding_chi_tiet_san_pham.imageView)


        binding_chi_tiet_san_pham.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding_chi_tiet_san_pham.btnThemVaoGioHang.setOnClickListener {
            if (products_in_cart.isEmpty()) {
                val call = viewPagerApi.addToCart(customer.id, item.id, 1)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            // Xử lý kết quả trả về nếu thêm hàng mới thành công
                            require_reload_data_cart = true
                            Toast.makeText(
                                this@ChiTietSanPhamActivity,
                                "Đã thêm vào giỏ hàng",
                                Toast.LENGTH_SHORT
                            ).show()
//                            products_in_cart.add(item)
//                            Info.quantity_product_in_cart.add(1)
//                            binding_gio_hang.recyclerView.adapter?.notifyDataSetChanged()
                        } else {
                            // Xử lý lỗi nếu thêm hàng mới thất bại
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // Xử lý lỗi nếu không thể kết nối tới server
                    }
                })
            } else {
                for (i in products_in_cart.indices) {
                    if (products_in_cart[i].id == item.id) {
                        println(products_in_cart[i].name)
                        Toast.makeText(this, "Sản phẩm đã có trong giỏ hàng", Toast.LENGTH_SHORT)
                            .show()
                        break
                    } else if (i == products_in_cart.size - 1 && products_in_cart[i].id != item.id) {
                        val call = viewPagerApi.addToCart(customer.id, item.id, 1)
                        call.enqueue(object : Callback<ResponseBody> {
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                if (response.isSuccessful) {
                                    // Xử lý kết quả trả về nếu thêm hàng mới thành công
                                    require_reload_data_cart = true
                                    Toast.makeText(
                                        this@ChiTietSanPhamActivity,
                                        "Đã thêm vào giỏ hàng",
                                        Toast.LENGTH_SHORT
                                    ).show()
//                                    products_in_cart.add(item)
//                                    Info.quantity_product_in_cart.add(1)
//                                    binding_gio_hang.recyclerView.adapter?.notifyDataSetChanged()
                                } else {
                                    // Xử lý lỗi nếu thêm hàng mới thất bại
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

        }

        binding_chi_tiet_san_pham.btnCart.setOnClickListener() {
            val intent = Intent(this, CartActivity::class.java)
//            loadDataCart()
            if (goto == "cart") {
                onBackPressed()
            } else {
                startActivity(intent)
                overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
            }
        }
    }
}