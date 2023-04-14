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
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    finishAffinity()
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


        binding_ho_so.tvTaikhoan.setOnClickListener() {
            val intent = Intent(this@HoSoActivity, ThongTinTaiKhoanActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
        }

    }


}