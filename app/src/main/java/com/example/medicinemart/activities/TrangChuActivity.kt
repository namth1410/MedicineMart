package com.example.medicinemart.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.medicinemart.R
import com.example.medicinemart.adapter.ViewPagerAdapter
import com.example.medicinemart.databinding.TrangchuBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

private lateinit var binding_trang_chu: TrangchuBinding
private lateinit var viewPagerAdapter: ViewPagerAdapter
private lateinit var viewPager: ViewPager
private lateinit var imageList: List<Int>


lateinit var bottomNav : BottomNavigationView



class TrangChuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_trang_chu = TrangchuBinding.inflate(layoutInflater)
        setContentView(binding_trang_chu.root)

        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.hoso -> {
                    // put your code here
                    val intent = Intent(this@TrangChuActivity, HoSoActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.thongbao -> {
                    // put your code here
                    val intent = Intent(this@TrangChuActivity, ThongBaoActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.donhang -> {
                    // put your code here
                    val intent = Intent(this@TrangChuActivity, DonHangActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

        binding_trang_chu.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

}