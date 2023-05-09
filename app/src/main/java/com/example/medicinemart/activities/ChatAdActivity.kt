package com.example.medicinemart.activities

import android.content.Intent
import android.os.Bundle
import com.example.medicinemart.databinding.ChatAdminBinding
import com.example.medicinemart.databinding.ChucnangAdminBinding
import com.example.medicinemart.databinding.ThongkeAdminBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicinemart.R
import com.example.medicinemart.adapter.CustomerAdapter
import com.example.medicinemart.common.Info._username
import com.example.medicinemart.models.Customer
import com.google.firebase.database.*


private lateinit var binding_chat_ad: ChatAdminBinding

class ChatAdActivity : AppCompatActivity() {
    class ChatAdActivity : AppCompatActivity() {

        private lateinit var userRecycleView: RecyclerView
        private lateinit var userList: ArrayList<Customer>
        private lateinit var adapter: CustomerAdapter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding_chat_ad = ChatAdminBinding.inflate(layoutInflater)
            setContentView(binding_chat_ad.root)

            userList = ArrayList()
            adapter = CustomerAdapter(this, userList)

            userRecycleView = findViewById(R.id.userRecycleView)

            userRecycleView.layoutManager = LinearLayoutManager(this)
            userRecycleView.adapter = adapter


            binding_chat_ad.bottomNavigationView.setSelectedItemId(R.id.chat)
            val mOnNavigationItemSelectedListener =
                BottomNavigationView.OnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.home -> {
                            // put your code here
                            val intent = Intent(this@ChatAdActivity, TrangChuAdActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
                            return@OnNavigationItemSelectedListener true
                        }

                        R.id.thongke -> {
                            // put your code here
                            val intent = Intent(this@ChatAdActivity, ThongKeAdActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
                            return@OnNavigationItemSelectedListener true
                        }
                        R.id.chucnang -> {
                            // put your code here
                            val intent = Intent(this@ChatAdActivity, ChucNangAdActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
                            return@OnNavigationItemSelectedListener true
                        }
                    }
                    false
                }

            binding_chat_ad.bottomNavigationView.setOnNavigationItemSelectedListener(
                mOnNavigationItemSelectedListener
            )

        }

    }
}