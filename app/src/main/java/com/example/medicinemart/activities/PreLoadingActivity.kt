package com.example.medicinemart.activities

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.medicinemart.common.Info
import com.example.medicinemart.common.Info.broadcastReceiver
import com.example.medicinemart.common.Info.products_in_cart
import com.example.medicinemart.common.Info.quantity_product_in_cart
import com.example.medicinemart.databinding.PreLoadingBinding
import com.example.medicinemart.models.*
import com.example.medicinemart.retrofit.RetrofitClient
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private lateinit var binding_preloading: PreLoadingBinding

class PreLoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_preloading = PreLoadingBinding.inflate(layoutInflater)
        setContentView(binding_preloading.root)

        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(broadcastReceiver, intentFilter)

//        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val networkInfo = connectivityManager.activeNetworkInfo
//
//        if (networkInfo != null && networkInfo.isConnected) {
//            // Có kết nối mạng, thực hiện các thao tác yêu cầu mạng ở đây
//            getAccounts()
//        } else {
//            // Không có kết nối mạng, hiển thị thông báo hoặc thông báo cho người dùng biết
//            val builder = AlertDialog.Builder(this)
//            builder.setCancelable(false)
//
//            val view = layoutInflater.inflate(R.layout.dialog_no_network, null)
//            val closeButton = view.findViewById<Button>(R.id.dialog_close_button)
//
//            builder.setView(view)
//            val dialog = builder.create()
//            dialog.show()
//
//            closeButton.setOnClickListener {
//                dialog.dismiss()
//            }
//
//        }

        binding_preloading.lottie.playAnimation()

        val call = RetrofitClient.viewPagerApi.getInfoCustomer(Info._username)
        call.enqueue(object : Callback<Customer> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<Customer>, response: Response<Customer>) {
                if (response.isSuccessful) {
                    // Xử lý kết quả trả về nếu thêm hàng mới thành công
                    Info.customer.id = response.body()!!.id
                    Info.customer.username = response.body()!!.username
                    Info.customer.phone = response.body()!!.phone
                    Info.customer.email = response.body()!!.email
                    Info.customer.full_name = response.body()!!.full_name

                    val call = RetrofitClient.viewPagerApi.getCart(Info.customer.id)
                    call.enqueue(object : Callback<java.util.ArrayList<JsonObject>> {
                        override fun onResponse(
                            call: Call<java.util.ArrayList<JsonObject>>,
                            response: Response<java.util.ArrayList<JsonObject>>
                        ) {
                            if (response.isSuccessful) {
                                // Xử lý kết quả trả về nếu thêm hàng mới thành công
                                require_reload_data_cart = false
                                products_in_cart.clear()
                                quantity_product_in_cart.clear()
                                for (i in response.body()!!) {
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

                                val call = RetrofitClient.viewPagerApi.getOrder(Info.customer.id, "All")
                                call.enqueue(object : Callback<java.util.ArrayList<JsonObject>> {
                                    override fun onResponse(
                                        call: Call<java.util.ArrayList<JsonObject>>,
                                        response: Response<java.util.ArrayList<JsonObject>>
                                    ) {
                                        if (response.isSuccessful) {
                                            // Xử lý kết quả trả về nếu thêm hàng mới thành công
                                            require_reload_data_order = false
                                            donHangChoXacNhanItemList.clear()
                                            donHangDangGiaoItemList.clear()
                                            donHangDaGiaoItemList.clear()
                                            donHangDaHuyItemList.clear()
                                            for (i in response.body()!!) {
                                                val id_order = i.getAsJsonPrimitive("id_order").asInt
                                                val quantity = i.getAsJsonPrimitive("quantity").asInt
                                                var status = i.getAsJsonPrimitive("status").toString()
                                                status = status.substring(1, status.length - 1)
                                                val id = i.getAsJsonPrimitive("id").asInt
                                                val name = i.getAsJsonPrimitive("name").toString()
                                                val type = i.getAsJsonPrimitive("type").toString()
                                                val price = i.getAsJsonPrimitive("price").asInt
                                                val describe = i.getAsJsonPrimitive("describe").toString()
                                                val image = i.getAsJsonPrimitive("image").toString()

                                                val id_address = i.getAsJsonPrimitive("id_address").asInt
                                                val full_name = i.getAsJsonPrimitive("full_name").asString
                                                val phone = i.getAsJsonPrimitive("phone").asString
                                                val td_x = i.getAsJsonPrimitive("td_x").asBigDecimal
                                                val td_y = i.getAsJsonPrimitive("td_y").asBigDecimal
                                                val location = i.getAsJsonPrimitive("location").asString

                                                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                                var _date = i.getAsJsonPrimitive("orderDate").asString
                                                val _orderdate = LocalDateTime.parse(_date, formatter)

                                                var _shipdate = Info.time_defaul
                                                if (!i.get("shipDate").isJsonNull) {
                                                    _date = i.getAsJsonPrimitive("shipDate")?.asString
                                                    _shipdate = LocalDateTime.parse(_date.toString(), formatter)
                                                } else {
                                                    _shipdate = Info.time_defaul
                                                }

                                                var _receiveddate = Info.time_defaul
                                                if (!i.get("receivedDate").isJsonNull) {
                                                    _date = i.getAsJsonPrimitive("receivedDate")?.asString
                                                    _receiveddate = LocalDateTime.parse(_date.toString(), formatter)
                                                } else {
                                                    _receiveddate = Info.time_defaul
                                                }

                                                var _canceldate = Info.time_defaul
                                                if (!i.get("cancelDate").isJsonNull) {
                                                    _date = i.getAsJsonPrimitive("cancelDate")?.asString
                                                    _canceldate = LocalDateTime.parse(_date.toString(), formatter)
                                                } else {
                                                    _canceldate = Info.time_defaul
                                                }

                                                val sanpham = Sanpham(id, name, type, price, describe, image)
                                                val address = Address(id_address, phone, Info._username, full_name, td_x, td_y, location)
                                                val time = Time(_orderdate, _shipdate, _receiveddate, _canceldate)
                                                val tmp = Order(id_order, sanpham, quantity, address, time)

                                                if (status == "Chờ xác nhận") {
                                                    donHangChoXacNhanItemList.add(tmp)
                                                } else if (status == "Đang giao") {
                                                    donHangDangGiaoItemList.add(tmp)
                                                } else if (status == "Đã giao") {
                                                    donHangDaGiaoItemList.add(tmp)
                                                } else if (status == "Đã hủy") {
                                                    donHangDaHuyItemList.add(tmp)
                                                }
                                            }
                                            val call = RetrofitClient.viewPagerApi.getNotification(
                                                Info.customer.id)
                                            call.enqueue(object : Callback<java.util.ArrayList<JsonObject>> {
                                                override fun onResponse(call: Call<java.util.ArrayList<JsonObject>>, response: Response<java.util.ArrayList<JsonObject>>) {
                                                    if (response.isSuccessful) {
                                                        // Xử lý kết quả trả về nếu thêm hàng mới thành công
                                                        notification_list.clear()
                                                        for (i in response.body()!!) {
                                                            val id = i.getAsJsonPrimitive("id").asInt
                                                            val id_customer = i.getAsJsonPrimitive("id_customer").asInt
                                                            val title = i.getAsJsonPrimitive("title").asString
                                                            val content = i.getAsJsonPrimitive("content").asString
                                                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                                            var _time = i.getAsJsonPrimitive("time").asString
                                                            val time = LocalDateTime.parse(_time, formatter)
                                                            val id_product = i.getAsJsonPrimitive("id_product").asInt
                                                            val id_order = i.getAsJsonPrimitive("id_order").asInt
                                                            val image = i.getAsJsonPrimitive("image").asString

                                                            val tmp = Notification(id, id_customer, title, content, time, id_product, id_customer, image)
                                                            notification_list.add(tmp)
                                                        }

                                                        val call = RetrofitClient.viewPagerApi.getAddress(Info._username)
                                                        call.enqueue(object : Callback<java.util.ArrayList<Address>> {
                                                            override fun onResponse(call: Call<java.util.ArrayList<Address>>, response: Response<java.util.ArrayList<Address>>) {
                                                                if (response.isSuccessful) {
                                                                    // Xử lý kết quả trả về nếu thêm hàng mới thành công
                                                                    Info.list_address.clear()
                                                                    for (i in response.body()!!) {
                                                                        val id = i.id
                                                                        val username = i.username
                                                                        val fullname = i.full_name
                                                                        val phone = i.phone
                                                                        val td_x = i.td_x
                                                                        val td_y = i.td_y
                                                                        val location = i.location
                                                                        val tmp = Address(id, phone, username, fullname, td_x, td_y, location)
                                                                        Info.list_address.add(tmp)
                                                                    }

                                                                    Handler().postDelayed({
                                                                        // Chuyển sang màn hình chính của ứng dụng sau khi hiển thị logo trong 3 giây
//                                                                        Info.broadcastReceiver = NetworkChangeReceiver()
//                                                                        unregisterReceiver(broadcastReceiver)
                                                                        startActivity(
                                                                            Intent(
                                                                                this@PreLoadingActivity,
                                                                                TrangChuActivity::class.java
                                                                            )
                                                                        )
//                                                                        Animatoo.animateZoom(this@PreLoadingActivity)
                                                                        finish()
                                                                    }, 1000)

                                                                } else {
                                                                    println(response.errorBody())
                                                                    // Xử lý lỗi nếu thêm hàng mới thất bại
                                                                }
                                                            }

                                                            override fun onFailure(call: Call<java.util.ArrayList<Address>>, t: Throwable) {
                                                                // Xử lý lỗi nếu không thể kết nối tới server

                                                            }
                                                        })

                                                    } else {
                                                        println(response.errorBody())
                                                        // Xử lý lỗi nếu thêm hàng mới thất bại
                                                    }
                                                }

                                                override fun onFailure(call: Call<java.util.ArrayList<JsonObject>>, t: Throwable) {
                                                    // Xử lý lỗi nếu không thể kết nối tới server

                                                }
                                            })

                                        } else {
                                            println(response.errorBody())
                                            // Xử lý lỗi nếu thêm hàng mới thất bại
                                        }
                                    }

                                    override fun onFailure(call: Call<java.util.ArrayList<JsonObject>>, t: Throwable) {
                                        // Xử lý lỗi nếu không thể kết nối tới server

                                    }
                                })
                            } else {
                                println(response.errorBody())
                                // Xử lý lỗi nếu thêm hàng mới thất bại
                            }
                        }

                        override fun onFailure(call: Call<java.util.ArrayList<JsonObject>>, t: Throwable) {
                            // Xử lý lỗi nếu không thể kết nối tới server

                        }
                    })
                } else {
                    // Xử lý lỗi nếu thêm hàng mới thất bại
                }
            }

            override fun onFailure(call: Call<Customer>, t: Throwable) {
                // Xử lý lỗi nếu không thể kết nối tới server
                println("API loi")
            }
        })

//        Handler().postDelayed({
//            // Chuyển sang màn hình chính của ứng dụng sau khi hiển thị logo trong 3 giây
//            startActivity(Intent(this, TrangChuActivity::class.java))
//            Animatoo.animateZoom(this)
//            finish()
//        }, 1500)

    }

    override fun onDestroy() {
        // Hủy đăng ký BroadcastReceiver
//        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }
}