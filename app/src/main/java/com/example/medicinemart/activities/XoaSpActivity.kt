package com.example.medicinemart.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.medicinemart.databinding.DangnhapBinding
import com.example.medicinemart.databinding.ThemSanPhamBinding
import com.example.medicinemart.databinding.XoaSanPhamBinding

private lateinit var binding_xoa_sp: XoaSanPhamBinding

class XoaSpActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_xoa_sp = XoaSanPhamBinding.inflate(layoutInflater)
        setContentView(binding_xoa_sp.root)
    }
}