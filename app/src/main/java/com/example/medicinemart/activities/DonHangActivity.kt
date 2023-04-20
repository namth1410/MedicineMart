package com.example.medicinemart.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.medicinemart.R
import com.example.medicinemart.adapter.DonHangAdapter
import com.example.medicinemart.models.Sanpham
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.medicinemart.databinding.DonhangBinding

private lateinit var binding_don_hang: DonhangBinding

var donHangChoXacNhanItemList = ArrayList<Sanpham>()
var donHangDangGiaoItemList = ArrayList<Sanpham>()
var donHangDaGiaoItemList = ArrayList<Sanpham>()
var donHangDaHuyItemList = ArrayList<Sanpham>()



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

    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    fun checkList(list: ArrayList<Sanpham>) {
        if (list.isEmpty()) {
            binding_don_hang.layoutEmpty.visibility = View.VISIBLE
            binding_don_hang.listview.visibility = View.INVISIBLE

        } else {
            binding_don_hang.layoutEmpty.visibility = View.INVISIBLE
            binding_don_hang.listview.visibility = View.VISIBLE
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
        val adapterDonhang = DonHangAdapter(this, list)
        val test = Sanpham(1, "Thuốc đi ỉa", "Xương khớp",20, "cho những thằng tên Tuyển", "as")
        list.clear()
        list.add(test)
        list.add(test)
        list.add(test)
        list.add(test)
        list.add(test)
        list.add(test)
        list.add(test)
        checkList(list)
        binding_don_hang.listview.adapter = adapterDonhang
    }

}
