package com.example.medicinemart.activities

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.medicinemart.common.Info
import com.example.medicinemart.common.Info.price_formatter
import com.example.medicinemart.common.Info.time_formatter
import com.example.medicinemart.databinding.ThongtindonhangBinding
import com.example.medicinemart.models.Notification
import com.example.medicinemart.models.Order
import com.example.medicinemart.retrofit.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

private lateinit var binding_thong_tin_don_hang: ThongtindonhangBinding
lateinit var order_detail: Order


private var progressDialog: ProgressDialog? = null


class ThongTinDonHangActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_thong_tin_don_hang = ThongtindonhangBinding.inflate(layoutInflater)
        setContentView(binding_thong_tin_don_hang.root)

        val type_order = intent.getStringExtra("type_order") as String
        if (type_order == "Chờ xác nhận") {
            binding_thong_tin_don_hang.btnCancel.visibility = View.VISIBLE
        } else {
            binding_thong_tin_don_hang.btnCancel.visibility = View.GONE
        }

        binding_thong_tin_don_hang.btnCancel.setOnClickListener() {
            val dialogBuilder = AlertDialog.Builder(this).setMessage("Hủy đơn hàng?")
                .setPositiveButton("Chắc chắn") { _, _ ->
                    val call = RetrofitClient.viewPagerApi.deleteOrderdetail(
                        order_detail.id, order_detail.sanpham.id
                    )
                    progressDialog = ProgressDialog(this)
                    progressDialog?.setCancelable(false)
                    progressDialog?.setMessage("Đợi xíu...")
                    progressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                    progressDialog?.setProgress(0)
                    progressDialog?.show()
                    call.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                val call1 = RetrofitClient.viewPagerApi.addNotification(
                                    Info.customer.id, Info.title_da_huy, "hih", order_detail.sanpham.id, order_detail.id
                                )
                                call1.enqueue(object : Callback<ResponseBody> {
                                    override fun onResponse(
                                        call: Call<ResponseBody>, response: Response<ResponseBody>
                                    ) {
                                        if (response.isSuccessful) {
                                            // Xóa thành công
                                            progressDialog?.dismiss()
                                            notification_list.add(
                                                Notification()
                                            )
                                            require_reload_data_order = true
                                            require_reload_data_thong_bao = true
                                            require_reload_data_cart = true
                                            onBackPressed()
                                        } else {
                                            // Xóa không thành công
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<ResponseBody>, t: Throwable
                                    ) {
                                        // Xóa không thành công do lỗi mạng hoặc lỗi server
                                    }
                                })
//                                progressDialog?.dismiss()
//                                require_reload_data_order = true
//                                onBackPressed()
                                // Xóa thành công
                            } else {
                                // Xóa không thành công
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            // Xóa không thành công do lỗi mạng hoặc lỗi server
                        }
                    })
                }.setNegativeButton("Không") { _, _ ->
                    // Xử lý khi người dùng chọn No
                }.create()

            dialogBuilder.show()
        }

        val currentDateTime = LocalDateTime.now()
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(time_formatter)

//        val quantity = intent.getSerializableExtra("quantity") as Int
        binding_thong_tin_don_hang.fullName.text = order_detail.address.full_name
        binding_thong_tin_don_hang.phone.text = order_detail.address.phone
        binding_thong_tin_don_hang.location.text = order_detail.address.location

        binding_thong_tin_don_hang.name.text = order_detail.sanpham.name

//        val price_formatter: NumberFormat = DecimalFormat("#,###")
        binding_thong_tin_don_hang.price.text =
            price_formatter.format(order_detail.sanpham.price) + "đ"
        binding_thong_tin_don_hang.quantity.text =
            "x" + price_formatter.format(order_detail.quantity)
        binding_thong_tin_don_hang.totalPrice.text =
            price_formatter.format(order_detail.sanpham.price * order_detail.quantity) + "đ"
        if (order_detail.sanpham.image.startsWith("\"") && order_detail.sanpham.image.endsWith("\"")) {
            order_detail.sanpham.image =
                order_detail.sanpham.image.substring(1, order_detail.sanpham.image.length - 1)
        }
        Glide.with(this).load(order_detail.sanpham.image)
            .into(binding_thong_tin_don_hang.imageviewOrder)

//        val receivedDateFormatted = order_detail.time.receiveddate.format(time_formatter).toString()
        binding_thong_tin_don_hang.tvThoigiandathang.text =
            order_detail.time.orderdate.format(time_formatter).toString()
        binding_thong_tin_don_hang.tvThoigiangiaohang.text =
            order_detail.time.shipdate.format(time_formatter).toString()
        binding_thong_tin_don_hang.tvThoigianhoanthanh.text =
            order_detail.time.receiveddate.format(time_formatter).toString()

        binding_thong_tin_don_hang.btnBack.setOnClickListener() {
            onBackPressed()
        }

    }

}