package com.example.medicinemart.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicinemart.R
import com.example.medicinemart.adapter.UserAdapter
import com.example.medicinemart.databinding.ChatAdminBinding
import com.example.medicinemart.databinding.ChucnangAdminBinding
import com.example.medicinemart.databinding.ThongkeAdminBinding
import com.example.medicinemart.models.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


private lateinit var binding_chat_ad: ChatAdminBinding


class ChatAdActivity : AppCompatActivity() {

    private lateinit var userRecycleView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_chat_ad = ChatAdminBinding.inflate(layoutInflater)
        setContentView(binding_chat_ad.root)

        auth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()

        userList = ArrayList()
        adapter = UserAdapter(this, userList)

        userRecycleView = findViewById(R.id.userRecycleView)

        userRecycleView.layoutManager = LinearLayoutManager(this)
        userRecycleView.adapter = adapter

        mDbRef.child("user").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for(postSnapshot in snapshot.children) {

                    val currentUser = postSnapshot.getValue(User::class.java)
                    if(auth.currentUser?.uid != currentUser?.uid) {
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            // logic dang xuat
            auth.signOut()
            val intent = Intent(this@ChatAdActivity, DangNhapActivity::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }


}