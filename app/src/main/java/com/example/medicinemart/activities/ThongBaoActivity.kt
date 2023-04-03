package com.example.medicinemart.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.medicinemart.R
import com.example.medicinemart.databinding.HosoBinding
import com.example.medicinemart.databinding.ThongbaoBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

private lateinit var binding_thong_bao: ThongbaoBinding


class ThongBaoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_thong_bao = ThongbaoBinding.inflate(layoutInflater)
        setContentView(binding_thong_bao.root)



        binding_thong_bao.bottomNavigationView.setSelectedItemId(R.id.thongbao)
        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.hoso -> {
                    // put your code here
                    val intent = Intent(this@ThongBaoActivity, HoSoActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.donhang -> {
                    // put your code here
                    val intent = Intent(this@ThongBaoActivity, DonHangActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.home -> {
                    // put your code here
                    val intent = Intent(this@ThongBaoActivity, TrangChuActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

        binding_thong_bao.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }


}