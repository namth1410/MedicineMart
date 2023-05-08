package com.example.medicinemart.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.medicinemart.databinding.ChitietdiachiBinding
import com.example.medicinemart.databinding.ThongtindonhangBinding

private lateinit var binding_thong_tin_don_hang: ThongtindonhangBinding


class ThongTinDonHangActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_thong_tin_don_hang = ThongtindonhangBinding.inflate(layoutInflater)
        setContentView(binding_thong_tin_don_hang.root)
    }
}