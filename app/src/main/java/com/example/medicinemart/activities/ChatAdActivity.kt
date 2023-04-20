package com.example.medicinemart.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.medicinemart.R
import com.example.medicinemart.databinding.ChatAdminBinding
import com.example.medicinemart.databinding.ChucnangAdminBinding
import com.example.medicinemart.databinding.ThongkeAdminBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


private lateinit var binding_chat_ad: ChatAdminBinding


class ChatAdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_chat_ad = ChatAdminBinding.inflate(layoutInflater)
        setContentView(binding_chat_ad.root)

        binding_chat_ad.bottomNavigationView.setSelectedItemId(R.id.chat)
        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // put your code here
                    val intent = Intent(this@ChatAdActivity, TrangChuAdActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.thongke -> {
                    // put your code here
                    val intent = Intent(this@ChatAdActivity, ThongKeAdActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.chucnang -> {
                    // put your code here
                    val intent = Intent(this@ChatAdActivity, ChucNangAdActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

        binding_chat_ad.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }


}