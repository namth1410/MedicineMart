package com.example.medicinemart.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.medicinemart.databinding.SplashScreenBinding

private lateinit var binding_splash_screen: SplashScreenBinding


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_splash_screen = SplashScreenBinding.inflate(layoutInflater)
        setContentView(binding_splash_screen.root)

        Handler().postDelayed({
            // Chuyển sang màn hình chính của ứng dụng sau khi hiển thị logo trong 3 giây
            startActivity(Intent(this, DangNhapActivity::class.java))
            Animatoo.animateZoom(this)
            finish()
        }, 1500)
    }
}