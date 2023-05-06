package com.example.medicinemart.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicinemart.adapter.OnItemClickListener
import com.example.medicinemart.adapter.RecycleViewAddressAdapter
import com.example.medicinemart.common.Info.delivery_address
import com.example.medicinemart.common.Info.list_address
import com.example.medicinemart.databinding.ChondiachinhanhangBinding

private lateinit var binding_chon_dia_chi_nhan_hang: ChondiachinhanhangBinding


class ChonDiaChiNhanHangActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_chon_dia_chi_nhan_hang = ChondiachinhanhangBinding.inflate(layoutInflater)
        setContentView(binding_chon_dia_chi_nhan_hang.root)


        val adapterRecyclerAddressAdapter= RecycleViewAddressAdapter(list_address, this, object :
            OnItemClickListener {
            override fun onItemClick(position: Int) {
                // Xử lý sự kiện click ở đây
                delivery_address = list_address.get(position)
                onBackPressed()
            }
        })

//        binding_gio_hang.recyclerView.isNestedScrollingEnabled = true
        binding_chon_dia_chi_nhan_hang.recyclerView.layoutManager = LinearLayoutManager(this)

        val VerticalLayout = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding_chon_dia_chi_nhan_hang.recyclerView.setLayoutManager(VerticalLayout)
        binding_chon_dia_chi_nhan_hang.recyclerView.adapter = adapterRecyclerAddressAdapter


        binding_chon_dia_chi_nhan_hang.btnBack.setOnClickListener() {
            onBackPressed()
        }
    }
}