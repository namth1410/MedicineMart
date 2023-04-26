package com.example.medicinemart.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.medicinemart.adapter.OnItemClickListener
import com.example.medicinemart.adapter.RecycleViewCartAdapter
import com.example.medicinemart.databinding.CartBinding
import com.example.medicinemart.models.Sanpham
import com.example.medicinemart.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


private lateinit var binding_gio_hang: CartBinding

var total_price = 0

fun loadDataCart() {
    GlobalScope.launch(Dispatchers.Main) {
        val getCart = async { RetrofitClient.viewPagerApi.getCart() }
        val res_getCart = getCart.await().body()
        products_in_cart.clear()
        quantity_product_in_cart.clear()
        for (i in res_getCart!!) {
            val id = i.getAsJsonPrimitive("id_product").asInt
            val name = i.getAsJsonPrimitive("name").toString()
            val type = i.getAsJsonPrimitive("type").toString()
            val price = i.getAsJsonPrimitive("price").asInt
            val describe = i.getAsJsonPrimitive("describe").toString()
            val image = i.getAsJsonPrimitive("image").toString()
            val tmp = Sanpham(id, name, type, price, describe, image)
            products_in_cart.add(tmp)
            quantity_product_in_cart.add(i.getAsJsonPrimitive("quantity").asInt)
        }
    }
}

fun reloadCartRecycleView() {
    binding_gio_hang.recyclerView.adapter?.notifyDataSetChanged()
}


class CartActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_gio_hang = CartBinding.inflate(layoutInflater)
        setContentView(binding_gio_hang.root)

        val adapterRecyclerCartAdapter= RecycleViewCartAdapter(products_in_cart, quantity_product_in_cart, this, object :
            OnItemClickListener {
            override fun onItemClick(position: Int) {
                // Xử lý sự kiện click ở đây
                println(products_in_cart)
            }
        })

//        binding_gio_hang.recyclerView.isNestedScrollingEnabled = true
        binding_gio_hang.recyclerView.layoutManager = LinearLayoutManager(this)

        val VerticalLayout = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding_gio_hang.recyclerView.setLayoutManager(VerticalLayout)
        binding_gio_hang.recyclerView.adapter = adapterRecyclerCartAdapter


        // Adding Listener
        binding_gio_hang.swipeContainer.setOnRefreshListener(OnRefreshListener {
            // Your code here
            Toast.makeText(applicationContext, "Works!", Toast.LENGTH_LONG).show()
            // To keep animation for 4 seconds
            loadDataCart()
            adapterRecyclerCartAdapter.notifyDataSetChanged()

            Handler().postDelayed(Runnable { // Stop animation (This will be after 3 seconds)
                binding_gio_hang.swipeContainer.setRefreshing(false)
            }, 1000) // Delay in millis
        })
//        binding_gio_hang.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (!recyclerView.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    // Load lại dữ liệu ở đây
//                    loadDataCart()
//                    adapterRecyclerCartAdapter.notifyDataSetChanged()
//
//                }
//            }
//        })

        binding_gio_hang.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

}