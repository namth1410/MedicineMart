package com.example.medicinemart.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.medicinemart.R
import com.example.medicinemart.databinding.ThongTinTaiKhoanBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

private lateinit var binding_thong_tin_tai_khoan: ThongTinTaiKhoanBinding


class ThongTinTaiKhoanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_thong_tin_tai_khoan = ThongTinTaiKhoanBinding.inflate(layoutInflater)
        setContentView(binding_thong_tin_tai_khoan.root)
        setContentView(R.layout.thong_tin_tai_khoan)

    }


}