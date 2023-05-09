package com.example.medicinemart.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.medicinemart.R
import com.example.medicinemart.databinding.ChatAdminBinding
import com.example.medicinemart.databinding.ChucnangAdminBinding
import com.example.medicinemart.databinding.QuanLyKhachHangBinding
import com.example.medicinemart.databinding.ThongkeAdminBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


private lateinit var binding_qlkh: QuanLyKhachHangBinding


class QuanLyKhachHangActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_qlkh = QuanLyKhachHangBinding.inflate(layoutInflater)
        setContentView(binding_qlkh.root)
    }
}