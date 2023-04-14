package com.example.medicinemart.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicinemart.adapter.OnItemClickListener
import com.example.medicinemart.adapter.RecycleViewCartAdapter
import com.example.medicinemart.adapter.RecycleViewHomeAdapter
import com.example.medicinemart.databinding.CartBinding
import com.example.medicinemart.models.Sanpham

private var list_product_in_cart = ArrayList<Sanpham>()

private lateinit var binding_gio_hang: CartBinding

class CartActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_gio_hang = CartBinding.inflate(layoutInflater)
        setContentView(binding_gio_hang.root)


        val adapterRecyclerCartAdapter= RecycleViewCartAdapter(siroHoThaoDuoc, this, object :
            OnItemClickListener {
            override fun onItemClick(position: Int) {
                // Xử lý sự kiện click ở đây
                println(products_in_cart)
            }
        })

        binding_gio_hang.recyclerView.layoutManager = LinearLayoutManager(this)

        val VerticalLayout = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding_gio_hang.recyclerView.setLayoutManager(VerticalLayout)
        binding_gio_hang.recyclerView.adapter = adapterRecyclerCartAdapter


        binding_gio_hang.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

}