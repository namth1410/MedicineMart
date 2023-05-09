package com.example.medicinemart.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo

import com.example.medicinemart.R
import com.example.medicinemart.common.Info._username
import com.example.medicinemart.common.Info.customer
import com.example.medicinemart.common.Info.password_list
import com.example.medicinemart.common.Info.salt_list
import com.example.medicinemart.common.Info.sharedPref
import com.example.medicinemart.common.Info.user_id
import com.example.medicinemart.common.Info.username_list
import com.example.medicinemart.databinding.DangnhapBinding

private lateinit var binding_dang_nhap: DangnhapBinding


class DangNhapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_dang_nhap = DangnhapBinding.inflate(layoutInflater)
        setContentView(binding_dang_nhap.root)


        binding_dang_nhap.chuacotaikhoan.setOnClickListener {
            val intent = Intent(this@DangNhapActivity, DangKyActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
            //finish()
        }

        binding_dang_nhap.btnDangnhap.setOnClickListener {
            var username = binding_dang_nhap.edtUsername.text.toString()
            var password = binding_dang_nhap.edtPassword.text.toString()
            println(username)
            println(password)
            //if (username == "admin" && password == "1") {
                val intent = Intent(this@DangNhapActivity, TrangChuActivity::class.java)
                startActivity(intent)
                Animatoo.animateSwipeLeft(this)
                finish()
            //}
        }

        binding_dang_nhap.button.setOnClickListener {
            val intent = Intent(this@DangNhapActivity, TrangChuAdActivity::class.java)
            startActivity(intent)
            Animatoo.animateSwipeLeft(this)
            finish()
        }
    }


}