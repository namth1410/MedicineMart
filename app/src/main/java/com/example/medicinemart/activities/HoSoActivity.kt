package com.example.medicinemart.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.medicinemart.R
import com.example.medicinemart.databinding.HosoBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

private lateinit var binding_ho_so: HosoBinding


class HoSoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_ho_so = HosoBinding.inflate(layoutInflater)
        setContentView(binding_ho_so.root)

        binding_ho_so.bottomNavigationView.setSelectedItemId(R.id.hoso)
        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.thongbao -> {
                    // put your code here
                    val intent = Intent(this@HoSoActivity, ThongBaoActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.donhang -> {
                    // put your code here
                    val intent = Intent(this@HoSoActivity, DonHangActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.home -> {
                    // put your code here
                    val intent = Intent(this@HoSoActivity, TrangChuActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

        binding_ho_so.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }


}