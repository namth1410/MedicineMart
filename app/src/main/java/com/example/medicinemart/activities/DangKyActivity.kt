package com.example.medicinemart.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.medicinemart.databinding.DangkyBinding

private lateinit var binding_dang_ky: DangkyBinding


class DangKyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_dang_ky = DangkyBinding.inflate(layoutInflater)
        setContentView(binding_dang_ky.root)

        binding_dang_ky.dacotaikhoan.setOnClickListener {
            val intent = Intent(this@DangKyActivity, DangNhapActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Animatoo.animateSlideRight(this)
            finish()
        }

        binding_dang_ky.btnDangky.setOnClickListener {
            Toast.makeText(this@DangKyActivity, "Đăng ký thành công, mời bạn đăng nhập lại", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@DangKyActivity, DangNhapActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Animatoo.animateSlideRight(this)
            finish()
        }
    }
}