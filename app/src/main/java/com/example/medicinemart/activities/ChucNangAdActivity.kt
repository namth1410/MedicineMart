package com.example.medicinemart.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.medicinemart.R
import com.example.medicinemart.databinding.ChucnangAdminBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

private lateinit var binding_chucnang_ad: ChucnangAdminBinding


class ChucNangAdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_chucnang_ad = ChucnangAdminBinding.inflate(layoutInflater)
        setContentView(binding_chucnang_ad.root)

        binding_chucnang_ad.btnThemSp.setOnClickListener {
            val intent = Intent(this@ChucNangAdActivity, ThemSpActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
            //finish()
        }

        binding_chucnang_ad.btnThemSp.setOnClickListener {
            val intent = Intent(this@ChucNangAdActivity, ThemSpActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
            //finish()
        }
        binding_chucnang_ad.btnXoaSp.setOnClickListener {
            val intent = Intent(this@ChucNangAdActivity, XoaSpActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
            //finish()
        }
        binding_chucnang_ad.btnSuaSp.setOnClickListener {
            val intent = Intent(this@ChucNangAdActivity, SuaSpActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
            //finish()
        }

        binding_chucnang_ad.btnQuanLyKhachHang.setOnClickListener {
            val intent = Intent(this@ChucNangAdActivity, QuanLyKhachHangActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
            //finish()
        }

        binding_chucnang_ad.btnQuanlydonhang.setOnClickListener {
            val intent = Intent(this@ChucNangAdActivity, QuanLyDonHangActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
            //finish()

        binding_chucnang_ad.bottomNavigationView.setSelectedItemId(R.id.chucnang)
            val mOnNavigationItemSelectedListener =
                BottomNavigationView.OnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.home -> {
                            // put your code here
                            val intent =
                                Intent(this@ChucNangAdActivity, TrangChuAdActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
                            return@OnNavigationItemSelectedListener true
                        }

                        R.id.chat -> {
                            // put your code here
                            val intent = Intent(this@ChucNangAdActivity, ChatAdActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
                            return@OnNavigationItemSelectedListener true
                        }
                        R.id.thongke -> {
                            // put your code here
                            val intent =
                                Intent(this@ChucNangAdActivity, ThongKeAdActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
                            return@OnNavigationItemSelectedListener true
                        }
                    }
                    false
                }

            binding_chucnang_ad.bottomNavigationView.setOnNavigationItemSelectedListener(
                mOnNavigationItemSelectedListener
            )

        }

    }
}