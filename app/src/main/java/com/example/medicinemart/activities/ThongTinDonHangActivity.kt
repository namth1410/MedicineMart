package com.example.medicinemart.activities

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.medicinemart.common.Info.price_formatter
import com.example.medicinemart.common.Info.time_formatter
import com.example.medicinemart.databinding.ThongtindonhangBinding
import com.example.medicinemart.models.Order
import java.time.LocalDateTime

private lateinit var binding_thong_tin_don_hang: ThongtindonhangBinding
lateinit var order_detail: Order




class ThongTinDonHangActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_thong_tin_don_hang = ThongtindonhangBinding.inflate(layoutInflater)
        setContentView(binding_thong_tin_don_hang.root)

        val currentDateTime = LocalDateTime.now()
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(time_formatter)
        println("formattedDateTime " + formattedDateTime)

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
        Glide
            .with(this)
            .load(order_detail.sanpham.image)
            .into(binding_thong_tin_don_hang.imageviewOrder)

//        val receivedDateFormatted = order_detail.time.receiveddate.format(time_formatter).toString()
        binding_thong_tin_don_hang.tvThoigiandathang.text = order_detail.time.orderdate.format(time_formatter).toString()
        binding_thong_tin_don_hang.tvThoigiangiaohang.text = order_detail.time.shipdate.format(time_formatter).toString()
        binding_thong_tin_don_hang.tvThoigianhoanthanh.text = order_detail.time.receiveddate.format(time_formatter).toString()

        binding_thong_tin_don_hang.btnBack.setOnClickListener()
        {
            onBackPressed()
        }

    }

}