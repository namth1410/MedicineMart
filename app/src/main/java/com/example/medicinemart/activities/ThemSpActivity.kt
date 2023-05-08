package com.example.medicinemart.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.medicinemart.databinding.DangnhapBinding
import com.example.medicinemart.databinding.ThemSanPhamBinding

private lateinit var binding_them_sp: ThemSanPhamBinding

class ThemSpActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_them_sp = ThemSanPhamBinding.inflate(layoutInflater)
        setContentView(binding_them_sp.root)

        binding_them_sp.btnSave.setOnClickListener {
            var tenSp = binding_them_sp.edtTenSp.text.toString()
            var giaSp = binding_them_sp.edtGiaSp.text.to(Double)
            var motaSp = binding_them_sp.edtMotaSp.text.toString()
            var hinhanhSp = binding_them_sp.edtHinhanhSp.text.to(Int)
            println(tenSp)
            println(motaSp)
            val intent = Intent(this@ThemSpActivity, ChucNangAdActivity::class.java)
            startActivity(intent)
            Animatoo.animateSwipeLeft(this)
            finish()
        }
        binding_them_sp.btnCancel.setOnClickListener {
            Animatoo.animateSwipeLeft(this)
            finish()
        }
    }
}