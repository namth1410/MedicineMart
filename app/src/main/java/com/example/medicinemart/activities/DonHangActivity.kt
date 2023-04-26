package com.example.medicinemart.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicinemart.R
import com.example.medicinemart.adapter.DonHangAdapter
import com.example.medicinemart.adapter.OnItemClickListener
import com.example.medicinemart.adapter.RecycleViewHomeAdapter
import com.example.medicinemart.adapter.RecycleViewOrderAdapter
import com.example.medicinemart.models.Sanpham
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.medicinemart.databinding.DonhangBinding
import com.example.medicinemart.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.Objects
import kotlin.reflect.typeOf

private lateinit var binding_don_hang: DonhangBinding

var donHangChoXacNhanItemList = ArrayList<Sanpham>()
var donHangDangGiaoItemList = ArrayList<Sanpham>()
var donHangDaGiaoItemList = ArrayList<Sanpham>()
var donHangDaHuyItemList = ArrayList<Sanpham>()

var quantity_product_in_order = ArrayList<Int>()


fun loadDataDonhang() {
    GlobalScope.launch(Dispatchers.Main) {
        donHangChoXacNhanItemList.clear()
        donHangDangGiaoItemList.clear()
        donHangDaGiaoItemList.clear()
        donHangDaHuyItemList.clear()
        val getOrder = async { RetrofitClient.viewPagerApi.getOrder(1, "All") }
        val res_getOrder = getOrder.await().body()
        for (i in res_getOrder!!) {
            val id = i.getAsJsonPrimitive("id").asInt
            val name = i.getAsJsonPrimitive("name").toString()
            val type = i.getAsJsonPrimitive("type").toString()
            val price = i.getAsJsonPrimitive("price").asInt
            val describe = i.getAsJsonPrimitive("describe").toString()
            val image = i.getAsJsonPrimitive("image").toString()
            var status = i.getAsJsonPrimitive("status").toString()
            status = status.substring(1, status.length - 1)
            val tmp = Sanpham(id, name, type, price, describe, image)
            println(status)
            if (status == "Chờ xác nhận") {
                donHangChoXacNhanItemList.add(tmp)
            } else if (status == "Đang giao") {
                donHangDangGiaoItemList.add(tmp)
            } else if (status == "Đã giao") {
                donHangDaGiaoItemList.add(tmp)
            } else if (status == "Đã hủy") {
                donHangDaHuyItemList.add(tmp)
            }
            quantity_product_in_order.add(i.getAsJsonPrimitive("quantity").asInt)
        }
        println("donhang " + donHangChoXacNhanItemList)
    }
}

class DonHangActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_don_hang = DonhangBinding.inflate(layoutInflater)
        setContentView(binding_don_hang.root)

        binding_don_hang.bottomNavigationView.setSelectedItemId(R.id.donhang)
        val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.thongbao -> {
                        // put your code here
                        val intent = Intent(this@DonHangActivity, ThongBaoActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                        return@OnNavigationItemSelectedListener true
                    }

                    R.id.hoso -> {
                        // put your code here
                        val intent = Intent(this@DonHangActivity, HoSoActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.home -> {
                        // put your code here
                        val intent = Intent(this@DonHangActivity, TrangChuActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }

        binding_don_hang.bottomNavigationView.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )


        binding_don_hang.btnChoXacNhan.setOnClickListener {
            handlerButton(binding_don_hang.btnChoXacNhan)
            handlerData(binding_don_hang.btnChoXacNhan)
        }
        binding_don_hang.btnDangGiao.setOnClickListener {
            handlerButton(binding_don_hang.btnDangGiao)
            handlerData(binding_don_hang.btnDangGiao)
        }
        binding_don_hang.btnDaGiao.setOnClickListener {
            handlerButton(binding_don_hang.btnDaGiao)
            handlerData(binding_don_hang.btnDaGiao)
        }
        binding_don_hang.btnDaHuy.setOnClickListener {
            handlerButton(binding_don_hang.btnDaHuy)
            handlerData(binding_don_hang.btnDaHuy)
        }

        binding_don_hang.btnChoXacNhan.performClick()
        handlerData(binding_don_hang.btnChoXacNhan)

    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    fun checkList(list: ArrayList<Sanpham>) {
        if (list.isEmpty()) {
            binding_don_hang.layoutEmpty.visibility = View.VISIBLE
            binding_don_hang.recyclerView.visibility = View.INVISIBLE

        } else {
            binding_don_hang.layoutEmpty.visibility = View.INVISIBLE
            binding_don_hang.recyclerView.visibility = View.VISIBLE
        }
    }

    fun handlerButton(p: AppCompatButton) {
        binding_don_hang.btnDangGiao.setActivated(false)
        binding_don_hang.btnDaGiao.setActivated(false)
        binding_don_hang.btnChoXacNhan.setActivated(false)
        binding_don_hang.btnDaHuy.setActivated(false)
        binding_don_hang.btnDangGiao.setTextColor(Color.parseColor("#D3D3D3"))
        binding_don_hang.btnDaGiao.setTextColor(Color.parseColor("#D3D3D3"))
        binding_don_hang.btnChoXacNhan.setTextColor(Color.parseColor("#D3D3D3"))
        binding_don_hang.btnDaHuy.setTextColor(getResources().getColor(R.color.grey))
        p.setActivated(true)
        p.setTextColor(Color.parseColor("#FFFFFF"))

    }

    fun handlerData(p: AppCompatButton) {
        var list = ArrayList<Sanpham>()
        when (p) {
            binding_don_hang.btnChoXacNhan -> {
                list = donHangChoXacNhanItemList
            }
            binding_don_hang.btnDangGiao -> {
                list = donHangDangGiaoItemList
            }
            binding_don_hang.btnDaGiao -> {
                list = donHangDaGiaoItemList
            }
            binding_don_hang.btnDaHuy -> {
                list = donHangDaHuyItemList
            }
        }
        println(list)
        val adapterRecyclerOrder = RecycleViewOrderAdapter(list, quantity_product_in_order, this, object :
            OnItemClickListener {
            override fun onItemClick(position: Int) {
                println("Mày vừa click vào item đúng không?")
            }
        })
        binding_don_hang.recyclerView.layoutManager = LinearLayoutManager(this)

        val VerticalLayout = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding_don_hang.recyclerView.setLayoutManager(VerticalLayout)
        binding_don_hang.recyclerView.adapter = adapterRecyclerOrder


    }

}
