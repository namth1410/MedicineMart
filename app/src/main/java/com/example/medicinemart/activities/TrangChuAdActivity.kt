package com.example.medicinemart.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.medicinemart.R
import com.example.medicinemart.adapter.ViewPagerAdapter
import com.example.medicinemart.databinding.TrangchuadminBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

private lateinit var binding_trang_chu_ad: TrangchuadminBinding
private lateinit var viewPagerAdapter: ViewPagerAdapter
private lateinit var viewPager: ViewPager
private lateinit var imageList: List<Int>

class TrangChuAdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_trang_chu_ad = TrangchuadminBinding.inflate(layoutInflater)
        setContentView(binding_trang_chu_ad.root)

        binding_trang_chu_ad.bottomNavigationView.setSelectedItemId(R.id.home)
        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.chat -> {
                    // put your code here
                    val intent = Intent(this@TrangChuAdActivity, ChatAdActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.thongke -> {
                    // put your code here
                    val intent = Intent(this@TrangChuAdActivity, ThongKeAdActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.chucnang -> {
                    // put your code here
                    val intent = Intent(this@TrangChuAdActivity, ChucNangAdActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

        binding_trang_chu_ad.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

}